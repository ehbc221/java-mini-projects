/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.entities.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author bashizip
 */
@Entity
@Table(name = "facture", catalog = "couturedb", schema = "")
@NamedQueries({
    @NamedQuery(name = "Facture.findAll", query = "SELECT f FROM Facture f"),
    @NamedQuery(name = "Facture.findByIdfacture", query = "SELECT f FROM Facture f WHERE f.idfacture = :idfacture"),
    @NamedQuery(name = "Facture.findByMontant", query = "SELECT f FROM Facture f WHERE f.montant = :montant"),
    @NamedQuery(name = "Facture.findByDateEmission", query = "SELECT f FROM Facture f WHERE f.dateEmission = :dateEmission")})
public class Facture implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idfacture", nullable = false)
    private Integer idfacture;
    @Basic(optional = false)
    @Column(name = "montant", nullable = false)
    private double montant;
    @Column(name = "date_emission")
    @Temporal(TemporalType.DATE)
    private Date dateEmission;
    @JoinColumn(name = "idpayement_mode", referencedColumnName = "idpayement_mode", nullable = false)
    @ManyToOne(optional = false)
    private PayementMode payementMode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facture")
    private List<MouvementCompte> mouvementCompteList;

    public Facture() {
    }

    public Facture(Integer idfacture) {
        this.idfacture = idfacture;
    }

    public Facture(Integer idfacture, double montant) {
        this.idfacture = idfacture;
        this.montant = montant;
    }

    public Integer getIdfacture() {
        return idfacture;
    }

    public void setIdfacture(Integer idfacture) {
        this.idfacture = idfacture;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(Date dateEmission) {
        this.dateEmission = dateEmission;
    }

    public PayementMode getPayementMode() {
        return payementMode;
    }

    public void setPayementMode(PayementMode payementMode) {
        this.payementMode = payementMode;
    }

    public List<MouvementCompte> getMouvementCompteList() {
        return mouvementCompteList;
    }

    public void setMouvementCompteList(List<MouvementCompte> mouvementCompteList) {
        this.mouvementCompteList = mouvementCompteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idfacture != null ? idfacture.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facture)) {
            return false;
        }
        Facture other = (Facture) object;
        if ((this.idfacture == null && other.idfacture != null) || (this.idfacture != null && !this.idfacture.equals(other.idfacture))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bash.jcouture.entities.model.Facture[idfacture=" + idfacture + "]";
    }

}
