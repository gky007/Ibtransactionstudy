package com.jamie.server1.controller;

import com.jamie.server1.entity.Course;
import com.jamie.server1.utils.ResponseData;
import com.jamie.server1.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/6 14:43
 * @description
 */
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping("getCourse/{id}")
    @ResponseBody
    public ResponseData<Course> findById(@PathVariable int id){
        ResponseData<Course> responseData= new ResponseData<Course>();
        Course course = courseService.findCourseById(id);
        responseData.setData(course);
        responseData.setReturnCode("0");
        responseData.setReturnStatus("successful");
        return responseData;
    }
}
