/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.entities.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author basnizip
 */
@Entity
public class Report implements Serializable {

    @Id
    private int idmouvement_compte;
    private int idarticle;
    private String client;
    private double montant;
    private String compte;
    private double entree;
    private double pourcentage;

    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date debut;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fin;


    public Report() {
    }

    public Report(int idarticle,
            String client, double montant,
            String compte,
            double entree,
            double pourcentage
            ) {
        this.idarticle = idarticle;
        this.client = client;
        this.montant = montant;
        this.compte = compte;
        this.entree = entree;
        this.pourcentage = pourcentage;
    }

    public int getIdmouvement_compte() {
        return idmouvement_compte;
    }

    public void setIdmouvement_compte(int idmouvement_compte) {
        this.idmouvement_compte = idmouvement_compte;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCompte() {
        return compte;
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public double getEntree() {
        return entree;
    }

    public void setEntree(double entree) {
        this.entree = entree;
    }

    public int getIdarticle() {
        return idarticle;
    }

    public void setIdarticle(int idarticle) {
        this.idarticle = idarticle;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    





}
