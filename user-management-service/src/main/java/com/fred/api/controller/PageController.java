package com.fred.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fred.api.pojo.UserVo;
import com.fred.api.service.UserService;
import com.fred.common.dao.domain.User;
import com.fred.common.utils.BeanCopyUtils;

/**
 * 
 * @Description: Web UI entry
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@RequestMapping("/ui")
@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("userList", BeanCopyUtils.copyBeanList(users, UserVo.class));
        return "user_list";
    }

}
