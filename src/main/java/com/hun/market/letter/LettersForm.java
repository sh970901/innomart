package com.hun.market.letter;

import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "from")
@AllArgsConstructor
public class LettersForm implements Serializable {

    public LettersForm() {
        // 기본 생성자
    }
    Set<Letter> letters;
    private String receiver;
}
