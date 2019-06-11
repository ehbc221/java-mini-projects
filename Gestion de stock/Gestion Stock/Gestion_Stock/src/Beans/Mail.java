package Beans;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ibrahima
 */
public class Mail {
private String adress_envoi,adresse_reception,ip_server,password,message;

    public String getAdress_envoi() {
        return adress_envoi;
    }

    public void setAdress_envoi(String adress_envoi) {
        this.adress_envoi = adress_envoi;
    }

    public String getAdresse_reception() {
        return adresse_reception;
    }

    public void setAdresse_reception(String adresse_reception) {
        this.adresse_reception = adresse_reception;
    }

    public String getIp_server() {
        return ip_server;
    }

    public void setIp_server(String ip_server) {
        this.ip_server = ip_server;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
