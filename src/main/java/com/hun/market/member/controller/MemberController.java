package com.hun.market.member.controller;

import com.hun.market.member.domain.MemberContext;
import com.hun.market.member.dto.MemberDto;
import com.hun.market.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/m")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/main")
    public String myInfo(Model model,
                         @AuthenticationPrincipal MemberContext memberSession){
        MemberDto.MemberResponseDto memberDto = memberService.getMember(memberSession.getMemberId());
        model.addAttribute("memberDto", memberDto);
        return "member/my_info";
    }

    @PostMapping("/change-password")
    public String changePassword(Model model,
                                 @AuthenticationPrincipal MemberContext memberSession,
                                 @Valid @ModelAttribute MemberDto.MemberChangePwdRequestDto mbrChangePwdDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes){

        String error = isValid_LEE_SEUNGHUN06_JOKE_PASSWORD_CHECK(mbrChangePwdDto, memberSession, bindingResult);

        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
            redirectAttributes.addFlashAttribute("activeTab", "profile");
            return "redirect:/m/main";
        }

        memberService.updatePassword(memberSession.getMemberId(), passwordEncoder.encode(mbrChangePwdDto.getNewPassword()));
        redirectAttributes.addFlashAttribute("message", "비밀번호가 변경되었습니다. 다시 로그인해주세요.");
        redirectAttributes.addFlashAttribute("activeTab", "profile");

        return "redirect:/m/main";
    }


    private String isValid_LEE_SEUNGHUN06_JOKE_PASSWORD_CHECK(MemberDto.MemberChangePwdRequestDto mbrChangePwdDto,
                                          MemberContext memberSession,
                                          BindingResult bindingResult) {
        if (!isMatchedCurrentPassword(mbrChangePwdDto, memberSession)){
            return "현재 비밀번호가 일치하지 않습니다.";
        }
        if (!isMatchedNewConfirmPassword(mbrChangePwdDto)){
            return "새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.";
        }
        if (isSameCurrentPassword(mbrChangePwdDto)){
            return "현재 비밀번호와 새 비밀번호가 일치합니다.";
        }
        if (isValidMemberDtoError(bindingResult)){
            return bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        return null;
    }



    private boolean isValidMemberDtoError(BindingResult bindingResult) {
        return bindingResult.hasErrors();
    }

    private boolean isSameCurrentPassword(MemberDto.MemberChangePwdRequestDto mbrChangePwdDto) {
        return mbrChangePwdDto.getCurrentPassword().equals(mbrChangePwdDto.getNewPassword());
    }

    private boolean isMatchedNewConfirmPassword(MemberDto.MemberChangePwdRequestDto mbrChangePwdDto) {
        return mbrChangePwdDto.getNewPassword().equals(mbrChangePwdDto.getConfirmPassword());
    }

    private boolean isMatchedCurrentPassword(MemberDto.MemberChangePwdRequestDto mbrChangePwdDto,
                                      MemberContext memberSession) {
        return passwordEncoder.matches(mbrChangePwdDto.getCurrentPassword(), memberSession.getMbPassword());
    }


    @GetMapping("/exam")
    public String myInfoExam(Model model,
        @AuthenticationPrincipal MemberContext memberSession){
        MemberDto.MemberResponseDto memberDto = memberService.getMember(memberSession.getMemberId());
        model.addAttribute("memberDto", memberDto);
        return "exam/exam";
    }

}
