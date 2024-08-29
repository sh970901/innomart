package com.hun.market.item.service;

import com.hun.market.backoffice.dto.ItemModifyDto;
import com.hun.market.item.domain.Item;
import com.hun.market.item.dto.ItemDto;
import com.hun.market.item.dto.ItemDto.ItemCreatResponseDto;
import com.hun.market.item.dto.ItemDto.ItemCreateRequestDto;
import com.hun.market.item.exception.ItemNotFoundException;
import com.hun.market.item.repository.ItemRepository;
import com.hun.market.item.service.WrapperItemService.WrapperItemResponseDtos;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final WrapperItemService wrapperItemService;

    /**
     * TODO
     * 캐시 처리 시 List는 GenericJackson2JsonRedisSerializer로 역직렬화 시
     * @class 라는 Key 값에 클래스의 패키지 정보까지 전부 저장.
     * List 를 통째로 저장하면 { "@class": "..." } 이 아니라 [{ "@class": "..."}] 로 저장되어 찾지 못해서 발생하는 이슈가 발생
     * 따라서, List 를 감싸는 Wrapper 클래스를 만들어 주어 해결
     * 
     * Cacheable은 AOP Base라 클래스 분리 하지않으면 타지 않음. 따라서 WrapperService 생성
     */
    @Override
    public List<ItemDto.ItemCreatResponseDto> getItemList(int page, int size, String sort, String stock) {
        // Redis cache or DB find
        WrapperItemResponseDtos wrapperItemResponseDtos = wrapperItemService.getItemList(page, size, sort, stock);

        return wrapperItemResponseDtos.getItemCreatResponseDtos();
    }

    @Override
    public List<ItemDto.ItemCreatResponseDto> getSearchItemList(String query, int page, int size) {
        // Redis cache or DB find
        WrapperItemResponseDtos wrapperItemResponseDtos = wrapperItemService.getSearchItemList(query, page, size);
        
        return wrapperItemResponseDtos.getItemCreatResponseDtos();
    }

    @Override

    public ItemDto.ItemCreatResponseDto getItemOne(Long itemId) {

        Item item = itemRepository.findById(itemId).orElseThrow(()->new ItemNotFoundException("아이템을 찾을 수 없습니다."));

        return ItemCreatResponseDto.of(item);
    }

    @Override
    public List<ItemDto.ItemCreatResponseDto> getAllItems() {
        return itemRepository.findAllByOrderById().stream()
                             .map(ItemDto.ItemCreatResponseDto::of)
                             .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteItem(Long itemNo) {
        itemRepository.findById(itemNo)
                      .ifPresent(itemRepository::delete);
    }

    @Override
    @Transactional
    public void updateItem(ItemModifyDto itemModifyDto) {

        Optional<Item> optionalItem = itemRepository.findById(itemModifyDto.getItemNo());
        log.info("단독상품 수정 updateItem:{}", itemModifyDto.getItemNo());
        optionalItem.ifPresentOrElse(
            item -> {
                item.updateItem(itemModifyDto);
                itemRepository.save(item);
            },
            () -> {
                throw new ItemNotFoundException("해당 번호의 상품은 존재하지 않습니다." + itemModifyDto.getItemNo());
            }
        );

    }

    @Override
    @Transactional
    public void deleteAllItem() {
        itemRepository.deleteAll();
    }

    @Override
    @Transactional
    public void createOneItem(ItemCreateRequestDto itemCreateRequestDto) {
        itemCreateRequestDto.setImagePath("default");
        itemRepository.save(Item.from(itemCreateRequestDto));
    }
}
