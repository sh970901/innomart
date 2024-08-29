package com.hun.market.order.order.dto;

import com.hun.market.item.domain.Item;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class OrderDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class OrderCreateRequestDto {

        private List<OrderItemByCartCreateRequestDto> orderItemDtos = new ArrayList<>();

    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class OrderItemCreateRequestDto {

        @NotNull(message = "Item  is required")
        private Long itemId;

        @NotNull(message = "quantity is required")
        @Positive
        private Integer quantity;

        @NotNull(message = "itemName is required")
        private String itemName;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class OrderItemByCartCreateRequestDto {
        @NotNull(message = "cartItem  is required")
        private Long cartItemId;

        @NotNull(message = "Item  is required")
        private Long itemId;

        @NotNull(message = "quantity is required")
        @Size(max = 10000, message = "quantity cannot exceed 10000")
        private Integer quantity;


        @NotNull(message = "Item  is required")
        private String itemName;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class OrderCreateResponseDto {

        private List<Map<Item, Integer>> itemQuantity = new ArrayList<>();

        private Long totalPrice;

        private String description;

    }



}
