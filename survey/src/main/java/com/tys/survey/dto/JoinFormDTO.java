package com.tys.survey.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter@Setter
public class JoinFormDTO extends PasswordDTO{

    @Length(min = 2, max = 10)
    private String memberNickname;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String memberEmail;

    @NotBlank
    private String certified;

    private LocalDateTime memberDate;
}
