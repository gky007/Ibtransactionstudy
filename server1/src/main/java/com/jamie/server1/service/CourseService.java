package com.jamie.server1.service;

import com.jamie.server1.dao.CourseMapper;
import com.jamie.server1.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/6 14:55
 * @description
 */

@Service
public class CourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Transactional
    public Course findCourseById(int id){
        return courseMapper.findCourseById(id);
    }

}
