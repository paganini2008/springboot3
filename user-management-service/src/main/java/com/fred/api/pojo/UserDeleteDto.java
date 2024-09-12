package com.fred.api.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @Description: UserDeleteDto
 * @Author: Fred Feng
 * @Date: 12/09/2024
 * @Version 1.0.0
 */
@Getter
@Setter
@ToString
@Schema(name = "UserDeleteDto", description = "User Dto for batch delete")
public class UserDeleteDto {

    @Schema(name = "ids", description = "User IDs")
    private Long[] ids;

    @Schema(name = "emails", description = "User Emails")
    private String[] emails;

}
