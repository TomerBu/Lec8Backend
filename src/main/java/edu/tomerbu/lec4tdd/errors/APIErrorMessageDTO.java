package edu.tomerbu.lec4tdd.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class APIErrorMessageDTO {
    private long timeStamp;
    private String message;
    private int status;
    private String failedURL;
    private Map<String, String> validationErrors;

    public APIErrorMessageDTO(String message, int status, String failedURL) {
        this.timeStamp = new Date().getTime();//Date, Calendar, Local
        this.message = message;
        this.status = status;
        this.failedURL = failedURL;
    }

    public APIErrorMessageDTO(String message, int status,
                              String failedURL,
                              Map<String, String> validationErrors) {

        this.timeStamp = new Date().getTime();//Date, Calendar, Local
        this.message = message;
        this.status = status;
        this.failedURL = failedURL;
        this.validationErrors = validationErrors;
    }
}
