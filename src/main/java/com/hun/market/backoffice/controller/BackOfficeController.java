package com.hun.market.backoffice.controller;

import com.hun.market.letter.Letter;
import com.hun.market.letter.LetterController;
import com.hun.market.letter.limiter.annotation.TrafficLimiter;
import com.hun.market.member.domain.MemberContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @TrafficLimiter(gateId = "baobabtraffic30", waitingPagePath = "backoffice/waiting")
    public String welcomeView() {
       return "backoffice/welcome" ;
    }

    @PostMapping("/tree")
    @TrafficLimiter(gateId = "baobabtraffic30", waitingPagePath = "backoffice/waiting-post")
    public String treeView(@RequestParam("code") String code, Model model) {

        /**
         * 로그인 처리 하여도 됨
         */
        if(LetterController.employees.get(code) == null) {
            return "redirect:/welcome";
        }
        model.addAttribute("code",code);
        return "backoffice/tree";
    }
//@PostMapping("/tree")
//public String treeView(@RequestParam("code") String code, RedirectAttributes redirectAttributes) {
//    /**
//     //         * 로그인 처리 하여도 됨
//     //         */
//    ////        if(LetterController.employees.get(code) == null) {
//    ////            log.info("없는 사번");
//    ////        }
//    redirectAttributes.addFlashAttribute("code", code);
//    return "redirect:/tree";
//}
//
//
//    @GetMapping("/tree")
//    public String treeViewRedirect(Model model) {
//        // FlashAttribute로 전달된 데이터는 Model에 자동 추가됨
//        return "backoffice/tree";
//    }

}
