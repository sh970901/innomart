package com.hun.market.letter;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/letter")
@Slf4j
@RequiredArgsConstructor
public class LetterController {
    private final GoogleService googleService;
    private final RedisTemplate redisCacheTemplate;


    @PostMapping("/write/cache")
    @Scheduled(fixedRate = 3600000)
//    @Scheduled(fixedRate = 5000)
    public void writeToCache() {
        long startTime = System.nanoTime(); // 시작 시간 기록
        log.info("Redis Update 실행");

        googleService.writeToSh22tCache();

        long endTime = System.nanoTime(); // 종료 시간 기록
        double durationInSeconds = (endTime - startTime) / 1_000_000_000.0; // 초 단위로 변환
        log.info("writeToCache 실행 시간: {} 초", durationInSeconds);
    }

    @GetMapping("/employee/{id}")
    public LettersForm getLetters(@PathVariable("id") String userId){

        SetOperations setOperations = redisCacheTemplate.opsForSet();
        Set<Letter> letters = setOperations.members(userId);

        for (Letter letter : letters){
            String message = letter.getMessage();
            message = message.replace("\n", " ");
            letter.setMessage(message);
        }

        String receiver = String.valueOf(redisCacheTemplate.opsForValue().get("name:" + userId));

        return LettersForm.from().letters(letters).receiver(receiver).build();
    }
}
