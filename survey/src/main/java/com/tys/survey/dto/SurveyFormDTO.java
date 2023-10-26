package com.tys.survey.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
@ToString
@Getter@Setter
public class SurveyFormDTO {

    private int surveyNo;

    private int memberNo;

    private Integer questionNo;

    private Integer answerNo;

    private LocalDateTime resultDate;

    private List<SurveyFormDTO> surveyFormList;

    public SurveyFormDTO() {
    }

}
