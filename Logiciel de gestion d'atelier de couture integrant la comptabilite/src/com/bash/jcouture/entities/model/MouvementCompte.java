/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.entities.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author bashizip
 */
@Entity
@Table(name = "mouvement_compte", catalog = "couturedb", schema = "")
@NamedQueries({
    @NamedQuery(name = "MouvementCompte.findAll", query = "SELECT m FROM MouvementCompte m"),
    @NamedQuery(name = "MouvementCompte.findByIdcompte", query = "SELECT m FROM MouvementCompte m WHERE m.mouvementComptePK.idcompte = :idcompte"),
    @NamedQuery(name = "MouvementCompte.findByEntree", query = "SELECT m FROM MouvementCompte m WHERE m.entree = :entree"),
    @NamedQuery(name = "MouvementCompte.findBySortie", query = "SELECT m FROM MouvementCompte m WHERE m.sortie = :sortie"),
    @NamedQuery(name = "MouvementCompte.findByDate", query = "SELECT m FROM MouvementCompte m WHERE m.date = :date"),
    @NamedQuery(name = "MouvementCompte.findByDescription", query = "SELECT m FROM MouvementCompte m WHERE m.description = :description"),
    @NamedQuery(name = "MouvementCompte.findByIdfacture", query = "SELECT m FROM MouvementCompte m WHERE m.mouvementComptePK.idfacture = :idfacture"),
    @NamedQuery(name = "MouvementCompte.findByIdmouvementCompte", query = "SELECT m FROM MouvementCompte m WHERE m.mouvementComptePK.idmouvementCompte = :idmouvementCompte")})
public class MouvementCompte implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MouvementComptePK mouvementComptePK;
    @Basic(optional = false)
    @Column(name = "entree", nullable = false)
    private double entree;
    @Basic(optional = false)
    @Column(name = "sortie", nullable = false)
    private double sortie;
    @Basic(optional = false)
    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @Column(name = "description", nullable = false, length = 45)
    private String description;
    @JoinColumn(name = "idfacture", referencedColumnName = "idfacture", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Facture facture;
    @JoinColumn(name = "idcompte", referencedColumnName = "idcompte", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Compte compte;

    public MouvementCompte() {
    }

    public MouvementCompte(MouvementComptePK mouvementComptePK) {
        this.mouvementComptePK = mouvementComptePK;
    }

    public MouvementCompte(MouvementComptePK mouvementComptePK, double entree, double sortie, Date date, String description) {
        this.mouvementComptePK = mouvementComptePK;
        this.entree = entree;
        this.sortie = sortie;
        this.date = date;
        this.description = description;
    }

    public MouvementCompte(int idcompte, int idfacture, int idmouvementCompte) {
        this.mouvementComptePK = new MouvementComptePK(idcompte, idfacture, idmouvementCompte);
    }

    public MouvementComptePK getMouvementComptePK() {
        return mouvementComptePK;
    }

    public void setMouvementComptePK(MouvementComptePK mouvementComptePK) {
        this.mouvementComptePK = mouvementComptePK;
    }

    public double getEntree() {
        return entree;
    }

    public void setEntree(double entree) {
        this.entree = entree;
    }

    public double getSortie() {
        return sortie;
    }

    public void setSortie(double sortie) {
        this.sortie = sortie;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mouvementComptePK != null ? mouvementComptePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MouvementCompte)) {
            return false;
        }
        MouvementCompte other = (MouvementCompte) object;
        if ((this.mouvementComptePK == null && other.mouvementComptePK != null) || (this.mouvementComptePK != null && !this.mouvementComptePK.equals(other.mouvementComptePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bash.jcouture.entities.model.MouvementCompte[mouvementComptePK=" + mouvementComptePK + "]";
    }

}
