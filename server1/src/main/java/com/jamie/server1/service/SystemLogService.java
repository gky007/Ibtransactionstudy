package com.jamie.server1.service;

import com.jamie.server1.dao.SystemLogMapper;
import com.jamie.server1.entity.SystemLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/8 16:41
 * @description
 */
@Service
public class SystemLogService {
    @Resource
    private SystemLogMapper systemLogMapper;

    public int deleteSystemLog(String id) {
        return systemLogMapper.deleteByPrimaryKey(id);
    }


    public int insert(SystemLog record) {
        return systemLogMapper.insertSelective(record);
    }

    public SystemLog selectSystemLog(String id) {
        return systemLogMapper.selectByPrimaryKey(id);
    }

    public int updateSystemLog(SystemLog record) {
        return systemLogMapper.updateByPrimaryKeySelective(record);
    }

    public int insertTest(SystemLog record) {
        return systemLogMapper.insert(record);
    }

    public int updateByPrimaryKey(SystemLog record) {
        return systemLogMapper.updateByPrimaryKey(record);
    }
}
