package com.fred.api.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @Description: UserEditDto
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
@Getter
@Setter
@ToString
@Schema(name = "UserEditDto", description = "User Edit Dto")
public class UserEditDto {

    @NotNull(message = "ID must be required")
    @Schema(name = "id", description = "User ID")
    private Long id;

    @NotBlank(message = "Username must be required")
    @Size(min = 6, max = 32)
    @Schema(name = "username", description = "User Name")
    private String username;

}
