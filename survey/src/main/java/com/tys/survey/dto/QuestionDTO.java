package com.tys.survey.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter@Setter@Builder
public class QuestionDTO {

    private Integer questionNo;

    private Integer surveyNo;

    private Integer questionSeq;

    private MultipartFile file;

    private String questionFileName;

    private String questionRename;

    private String questionPath;

    private String questionTitle;

    private String answer1;

    private String answer2;

    private String answer3;

    private String answer4;

    private String answer5;

    private Integer answer1Count;

    private Integer answer2Count;

    private Integer answer3Count;

    private Integer answer4Count;

    private Integer answer5Count;

    public QuestionDTO() {
    }
}
