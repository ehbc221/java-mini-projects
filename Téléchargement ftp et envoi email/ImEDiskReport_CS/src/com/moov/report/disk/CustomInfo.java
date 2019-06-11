package com.moov.report.disk;

import java.util.ArrayList;
import java.util.List;

public class CustomInfo {

    private String from;
    private List<String> to = new ArrayList<>();
    private String subject;
    private String message;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomInfo() {
    }

    public CustomInfo(String from, String to, String subject, String message) {
        super();
        this.from = from;
        //this.to = to;
        this.subject = subject;
        this.message = message;
    }
}
