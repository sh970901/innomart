package com.hun.market.item.service;

import com.hun.market.item.domain.Item;
import com.hun.market.item.dto.ItemDto;
import com.hun.market.item.dto.ItemDto.ItemCreatResponseDto;
import com.hun.market.item.repository.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class WrapperItemService {
    private final ItemRepository itemRepository;

    @Cacheable(cacheNames = "itemListCache", key = "#root.methodName + '.' + #page + '.' + #size + '.' + #sort + '.' + #stock",  sync = true, cacheManager = "itemCacheManager")
    public WrapperItemResponseDtos getItemList(int page, int size, String sort, String stock) {
        log.info("-----------------------DB find------------------------");


        Page<Item> resultItemPage;
        /*가격 낮은순*/
        if(StringUtils.equals(sort,"Y")) {
            resultItemPage = itemRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "itemPrice")));
        } else if(StringUtils.equals(sort,"N")) { /* 가격 높은 순*/
            resultItemPage = itemRepository.findAll(PageRequest.of(page, size, Sort.by(Direction.DESC, "itemPrice")));
        } else {
            /*상품번호 순*/
            resultItemPage = itemRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        }

        List<ItemCreatResponseDto> itemCreatResponseDtos = resultItemPage.getContent().stream()
                                                                         .map(ItemCreatResponseDto::of)
                                                                         .toList();

        /*품절제외*/
        if (StringUtils.equals("Y", stock)) {
            itemCreatResponseDtos = itemCreatResponseDtos.stream()
                                                         .filter(item -> item.getItemStock() != 0L)
                                                         .collect(Collectors.toList());
        }


        return WrapperItemResponseDtos.builder().itemCreatResponseDtos(itemCreatResponseDtos).build();
    }

    @Validated
//    @Cacheable(cacheNames = "searchItemListCache", key = "#root.methodName + '.' + #page + '.' + #size", sync = true, cacheManager = "itemCacheManager")
    public WrapperItemResponseDtos getSearchItemList(String query, int page, int size) {
        log.info("-----------------------search------------------------");

        Page<Item> resultItemPage = itemRepository.findByItemNameContaining(query, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));

        List<ItemCreatResponseDto> itemCreatResponseDtos = resultItemPage.getContent().stream()
                                                                         .map(ItemCreatResponseDto::of)
                                                                         .toList();

        return WrapperItemResponseDtos.builder().itemCreatResponseDtos(itemCreatResponseDtos).build();
    }


    @Data
    @NoArgsConstructor
    public static class WrapperItemResponseDtos{
        private List<ItemDto.ItemCreatResponseDto> itemCreatResponseDtos = new ArrayList<>();

        @Builder
        public WrapperItemResponseDtos(List<ItemDto.ItemCreatResponseDto> itemCreatResponseDtos){
            this.itemCreatResponseDtos = itemCreatResponseDtos;
        }
    }
}
