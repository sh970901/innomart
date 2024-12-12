package com.hun.market.backoffice.controller;

import com.hun.market.member.domain.MemberContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BackOfficeController {


    @GetMapping("/backoffice/home")
    public String backOfficeHomeView(@AuthenticationPrincipal MemberContext memberSession) {
        String role = memberSession.getAuthorities().get(0).getAuthority();

        if (!role.equals("ROLE_ADMIN"))
        {
            return "redirect:/m/main";
        }

        return "backoffice/page";
    }

    @GetMapping("/welcome")
    public String welcomeView() {
       return "backoffice/welcome" ;
    }

    @PostMapping("/tree")
    public String treeView(@RequestParam("code") String code, Model model) {

        model.addAttribute("code",code);
        return "backoffice/tree";
    }


}
