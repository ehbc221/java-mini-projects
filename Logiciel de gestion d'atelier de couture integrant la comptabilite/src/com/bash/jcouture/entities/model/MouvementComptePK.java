/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.entities.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author bashizip
 */
@Embeddable
public class MouvementComptePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idcompte", nullable = false)
    private int idcompte;
    @Basic(optional = false)
    @Column(name = "idfacture", nullable = false)
    private int idfacture;
    @Basic(optional = false)
    @Column(name = "idmouvement_compte", nullable = false)
    private int idmouvementCompte;

    public MouvementComptePK() {
    }

    public MouvementComptePK(int idcompte, int idfacture, int idmouvementCompte) {
        this.idcompte = idcompte;
        this.idfacture = idfacture;
        this.idmouvementCompte = idmouvementCompte;
    }

    public int getIdcompte() {
        return idcompte;
    }

    public void setIdcompte(int idcompte) {
        this.idcompte = idcompte;
    }

    public int getIdfacture() {
        return idfacture;
    }

    public void setIdfacture(int idfacture) {
        this.idfacture = idfacture;
    }

    public int getIdmouvementCompte() {
        return idmouvementCompte;
    }

    public void setIdmouvementCompte(int idmouvementCompte) {
        this.idmouvementCompte = idmouvementCompte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idcompte;
        hash += (int) idfacture;
        hash += (int) idmouvementCompte;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MouvementComptePK)) {
            return false;
        }
        MouvementComptePK other = (MouvementComptePK) object;
        if (this.idcompte != other.idcompte) {
            return false;
        }
        if (this.idfacture != other.idfacture) {
            return false;
        }
        if (this.idmouvementCompte != other.idmouvementCompte) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bash.jcouture.entities.model.MouvementComptePK[idcompte=" + idcompte + ", idfacture=" + idfacture + ", idmouvementCompte=" + idmouvementCompte + "]";
    }

}
