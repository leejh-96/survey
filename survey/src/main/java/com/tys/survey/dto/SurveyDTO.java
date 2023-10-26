package com.tys.survey.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter@Setter
public class SurveyDTO extends SurveyInfoDTO{

    private int num;

    private String memberNickname;

    private String time;

    private boolean status;

}
