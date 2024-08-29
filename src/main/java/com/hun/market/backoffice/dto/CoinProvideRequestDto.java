package com.hun.market.backoffice.dto;

import com.hun.market.member.domain.CoinTransType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CoinProvideRequestDto {


    @NotNull(message = "이벤트 타입은 필수값 입니다.")
    private CoinTransType coinTransType;

    private int coin;

    @NotNull(message = "이벤트 날짜는 필수값 입니다.")
    private LocalDateTime paymentDate;

    private String description;

    private List<Long> employeeList;

}
