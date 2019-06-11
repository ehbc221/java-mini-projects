/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.Date;

/**
 *
 * @author khaled.medjhoum
 */

public class Abonnement {
    private int reste;
    private int idAbonnement;
    private int idClient;
    private Date dateDebut;
    private Date dateFin;
    private int etatAbonnement;
    private String nomClient;
    private String emailClient;
    private String contact;
    
    private int couperClient;
    private int sendMailClient;
    private int factureRegle;
    private String connexion;

    public String getConnexion() {
        return connexion;
    }

    public void setConnexion(String connexion) {
        this.connexion = connexion;
    }

    public int getCouperClient() {
        return couperClient;
    }

    public void setCouperClient(int couperClient) {
        this.couperClient = couperClient;
    }

    public int getFactureRegle() {
        return factureRegle;
    }

    public void setFactureRegle(int factureRegle) {
        this.factureRegle = factureRegle;
    }

    public int getSendMailClient() {
        return sendMailClient;
    }

    public void setSendMailClient(int sendMailClient) {
        this.sendMailClient = sendMailClient;
    }
    
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public int getEtatAbonnement() {
        return etatAbonnement;
    }

    public void setEtatAbonnement(int etatAbonnement) {
        this.etatAbonnement = etatAbonnement;
    }

    public int getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(int idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public int getReste() {
        return reste;
    }

    public void setReste(int reste) {
        this.reste = reste;
    }

    public Abonnement(int reste, int idAbonnement, int idClient, Date dateDebut, Date dateFin, int etatAbonnement, String nomClient, String emailClient, String contact, int couperClient, int sendMailClient, int factureRegle, String connexion) {
        this.reste = reste;
        this.idAbonnement = idAbonnement;
        this.idClient = idClient;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.etatAbonnement = etatAbonnement;
        this.nomClient = nomClient;
        this.emailClient = emailClient;
        this.contact = contact;
        this.couperClient = couperClient;
        this.sendMailClient = sendMailClient;
        this.factureRegle = factureRegle;
        this.connexion = connexion;
    }
}


