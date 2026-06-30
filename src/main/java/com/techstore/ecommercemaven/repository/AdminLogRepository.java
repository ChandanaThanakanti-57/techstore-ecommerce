package com.techstore.ecommercemaven.repository;

import com.techstore.ecommercemaven.model.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminLogRepository
        extends JpaRepository<AdminLog, Long> {

    List<AdminLog> findTop20ByOrderByCreatedAtDesc();
}
