package com.tys.survey.controller;

import com.tys.survey.commons.PageInfo;
import com.tys.survey.dto.LoginMemberDTO;
import com.tys.survey.dto.NoticeFormDTO;
import com.tys.survey.service.FileUploadService;
import com.tys.survey.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@Controller
@RequestMapping("/notice")
public class NoticeController {

    private final FileUploadService fileService;

    private final NoticeService noticeService;

    public NoticeController(FileUploadService fileService, NoticeService noticeService) {
        this.fileService = fileService;
        this.noticeService = noticeService;
    }

    @GetMapping("/form")
    public String noticeForm(Model model){
        model.addAttribute("noticeFormDTO",new NoticeFormDTO());
        model.addAttribute("currentTime",new Timestamp(System.currentTimeMillis()));
        return "notice/noticeForm";
    }

    @GetMapping("/list")
    public String noticeList(@ModelAttribute PageInfo pageInfo, Model model){
        pageInfo.setCount(noticeService.getCount(pageInfo));
        pageInfo.pageSettings();

        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("noticeList",noticeService.getList(pageInfo));
        return "notice/noticeList";
    }

    @GetMapping("/detail/{boardNo}")
    public String noticeDetail(@PathVariable int boardNo, @ModelAttribute PageInfo pageInfo,Model model,
                               HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("notice",noticeService.getDetail(boardNo,request,response));
        model.addAttribute("pageInfo",pageInfo);
        return "notice/noticeDetail";
    }

    @GetMapping("/update/{boardNo}")
    public String updateForm(@PathVariable int boardNo,Model model){
        model.addAttribute("noticeFormDTO",noticeService.findByNotice(boardNo));
        model.addAttribute("currentTime",new Timestamp(System.currentTimeMillis()));
        return "notice/noticeUpdate";
    }

    @PostMapping("/save")
    public String saveNotice(@Validated @ModelAttribute NoticeFormDTO notice, BindingResult result,
                             @SessionAttribute(value = "loginMember",required = false)LoginMemberDTO loginMember){
        if (result.hasErrors()){
            return "notice/noticeForm";
        }
        noticeService.save(notice,loginMember);
        return "redirect:/notice/list";
    }

    @PostMapping("/update")
    public String updateNotice(@Validated @ModelAttribute NoticeFormDTO notice, BindingResult result,
                               @SessionAttribute(value = "loginMember",required = false)LoginMemberDTO loginMember){
        if (result.hasErrors()){
            return "notice/noticeUpdate";
        }
        noticeService.updateNotice(notice,loginMember);
        return "redirect:/notice/detail/" + notice.getBoardNo();
    }

    @ResponseBody
    @PostMapping("/image/upload")
    public String imageUpload(@ModelAttribute MultipartFile file){
        return fileService.saveFile(file).getPath();
    }

    @ResponseBody
    @PostMapping("/delete")
    public boolean deleteNotice(@RequestParam int boardNo){
        return noticeService.deleteNotice(boardNo);
    }
}
