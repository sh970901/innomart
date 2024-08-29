package com.hun.market.base.initData;


import com.hun.market.member.domain.Department;
import com.hun.market.member.domain.Member;
import com.hun.market.member.dto.MemberDto.MemberRequestDto;
import com.hun.market.member.repository.MemberRepository;
import com.hun.market.order.order.domain.OrderPoss;
import com.hun.market.order.order.repository.OrderItemRepository;
import com.hun.market.order.order.repository.OrderPossRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class InitDataForDev extends AbstractInitData {

    private boolean initDataDone = false;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final OrderPossRepository orderPossRepository;

    @Bean
    CommandLineRunner initData(OrderItemRepository orderItemRepository) {
        return args -> {
            if (initDataDone) return;
            before();
            orderPossRepository.saveAndFlush(new OrderPoss(1L, "Y"));

            String password = passwordEncoder.encode("1234");
            Department department = Department.builder().departmentName("커머스사업부").teamName("상품개발").build();
            MemberRequestDto mbDto = MemberRequestDto.builder().mbName("admin").mbEmail("lee_seunghun06@eland.co.kr").mbPassword(password).mbCoin(100000).department(department).build();

            Member member1 = Member.from(mbDto);
            memberRepository.save(member1);

            MemberRequestDto mbDto2= MemberRequestDto.builder().mbName("admin2").mbEmail("이메일").mbPassword(password).mbCoin(12000).department(department).build();
            memberRepository.save(Member.from(mbDto2));

            MemberRequestDto mbDto3= MemberRequestDto.builder().mbName("김성수").mbEmail("이메일").mbPassword(password).mbCoin(12000).department(department).build();
            memberRepository.save(Member.from(mbDto3));

            initDataDone = true;
        };
    }
}

