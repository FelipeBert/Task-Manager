package org.FelipeBert.TaskManager.service;

public interface JavaMailService {
    void sendEmail(String to, String subject, String text);
}
