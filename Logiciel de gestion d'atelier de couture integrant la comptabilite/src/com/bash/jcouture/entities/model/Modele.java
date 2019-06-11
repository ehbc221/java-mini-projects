/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.entities.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author bashizip
 */
@Entity
@Table(name = "modele", catalog = "couturedb", schema = "")
@NamedQueries({
    @NamedQuery(name = "Modele.findAll", query = "SELECT m FROM Modele m"),
    @NamedQuery(name = "Modele.findByIdmodele", query = "SELECT m FROM Modele m WHERE m.idmodele = :idmodele"),
    @NamedQuery(name = "Modele.findByNom", query = "SELECT m FROM Modele m WHERE m.nom = :nom")})
public class Modele implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "idmodele", nullable = false)
    private Integer idmodele;
    @Basic(optional = false)
    @Column(name = "nom", nullable = false, length = 45)
    private String nom;
    @Lob
    @Column(name = "description", length = 65535)
    private String description;
    @Lob
    @Column(name = "image")
    private byte[] image;

    public Modele() {
    }

    public Modele(Integer idmodele) {
        this.idmodele = idmodele;
    }

    public Modele(Integer idmodele, String nom) {
        this.idmodele = idmodele;
        this.nom = nom;
    }

    public Integer getIdmodele() {
        return idmodele;
    }

    public void setIdmodele(Integer idmodele) {
        this.idmodele = idmodele;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmodele != null ? idmodele.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modele)) {
            return false;
        }
        Modele other = (Modele) object;
        if ((this.idmodele == null && other.idmodele != null) || (this.idmodele != null && !this.idmodele.equals(other.idmodele))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bash.jcouture.entities.model.Modele[idmodele=" + idmodele + "]";
    }

}
