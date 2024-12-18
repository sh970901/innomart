package com.hun.market.item.service;

import com.hun.market.backoffice.dto.ItemModifyDto;
import com.hun.market.item.dto.ItemDto;
import com.hun.market.item.dto.ItemDto.ItemCreateRequestDto;
import java.util.List;

public interface ItemService {
    List<ItemDto.ItemCreatResponseDto> getItemList(int page, int size, String sort, String stock);

    void updateItem(ItemModifyDto itemModifyDto);

    List<ItemDto.ItemCreatResponseDto> getSearchItemList(String query, int page, int size);

    ItemDto.ItemCreatResponseDto getItemOne(Long itemId);

    List<ItemDto.ItemCreatResponseDto> getAllItems();

    void deleteItem(Long itemNo);

    void deleteAllItem();

    void createOneItem(ItemCreateRequestDto itemCreateRequestDto);
}
