package com.hun.market.item.controller;

import com.hun.market.item.dto.ItemDto;
import com.hun.market.item.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/i")
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @GetMapping("/items")
    public List<ItemDto.ItemCreatResponseDto> getItems(@RequestParam(value = "q", required = false) String query, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "18") int size, @RequestParam(defaultValue = "") String sort, @RequestParam(defaultValue = "") String stock) {
        if (query == null || query.isEmpty()) {
            return itemService.getItemList(page, size, sort, stock);
        } else {
            return itemService.getSearchItemList(query, page, size);
        }
    }

    @GetMapping("/items/{id}")
    public ItemDto.ItemCreatResponseDto getItem(@PathVariable Long id) {
        return itemService.getItemOne(id);
    }






}
