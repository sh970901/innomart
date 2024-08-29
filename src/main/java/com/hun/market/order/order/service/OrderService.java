package com.hun.market.order.order.service;

import com.hun.market.core.exception.ResponseServiceException;
import com.hun.market.item.domain.Item;
import com.hun.market.item.exception.ItemNotFoundException;
import com.hun.market.item.exception.ItemStockException;
import com.hun.market.item.repository.ItemRepository;
import com.hun.market.member.domain.CoinTransHistory;
import com.hun.market.member.domain.Member;
import com.hun.market.member.domain.MemberContext;
import com.hun.market.member.exception.MemberCoinLackException;
import com.hun.market.member.exception.MemberValidException;
import com.hun.market.member.repository.CoinTransHistoryRepository;
import com.hun.market.member.repository.MemberRepository;
import com.hun.market.order.cart.service.CartService;
import com.hun.market.order.order.domain.Order;
import com.hun.market.order.order.domain.OrderItem;
import com.hun.market.order.order.dto.OrderDto;
import com.hun.market.order.order.dto.OrderDto.OrderItemByCartCreateRequestDto;
import com.hun.market.order.order.repository.OrderPossRepository;
import com.hun.market.order.pay.service.PaymentService;
import jakarta.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderPossRepository orderPossRepository;
    private final PaymentService paymentCartService;
    private final AuthenticationManager authenticationManager;

    private final CoinTransHistoryRepository coinTransHistoryRepository;

    private final CartService cartService;

    @Validated
    @Transactional(rollbackFor = Exception.class)
    @Retryable(
        retryFor = {ObjectOptimisticLockingFailureException.class, OptimisticLockException.class, StaleObjectStateException.class},
        maxAttempts = 2,
        backoff = @Backoff(100)
    )
    public OrderDto.OrderCreateResponseDto createOrderByMemberCart(OrderDto.OrderCreateRequestDto orderDto, String buyer) {
        // 주문가능시간 validation check
        if(orderPossRepository.findByIdAndOrderPossYn(1L, "Y").isEmpty()) {
            throw new ResponseServiceException("주문 가능한 시간이 아닙니다.");
        }

        List<OrderItem> orderItems = orderDto2OrderItems(orderDto);

        Member member = memberRepository.findByMbNameWithCart(buyer).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        Order order = Order.createByMember(orderItems, member);

        try {
            paymentCartService.processPayment(order);
            CoinTransHistory  coinTransHistory = CoinTransHistory.createWithdrawalTransaction(orderDto.getOrderItemDtos(), member,order.getTotalPrice());
//            coinTransHistoryRepository.save(coinTransHistory);
            member.addCoinTransHistories(coinTransHistory);
            memberRepository.save(member);
        }
        catch (ItemStockException | MemberCoinLackException | MemberValidException e){
            log.info("주문 처리 중 예외 발생: " + e.getMessage());
            throw new ResponseServiceException(e.getMessage());
        }

        List<Long> cartItemsIds = orderDto.getOrderItemDtos().stream().map(OrderItemByCartCreateRequestDto::getCartItemId).toList();
        cartService.deleteAllCartItem(cartItemsIds, buyer);

        updateAuthenticationSession(member);

        return OrderDto.OrderCreateResponseDto.builder().description("주문이 완료되었습니다.").build();

    }

    private List<Long> getItemsIds(OrderDto.OrderCreateRequestDto orderDto){

        List<OrderItemByCartCreateRequestDto> orderItemDtos = orderDto.getOrderItemDtos();

        return orderItemDtos.stream()
                            .map(OrderItemByCartCreateRequestDto::getItemId)
                            .toList();

    }

    private List<OrderItem> orderDto2OrderItems(OrderDto.OrderCreateRequestDto orderDto){
        List<Item> items = itemRepository.findAllById(getItemsIds(orderDto));

        List<OrderItem> orderItems = new ArrayList<>();

        // 조회한 Item 엔티티들을 Map으로 변환 (id -> Item)
        Map<Long, Item> itemsByIds = items.stream()
                                          .collect(Collectors.toMap(Item::getId, item -> item));

        // 각 OrderItemCreateRequestDto를 OrderItem으로 변환
        for (OrderItemByCartCreateRequestDto orderItemDto : orderDto.getOrderItemDtos()) {
            Long itemId = orderItemDto.getItemId();

            Item item = Optional.ofNullable(itemsByIds.get(itemId))
                                .orElseThrow(() -> new ItemNotFoundException("상품이 존재하지 않습니다."));

            OrderItem orderItem = OrderItem.createByItem(item, orderItemDto.getQuantity());
            orderItems.add(orderItem);
        }
        return orderItems;
    }


    private void updateAuthenticationSession(Member member){
        // 현재 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return;
        // 현재 사용자의 정보를 이용하여 새로운 MemberContext 생성
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        if (authorities == null || authorities.isEmpty()){
            authorities = new ArrayList<>();
        }

        MemberContext newMemberContext = new MemberContext(member, authorities);
        // 새로운 MemberContext로 Principal을 변경
        UsernamePasswordAuthenticationToken newAuthentication =
                new UsernamePasswordAuthenticationToken(newMemberContext, authentication.getCredentials(), authentication.getAuthorities());
        // 현재 세션에 새로운 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }

    @Recover
    public OrderDto.OrderCreateResponseDto recover(OptimisticLockException e) {
        return OrderDto.OrderCreateResponseDto.builder().description("주문 처리 중 오류가 발생했습니다. 다시 시도해주세요.").build();
    }

    @Recover
    public OrderDto.OrderCreateResponseDto recover(ObjectOptimisticLockingFailureException e, OrderDto.OrderCreateRequestDto orderDto, String buyer) {
        return OrderDto.OrderCreateResponseDto.builder().description("주문 처리 중 오류가 발생했습니다. 다시 시도해주세요.").build();
    }

    @Recover
    public OrderDto.OrderCreateResponseDto recover(StaleObjectStateException e, OrderDto.OrderCreateRequestDto orderDto, String buyer) {
        return OrderDto.OrderCreateResponseDto.builder().description("주문 처리 중 오류가 발생했습니다. 다시 시도해주세요.").build();
    }

    @Validated
    @Transactional(rollbackFor = Exception.class)
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class, OptimisticLockException.class, StaleObjectStateException.class},
            maxAttempts = 2,
            backoff = @Backoff(100)
    )
    public OrderDto.OrderCreateResponseDto createOrderByMember(OrderDto.OrderItemCreateRequestDto orderItemDto, String buyer) {
        // 주문가능시간 validation check
        if(orderPossRepository.findByIdAndOrderPossYn(1L, "Y").isEmpty()) {
            throw new ResponseServiceException("주문 가능한 시간이 아닙니다.");
        }

        Member member = memberRepository.findByMbNameWithCart(buyer).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Item item = itemRepository.findById(orderItemDto.getItemId()).orElseThrow(() -> new ItemNotFoundException("상품을 찾을 수 없습니다."));
        OrderItem orderItem = OrderItem.createByItem(item, orderItemDto.getQuantity());

        List<OrderItem> orderItems = List.of(orderItem);
        Order order = Order.createByMember(orderItems, member);

        try {
            paymentCartService.processPayment(order);
            CoinTransHistory  coinTransHistory = CoinTransHistory.createWithdrawalTransaction(orderItemDto, member,order.getTotalPrice());
            //            coinTransHistoryRepository.save(coinTransHistory);
            member.addCoinTransHistories(coinTransHistory);
            memberRepository.save(member);



        }
        catch (ItemStockException | MemberCoinLackException | MemberValidException e){
            log.info("주문 처리 중 예외 발생: " + e.getMessage());
            throw new ResponseServiceException(e.getMessage());
        }

        updateAuthenticationSession(member);

        return OrderDto.OrderCreateResponseDto.builder().description("주문이 완료되었습니다.").build();
    }
}
