package com.tys.survey.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter@Setter@Builder
public class NoticeFormDTO {

    private int memberNo;

    private int boardNo;

    @NotBlank
    private String boardTitle;

    @NotBlank
    private String boardMarkDown;

    private String boardContent;

    private LocalDateTime boardWriteTime;

    public NoticeFormDTO() {
    }

    public NoticeFormDTO(int memberNo, int boardNo, String boardTitle, String boardMarkDown, String boardContent, LocalDateTime boardWriteTime) {
        this.memberNo = memberNo;
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardMarkDown = boardMarkDown;
        this.boardContent = boardContent;
        this.boardWriteTime = boardWriteTime;
    }
}
