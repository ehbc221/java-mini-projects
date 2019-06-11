/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.List;

/**
 *
 * @author khaled.medjhoum
 */

public class ChargeCompte {
    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private List<Abonnement> abonnementsList;

    public List<Abonnement> getAbonnementsList() {
        return abonnementsList;
    }

    public void setAbonnementsList(List<Abonnement> abonnementsList) {
        this.abonnementsList = abonnementsList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPreom(String preom) {
        this.prenom = preom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    
    @Override
    public String toString(){
        return nom;
    }

    public ChargeCompte(String nom, String preom, String email, String tel) {
        this.nom = nom;
        this.prenom = preom;
        this.email = email;
        this.tel = tel;
    }

    public ChargeCompte(String nom, String preom, String email, String tel, List<Abonnement> abonnementsList) {
        this.nom = nom;
        this.prenom = preom;
        this.email = email;
        this.tel = tel;
        this.abonnementsList = abonnementsList;
    }
}
