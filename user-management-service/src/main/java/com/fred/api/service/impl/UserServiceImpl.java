package com.fred.api.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.fred.api.UserErrorCodes;
import com.fred.api.pojo.UserEditDto;
import com.fred.api.pojo.UserRegisterDto;
import com.fred.api.service.UserService;
import com.fred.api.utils.EmailMessage;
import com.fred.api.utils.SpringEmailService;
import com.fred.common.dao.domain.User;
import com.fred.common.dao.repo.UserRepository;
import com.fred.common.utils.BizException;

import jakarta.persistence.EntityNotFoundException;

/**
 * 
 * @Description: UserServiceImpl
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private SpringEmailService springEmailService;

    @Override
    public User getUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("email",
                ExampleMatcher.GenericPropertyMatchers.exact());
        Optional<User> optional = userDao.findOne(Example.of(user, matcher));
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Email '" + email + "' is not existed");
        }
        user = optional.get();
        checkDeleted(user);
        return user;
    }

    @Override
    public User getById(Long userId) {
        User user = userDao.getReferenceById(userId);
        checkDeleted(user);
        return user;
    }

    private void checkDeleted(User user) {
        if (user.getDeleted() == 1) {
            throw new EntityNotFoundException("UserId '" + user.getId() + "' has been deleted");
        }
    }

    @Override
    public User registerUser(UserRegisterDto userDto) {
        checkEmailExisted(userDto.getEmail());
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user = userDao.saveAndFlush(user);
        if (userDto.getSendEmail()) {
            sendWelcomeEmail(user);
        }
        return user;
    }

    private void sendWelcomeEmail(User user) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setSubject("WELCOME NEW USER!");
        emailMessage.setFrom("no-reply@consoleconnect.com");
        emailMessage.setTo(new String[]{user.getEmail()});
        emailMessage.setTemplate("<h1>Welcome to here: ${username}</h1>");
        emailMessage.setVariables(Collections.singletonMap("username", user.getUsername()));
        springEmailService.sendHtmlEmail(emailMessage);
    }

    @Override
    public void checkEmailExisted(String email) {
        long result = userDao.count((r, q, c) -> c.equal(r.get("email"), email));
        if (result > 0) {
            throw new BizException(UserErrorCodes.EMAIL_EXISTED, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public User updateUser(UserEditDto userDto) {
        User user = userDao.getReferenceById(userDto.getId());
        user.setUsername(userDto.getUsername());
        return userDao.saveAndFlush(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userDao.getReferenceById(userId);
        user.setDeleted(1);// soft delete
        userDao.saveAndFlush(user);
    }

    @Override
    public void deleteUsers(Long[] userIds) {
        List<User> users = userDao.findAll((r, q, c) -> c.in(r.get("id")).in(Arrays.asList(userIds)));
        users.forEach(u -> u.setDeleted(1));
        userDao.saveAllAndFlush(users);
    }

    @Override
    public void deleteUsers(String[] emails) {
        List<User> users = userDao.findAll((r, q, c) -> c.in(r.get("email")).in(Arrays.asList(emails)));
        users.forEach(u -> u.setDeleted(1));
        userDao.saveAllAndFlush(users);
    }

    @Override
    public void deleteUserByEmail(String email) {
        User user = getUserByEmail(email);
        user.setDeleted(1);
        userDao.saveAndFlush(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAll((r, q, c) -> c.equal(r.get("deleted"), 0));
    }

    @Override
    public void clean() {
        userDao.deleteAll();
    }

}
