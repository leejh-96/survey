package com.tys.survey.repository;

import com.tys.survey.dto.JoinFormDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JoinRepository {

    void save(JoinFormDTO joinFormDTO);

    void updatePassword(JoinFormDTO joinFormDTO);

    int duplicateNickName(String nickName);

    int duplicateEmail(String memberId);

}
