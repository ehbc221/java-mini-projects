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
@Table(name = "compte", catalog = "couturedb", schema = "")
@NamedQueries({
    @NamedQuery(name = "Compte.findAll", query = "SELECT c FROM Compte c"),
    @NamedQuery(name = "Compte.findByIdcompte", query = "SELECT c FROM Compte c WHERE c.idcompte = :idcompte"),
    @NamedQuery(name = "Compte.findByLibele", query = "SELECT c FROM Compte c WHERE c.libele = :libele"),
    @NamedQuery(name = "Compte.findByDescription", query = "SELECT c FROM Compte c WHERE c.description = :description"),
    @NamedQuery(name = "Compte.findByExercice", query = "SELECT c FROM Compte c WHERE c.exercice = :exercice")})
public class Compte implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idcompte", nullable = false)
    private Integer idcompte;
    @Basic(optional = false)
    @Column(name = "libele", nullable = false, length = 45)
    private String libele;
    @Basic(optional = false)
    @Column(name = "Description", nullable = false, length = 45)
    private String description;
    @Basic(optional = false)
    @Column(name = "exercice", nullable = false, length = 45)
    private String exercice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "compte")
    private List<MouvementCompte> mouvementCompteList;

    public Compte() {
    }

    public Compte(Integer idcompte) {
        this.idcompte = idcompte;
    }

    public Compte(Integer idcompte, String libele, String description, String exercice) {
        this.idcompte = idcompte;
        this.libele = libele;
        this.description = description;
        this.exercice = exercice;
    }

    public Integer getIdcompte() {
        return idcompte;
    }

    public void setIdcompte(Integer idcompte) {
        this.idcompte = idcompte;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExercice() {
        return exercice;
    }

    public void setExercice(String exercice) {
        this.exercice = exercice;
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
        hash += (idcompte != null ? idcompte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compte)) {
            return false;
        }
        Compte other = (Compte) object;
        if ((this.idcompte == null && other.idcompte != null) || (this.idcompte != null && !this.idcompte.equals(other.idcompte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bash.jcouture.entities.model.Compte[idcompte=" + idcompte + "]";
    }

}
