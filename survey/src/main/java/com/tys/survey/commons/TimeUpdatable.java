package com.tys.survey.commons;

import java.time.LocalDateTime;

public interface TimeUpdatable {

    LocalDateTime getCreateTime();

    void setCreateTime(String timeAgo);
}
