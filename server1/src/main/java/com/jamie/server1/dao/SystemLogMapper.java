package com.jamie.server1.dao;

import com.jamie.server1.entity.SystemLog;
import org.springframework.stereotype.Repository;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/8 16:40
 * @description 定义记录日志接口
 */
public interface SystemLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(SystemLog record);

    int insertSelective(SystemLog record);

    SystemLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SystemLog record);

    int updateByPrimaryKey(SystemLog record);
}
