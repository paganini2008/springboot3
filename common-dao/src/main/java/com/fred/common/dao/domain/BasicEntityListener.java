package com.fred.common.dao.domain;

import java.util.Date;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: BasicEntityListener
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@Slf4j
public class BasicEntityListener {

    @PrePersist
    public void prePersist(Object bean) {
        if (!(bean instanceof BaseEntity)) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Persist entity: " + bean);
        }
        BaseEntity entity = (BaseEntity) bean;
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(new Date());
        }
        if (entity.getUpdatedAt() == null) {
            entity.setUpdatedAt(new Date());
        }
    }

    @PreUpdate
    public void preUpdate(Object bean) {
        if (!(bean instanceof BaseEntity)) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Update entity: " + bean);
        }
        BaseEntity entity = (BaseEntity) bean;
        entity.setUpdatedAt(new Date());
    }

    @PreRemove
    public void preRemove(Object bean) {
        if (!(bean instanceof BaseEntity)) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove entity: " + bean);
        }
    }

}
