package com.hun.market.base.initData;

import com.hun.market.item.domain.Item;
import com.hun.market.item.dto.ItemDto;
import com.hun.market.item.exception.ItemNotFoundException;
import com.hun.market.item.repository.ItemRepository;
import com.hun.market.member.domain.Department;
import com.hun.market.member.domain.Member;
import com.hun.market.member.dto.MemberDto.MemberRequestDto;
import com.hun.market.member.repository.MemberRepository;
import com.hun.market.order.claim.domain.Claim;
import com.hun.market.order.claim.dto.ClaimDto;
import com.hun.market.order.claim.repository.ClaimRepository;
import com.hun.market.order.order.domain.OrderItem;
import com.hun.market.order.order.dto.OrderDto;
import com.hun.market.order.order.repository.OrderItemRepository;
import com.hun.market.order.order.repository.OrderRepository;
import com.hun.market.order.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("local")
public class InitDataForLocal extends AbstractInitData {

    private boolean initDataDone = false;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final OrderService orderService;
    private final ClaimRepository claimRepository;

    @Bean
    CommandLineRunner initData(OrderItemRepository orderItemRepository) {
        return args -> {
            if (initDataDone) return;
            before();
            ItemDto.ItemCreateRequestDto itemDto1 = ItemDto.ItemCreateRequestDto.builder().itemName("품절상품").itemPrice(2000L).imagePath("https://cdn.pixabay.com/photo/2024/04/01/06/57/cookies-8668140_1280.jpg").itemStock(0L).description("에구저런 품절이네요").build();
            Item item1 = Item.from(itemDto1);
            itemRepository.save(item1);
            ItemDto.ItemCreateRequestDto itemDto12 = ItemDto.ItemCreateRequestDto.builder().itemName("다팔린상품").itemPrice(2000L).imagePath("https://cdn.pixabay.com/photo/2024/04/01/06/57/cookies-8668140_1280.jpg").itemStock(0L).description("쀼쀼").build();
            Item item2 = Item.from(itemDto12);
            itemRepository.save(item2);
            for(int i = 2; i<100; i++){
                ItemDto.ItemCreateRequestDto itemDto;
                if(i % 7 == 0) {
                    itemDto = ItemDto.ItemCreateRequestDto.builder().itemName("item"+i).itemPrice(2000L).imagePath("https://cdn.pixabay.com/photo/2024/04/01/06/57/cookies-8668140_1280.jpg").itemStock(0L).description(i+"번 상품입니다.").build();
                } else {
                    itemDto = ItemDto.ItemCreateRequestDto.builder().itemName("item"+i).itemPrice(2000L).imagePath("https://cdn.pixabay.com/photo/2024/04/01/06/57/cookies-8668140_1280.jpg").itemStock(3L).description(i+"번 상품입니다.").build();
                }
                Item item = Item.from(itemDto);
                itemRepository.save(item);
            }
            String password = passwordEncoder.encode("1234");

            Department department = Department.builder().departmentName("커머스사업부").teamName("상품개발").build();
            MemberRequestDto mbDto = MemberRequestDto.builder().mbName("admin").mbEmail("lee_seunghun06@eland.co.kr").mbPassword(password).mbCoin(100000).department(department).build();
            Member member1 = Member.from(mbDto);
            memberRepository.save(member1);

            MemberRequestDto mbDto2= MemberRequestDto.builder().mbName("admin2").mbEmail("이메일").mbPassword(password).mbCoin(12000).department(department).build();
            memberRepository.save(Member.from(mbDto2));

            MemberRequestDto mbDto3= MemberRequestDto.builder().mbName("김성수").mbEmail("이메일").mbPassword(password).mbCoin(12000).department(department).build();
            memberRepository.save(Member.from(mbDto3));


            OrderDto.OrderItemCreateRequestDto orderItemDto = OrderDto.OrderItemCreateRequestDto.builder().itemId(4L).quantity(2).build();
            orderService.createOrderByMember(orderItemDto, mbDto.getMbName());

            Item item = itemRepository.findById(orderItemDto.getItemId()).orElseThrow(() -> new ItemNotFoundException("상품을 찾을 수 없습니다."));
            OrderItem orderItem = orderItemRepository.findByItemId(item.getId());

            ClaimDto.ClaimCreateRequestDto claimCreateRequestDto = ClaimDto.ClaimCreateRequestDto.builder().refundAmount(3000L).orderItem(orderItem).time(LocalDateTime.now()).member(member1).build();
            Claim claim = Claim.fromDto(claimCreateRequestDto);
            member1.s2tClaims(List.of(claim));

            orderItemRepository.save(orderItem);
            claimRepository.save(claim);

            initDataDone = true;
        };
    }
}
