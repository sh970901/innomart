package com.hun.market.member.dto;

import com.hun.market.backoffice.dto.EmployeeExcelUploadDto;
import com.hun.market.member.domain.CoinTransHistory;
import com.hun.market.member.domain.CoinTransType;
import com.hun.market.member.domain.Department;
import com.hun.market.member.domain.Member;
import com.hun.market.order.claim.domain.ClaimStatus;
import com.hun.market.order.order.domain.OrderStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberDto {


    @Getter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    public static class MemberRequestDto {

        @NotNull(message = "member name is required")
        @Size(max = 100, message = "member name must be less than 100 characters")
        private String mbName;

        @NotNull(message = "password is required")
        private String mbPassword;

        @NotNull(message = "email is required")
        private String mbEmail;

        @NotNull
        @PositiveOrZero
        private int mbCoin;

        private Department department;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberResponseDto {

        private Long memberNo;

        private String memberName;

        private String memberPassword;

        private int memberCoin;

        private String memberEmail;

        private Department department;

    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    public static class MemberChangePwdRequestDto {

//        @NotNull(message = "member name is required")
//        @Size(max = 100, message = "member name must be less than 100 characters")
//        private String mbName;

        @NotNull(message = "현재 비밀번호는 필수 입력 값입니다.")
        private String currentPassword;

        @NotBlank(message = "새 비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String newPassword;

        @NotNull(message = "새 비밀번호는 필수 입력 값입니다.")
        private String confirmPassword;
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    public static class MemberForgotPwdRequestDto {

        @Email
        String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    public static class MemberForgotPwdResponseDto {

        String description;
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    public static class MemberCoinHistoryResponseDtos {

        private Long id;
        private CoinTransType eventName;
        private int coin;
        private int totalCoin;
        private LocalDateTime eventDate;
        private LocalDateTime createDate;
        private String description;

    }

    public static MemberCoinHistoryResponseDtos from(CoinTransHistory coinTransHistory) {

        return MemberCoinHistoryResponseDtos.builder()
                                           .id(coinTransHistory.getId())
                                           .eventName(coinTransHistory.getTransactionType())
                                           .coin(coinTransHistory.getAmount())
                                           .totalCoin(coinTransHistory.getTotalCoin())
                                           .eventDate(coinTransHistory.getEventDate())
                                           .createDate(coinTransHistory.getCreateDate())
                                           .description(coinTransHistory.getDescription()).build();

    }


    public static MemberRequestDto from(EmployeeExcelUploadDto excelUploadDto) {

        return MemberRequestDto.builder()
                               .mbName(excelUploadDto.getEmployeeName())
                               .mbEmail(excelUploadDto.getEmail())
                               .mbPassword(excelUploadDto.getEmployeeName())
                               .mbCoin(excelUploadDto.getCoin().intValue())
                               .department(Department.builder().departmentName(excelUploadDto.getDepartmentName()).teamName(excelUploadDto.getTeamName()).build())
                               .build();
    }

    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
                                .memberNo(member.getId())
                                .memberName(member.getMbName())
                                .memberPassword(member.getMbPassword())
                                .memberCoin(member.getMbCoin())
                                .memberEmail(member.getMbEmail())
                                .department(member.getDepartment())
                                .build();
    }


    /**
     * 전체 정보를 한번에 조회할까? 탭마다 API를 조회할까?
     */

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberCoinHistoryResponseDto {

        private CoinTransType transactionType;
        private int amount;
        private int totalCoin;
        private LocalDateTime eventDate;
        private String description;

    }

    public static MemberCoinHistoryResponseDto fromCTH(CoinTransHistory coinTransHistory) {
        return MemberCoinHistoryResponseDto.builder()
                .transactionType(coinTransHistory.getTransactionType())
                .amount(coinTransHistory.getAmount())
                .totalCoin(coinTransHistory.getTotalCoin())
                .eventDate(coinTransHistory.getEventDate())
                .description(coinTransHistory.getDescription())
                .build();
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(builderMethodName = "from")
    public static class MemberClaimsResponseDto {

        private ClaimStatus claimStatus;
        private Long refundAmount;
        private LocalDateTime time;
        private String itemName;


    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberOrdersResponseDto {

        private LocalDateTime orderDate;
        private List<OrderItemResponseDto> orderItems = new ArrayList<>();
        private Long totalPrice;
        private OrderStatus orderStatus;

        @Getter
        @Setter
        @Builder
        public static class OrderItemResponseDto {
            private String itemName;
            private int quantity;
            private Long itemPrice;

        }
    }

}
