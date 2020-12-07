package com.jamie.server1.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.jamie.server1.constant.Constant;
import com.jamie.server1.dao.UserMapper;
import com.jamie.server1.entity.User;
import io.netty.util.internal.ObjectUtil;
import org.hibernate.boot.model.naming.ImplicitAnyDiscriminatorColumnNameSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/6 14:55
 * @description
 */

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private  RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public User findUserById(User user){
        return userMapper.findUserById(user);
    }

    @Transactional
    public List<User> findUserByNamePage(User user, int pageNum, int pageSize){
        List<User> userList = new ArrayList<>();
        int size = 0;
        BoundValueOperations<String, Object> boundValueOps = redisTemplate.boundValueOps(
                Constant.findUserByNamePrefix + "/" + user.getFirstName() + "/"+ pageNum + "/" + pageSize);
        Object object = boundValueOps.get();
        if (object == null) {
            userList = userMapper.findUserByNamePage(user);
            String userListJSONString = JSON.toJSONString(userList);
            boundValueOps.set(userListJSONString);
        } else {
            userList = JSONObject.parseArray((String) object, User.class);
            System.out.println("redis缓存！");
        }
        return userList;
    }
}
