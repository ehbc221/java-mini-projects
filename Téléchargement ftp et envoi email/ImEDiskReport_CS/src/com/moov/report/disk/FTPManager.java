package com.moov.report.disk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPManager {
        
    private FTPInfo ftpInfo;
    
    public FTPInfo getFtpInfo() {
        return ftpInfo;
    }
    
    public void setFtpInfo(FTPInfo ftpInfo) {
        this.ftpInfo = ftpInfo;
    }
    
    public FTPManager() {        
        ftpInfo = new FTPInfo();        
    }

    /**
     * Function that get the report file in the local folder
     *
     * @throws SocketException
     * @throws IOException
     */
    public void getFile() throws SocketException, IOException {
        String msg = "";
        FTPClient ftpClient = new FTPClient();
        // Connection to the server
        ftpClient.connect(this.ftpInfo.getServer());
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            msg = "Failed to connect to the server";
            AppLogger.getApplogger().error(msg);
            throw new CustomException(msg);            
        } else {
            // Login to the server
            if (!ftpClient.login(this.ftpInfo.getLogin(), this.ftpInfo.getPassword())) {
                msg = "Failed to login to the server";   
                AppLogger.getApplogger().error(msg);
                throw new CustomException(msg);
            } else {                
                if (ftpClient.listFiles(this.ftpInfo.getRemotePath()).length == 0) {
                    msg = "There is no file to download"; 
                    AppLogger.getApplogger().error(msg);
                    throw new CustomException(msg);
                } else {
                    FTPFile[] files = ftpClient.listFiles(this.ftpInfo.getRemotePath());
                    boolean fileFound = false;
                    // We iterate the list of files
                    for (FTPFile f : files) {
                        if (f.getName().equals(this.ftpInfo.getFileName())) {
                            fileFound = true;
                            break;
                        }                        
                    }
                    // We check to see if the file exists
                    if (fileFound != true) {
                        msg = "The report file is not available";
                        AppLogger.getApplogger().error(msg);
                        throw new CustomException(msg);
                    } else {
                        this.ftpInfo.setFileName(this.ftpInfo.getFileName());
                        String remote = this.ftpInfo.getRemotePath() + this.ftpInfo.getFileName();
                        String local = this.ftpInfo.getLocalPath() + this.ftpInfo.getFileName();                        
                        ftpClient.retrieveFile(remote, new FileOutputStream(local));                    
                    }
                    ftpClient.noop();
                    ftpClient.logout();
                    if (ftpClient.isConnected()) {
                        ftpClient.disconnect();
                    }
                }                
            }
        }        
        
    }

    /**
     * Function that get the content of the report file
     *
     * @return a String
     * @throws FileNotFoundException
     * @throws CustomException
     */
    public String getFileData() throws FileNotFoundException, CustomException {
        return this.formatFileContent();
    }

    /**
     * Function that return the formated string
     *
     * @return a String
     */
    private String formatFileContent() throws FileNotFoundException, CustomException {
        String msg = "";
        // We check to see if the file exists
        if (!(new File(this.ftpInfo.getLocalPath() + this.ftpInfo.getFileName()).exists())) {
            msg = "The local file doesn't exists";
            AppLogger.getApplogger().error(msg);
            throw new CustomException(msg);            
        } else {
            StringBuilder strBuilder;
            try (Scanner scanner = new Scanner(new File(this.ftpInfo.getLocalPath() + this.ftpInfo.getFileName()))) {
                strBuilder = new StringBuilder();
                while (scanner.hasNextLine()) {                    
                    strBuilder.append(scanner.nextLine() + "\n");
                }
            }           
            return strBuilder.toString();
        }
    }
}
