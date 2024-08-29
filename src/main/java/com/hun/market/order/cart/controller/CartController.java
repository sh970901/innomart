package com.hun.market.order.cart.controller;

import com.hun.market.member.domain.MemberContext;
import com.hun.market.order.cart.dto.CartDto;
import com.hun.market.order.cart.service.CartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/c")
public class CartController {

    private final CartService cartService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cart")
    public String cartItems(@PageableDefault(page = 0, size = 18) Pageable pageable,
                            Model model,
                            @AuthenticationPrincipal MemberContext memberDto){

        List<CartDto.CartItemCreateResponseDto> cartItemList = cartService.getCartItemList(pageable, memberDto.getUsername());
        model.addAttribute("cartItemList", cartItemList);


        return "cart/cart_items";
    }


}
