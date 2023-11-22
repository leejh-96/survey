package com.tys.survey.dto;

import com.tys.survey.commons.TimeUpdatable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString(callSuper = true)
@Getter@Setter
public class SurveyDTO extends SurveyInfoDTO implements TimeUpdatable{

    private int num;

    private String memberNickname;

    private String time;

    private boolean status;

    @Override
    public LocalDateTime getCreateTime() {
        return super.getSurveyWriteTime();
    }

   @Override
    public void setCreateTime(String timeAgo) {
        this.setTime(timeAgo);
    }
}
