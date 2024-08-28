package com.hun.market.order.cart.service;

import com.hun.market.item.domain.Item;
import com.hun.market.item.exception.ItemNotFoundException;
import com.hun.market.item.repository.ItemRepository;
import com.hun.market.member.domain.Member;
import com.hun.market.member.exception.MemberNotMatchException;
import com.hun.market.member.repository.MemberRepository;
import com.hun.market.order.cart.domain.Cart;
import com.hun.market.order.cart.domain.CartItem;
import com.hun.market.order.cart.dto.CartDto;
import com.hun.market.order.cart.dto.CartDto.CartItemDeleteResponseDto;
import com.hun.market.order.cart.exception.CartFullException;
import com.hun.market.order.cart.exception.CartItemNotFoundException;
import com.hun.market.order.cart.repository.CartItemRepository;
import com.hun.market.order.cart.repository.CartRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CartServiceImpl implements CartService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Validated
    @Override
    public CartDto.CartCreateResponseDto addCartItemByMember(@Valid CartDto.CartItemCreateRequestDto cartItemDto, String member) {

        Member cartMember = memberRepository.findByMbNameWithCart(member).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        CartItem cartItem = cartItemDto2CartItem(cartItemDto);

        Cart cart = null;

        try {
             cart = cartMember.addCartItem(cartItem);
        }
        catch (CartFullException e){
            return createCartResponse("최대 15개까지 찜등록이 가능합니다.");
        }

        cartRepository.save(cart);

        return createCartResponse("찜하기 성공");
    }

    @Override
    public List<CartDto.CartItemCreateResponseDto> getCartItemList(Pageable pageable, String member) {
        Member cartMember = memberRepository.findByMbNameWithCart(member).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        Cart cart = cartRepository.findByMember(cartMember).orElse(null);
        if (cart == null) return null;

        Page<CartItem> cartItemsPage = cartItemRepository.findByCartOrderByItemAsc(cart, pageable);
        if (cartItemsPage.getContent().isEmpty()) return null;


        return cartItemsPage.stream()
                .map(cartItem -> CartDto.CartItemCreateResponseDto.builder().cartItemId(cartItem.getId()).itemPrice(cartItem.getItem().getItemPrice()).itemId(cartItem.getItem().getId()).itemName(cartItem.getItem().getItemName()).itemStock(cartItem.getItem().getItemStock()).imagePath(cartItem.getItem().getImagePath()).quantity(cartItem.getQuantity()).build())
                .toList();
    }

    @Transactional
    @Override
    public CartItemDeleteResponseDto deleteCartItem(Long cartItemId, String username) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                                              .orElseThrow(() -> new CartItemNotFoundException("CartItem not found"));

        if (!cartItem.getCart().getMember().getMbName().equals(username)) {
            throw new MemberNotMatchException("Unauthorized action");
        }

        cartItem.deleteThis();
//        cartItemRepository.delete(cartItem);

        return decreaseResponse("찜목록이 취소되었습니다.");
    }

    @Transactional
    @Override
    public CartDto.CartItemDeleteResponseDto decreaseCartItem(Long cartItemId, String username) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("CartItem not found"));

        if (!cartItem.getCart().getMember().getMbName().equals(username)) {
            throw new MemberNotMatchException("Unauthorized action");
        }

        cartItem.decreaseQuantity();

        if (cartItem.getCart() == null){
            return decreaseResponse("찜목록이 취소되었습니다.");
        }
        else {
            return decreaseResponse("갯수가 차감되었습니다.");
        }
    }

    @Transactional
    @Override
    public CartDto.CartItemDeleteResponseDto deleteAllCartItem(List<Long> cartItemsIds, String username) {
        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemsIds);
        if (cartItems.isEmpty()) {
            throw new CartItemNotFoundException("CartItems not found");
        }

        List<CartItem> itemsToDelete = cartItems.stream()
                                                .filter(cartItem -> cartItem.getCart().getMember().getMbName().equals(username))
                                                .toList();

        if (itemsToDelete.size() != cartItems.size()) {
            throw new MemberNotMatchException("Unauthorized action");
        }

        cartItemRepository.deleteAllByIds(cartItemsIds);

        return decreaseResponse("Selected cart items have been deleted.");
    }


    private CartItem cartItemDto2CartItem(CartDto.CartItemCreateRequestDto cartItemDto) {
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(() -> new ItemNotFoundException("사용자를 찾을 수 없습니다."));

        return CartItem.createByItem(item, cartItemDto.getQuantity());
    }
    private CartDto.CartItemDeleteResponseDto decreaseResponse(String description) {
        return CartDto.CartItemDeleteResponseDto.builder().description(description).build();
    }

    private CartDto.CartCreateResponseDto createCartResponse(String description) {
        return CartDto.CartCreateResponseDto.builder().description(description).build();
    }


}
