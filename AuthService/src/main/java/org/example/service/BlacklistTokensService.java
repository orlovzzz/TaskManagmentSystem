package org.example.service;

import org.example.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BlacklistTokensService {

    @Autowired
    private RedisTemplate<Integer, String> redisTemplate;
    @Autowired
    private JwtUtils jwtUtils;

    public void addTokenInBlacklist(String token) {
        redisTemplate.opsForValue().set(token.hashCode(), token);
        long duration = jwtUtils.extractExpiration(token).getTime() - System.currentTimeMillis();
        redisTemplate.expire(token.hashCode(), duration, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenInBlacklist(String token) {
        String value = redisTemplate.opsForValue().get(token.hashCode());
        return value == null ? false : true;
    }

}
