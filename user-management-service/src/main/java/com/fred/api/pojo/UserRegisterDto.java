package com.fred.api.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @Description: UserRegisterDto
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
@Getter
@Setter
@ToString
@Schema(name = "UserRegisterDto", description = "User Register Dto")
public class UserRegisterDto {

    @NotBlank(message = "Username must be required")
    @Size(min = 6, max = 32)
    @Schema(name = "username", description = "User Name")
    private String username;

    @NotBlank(message = "Password must be required")
    @Size(min = 6, max = 32)
    @Schema(name = "password", description = "Password")
    private String password;

    @NotBlank(message = "Email must be required")
    @Email(message = "Email has invalid format", regexp = "^(.+)@(.+)$")
    @Schema(name = "email", description = "Email")
    private String email;

    @Schema(name = "sendEmail", description = "Send welcome email or not", defaultValue = "false")
    private Boolean sendEmail = false;

}
