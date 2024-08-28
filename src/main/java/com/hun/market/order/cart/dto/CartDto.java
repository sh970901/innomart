package com.hun.market.order.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
public class CartDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class CartItemCreateRequestDto {

        @NotNull(message = "Item  is required")
        private Long itemId;

        @NotNull(message = "quantity is required")
        private int quantity;

        @NotNull(message = "quantity is required")
        private String itemName;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class CartItemDeleteResponseDto {

        private String itemName;

        private String description;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class CartCreateResponseDto {

        private String description;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    public static class CartItemCreateResponseDto {

        @NotNull(message = "cartItem  is required")
        private Long cartItemId;

        @NotNull(message = "Item  is required")
        private Long itemId;

        @NotNull(message = "quantity is required")
        private int quantity;

        @NotNull(message = "itemName is required")
        private String itemName;

        @NotNull(message = "itemStock is required")
        private Long itemStock;

        @Setter
        private String imagePath;

        @NotNull(message = "itemPrice is required")
        private Long itemPrice;

    }
}
