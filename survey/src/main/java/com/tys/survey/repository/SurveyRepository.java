package com.tys.survey.repository;

import com.tys.survey.commons.PageInfo;
import com.tys.survey.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SurveyRepository {

    void saveSurveyInfo(SurveyInfoDTO surveyInfo);

    void saveQuestion(SurveyInfoDTO surveyInfo);

    void saveResult(SurveyFormDTO survey);

    void updateHit(int surveyNo);

    void updateSurveyInfo(int surveyNo);

    int deleteSurvey(int surveyNo);

    int getCount(PageInfo pageInfo);

    int duplicateVote(@Param("memberNo") Integer memberNo,@Param("surveyNo") int surveyNo);

    SurveyDTO getDetail(int surveyNo);

    MainDTO getMainSurveyList();

    List<SurveyDTO> getList(PageInfo pageInfo);

    List<String> getFileList(@Param("loginMember") LoginMemberDTO loginMember, @Param("surveyNo") int surveyNo);

}
