package com.hun.market.letter;

import lombok.Builder;
import lombok.Data;

@Builder(builderMethodName = "from")
@Data
public class Employee {
    private String idenNum;
    private String name;
}
