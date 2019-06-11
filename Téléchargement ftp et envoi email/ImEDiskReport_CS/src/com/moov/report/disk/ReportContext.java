package com.moov.report.disk;

public class ReportContext {

    private IReportStrategy strategy;

    public ReportContext(IReportStrategy strategy) {
        super();
        this.strategy = strategy;
    }

    public void executeSend() {
        this.strategy.send();
    }
}
