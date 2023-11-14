package com.app.lms.dto.taskDto;

import java.time.LocalDate;

public record TaskRequest(String TaskName,
                          String text,
                          String fileName,
                          String fileLink,
                          String linkName,
                          String link,
                          String image,
                          String code,
                          LocalDate deadLine) {
}