package com.bce.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.bce.domain.OauthRefreshToken;

public interface OauthRefereshTokenRepository  extends MongoRepository<OauthRefreshToken, String>{
	OauthRefreshToken findByTokenId(@Param("tokenId") String tokenId);
}
