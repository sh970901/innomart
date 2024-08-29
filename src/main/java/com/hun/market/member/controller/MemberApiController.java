package com.hun.market.member.controller;

import com.hun.market.core.exception.ResponseServiceException;
import com.hun.market.member.domain.MemberContext;
import com.hun.market.member.dto.MemberDto;
import com.hun.market.member.dto.MemberDto.MemberCoinHistoryResponseDtos;
import com.hun.market.member.dto.MemberDto.MemberRequestDto;
import com.hun.market.member.dto.MemberDto.MemberResponseDto;
import com.hun.market.member.exception.MemberNotMatchException;
import com.hun.market.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/m")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/modify/{memberId}")
    public void updateEmployee(@PathVariable Long memberId, @RequestBody MemberRequestDto memberModifyRequestDto) {
        memberService.updateMember(memberId, memberModifyRequestDto);
    }

    @PostMapping("/send-password-email")
    public MemberDto.MemberForgotPwdResponseDto sendPasswordEmail(@RequestBody MemberDto.MemberForgotPwdRequestDto forgotPwdRequestDto) {
        try{
            memberService.resetPassword(forgotPwdRequestDto.getEmail());
        }
        catch (MemberNotMatchException e){
            throw new ResponseServiceException(e.getMessage());
        }

        return MemberDto.MemberForgotPwdResponseDto.builder().description("등록된 메일 주소로 임시 비밀번호가 발송되었습니다.").build();
    }

    @GetMapping("/employee")
    public List<MemberResponseDto> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/employee/history/{memberId}")
    public List<MemberCoinHistoryResponseDtos> getMemberHistory(@PathVariable Long memberId) {
        return memberService.getMemberHistory(memberId);
    }

    @GetMapping("/coinTransHistory")
    public List<MemberDto.MemberCoinHistoryResponseDto> getMemberCoinTransHistory(@AuthenticationPrincipal MemberContext memberSession) throws InterruptedException {
//        Thread.sleep(20000);
        return memberService.getMemberCoinTransHistory(memberSession.getMemberId());
    }

    @GetMapping("/claims")
    public List<MemberDto.MemberClaimsResponseDto> getMemberClaims(@AuthenticationPrincipal MemberContext memberSession){
        return memberService.getMemberClaims(memberSession.getMemberId());
    }

    @GetMapping("/orders")
    public List<MemberDto.MemberOrdersResponseDto> getMemberOrders(@AuthenticationPrincipal MemberContext memberSession){
        return memberService.getMemberOrders(memberSession.getMemberId());
    }

}
