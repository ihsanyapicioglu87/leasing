package com.allane.leasing.utils;

import com.allane.leasing.enums.ActionType;
import com.allane.leasing.model.AuditLog;
import com.allane.leasing.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditLogUtils {

    private static AuditLogRepository auditLogRepository;

    @Autowired
    public AuditLogUtils(AuditLogRepository auditLogRepository) {
        AuditLogUtils.auditLogRepository = auditLogRepository;
    }

    public static void logAction(ActionType actionType, String entity, Long entityId, String username) {
        AuditLog logEntry = new AuditLog();
        logEntry.setAction(actionType.getAction());
        logEntry.setEntityType(entity);
        logEntry.setEntityId(entityId);
        logEntry.setUsername(username);
        logEntry.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(logEntry);
    }
}
