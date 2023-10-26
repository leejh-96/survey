package com.tys.survey.repository;

import com.tys.survey.dto.LoginMemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyPageRepository {

    void updatePassword(LoginMemberDTO loginMember);

    int deleteMember(LoginMemberDTO loginMember);

    List<String> getFileList(Integer memberNo);
}
