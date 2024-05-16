package org.example.configuration;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

    @Value("${spring.data.redis.port}")
    private String port;
    @Value("${spring.data.redis.host}")
    private String host;

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(host, Integer.valueOf(port));
        lettuceConnectionFactory.start();
        System.out.println(lettuceConnectionFactory.getHostName() + ":" + lettuceConnectionFactory.getPort());
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<Integer, String> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<Integer, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

//    @PreDestroy
//    public void closeConnectionFactory(LettuceConnectionFactory lettuceConnectionFactory) {
//        lettuceConnectionFactory.stop();
//        System.out.println(lettuceConnectionFactory.getConnection());
//    }

}