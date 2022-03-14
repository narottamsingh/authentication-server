package com.bce.oauth.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bce.enume.StatusCode;
import com.bce.oauth.entity.Login;

/**
 * @author NAROTTAMS
 * @created_date Sep 20, 2020
 * @modify_by NAROTTAMS
 * @modify_time
 */
@Repository("login")
public interface LoginRepository extends JpaRepository<Login, Long> {

	Login findByEmailId(@Param("email_id") String email_id);

	Login findByMobile(@Param("mobile") String mobile);

	Login findByEmailIdAndPassword(@Param("email_id") String email_id, @Param("password") String password);

	@Transactional
	@Modifying
	@Query("update #{#entityName} l set l.verifyEmail=:statusCode, l.loginStatus=:statusCode where l.ID=:ID")
	int updateLoginSetVerifyEmailForId(@Param("statusCode") StatusCode statusCode, @Param("ID") Long ID);

}
