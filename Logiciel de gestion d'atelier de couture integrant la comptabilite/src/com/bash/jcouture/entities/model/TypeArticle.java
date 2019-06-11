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
@Table(name = "type_article", catalog = "couturedb", schema = "")
@NamedQueries({
    @NamedQuery(name = "TypeArticle.findAll", query = "SELECT t FROM TypeArticle t"),
    @NamedQuery(name = "TypeArticle.findByIdtypeArticle", query = "SELECT t FROM TypeArticle t WHERE t.idtypeArticle = :idtypeArticle"),
    @NamedQuery(name = "TypeArticle.findByNom", query = "SELECT t FROM TypeArticle t WHERE t.nom = :nom")})
public class TypeArticle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtype_article", nullable = false)
    private Integer idtypeArticle;
    @Basic(optional = false)
    @Column(name = "nom", nullable = false, length = 45)
    private String nom;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeArticle")
    private List<Article> articleList;

    public TypeArticle() {
    }

    public TypeArticle(Integer idtypeArticle) {
        this.idtypeArticle = idtypeArticle;
    }

    public TypeArticle(Integer idtypeArticle, String nom) {
        this.idtypeArticle = idtypeArticle;
        this.nom = nom;
    }

    public Integer getIdtypeArticle() {
        return idtypeArticle;
    }

    public void setIdtypeArticle(Integer idtypeArticle) {
        this.idtypeArticle = idtypeArticle;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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
        hash += (idtypeArticle != null ? idtypeArticle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypeArticle)) {
            return false;
        }
        TypeArticle other = (TypeArticle) object;
        if ((this.idtypeArticle == null && other.idtypeArticle != null) || (this.idtypeArticle != null && !this.idtypeArticle.equals(other.idtypeArticle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bash.jcouture.entities.model.TypeArticle[idtypeArticle=" + idtypeArticle + "]";
    }

}
