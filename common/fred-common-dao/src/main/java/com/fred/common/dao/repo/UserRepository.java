package com.fred.common.dao.repo;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.fred.common.dao.domain.User;

/**
 * 
 * @Description: UserRepository
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepositoryImplementation<User, Long> {

}
