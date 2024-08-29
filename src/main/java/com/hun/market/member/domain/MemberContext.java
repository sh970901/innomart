package com.hun.market.member.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class MemberContext extends User {

    private Long memberId;
    private String mbName;
    private String mbEmail;
    private String mbPassword;
    private int mbCoin;
    private List<GrantedAuthority> authorities = new ArrayList<>();

    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getMbName(), member.getMbPassword(), authorities);
        this.authorities = authorities;
        this.mbCoin = member.getMbCoin();
        this.mbName = member.getMbName();
        this.memberId = member.getId();
        this.mbEmail = member.getMbEmail();
        this.mbPassword = member.getMbPassword();
    }

    @Override
    public String toString() {
        return "MemberContext{" +
                "mbName='" + mbName + '\'' +
                ", mbPassword='" + mbPassword + '\'' +
                ", mbCoin=" + mbCoin +
                ", authorities=" + authorities +
                '}';
    }
}
