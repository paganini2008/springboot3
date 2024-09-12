package com.example.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.UserManagementApplication;
import com.example.dao.model.User;
import com.example.pojo.UserEditDto;
import com.example.pojo.UserRegisterDto;
import com.example.pojo.UserVo;
import com.example.service.UserService;
import com.example.utils.ApiResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @Description: UserControllerTest
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@SpringBootTest(classes = UserManagementApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @BeforeAll
    public void setUp() {
        userService.clean();
    }

    @Test
    @Order(1)
    public void testRegisterUser() throws Exception {
        UserRegisterDto registerDto = new UserRegisterDto();
        registerDto.setUsername("tester1");
        registerDto.setPassword("123456");
        registerDto.setEmail("tester1@test.com");
        String jsonStr = objectMapper.writeValueAsString(registerDto);
        String contentStr = mockMvc
                .perform(post("/user/register").content(jsonStr)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ApiResult<UserVo> apiResult =
                objectMapper.readValue(contentStr, new TypeReference<ApiResult<UserVo>>() {});
        Assertions.assertTrue(apiResult.getData() instanceof UserVo);
    }

    @Test
    @Order(2)
    public void testRegisterUserWhenEmailExisted() throws Exception {
        UserRegisterDto registerDto = new UserRegisterDto();
        registerDto.setUsername("tester1");
        registerDto.setPassword("123456");
        registerDto.setEmail("tester1@test.com");
        String jsonStr = objectMapper.writeValueAsString(registerDto);
        String contentStr = mockMvc
                .perform(post("/user/register").content(jsonStr)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(contentStr.contains("Email already existed"), contentStr);
    }

    @Test
    @Order(3)
    public void testUpdateUser() throws Exception {
        final String newName = "tester123";
        User user = userService.getUserByEmail("tester1@test.com");
        UserEditDto userEditDto = new UserEditDto();
        userEditDto.setId(user.getId());
        userEditDto.setUsername(newName);
        String jsonStr = objectMapper.writeValueAsString(userEditDto);
        String contentStr = mockMvc
                .perform(put("/user/update").content(jsonStr)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ApiResult<UserVo> apiResult =
                objectMapper.readValue(contentStr, new TypeReference<ApiResult<UserVo>>() {});
        Assertions.assertTrue(apiResult.getData().getUsername().equals(newName), contentStr);
    }

    @Test
    @Order(4)
    public void testGetUserByEmail() throws Exception {
        String contentStr = mockMvc.perform(get("/user/by").param("email", "tester1@test.com"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ApiResult<UserVo> apiResult =
                objectMapper.readValue(contentStr, new TypeReference<ApiResult<UserVo>>() {});
        Assertions.assertTrue(apiResult.getData() != null, contentStr);
    }

    @Test
    @Order(5)
    public void testDeleteUserByEmail() throws Exception {
        String contentStr = mockMvc.perform(delete("/user/by").param("email", "tester1@test.com"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ApiResult<Boolean> apiResult =
                objectMapper.readValue(contentStr, new TypeReference<ApiResult<Boolean>>() {});
        Assertions.assertTrue(apiResult.getData(), contentStr);
    }

    @Test
    @Order(6)
    public void testGetUserByEmailIfNotExisted() throws Exception {
        String contentStr = mockMvc.perform(get("/user/by").param("email", "tester1@test.com"))
                .andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(contentStr.contains("User doesn't exist"), contentStr);
    }
}
