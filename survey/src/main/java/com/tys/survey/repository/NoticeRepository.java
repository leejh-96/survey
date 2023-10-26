package com.tys.survey.repository;

import com.tys.survey.commons.PageInfo;
import com.tys.survey.dto.NoticeListDTO;
import com.tys.survey.dto.NoticeFormDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeRepository {

    void save(NoticeFormDTO notice);

    void updateHit(int boardNo);

    void updateNotice(NoticeFormDTO notice);

    int deleteNotice(int boardNo);

    int getCount(PageInfo pageInfo);

    List<NoticeListDTO> getList(PageInfo pageInfo);

    NoticeListDTO getDetail(int boardNo);

    List<NoticeListDTO> getMainNoticeList();

}
