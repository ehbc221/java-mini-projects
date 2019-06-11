/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moov.report.disk;

import java.io.IOException;
import java.net.SocketException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author esombugma
 */
public class Program {
   
    private static Logger logger = Logger.getLogger(Program.class);    
    private static FTPInfoProps ftpInfo = new FTPInfoProps();   
    private static FTPManager ftpManager = new FTPManager();
    private static CustomInfo customInfo = new CustomInfo();
    private static EmailReport emailReport;
    
    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        AppLogger.setApplogger(logger);
        // We set the ftp info to work with
        ftpManager.setFtpInfo(ftpInfo);
        try {
            //We get the file
            ftpManager.getFile();
            // We get the custom info for the Email
            customInfo.setFrom("imereport@moov.tg");
            customInfo.setTo(ftpInfo.getRecipients());
            customInfo.setSubject("ImE Disk Space Report");
            customInfo.setMessage(ftpManager.getFileData());
            emailReport = new EmailReport(customInfo);
            emailReport.send();
        } catch (SocketException ex) {
            logger.error(ex.getMessage());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
}
