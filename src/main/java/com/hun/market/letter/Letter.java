package com.hun.market.letter;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder(builderMethodName = "from")
@AllArgsConstructor
public class Letter implements Serializable {
    private static final long serialVersionUID = 1L;

    public Letter() {
        // 기본 생성자
    }
    String sender;
    String senderDepart;
    String message;

}
