package com.hun.market.letter;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncTaskService {

    @Async("baobabExecutor")
    public void processRowAsync(List<Object> row, RedisTemplate redisCacheTemplate) {
        try {
            System.out.println("###############Thread Name: "+Thread.currentThread().getName());
            SetOperations setOperations = redisCacheTemplate.opsForSet();

            String employeeNum = String.valueOf(row.get(6));
            String sender = String.valueOf(row.get(2));
            String receiver = String.valueOf(row.get(4));
            String senderDepart = String.valueOf(row.get(1));
            String message = String.valueOf(row.get(5));

            Letter letter = Letter.from()
                                  .sender(sender)
                                  .senderDepart(senderDepart)
                                  .message(message)
                                  .build();

            setOperations.add(employeeNum, letter);
            redisCacheTemplate.opsForValue().set("name:" + employeeNum, receiver);
        } catch (Exception e) {
            log.error("Error processing row: {}", row, e);
            throw new RedisPostException("Failed to process row: " + e.getMessage());
        }
    }
}
