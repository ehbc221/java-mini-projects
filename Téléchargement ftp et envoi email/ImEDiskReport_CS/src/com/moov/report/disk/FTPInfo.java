package com.moov.report.disk;

public class FTPInfo {

    private String server;
    private String login;
    private String password;
    private String remotePath;
    private String localPath;
    private String fileName;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FTPInfo() {
    }

    public FTPInfo(String server, String login, String password, String remotePath, String localPath, String fileName) {
        super();
        this.server = server;
        this.login = login;
        this.password = password;
        this.remotePath = remotePath;
        this.localPath = localPath;
        this.fileName = fileName;
    }
}
