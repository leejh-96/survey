package com.tys.survey.controller;

import com.tys.survey.commons.PageInfo;
import com.tys.survey.dto.*;
import com.tys.survey.service.SurveyService;
import com.tys.survey.validation.SurveyFormValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService service;

    private final SurveyFormValidation validation;

    public SurveyController(SurveyService service, SurveyFormValidation validation) {
        this.service = service;
        this.validation = validation;
    }

    @GetMapping("/form")
    public String surveyForm(Model model){
        model.addAttribute("currentTime",new Timestamp(System.currentTimeMillis()));
        return "survey/surveyForm";
    }

    @GetMapping("/list")
    public String surveyList(@ModelAttribute PageInfo pageInfo,Model model){
        pageInfo.setCount(service.getCount(pageInfo));
        pageInfo.pageSettings();

        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("surveyList",service.getList(pageInfo));
        return "survey/surveyList";
    }

    @GetMapping("/detail/{surveyNo}")
    public String surveyDetail(@PathVariable int surveyNo, @ModelAttribute PageInfo pageInfo, Model model,
                               HttpServletRequest request, HttpServletResponse response){
        SurveyDTO survey = service.getDetail(request,response,surveyNo);

        model.addAttribute("survey",survey);
        model.addAttribute("pageInfo",pageInfo);
        return "survey/surveyDetail";
    }

    @GetMapping("/result/{surveyNo}")
    public String resultList(@PathVariable int surveyNo,@ModelAttribute PageInfo pageInfo,Model model,
                             HttpServletRequest request, HttpServletResponse response){
        SurveyDTO survey = service.getDetail(request,response,surveyNo);

        model.addAttribute("survey",survey);
        model.addAttribute("pageInfo",pageInfo);
        return "survey/surveyResult";
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> surveySave(@SessionAttribute(value = "loginMember", required = false) LoginMemberDTO loginMember,
                                                          @ModelAttribute SurveyInfoDTO survey) {
        Map<String, Object> map = service.memberCheck(loginMember);
        if (!(boolean) map.get("result")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);//401
        }

        map = validation.makeSurveyValid(survey);
        if (!(boolean) map.get("result")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);//400
        }

        map = service.save(map, survey, loginMember);
        return ResponseEntity.status(HttpStatus.OK).body(map);//200
    }

    @PostMapping("/save/result")
    public ResponseEntity<Map<String, Object>> resultSave(@SessionAttribute(value = "loginMember",required = false) LoginMemberDTO loginMember,
                                                          @ModelAttribute SurveyFormDTO survey){
        Map<String, Object> map = service.memberCheck(loginMember);
        if (!(boolean)map.get("result")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);//401
        }

        map = service.duplicateVote(map,loginMember,survey);
        if (!(boolean)map.get("result")){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(map);//409
        }

        map = service.saveResult(map, survey, loginMember);
        return ResponseEntity.status(HttpStatus.OK).body(map);//200
    }

    @ResponseBody
    @PostMapping("/delete")
    public boolean deleteSurvey(@SessionAttribute(value = "loginMember",required = false) LoginMemberDTO loginMember,
                                @RequestParam int surveyNo){
        return service.deleteSurvey(loginMember, surveyNo) != 0 ? true : false;
    }
}
