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
@Table(name = "mesure", catalog = "couturedb", schema = "")
@NamedQueries({
    @NamedQuery(name = "Mesure.findAll", query = "SELECT m FROM Mesure m"),
    @NamedQuery(name = "Mesure.findByIdmesure", query = "SELECT m FROM Mesure m WHERE m.idmesure = :idmesure"),
    @NamedQuery(name = "Mesure.findByLongueur", query = "SELECT m FROM Mesure m WHERE m.longueur = :longueur"),
    @NamedQuery(name = "Mesure.findByContourPotrine", query = "SELECT m FROM Mesure m WHERE m.contourPotrine = :contourPotrine"),
    @NamedQuery(name = "Mesure.findByContourHanche", query = "SELECT m FROM Mesure m WHERE m.contourHanche = :contourHanche"),
    @NamedQuery(name = "Mesure.findByEpaule", query = "SELECT m FROM Mesure m WHERE m.epaule = :epaule"),
    @NamedQuery(name = "Mesure.findByMache", query = "SELECT m FROM Mesure m WHERE m.mache = :mache"),
    @NamedQuery(name = "Mesure.findByPince", query = "SELECT m FROM Mesure m WHERE m.pince = :pince"),
    @NamedQuery(name = "Mesure.findByContourVentre", query = "SELECT m FROM Mesure m WHERE m.contourVentre = :contourVentre"),
    @NamedQuery(name = "Mesure.findByContourTaille", query = "SELECT m FROM Mesure m WHERE m.contourTaille = :contourTaille")})
public class Mesure implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmesure", nullable = false)
    private Integer idmesure;
    @Column(name = "longueur", precision = 22)
    private Double longueur;
    @Column(name = "contour_potrine", precision = 22)
    private Double contourPotrine;
    @Column(name = "contour_hanche", precision = 22)
    private Double contourHanche;
    @Column(name = "epaule", precision = 22)
    private Double epaule;
    @Column(name = "mache", length = 45)
    private String mache;
    @Column(name = "pince", length = 45)
    private String pince;
    @Column(name = "contour_ventre", precision = 22)
    private Double contourVentre;
    @Column(name = "contour_taille", precision = 22)
    private Double contourTaille;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mesure")
    private List<Article> articleList;

    public Mesure() {
    }

    public Mesure(Integer idmesure) {
        this.idmesure = idmesure;
    }

    public Integer getIdmesure() {
        return idmesure;
    }

    public void setIdmesure(Integer idmesure) {
        this.idmesure = idmesure;
    }

    public Double getLongueur() {
        return longueur;
    }

    public void setLongueur(Double longueur) {
        this.longueur = longueur;
    }

    public Double getContourPotrine() {
        return contourPotrine;
    }

    public void setContourPotrine(Double contourPotrine) {
        this.contourPotrine = contourPotrine;
    }

    public Double getContourHanche() {
        return contourHanche;
    }

    public void setContourHanche(Double contourHanche) {
        this.contourHanche = contourHanche;
    }

    public Double getEpaule() {
        return epaule;
    }

    public void setEpaule(Double epaule) {
        this.epaule = epaule;
    }

    public String getMache() {
        return mache;
    }

    public void setMache(String mache) {
        this.mache = mache;
    }

    public String getPince() {
        return pince;
    }

    public void setPince(String pince) {
        this.pince = pince;
    }

    public Double getContourVentre() {
        return contourVentre;
    }

    public void setContourVentre(Double contourVentre) {
        this.contourVentre = contourVentre;
    }

    public Double getContourTaille() {
        return contourTaille;
    }

    public void setContourTaille(Double contourTaille) {
        this.contourTaille = contourTaille;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmesure != null ? idmesure.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mesure)) {
            return false;
        }
        Mesure other = (Mesure) object;
        if ((this.idmesure == null && other.idmesure != null) || (this.idmesure != null && !this.idmesure.equals(other.idmesure))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bash.jcouture.entities.model.Mesure[idmesure=" + idmesure + "]";
    }

}
