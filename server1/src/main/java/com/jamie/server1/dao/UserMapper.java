package com.jamie.server1.dao;

import com.github.pagehelper.Page;
import com.jamie.server1.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/6 19:27
 * @description
 */
public interface UserMapper {
    User findUserById(User user);

    List<User> findUserByNamePage(User user);

    List<User> findAllUser();
}
