package com.hun.market.member.domain;

import com.hun.market.backoffice.dto.CoinProvideRequestDto;
import com.hun.market.base.entity.BaseEntity;
import com.hun.market.order.order.domain.Order;
import com.hun.market.order.order.dto.OrderDto;
import com.hun.market.order.order.dto.OrderDto.OrderItemByCartCreateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "trans")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CoinTransHistory extends BaseEntity {

    @Id
    @Column(name = "trans_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "total_coin", nullable = false)
    private int totalCoin;

    @Column(name = "trans_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CoinTransType transactionType;


    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "description")
    private String description;

    public static CoinTransHistory createDepositTransaction(Member member, CoinProvideRequestDto coinProvideRequestDto) {
        return CoinTransHistory.builder()
                               .member(member)
                               .amount(coinProvideRequestDto.getCoin())
                               .totalCoin(member.getMbCoin())
                               .transactionType(coinProvideRequestDto.getCoinTransType())
                               .eventDate(coinProvideRequestDto.getPaymentDate())
                               .description(coinProvideRequestDto.getDescription())
                               .build();
    }

    public static CoinTransHistory registByAdmin(Member member, int totalCoin) {
        return CoinTransHistory.builder()
                               .member(member)
                               .amount(totalCoin)
                               .totalCoin(totalCoin)
                               .transactionType(CoinTransType.인사지급)
                               .eventDate(LocalDateTime.now())
                               .description("엑셀 등록")
                               .build();
    }

    // Todo 주문정보를 받아서 처리
    public static CoinTransHistory createWithdrawalTransaction(Member member, Order order) {

        return CoinTransHistory.builder()
                .member(member)
                .amount(Math.toIntExact(order.getTotalPrice()))
                .transactionType(CoinTransType.구매)
                .totalCoin(member.getMbCoin())
                .eventDate(LocalDateTime.now())
                .description("")
                .build();
    }

    public static CoinTransHistory createWithdrawalTransaction(List<OrderItemByCartCreateRequestDto> orderItemDtos, Member member, Long orderTotalPrice) {

        String description = "";

        for (OrderItemByCartCreateRequestDto orderItemDto : orderItemDtos) {
            description += (orderItemDto.getItemName()+ " "+ orderItemDto.getQuantity() +"개 구매, ");
        }
        String description2 = description.substring(0, description.length()-2);


        return CoinTransHistory.builder()
                               .member(member)
                               .amount(Math.toIntExact(orderTotalPrice))
                               .transactionType(CoinTransType.구매)
                               .totalCoin(member.getMbCoin())
                               .eventDate(LocalDateTime.now())
                               .description(description2)
                               .build();

    }

    public static CoinTransHistory createWithdrawalTransaction(OrderDto.OrderItemCreateRequestDto orderItemDto, Member member, Long orderTotalPrice) {

        String description = (orderItemDto.getItemName() + " " + orderItemDto.getQuantity() +"개 구매");
        return CoinTransHistory.builder()
                               .member(member)
                               .amount(Math.toIntExact(orderTotalPrice))
                               .transactionType(CoinTransType.구매)
                               .totalCoin(member.getMbCoin())
                               .eventDate(LocalDateTime.now())
                               .description(description)
                               .build();

    }
}
