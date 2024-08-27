package com.hun.market.member.domain;

import com.hun.market.backoffice.dto.CoinProvideRequestDto;
import com.hun.market.base.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;


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
    public static CoinTransHistory createWithdrawalTransaction(Member member, int amount) {
        return CoinTransHistory.builder()
                .member(member)
                .amount(amount)
                .transactionType(CoinTransType.구매)
                .eventDate(LocalDateTime.now())
                .description("주문")
                .build();
    }


}
