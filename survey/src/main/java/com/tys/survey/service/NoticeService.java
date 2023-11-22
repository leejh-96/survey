package com.tys.survey.service;

import com.tys.survey.commons.PageInfo;
import com.tys.survey.dto.LoginMemberDTO;
import com.tys.survey.dto.NoticeListDTO;
import com.tys.survey.dto.NoticeFormDTO;
import com.tys.survey.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final HitCookieService hitCookieService;

    private final TimeAgoUpdaterService timeUpdateService;

    private final NoticeRepository repository;

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
        List<NoticeListDTO> noticeList = repository.getList(pageInfo);
        timeUpdateService.updateListTime(noticeList);
        return noticeList;
    }

    public NoticeListDTO getDetail(int boardNo, HttpServletRequest request, HttpServletResponse response) {
        String cookieName = boardNo + "_board_cookie";
        if (!hitCookieService.updateHit(request, response, cookieName)){
            repository.updateHit(boardNo);
        }
        NoticeListDTO notice = repository.getDetail(boardNo);
        timeUpdateService.updateItemTime(notice);
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
        timeUpdateService.updateListTime(noticeList);
        return noticeList;
    }

//    private void updateHit(int boardNo, HttpServletRequest request, HttpServletResponse response) {
//        Cookie[] cookies = request.getCookies();
//        String cookieName = boardNo + "_board_cookie";
//        boolean status = false;
//        if (cookies != null){
//            for (Cookie c : cookies){
//                if (c.getName().equals(cookieName)){
//                    status = true;
//                    break;
//                }
//            }
//        }
//        if (!status){
//            Cookie cookie = new Cookie(cookieName,"true");
//            cookie.setMaxAge(24 * 60 * 60);
//            response.addCookie(cookie);
//            repository.updateHit(boardNo);
//        }
//    }

}
