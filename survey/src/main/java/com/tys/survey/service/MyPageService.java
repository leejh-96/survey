package com.tys.survey.service;

import com.tys.survey.dto.LoginMemberDTO;
import com.tys.survey.dto.PasswordDTO;
import com.tys.survey.repository.MyPageRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyPageService {

    private final FileUploadService fileUploadService;

    private final JoinService joinService;

    private final MyPageRepository myPageRepository;

    public MyPageService(FileUploadService fileUploadService, JoinService joinService, MyPageRepository myPageRepository) {
        this.fileUploadService = fileUploadService;
        this.joinService = joinService;
        this.myPageRepository = myPageRepository;
    }

    public void updatePassword(LoginMemberDTO loginMember, PasswordDTO passwordDTO) {
        String encryptPassword = joinService.encryptPassword(passwordDTO.getMemberPassword());
        loginMember.setMemberPassword(encryptPassword);
        myPageRepository.updatePassword(loginMember);
    }

    public Map<String, Object> deleteMember(LoginMemberDTO loginMember, String leave) {
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isEmpty(leave) || !leave.equals("회원탈퇴")) {
            map.put("result", false);
        }else {
            deleteAllFiles(myPageRepository.getFileList(loginMember.getMemberNo()));
            myPageRepository.deleteMember(loginMember);
            map.put("result",true);
        }
        return map;
    }

    private void deleteAllFiles(List<String> fileList) {
        for (String file : fileList){
            if (file != null){
                fileUploadService.deleteFile(file);
            }
        }
    }

}
