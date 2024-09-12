package com.fred.api.pojo;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @Description: UserVo
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
@Getter
@Setter
@ToString
@Schema(name = "UserVo", description = "User VO")
public class UserVo {

    @Schema(name = "id", description = "User ID")
    private Long id;

    @Schema(name = "username", description = "User Name")
    private String username;

    @Schema(name = "email", description = "Email")
    private String email;

    @Schema(name = "createdAt", description = "Created Time")
    private Date createdAt;

    @Schema(name = "updatedAt", description = "Updated Time")
    private Date updatedAt;

}
