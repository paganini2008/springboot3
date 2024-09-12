package com.fred.common.dao.domain;

import org.hibernate.annotations.DynamicInsert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @Description: User
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
@Getter
@Setter
@DynamicInsert
@Entity
@Table(name = "fred_user")
public class User extends BaseEntity {

    @Column(name = "username", nullable = true, length = 255)
    private String username;

    @Column(name = "password", nullable = true, length = 255)
    private String password;

    @Column(name = "email", nullable = true, length = 255)
    private String email;

    // 1 stands for activated
    @Column(name = "activated", nullable = true, columnDefinition = "int default 0")
    private Integer activated;

}
