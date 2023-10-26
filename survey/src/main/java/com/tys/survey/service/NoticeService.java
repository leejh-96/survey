package com.tys.survey.service;

import com.tys.survey.commons.PageInfo;
import com.tys.survey.dto.LoginMemberDTO;
import com.tys.survey.dto.NoticeListDTO;
import com.tys.survey.dto.NoticeFormDTO;
import com.tys.survey.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository repository;

    public NoticeService(NoticeRepository repository) {
        this.repository = repository;
    }

    public void save(NoticeFormDTO noticeFormDTO, LoginMemberDTO loginMember) {
        repository.save(NoticeFormDTO.builder()
                                    .memberNo(loginMember.getMemberNo())
                                    .boardTitle(noticeFormDTO.getBoardTitle())
                                    .boardContent(noticeFormDTO.getBoardContent())
                                    .boardWriteTime(LocalDateTime.now())
                                    .build());
    }

    public void updateNotice(NoticeFormDTO notice, LoginMemberDTO loginMember) {
        notice.setMemberNo(loginMember.getMemberNo());
        notice.setBoardWriteTime(LocalDateTime.now());
        repository.updateNotice(notice);
    }

    public int getCount(PageInfo pageInfo) {
        return repository.getCount(pageInfo);
    }

    public boolean deleteNotice(int boardNo) {
        return repository.deleteNotice(boardNo) > 0 ? true : false;
    }

    public List<NoticeListDTO> getList(PageInfo pageInfo) {
        return updateTime(repository.getList(pageInfo));
    }

    public NoticeListDTO getDetail(int boardNo, HttpServletRequest request, HttpServletResponse response) {
        updateHit(boardNo,request,response);
        NoticeListDTO notice = repository.getDetail(boardNo);
        notice.setTime(timeSettings(notice));
        return notice;
    }

    public NoticeFormDTO findByNotice(int boardNo) {
        NoticeListDTO notice = repository.getDetail(boardNo);
        return NoticeFormDTO.builder()
                            .boardTitle(notice.getBoardTitle())
                            .boardContent(notice.getBoardContent())
                            .boardNo(notice.getBoardNo())
                            .build();
    }

    public List<NoticeListDTO> getMainNoticeList() {
        List<NoticeListDTO> noticeList = repository.getMainNoticeList();
        return updateTime(noticeList);
    }

    private List<NoticeListDTO> updateTime(List<NoticeListDTO> list) {
        for (NoticeListDTO dto : list) {
            dto.setTime(timeSettings(dto));
        }
        return list;
    }

    private String timeSettings(NoticeListDTO dto) {
        LocalDateTime boardWriteTime = dto.getBoardWriteTime();
        LocalDateTime now = LocalDateTime.now();
        Duration between = Duration.between(boardWriteTime, now);
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

    private void updateHit(int boardNo, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String cookieName = boardNo + "_board_cookie";
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
            repository.updateHit(boardNo);
        }
    }

}
