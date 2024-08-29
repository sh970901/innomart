package com.hun.market.order.claim.domain;

import com.hun.market.base.entity.BaseEntity;
import com.hun.market.member.domain.Member;
import com.hun.market.order.claim.dto.ClaimDto;
import com.hun.market.order.order.domain.OrderItem;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "claims")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class Claim extends BaseEntity {

    @Id
    @Column(name = "claim_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClaimStatus status;

    @Column(name = "refund_amount", nullable = false)
    private Long refundAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static Claim fromDto(ClaimDto.ClaimCreateRequestDto claimCreateRequestDto){
        return Claim.builder()
                .status(ClaimStatus.PENDING)
                .refundAmount(claimCreateRequestDto.getRefundAmount())
                .orderItem(claimCreateRequestDto.getOrderItem())
                .member(claimCreateRequestDto.getMember())
                .build();
    }
}
