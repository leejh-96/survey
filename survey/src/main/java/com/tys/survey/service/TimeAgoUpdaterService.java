package com.tys.survey.service;

import com.tys.survey.commons.TimeUpdatable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeAgoUpdaterService {

    public <T extends TimeUpdatable> void updateListTime(List<T> list) {
        for (T dto : list) {
            updateItemTime(dto);
        }
    }

    public <T extends TimeUpdatable> void updateItemTime(T dto) {
        LocalDateTime writeTime = dto.getCreateTime();
        String timeAgo = getTimeAgo(writeTime);
        dto.setCreateTime(timeAgo);
    }

    private String getTimeAgo(LocalDateTime writeTime) {
        Duration between = Duration.between(writeTime, LocalDateTime.now());
        String timeAgo;
        if (between.toDays() > 0) {
            timeAgo = between.toDays() + "일 전";
        } else if (between.toHours() > 0) {
            timeAgo = between.toHours() + "시간 전";
        } else if (between.toMinutes() > 0) {
            timeAgo = between.toMinutes() + "분 전";
        } else {
            timeAgo = between.getSeconds() + "초 전";
        }
        return timeAgo;
    }

}
