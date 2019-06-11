/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.entities.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author bashizip
 */
@Entity
@Table(name = "payement_mode", catalog = "couturedb", schema = "")
@NamedQueries({
    @NamedQuery(name = "PayementMode.findAll", query = "SELECT p FROM PayementMode p"),
    @NamedQuery(name = "PayementMode.findByIdpayementMode", query = "SELECT p FROM PayementMode p WHERE p.idpayementMode = :idpayementMode"),
    @NamedQuery(name = "PayementMode.findByLabel", query = "SELECT p FROM PayementMode p WHERE p.label = :label")})
public class PayementMode implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idpayement_mode", nullable = false)
    private Integer idpayementMode;
    @Basic(optional = false)
    @Column(name = "label", nullable = false, length = 45)
    private String label;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "payementMode")
    private List<Facture> factureList;

    public PayementMode() {
    }

    public PayementMode(Integer idpayementMode) {
        this.idpayementMode = idpayementMode;
    }

    public PayementMode(Integer idpayementMode, String label) {
        this.idpayementMode = idpayementMode;
        this.label = label;
    }

    public Integer getIdpayementMode() {
        return idpayementMode;
    }

    public void setIdpayementMode(Integer idpayementMode) {
        this.idpayementMode = idpayementMode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Facture> getFactureList() {
        return factureList;
    }

    public void setFactureList(List<Facture> factureList) {
        this.factureList = factureList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpayementMode != null ? idpayementMode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PayementMode)) {
            return false;
        }
        PayementMode other = (PayementMode) object;
        if ((this.idpayementMode == null && other.idpayementMode != null) || (this.idpayementMode != null && !this.idpayementMode.equals(other.idpayementMode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bash.jcouture.entities.model.PayementMode[idpayementMode=" + idpayementMode + "]";
    }

}
