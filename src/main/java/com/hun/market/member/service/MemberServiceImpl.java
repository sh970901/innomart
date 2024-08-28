package com.hun.market.member.service;

import com.hun.market.backoffice.dto.CoinProvideRequestDto;
import com.hun.market.backoffice.dto.EmployeeExcelUploadDto;
import com.hun.market.core.util.RandomStringGenerator;
import com.hun.market.item.exception.ItemNotFoundException;
import com.hun.market.member.domain.CoinTransHistory;
import com.hun.market.member.domain.Member;
import com.hun.market.member.dto.MemberDto;
import com.hun.market.member.dto.MemberDto.MemberClaimsResponseDto;
import com.hun.market.member.dto.MemberDto.MemberCoinHistoryResponseDto;
import com.hun.market.member.dto.MemberDto.MemberCoinHistoryResponseDtos;
import com.hun.market.member.dto.MemberDto.MemberOrdersResponseDto;
import com.hun.market.member.dto.MemberDto.MemberRequestDto;
import com.hun.market.member.dto.MemberDto.MemberResponseDto;
import com.hun.market.member.exception.MemberNotMatchException;
import com.hun.market.member.repository.CoinTransHistoryRepository;
import com.hun.market.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.hun.market.order.claim.domain.Claim;
import com.hun.market.order.claim.repository.ClaimRepository;
import com.hun.market.order.order.domain.Order;
import com.hun.market.order.order.domain.OrderItem;
import com.hun.market.order.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final CoinTransHistoryRepository coinTransHistoryRepository;
    private final ClaimRepository claimRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void provideCoin(CoinProvideRequestDto coinProvideRequestDto) {

        coinProvideRequestDto.getEmployeeList().forEach(memberId ->
            memberRepository.findById(memberId)
                            .ifPresentOrElse(
                                member -> {
                                    member.provideCoin(coinProvideRequestDto);
                                    memberRepository.save(member);
                                },
                                () -> {
                                    throw new ItemNotFoundException("존재하지 않는 사원입니다. " + memberId);
                                }
                            )
        );

    }

    @Override
    public List<MemberResponseDto> getAllMembers() {

        return memberRepository.findAllByOrderById().stream()
                               .map(MemberDto::from)
                               .collect(Collectors.toList());

    }

    @Override
    public MemberResponseDto getMember(Long memberId) {

        return memberRepository.findById(memberId)
                               .map(MemberDto::from)
                               .orElseThrow(() -> new MemberNotMatchException("존재하지 않는 사원입니다. " + memberId));
    }

    @Override
    public MemberResponseDto getMember(String email) {

        return memberRepository.findByMbEmail(email)
                .map(MemberDto::from)
                .orElseThrow(() -> new MemberNotMatchException("존재하지 않는 사원입니다. " + email));
    }

    @Override
    @Transactional
    public void updateMember(Long memberId, MemberRequestDto memberRequestDto) {
        memberRepository.findById(memberId)
                        .ifPresentOrElse(
                            member -> {
                                member.modify(memberRequestDto);
                                memberRepository.save(member);
                            },
                            () -> {
                                throw new MemberNotMatchException("존재하지 않는 사원입니다. " + memberId);
                            }
                        );

    }

    @Transactional
    @Override
    public void updatePassword(Long memberId, String encodingPwd) {
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new MemberNotMatchException("존재하지 않는 사원입니다."));
        member.modifyPassword(encodingPwd);
        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void resetPassword(String email) {
        Member member = memberRepository.findByMbEmail(email).orElseThrow(()-> new MemberNotMatchException("존재하지 않는 사원입니다."));
        String newPassword = RandomStringGenerator.generateRandomString(20);
        member.resetPassword(newPassword);
        memberRepository.save(member);
    }

    @Override
    public List<MemberCoinHistoryResponseDtos> getMemberHistory(Long memberId) {

        return memberRepository.findById(memberId)
                               .map(Member::getCoinTransHistories)
                               .orElseGet(Collections::emptyList)
                               .stream()
                               .map(MemberDto::from)
                               .sorted(Comparator.comparing(MemberCoinHistoryResponseDtos::getCreateDate).reversed())
                               .toList();

    }

    @Override
    public List<MemberClaimsResponseDto> getMemberClaims(Long memberId) {
        List<Claim> claims = claimRepository.findByMemberId(memberId);
        List<MemberClaimsResponseDto> memberClaimsResponseDtos = new ArrayList<>();
        claims.forEach(claim -> {
           memberClaimsResponseDtos.add(
                   MemberClaimsResponseDto.from().
                           itemName(claim.getOrderItem().getItem().getItemName()).
                           claimStatus(claim.getStatus()).
                           time(claim.getCreateDate()).
                           refundAmount(claim.getRefundAmount())
                           .build());
        });


        return memberClaimsResponseDtos;
    }

    @Override
    public List<MemberOrdersResponseDto> getMemberOrders(Long memberId) {
        List<Order> orders = orderRepository.findOrdersWithItemsByMemberId(memberId);
        if (orders.isEmpty()) {
            return new ArrayList<>();
        }

        List<MemberOrdersResponseDto> memberOrdersResponseDtos = new ArrayList<>();

        for (Order order : orders) {
            List<MemberOrdersResponseDto.OrderItemResponseDto> oItemResponseDtos = new ArrayList<>();

            List<OrderItem> orderItems  = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                MemberOrdersResponseDto.OrderItemResponseDto oItemResponseDto = MemberOrdersResponseDto.OrderItemResponseDto
                        .builder()
                        .itemName(orderItem.getItem().getItemName())
                        .itemPrice(orderItem.getItem().getItemPrice())
                        .quantity(orderItem.getQuantity())
                        .build();
                oItemResponseDtos.add(oItemResponseDto);
            }

            MemberOrdersResponseDto ordersResponseDto = MemberOrdersResponseDto.builder()
                    .orderDate(order.getCreateDate())
                    .totalPrice(order.getTotalPrice())
                    .orderItems(oItemResponseDtos)
                    .orderStatus(order.getOrderStatus())
                    .build();

            memberOrdersResponseDtos.add(ordersResponseDto);
        }

        return memberOrdersResponseDtos;
    }

    @Transactional
    @Override
    public void deleteMember(Long memberId) {
        memberRepository.findById(memberId)
                        .ifPresent(memberRepository::delete);
    }

    @Transactional
    @Override public void createOneMember(EmployeeExcelUploadDto excelUploadDto) {
        Member member = Member.from(MemberDto.from(excelUploadDto));
        member.resetPassword(excelUploadDto.getPassword());

        CoinTransHistory initHistory = CoinTransHistory.registByAdmin(member, excelUploadDto.getCoin().intValue());
        member.getCoinTransHistories().add(initHistory);

        memberRepository.save(member);

    }

    @Override
    public List<MemberDto.MemberCoinHistoryResponseDto> getMemberCoinTransHistory(Long memberId) {
        List<CoinTransHistory> coinTransHistories = coinTransHistoryRepository.findByMemberIdOrderByEventDateDesc(memberId);

        return coinTransHistories.stream()
                .map(MemberDto::fromCTH)
                .toList();
    }

}
