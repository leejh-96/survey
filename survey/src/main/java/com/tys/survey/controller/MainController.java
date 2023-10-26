package com.tys.survey.controller;

import com.tys.survey.dto.MainDTO;
import com.tys.survey.service.NoticeService;
import com.tys.survey.service.SurveyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private final SurveyService surveyService;

    private final NoticeService noticeService;

    public MainController(SurveyService surveyService, NoticeService noticeService) {
        this.surveyService = surveyService;
        this.noticeService = noticeService;
    }

    @RequestMapping(value = {"", "/"})
    public String main(Model model){
        MainDTO main = surveyService.getMainSurveyList();
        main.setNoticeList(noticeService.getMainNoticeList());
        model.addAttribute("main",main);
        return "main/main";
    }

}
