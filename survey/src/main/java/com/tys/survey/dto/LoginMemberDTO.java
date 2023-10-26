package com.tys.survey.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter@Setter@Builder
public class LoginMemberDTO {

    private int count;

    private Integer memberNo;

    private String memberNickname;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String memberId;

    private String memberRole;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$")
    private String memberPassword;

    public LoginMemberDTO() {
    }

    public LoginMemberDTO(int count, Integer memberNo, String memberNickname, String memberId, String memberRole, String memberPassword) {
        this.count = count;
        this.memberNo = memberNo;
        this.memberNickname = memberNickname;
        this.memberId = memberId;
        this.memberRole = memberRole;
        this.memberPassword = memberPassword;
    }
}
