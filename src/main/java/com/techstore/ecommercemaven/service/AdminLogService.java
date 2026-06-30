package com.techstore.ecommercemaven.service;

import com.techstore.ecommercemaven.model.AdminLog;
import com.techstore.ecommercemaven.repository.AdminLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdminLogService {

    private final AdminLogRepository repository;

    public AdminLogService(
            AdminLogRepository repository) {

        this.repository = repository;
    }

    public void log(String action) {

        AdminLog log = new AdminLog();

        log.setAction(action);
        log.setCreatedAt(LocalDateTime.now());

        repository.save(log);
    }
}
