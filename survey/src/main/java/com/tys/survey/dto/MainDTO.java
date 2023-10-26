package com.tys.survey.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
public class MainDTO {

    private int surveyCount;//survey 카운트

    private List<SurveyDTO> highViews;//설문 조회수

    private List<SurveyDTO> popularViews;//설문 참여도

    private List<SurveyDTO> latelySurvey;//최신 설문조사

    private List<NoticeListDTO> noticeList;//공지사항 리스트
}
