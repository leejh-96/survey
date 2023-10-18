package com.tys.survey.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter@Setter
public class NoticeListDTO {

    private int num;

    private Integer boardNo;

    private String memberNickname;

    private String boardTitle;

    private String boardContent;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate boardDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate boardUpdate;

    private LocalDateTime boardWriteTime;

    private String time;

    private int boardHit;
}
