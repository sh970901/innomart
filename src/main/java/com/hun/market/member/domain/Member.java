package com.hun.market.member.domain;

import com.hun.market.backoffice.dto.CoinProvideRequestDto;
import com.hun.market.base.entity.BaseEntity;
import com.hun.market.core.event.Events;
import com.hun.market.member.dto.MemberDto.MemberRequestDto;
import com.hun.market.member.event.MbResetRandomPasswordEvent;
import com.hun.market.member.exception.MemberCoinLackException;
import com.hun.market.member.exception.MemberValidException;
import com.hun.market.order.cart.domain.Cart;
import com.hun.market.order.cart.domain.CartItem;
import com.hun.market.order.claim.domain.Claim;
import com.hun.market.order.order.domain.Order;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="member_name", nullable = false, length = 1000)
    private String mbName;

    @Column(name="member_password", nullable = false, length = 1000)
    private String mbPassword;

    @Column(name="member_coin", nullable = false, length = 1000)
    private int mbCoin;

    @Column(name="member_email")
    private String mbEmail;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claim> claims = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoinTransHistory> coinTransHistories = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "departmentName", column = @Column(nullable = false))
    })
    @Nullable
    private Department department;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + mbName + '\'' +
                ", email='" + mbPassword + '\'' +
                ", coin='" + mbCoin + '\'' +
                '}';
    }

    public static Member from(MemberRequestDto memberDto) {
        return Member.builder()
                     .mbName(memberDto.getMbName())
                     .mbPassword(memberDto.getMbPassword())
                     .mbCoin(memberDto.getMbCoin())
                     .coinTransHistories(new ArrayList<>())
                     .mbEmail(memberDto.getMbEmail())
                     .department(memberDto.getDepartment())
                     .build();
    }

    public void modify(MemberRequestDto memberRequestDto) {
      this.mbCoin = memberRequestDto.getMbCoin();
      this.department = memberRequestDto.getDepartment();
    }

    public void addCoinTransHistories(CoinTransHistory coinTransHistory) {
        this.coinTransHistories.add(coinTransHistory);
    }

    public void provideCoin(int coin) {
        this.mbCoin += coin;
    }

    public void provideCoin(CoinProvideRequestDto coinProvideRequestDto) {
        this.mbCoin += coinProvideRequestDto.getCoin();
        this.coinTransHistories.add(CoinTransHistory.createDepositTransaction(this, coinProvideRequestDto));
    }

    public void deductCoin(Order order){
        int coin = Math.toIntExact(order.getTotalPrice());

        if (coin < 0){
            throw new MemberValidException("회원정보 수정 중 오류가 발생했습니다.");
        }
        if (coin > mbCoin){
            throw new MemberCoinLackException("코인이 부족합니다. \n 결제 코인: " + coin+" \n 잔여 코인: "+mbCoin);
        }
        this.mbCoin -= coin;
//        CoinTransHistory coinTransHistory = CoinTransHistory.createWithdrawalTransaction(this, order);
//        this.coinTransHistories.add(coinTransHistory);
    }

    public void mappingCart(Cart cart) {
        this.cart = cart;
    }

    public boolean isExistCart() {
        return this.cart != null;
    }

    public Cart addCartItem(CartItem cartItem) {

        /**
         * 카트가 있다면 기존 카트 아이템 리스트에 추가
         * 카트가 없다면 카트 생성
         */

        if (isExistCart()) {
            return cart.addCartItem(cartItem);
        }
        else {
            return Cart.createByMember(cartItem, this);
        }
    }

    public void modifyPassword(String encodingPwd) {
        this.mbPassword = encodingPwd;
    }

    public void resetPassword(String newPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.mbPassword = passwordEncoder.encode(newPassword);
        Events.raise(new MbResetRandomPasswordEvent(mbName, newPassword));
    }

    public void setPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.mbPassword = passwordEncoder.encode(password);
    }

    public void s2tClaims(List<Claim> claims) {
        this.claims = claims;
    }
}
