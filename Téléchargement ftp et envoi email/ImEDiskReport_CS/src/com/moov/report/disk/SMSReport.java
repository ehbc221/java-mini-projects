package com.moov.report.disk;

public class SMSReport implements IReportStrategy {

    private CustomInfo info = new CustomInfo();

    public CustomInfo getInfo() {
        return info;
    }

    public void setInfo(CustomInfo info) {
        this.info = info;
    }

    public SMSReport() {
    }

    @Override
    public void send() {
    }
}
