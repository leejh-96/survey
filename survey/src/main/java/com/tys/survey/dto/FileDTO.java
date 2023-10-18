package com.tys.survey.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class FileDTO {

    private Integer fileNo;

    private Integer referenceNo;

    private String fileName;

    private String rename;

    private String path;

    public FileDTO() {
    }

    public FileDTO(Integer fileNo, Integer referenceNo, String fileName, String rename, String path) {
        this.fileNo = fileNo;
        this.referenceNo = referenceNo;
        this.fileName = fileName;
        this.rename = rename;
        this.path = path;
    }
}
