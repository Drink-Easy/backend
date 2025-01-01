package com.drinkeg.drinkeg.domain.notice.repository;

import com.drinkeg.drinkeg.domain.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
