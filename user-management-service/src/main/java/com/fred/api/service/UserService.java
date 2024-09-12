package com.fred.api.service;

import java.util.List;

import com.fred.api.pojo.UserEditDto;
import com.fred.api.pojo.UserRegisterDto;
import com.fred.common.dao.domain.User;

/**
 * 
 * @Description: All User operations
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
public interface UserService {

    /**
     * Get one by ID
     * 
     * @param userId
     * @return
     */
    User getById(Long userId);

    /**
     * Register User
     * 
     * @param userDto
     * @return
     */
    User registerUser(UserRegisterDto userDto);

    /**
     * Check existence of email address
     * 
     * @param email
     */
    void checkEmailExisted(String email);

    /**
     * Get one by email
     * 
     * @param email
     * @return
     */
    User getUserByEmail(String email);

    /**
     * Find all users
     * 
     * @return
     */
    List<User> findAllUsers();

    /**
     * Update user info
     * 
     * @param userDto
     * @return
     */
    User updateUser(UserEditDto userDto);

    /**
     * Delete user by id
     * 
     * @param userId
     */
    void deleteUser(Long userId);

    /**
     * Delete user by multiple ids
     * 
     * @param userIds
     */
    void deleteUsers(Long[] userIds);

    /**
     * Delete user by email
     * 
     * @param email
     */
    void deleteUserByEmail(String email);

    /**
     * Delete user by multiple emails
     * 
     * @param emails
     */
    void deleteUsers(String[] emails);

    /**
     * Delete all
     */
    void clean();

}
