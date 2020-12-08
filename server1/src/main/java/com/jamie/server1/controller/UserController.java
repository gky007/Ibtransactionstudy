package com.jamie.server1.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jamie.server1.annotation.GkyLog;
import com.jamie.server1.utils.PageResultData;
import com.jamie.server1.utils.ResponseData;
import com.jamie.server1.entity.User;
import com.jamie.server1.service.UserService;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/6 14:43
 * @description
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("getUserById/{id}")
    @ResponseBody
    @GkyLog(operationType="查询", operationName="通过用户名id查询用户")
    public ResponseData<User> findById(@PathVariable int id){
        ResponseData<User> responseData= new ResponseData<User>();
        User user = new User();
        user.setId(id);
        responseData.setData(userService.findUserById(user));
        responseData.setReturnCode("0");
        responseData.setReturnStatus("successful");
        return responseData;
    }

    @RequestMapping("getUserByNamePage/{name}/{pageNum}/{pageSize}")
    @ResponseBody
    @GkyLog(operationType="查询", operationName="通过用户名查询用户")
    public PageResultData<User> findUserByNamePage(@PathVariable String name, @PathVariable int pageNum, @PathVariable int pageSize){
        PageResultData<User> responseData= new PageResultData<User>();
        User user = new User();
        user.setFirstName(name);
        //pageNum:表示第几页  pageSize:表示一页展示的数据
        PageHelper.startPage(pageNum, pageSize);
        // 设置分页查询条件
        PageInfo<User> pageInfo = new PageInfo<User>(userService.findUserByNamePage(user, pageNum, pageSize));
        // 获取查询结果
        responseData.setReturnCode("0");
        responseData.setReturnStatus("successful");
        responseData.setPageInfo(pageInfo);
        return responseData;
    }

    @RequestMapping(value = "/findAllUser", method=RequestMethod.POST)
    @ResponseBody
    public ResponseData<User> findUserById(@RequestBody User user){
        ResponseData<User> responseData= new ResponseData<User>();
        responseData.setData(userService.findUserById(user));
        responseData.setReturnCode("0");
        responseData.setReturnStatus("successful");
        return responseData;
    }
}
