package com.fred.common.dao.domain;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @Description: BaseEntity
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
@MappedSuperclass
@EntityListeners(BasicEntityListener.class)
@Getter
@Setter
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigserial")
    protected Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", columnDefinition = "timestamp null default current_timestamp")
    protected Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", columnDefinition = "timestamp null default current_timestamp")
    protected Date updatedAt;

    // 1 stands for deleted
    @Column(name = "deleted", nullable = true, columnDefinition = "int default 0")
    private Integer deleted;

}
