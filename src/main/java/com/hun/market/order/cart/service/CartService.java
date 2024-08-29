package com.hun.market.order.cart.service;

import com.hun.market.order.cart.dto.CartDto;
import com.hun.market.order.cart.dto.CartDto.CartItemDeleteResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CartService {
    CartDto.CartCreateResponseDto addCartItemByMember(CartDto.CartItemCreateRequestDto cartItemDto, String member);

    List<CartDto.CartItemCreateResponseDto> getCartItemList(Pageable pageable, String member);

    CartDto.CartItemDeleteResponseDto deleteCartItem(Long cartItemId, String username);

    CartDto.CartItemDeleteResponseDto deleteAllCartItem (List<Long> cartItemIds, String username);

    CartItemDeleteResponseDto decreaseCartItem(Long cartItemId, String username);
}
