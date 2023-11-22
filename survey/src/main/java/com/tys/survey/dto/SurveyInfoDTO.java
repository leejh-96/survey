package com.tys.survey.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter@Setter@Builder
public class SurveyInfoDTO {

    private Integer surveyNo;

    private Integer memberNo;

    private String surveyTitle;

    private Integer surveyCount;

    private MultipartFile file;

    private String thumbnailName;

    private String thumbnailRename;

    private String thumbnailPath;

    private String surveyPublic;

    private Integer surveyHit;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate surveyDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate surveyEndDate;

    private LocalDateTime surveyWriteTime;

    private String time;

    private Integer resultCount;

    List<QuestionDTO> questionList;

    public SurveyInfoDTO() {
    }
}
