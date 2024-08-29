package com.hun.market.order.cart.controller;

import com.hun.market.member.domain.MemberContext;
import com.hun.market.order.cart.dto.CartDto;
import com.hun.market.order.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/c")
@RequiredArgsConstructor
@Slf4j
public class CartApiController {

    private final CartService cartService;

    @PostMapping("/cart/item")
    public CartDto.CartCreateResponseDto addCartItem(@Valid @RequestBody CartDto.CartItemCreateRequestDto cartItemDto, @AuthenticationPrincipal MemberContext memberDto){

        // 카트는 일단 하나씩
        if (cartItemDto.getQuantity() != 1){
           return CartDto.CartCreateResponseDto.builder().description("no").build();
        }

        String buyer = memberDto.getUsername();
        log.info("member: {}", buyer);

        return cartService.addCartItemByMember(cartItemDto, buyer);
    }


    @DeleteMapping("/cart/items/{cartItemId}")
    public CartDto.CartItemDeleteResponseDto deleteOrDecreaseCartItem(@PathVariable Long cartItemId, @AuthenticationPrincipal MemberContext memberDto){
        return cartService.decreaseCartItem(cartItemId, memberDto.getUsername());
    }

    @DeleteMapping("/cart/{cartItemId}")
    public CartDto.CartItemDeleteResponseDto deleteCartItem(@PathVariable Long cartItemId, @AuthenticationPrincipal MemberContext memberDto){
        return cartService.deleteCartItem(cartItemId, memberDto.getUsername());
    }

}
