package com.hun.market.backoffice.controller;

import com.hun.market.member.domain.MemberContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BackOfficeController {

    /*@GetMapping("/backoffice/home")
    public String backOfficeHomeView() {
        return "backoffice/backoffice";
    }*/

    @GetMapping("/backoffice/home")
    public String backOfficeHomeView(@AuthenticationPrincipal MemberContext memberSession) {
        String role = memberSession.getAuthorities().get(0).getAuthority();

        if (!role.equals("ROLE_ADMIN"))
        {
            return "redirect:/m/main";
        }

        return "backoffice/page";
    }
}
