package com.jamie.server1;

import com.alibaba.fastjson.JSON;
import com.jamie.server1.dao.UserMapper;
import com.jamie.server1.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Server1ApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;
    @Test
    public void testRedis()
    {
//        redisTemplate.opsForValue().append("ms","hello");

        BoundValueOperations boundValueOps = redisTemplate.boundValueOps("allUser");
        Object object = boundValueOps.get();
        if (object == null) {
            List<User> allUser = userMapper.findAllUser();
            System.out.println("allUser = " + allUser);
            String toJSONString = JSON.toJSONString(allUser);
            boundValueOps.set(toJSONString);
        } else {
            System.out.println("object = " + JSON.toJSONString(object));
        }
    }

}
