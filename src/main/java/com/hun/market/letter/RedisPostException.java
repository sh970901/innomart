package com.hun.market.letter;

import lombok.Getter;

@Getter
public class RedisPostException extends RuntimeException {

    private String code = "500";
    private String message;
    public RedisPostException() {
        this.message = "Not Found Item";
    }

    public RedisPostException(String message) {
        this.message = message;
    }
    public RedisPostException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public RedisPostException(Exception e) {
        super(e);
    }
}
