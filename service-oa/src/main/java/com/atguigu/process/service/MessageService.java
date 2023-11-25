package com.atguigu.process.service;

public interface MessageService {

    /**
     * t推送待审批人员
     */
    void pushPendingMessage(Long processId, Long userId, String taskId);
}
