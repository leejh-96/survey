package com.tys.survey.repository;

import com.tys.survey.dto.LoginMemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginRepository {
    LoginMemberDTO login(@Param("memberId") String memberId);
}
