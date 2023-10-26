package com.tys.survey.validation;

import com.tys.survey.dto.QuestionDTO;
import com.tys.survey.dto.SurveyFormDTO;
import com.tys.survey.dto.SurveyInfoDTO;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class SurveyFormValidation {

    private final MessageSource messageSource;

    public SurveyFormValidation(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Map<String, Object> takeSurveyValid(SurveyFormDTO survey) {
        Map<String, Object> map = new HashMap<>();
        List<SurveyFormDTO> list = survey.getSurveyFormList();

        for (SurveyFormDTO dto : list) {
            if (dto.getQuestionNo() == null || dto.getAnswerNo() == null) {
                return createValidFail(map,messageSource.getMessage("uncheckedSurveyMsg", null, Locale.getDefault()));
            }
        }
        return createValidTrue(map);
    }

    public Map<String,Object> makeSurveyValid(SurveyInfoDTO survey){
        Map<String,Object> map = new HashMap<>();
        return dateValidation(map, survey);
    }

    private Map<String,Object> dateValidation(Map<String,Object> map, SurveyInfoDTO survey){
        LocalDate now = LocalDate.now();
        LocalDate maxDate = now.plusDays(29);
        LocalDate surveyEndDate = survey.getSurveyEndDate();
        if (surveyEndDate == null) {
            return createValidFail(map,messageSource.getMessage("endDateRequiredMsg", null, Locale.getDefault()));
        } else if (surveyEndDate.isBefore(now) || surveyEndDate.isAfter(maxDate)) {
            return createValidFail(map,messageSource.getMessage("endDateRangeMsg", null, Locale.getDefault()));
        }
        return textValidation(map,survey);
    }

    private Map<String,Object> textValidation(Map<String, Object> map,SurveyInfoDTO survey){
        if (textIsNull(survey.getSurveyTitle())){
            return createValidFail(map,messageSource.getMessage("isNullMsg", null, Locale.getDefault()));
        } else if (!titleLengthCheck(survey.getSurveyTitle())) {
            return createValidFail(map,messageSource.getMessage("lengthMsg1", null, Locale.getDefault()));
        }
        return questionValidation(map,survey);
    }
    private Map<String, Object> questionValidation(Map<String, Object> map,SurveyInfoDTO survey) {
        List<QuestionDTO> questionList = survey.getQuestionList();
        for (int i = 0; i < questionList.size(); i++) {
            QuestionDTO questionDTO = questionList.get(i);
            String questionTitle = questionDTO.getQuestionTitle();

            if (textIsNull(questionTitle)) {
                return createValidFail(map,messageSource.getMessage("isNullMsg", null, Locale.getDefault()));
            } else if (questionTitle.length() > 400) {
                return createValidFail(map,messageSource.getMessage("lengthMsg2", null, Locale.getDefault()));
            }

            String[] answers = {
                    questionDTO.getAnswer1(),
                    questionDTO.getAnswer2(),
                    questionDTO.getAnswer3(),
                    questionDTO.getAnswer4(),
                    questionDTO.getAnswer5()
            };

            for (int j = 0; j < answers.length; j++) {
                if (answers[j] != null) {
                    if (answers[j].isEmpty()) {
                        return createValidFail(map,messageSource.getMessage("isNullMsg", null, Locale.getDefault()));
                    } else if (answers[j].length() > 400) {
                        return createValidFail(map,messageSource.getMessage("lengthMsg2", null, Locale.getDefault()));
                    }
                }
            }
        }
        return fileValidation(map,survey);
    }

    private Map<String, Object> fileValidation(Map<String, Object> map, SurveyInfoDTO survey) {
        if(!StringUtils.isEmpty(survey.getFile().getOriginalFilename())){
            String originalFilename = survey.getFile().getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.indexOf("."));
            if (!extValidation(ext)){
                return createValidFail(map,messageSource.getMessage("unSupportFileExtMsg", null, Locale.getDefault()));
            }
        }
        return filesValidation(map,survey.getQuestionList());
    }

    private Map<String, Object> filesValidation(Map<String, Object> map,List<QuestionDTO> questionList){
        for (QuestionDTO dto : questionList){
            if (!StringUtils.isEmpty(dto.getFile().getOriginalFilename())){
                String originalFilename = dto.getFile().getOriginalFilename();
                String ext = originalFilename.substring(originalFilename.indexOf("."));
                if (!extValidation(ext)){
                    return createValidFail(map,messageSource.getMessage("unSupportFileExtMsg", null, Locale.getDefault()));
                }
            }
        }
        return createValidTrue(map);
    }

    private boolean extValidation(String ext){
        String[] supportExt = {".jpeg", ".jpg", ".png", ".gif"};
        for (String supportedExt : supportExt) {
            if (supportedExt.equals(ext)) {
                return true;
            }
        }
        return false;
    }

    private boolean titleLengthCheck(String title){
        int maxLength = 100;
        return title.length() <= maxLength;
    }

    private boolean textIsNull(String content) {
        return content == null || content.trim().isEmpty();
    }

    private Map<String, Object> createValidFail(Map<String, Object> map, String msg){
        map.put("result", false);
        map.put("message", msg);
        return map;
    }

    private Map<String, Object> createValidTrue(Map<String, Object> map){
        map.put("result", true);
        return map;
    }

}