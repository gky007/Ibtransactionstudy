package com.jamie.server1.dao;

import com.jamie.server1.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/6 14:59
 * @description
 */
@Repository
public interface CourseMapper {
    Course findCourseById(@Param("id") int id);
}
