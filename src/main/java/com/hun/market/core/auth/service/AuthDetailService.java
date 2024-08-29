package com.hun.market.core.auth.service;

import com.hun.market.member.domain.Member;
import com.hun.market.member.domain.MemberContext;
import com.hun.market.member.domain.MemberRole;
import com.hun.market.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String mbName) throws UsernameNotFoundException {
        mbName = mbName.toUpperCase();

        Member member = memberRepository.findByMbNameWithCart(mbName)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 임시 계정 생성
        if ("admin".equals(member.getMbName()) || "jo_minju01".equalsIgnoreCase(member.getMbName())) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
        }

        return new MemberContext(member, authorities);
    }

}
