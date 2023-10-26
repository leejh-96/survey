package com.tys.survey.service;

import com.tys.survey.dto.JoinFormDTO;
import com.tys.survey.repository.JoinRepository;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class JoinService {

    private final JoinRepository repository;

    private final MessageSource messageSource;

    public JoinService(JoinRepository repository, MessageSource messageSource) {
        this.repository = repository;
        this.messageSource = messageSource;
    }

    public void save(JoinFormDTO joinFormDTO) {
        String encryptPassword = encryptPassword(joinFormDTO.getMemberPassword());
        joinFormDTO.setMemberPassword(encryptPassword);
        joinFormDTO.setMemberDate(LocalDateTime.now());
        repository.save(joinFormDTO);
    }

    public void updatePassword(JoinFormDTO joinFormDTO) {
        String encryptPassword = encryptPassword(joinFormDTO.getMemberPassword());
        joinFormDTO.setMemberPassword(encryptPassword);
        repository.updatePassword(joinFormDTO);
    }

    public int duplicateEmail(String memberId){
        return repository.duplicateEmail(memberId);
    }

    public int duplicateNickName(String nickName){
        return repository.duplicateNickName(nickName);
    }

    public boolean nickNameCheck(String nickName) {
        return duplicateNickName(nickName) == 0;
    }

    public String encryptPassword(String password) {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        return encryptedPassword;
    }

    public boolean checkPassword(String password, String encryptedPassword) {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        return passwordEncryptor.checkPassword(password, encryptedPassword);
    }

    public Map<String,Object> emailCheck(String memberId) {
        Map<String,Object> map = new HashMap<>();
        if (duplicateEmail(memberId) != 0){
            return createJoinParam(map,false,messageSource.getMessage("isEmailAlreadyRegistered",null, Locale.getDefault()));
        }
        return createJoinStatus(map,true);
    }

    private Map<String,Object> createJoinStatus(Map<String,Object> map, boolean status){
        map.put("result",status);
        return map;
    }

    private Map<String,Object> createJoinParam(Map<String,Object> map, boolean status, String param){
        map.put("result",status);
        map.put("param",param);
        return map;
    }

}
