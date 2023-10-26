package com.tys.survey.service;

import com.tys.survey.commons.PageInfo;
import com.tys.survey.dto.*;
import com.tys.survey.repository.SurveyRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SurveyService {

    private final FileUploadService fileUploadService;

    private final SurveyRepository repository;

    private final MessageSource messageSource;

    public SurveyService(FileUploadService fileUploadService, SurveyRepository repository, MessageSource messageSource) {
        this.fileUploadService = fileUploadService;
        this.repository = repository;
        this.messageSource = messageSource;
    }

    @Transactional
    public Map<String,Object> save(Map<String,Object> map, SurveyInfoDTO survey, LoginMemberDTO loginMember) {
        SurveyInfoDTO surveyInfo = createSurvey(survey, loginMember);

        repository.saveSurveyInfo(surveyInfo);
        repository.saveQuestion(surveyInfo);

        return createTrueParam(map,surveyInfo.getSurveyNo());
    }

    @Transactional
    public Map<String, Object> saveResult(Map<String, Object> map, SurveyFormDTO survey, LoginMemberDTO loginMember) {
        SurveyFormDTO result = createResult(survey, loginMember);

        repository.updateSurveyInfo(result.getSurveyNo());
        repository.saveResult(result);

        return createTrueParam(map, survey.getSurveyNo());
    }

//    @Transactional
//    public boolean deleteSurvey(LoginMemberDTO loginMember, int surveyNo) {
//        deleteSurveyFiles(repository.getFileList(loginMember,surveyNo));
//        return repository.deleteSurvey(surveyNo) > 0 ? true : false;
//    }

    @Transactional
    public int deleteSurvey(LoginMemberDTO loginMember, int surveyNo) {
        deleteSurveyFiles(repository.getFileList(loginMember,surveyNo));
        return repository.deleteSurvey(surveyNo);
    }

    private SurveyFormDTO createResult(SurveyFormDTO survey, LoginMemberDTO loginMember) {
        for (SurveyFormDTO dto : survey.getSurveyFormList()){
            dto.setSurveyNo(survey.getSurveyNo());
            dto.setMemberNo(getMemberNo(loginMember));
            dto.setResultDate(LocalDateTime.now());
        }
        return survey;
    }

    private SurveyInfoDTO createSurvey(SurveyInfoDTO survey, LoginMemberDTO loginMember){
        FileDTO file = fileUploadService.saveFile(survey.getFile());
        return SurveyInfoDTO.builder()
                            .memberNo(getMemberNo(loginMember))
                            .surveyEndDate(survey.getSurveyEndDate())
                            .surveyWriteTime(LocalDateTime.now())
                            .thumbnailName(file.getFileName())
                            .thumbnailRename(file.getRename())
                            .thumbnailPath(file.getPath())
                            .surveyTitle(survey.getSurveyTitle())
                            .surveyCount(survey.getQuestionList().size())
                            .questionList(getQuestionList(survey.getQuestionList()))
                            .build();
    }

    private List<QuestionDTO> getQuestionList(List<QuestionDTO> questionList) {
        List<QuestionDTO> list = new ArrayList<>();
        int seq = 1;
        for (QuestionDTO dto : questionList){
            FileDTO file = fileUploadService.saveFile(dto.getFile());
            list.add(QuestionDTO.builder()
                                .questionFileName(file.getFileName())
                                .questionRename(file.getRename())
                                .questionPath(file.getPath())
                                .questionSeq(seq)
                                .questionTitle(dto.getQuestionTitle())
                                .answer1(dto.getAnswer1())
                                .answer2(dto.getAnswer2())
                                .answer3(dto.getAnswer3())
                                .answer4(dto.getAnswer4())
                                .answer5(dto.getAnswer5())
                                .build());
            seq++;
        }
        return list;
    }

    public SurveyDTO getDetail(HttpServletRequest request, HttpServletResponse response, int surveyNo) {
        updateHit(request,response,surveyNo);
        SurveyDTO detail = repository.getDetail(surveyNo);
        timeAndStatus(detail);
        return detail;
    }

    public int getCount(PageInfo pageInfo) {
        return repository.getCount(pageInfo);
    }

    private int getMemberNo(LoginMemberDTO loginMember){
        return loginMember != null ? loginMember.getMemberNo() : 2;
    }

    public List<SurveyDTO> getList(PageInfo pageInfo) {
        return updateTimeAndStatus(repository.getList(pageInfo));
    }

    private List<SurveyDTO> updateTimeAndStatus(List<SurveyDTO> list) {
        for (SurveyDTO dto : list) {
            timeAndStatus(dto);
        }
        return list;
    }

    private void timeAndStatus(SurveyDTO dto) {
        LocalDateTime surveyWriteTime = dto.getSurveyWriteTime();
        LocalDateTime now = LocalDateTime.now();
        String timeAgo = timeSettings(surveyWriteTime, now);
        dto.setTime(timeAgo);

        LocalDate surveyEndDate = dto.getSurveyEndDate();
        LocalDate endDate = LocalDate.now();
        boolean status = surveyEndDate.isBefore(endDate);
        dto.setStatus(status);
    }

    private String timeSettings(LocalDateTime surveyWriteTime, LocalDateTime now) {
        Duration between = Duration.between(surveyWriteTime, now);
        long seconds = between.getSeconds();
        long minutes = between.toMinutes();
        long hours = between.toHours();
        long days = between.toDays();
        String timeAgo = "";
        if (days > 0) {
            timeAgo = days + "일 전";
        } else if (hours > 0) {
            timeAgo = hours + "시간 전";
        } else if (minutes > 0) {
            timeAgo = minutes + "분 전";
        } else {
            timeAgo = seconds + "초 전";
        }
        return timeAgo;
    }

    public Map<String, Object> duplicateVote(Map<String, Object> map, LoginMemberDTO loginMember, SurveyFormDTO survey) {
        if (repository.duplicateVote(loginMember.getMemberNo(), survey.getSurveyNo()) != 0){
            return createFailMsg(map,messageSource.getMessage("isParticipationDuplicate",null, Locale.getDefault()));
        }
        return createTrue(map);
    }

    public MainDTO getMainSurveyList() {
        MainDTO main = repository.getMainSurveyList();
        main.setHighViews(updateTimeAndStatus(main.getHighViews()));
        main.setPopularViews(updateTimeAndStatus(main.getPopularViews()));
        main.setLatelySurvey(updateTimeAndStatus(main.getLatelySurvey()));
        return main;
    }

    private void deleteSurveyFiles(List<String> fileList) {
        for (String rename : fileList){
            if (rename != null){
                fileUploadService.deleteFile(rename);
            }
        }
    }

    private void updateHit(HttpServletRequest request, HttpServletResponse response, int surveyNo) {
        Cookie[] cookies = request.getCookies();
        String cookieName = surveyNo + "_survey_cookie";
        boolean status = false;
        if (cookies != null){
            for (Cookie c : cookies){
                if (c.getName().equals(cookieName)){
                    status = true;
                    break;
                }
            }
        }
        if (!status){
            Cookie cookie = new Cookie(cookieName,"true");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);
            repository.updateHit(surveyNo);
        }
    }


    public Map<String, Object> memberCheck(LoginMemberDTO loginMember) {
        Map<String, Object> map = new HashMap<>();
        if (loginMember == null || loginMember.getMemberNo() == null){
            return createFailMsg(map,messageSource.getMessage("loginRequiredMsg",null, Locale.getDefault()));
        }
        return createTrue(map);

    }

    private Map<String, Object> createFailMsg(Map<String, Object> map, String msg){
        map.put("result", false);
        map.put("message", msg);
        return map;
    }

    private Map<String, Object> createTrueParam(Map<String, Object> map, int param){
        map.put("result", true);
        map.put("param", param);
        return map;
    }

    private Map<String, Object> createTrue(Map<String, Object> map){
        map.put("result", true);
        return map;
    }
}
