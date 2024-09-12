package com.fred.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fred.api.pojo.UserEditDto;
import com.fred.api.pojo.UserRegisterDto;
import com.fred.api.pojo.UserVo;
import com.fred.api.service.UserService;
import com.fred.common.dao.domain.User;
import com.fred.common.utils.ApiResult;
import com.fred.common.utils.BeanCopyUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 
 * @Description: UserController
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
@Validated
@Tag(name = "User Management Restful API", description = "User Management Restful API")
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Find all users", description = "Find all users")
    @ApiResponse(responseCode = "200", description = "Find all users")
    @GetMapping
    public ApiResult<List<UserVo>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ApiResult.ok(BeanCopyUtils.copyBeanList(users, UserVo.class));
    }

    @Operation(summary = "Get user by id", description = "Get user by id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Get user information by id"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    @GetMapping("/{userId}")
    public ApiResult<UserVo> getUser(
            @Parameter(description = "User ID") @PathVariable("userId") Long userId) {
        User user = userService.getById(userId);
        return ApiResult.ok(BeanCopyUtils.copyBean(user, UserVo.class));
    }

    @Operation(summary = "Get user by email", description = "Get user by email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get user information by email"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    @GetMapping("/by")
    public ApiResult<UserVo> getUserByEmail(
            @Parameter(description = "Email") @RequestParam("email") String email) {
        User user = userService.getUserByEmail(email);
        return ApiResult.ok(BeanCopyUtils.copyBean(user, UserVo.class));
    }

    @Operation(summary = "Register new user", description = "Register new user")
    @ApiResponse(responseCode = "200", description = "Register new user")
    @PostMapping("/register")
    public ApiResult<UserVo> registerUser(@Parameter(description = "User Json Object",
            required = true) @Validated @RequestBody UserRegisterDto userDto) {
        User user = userService.registerUser(userDto);
        return ApiResult.ok(BeanCopyUtils.copyBean(user, UserVo.class));
    }

    @Operation(summary = "Delete user by id", description = "Delete user by id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Delete user by id"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    @DeleteMapping("/{userId}")
    public ApiResult<Boolean> deleteUser(
            @Parameter(description = "User ID") @PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ApiResult.ok(true);
    }

    @Operation(summary = "Delete user by email", description = "Delete user by email")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Delete user by email"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    @DeleteMapping("/by")
    public ApiResult<Boolean> deleteUserByEmail(
            @Parameter(description = "Email") @RequestParam("email") String email) {
        userService.deleteUserByEmail(email);
        return ApiResult.ok(true);
    }

    @Operation(summary = "Update user by id", description = "Update user by id")
    @ApiResponse(responseCode = "200", description = "Update user information")
    @PutMapping("/update")
    public ApiResult<UserVo> updateUser(@Validated @RequestBody UserEditDto userDto) {
        User user = userService.updateUser(userDto);
        return ApiResult.ok(BeanCopyUtils.copyBean(user, UserVo.class));
    }

}
