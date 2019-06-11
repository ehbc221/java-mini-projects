/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moov.report.disk;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author esombugma
 */
public class FTPInfoProps extends FTPInfo {
    
    private Properties prop;      
    private List<String> recipients;
    
    /**
     * @return the recipient
     */
    public List<String> getRecipients() {
        return recipients;
    }

    /**
     * @param recipient the recipient to set
     */
    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }
    
    public FTPInfoProps() {
        this.prop = new Properties();
        recipients = new ArrayList<>(); 
        this.loadProps();
    }       
    
    /**
     * Function that get the list of all the recipient
     * @param list
     * @return An array of String
     */
    private String[] listOfRecipient(String list) {        
        String [] recipientTab = list.split(",");
        return recipientTab;
    }
    
    /**
     * Function that load the properties
     */
    private void loadProps() {
        try {
            //We load properties
            prop.load(new FileInputStream("config.properties"));

            this.setServer(prop.getProperty("config.server"));
            this.setLogin(prop.getProperty("config.login"));
            this.setPassword(prop.getProperty("config.password"));
            this.setRemotePath(prop.getProperty("config.remotepath"));
            this.setLocalPath(prop.getProperty("config.localpath"));
            this.setFileName(prop.getProperty("config.file"));
            
            //We get the list of all the recipients
            String [] tmp = this.listOfRecipient(prop.getProperty("config.recipients"));
            this.recipients.addAll(Arrays.asList(tmp));
        } catch(IOException ex) {
            
        }                        
    }    
}
