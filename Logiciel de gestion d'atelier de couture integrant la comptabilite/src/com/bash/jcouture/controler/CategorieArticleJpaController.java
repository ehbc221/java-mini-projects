/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.controler;

import com.bash.jcouture.controler.exceptions.NonexistentEntityException;
import com.bash.jcouture.entities.model.CategorieArticle;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author bashizip
 */
public class CategorieArticleJpaController {

    public CategorieArticleJpaController() {
        emf = Persistence.createEntityManagerFactory("JCouturePU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CategorieArticle categorieArticle) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(categorieArticle);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CategorieArticle categorieArticle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            categorieArticle = em.merge(categorieArticle);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categorieArticle.getIdcategorie();
                if (findCategorieArticle(id) == null) {
                    throw new NonexistentEntityException("The categorieArticle with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategorieArticle categorieArticle;
            try {
                categorieArticle = em.getReference(CategorieArticle.class, id);
                categorieArticle.getIdcategorie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categorieArticle with id " + id + " no longer exists.", enfe);
            }
            em.remove(categorieArticle);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CategorieArticle> findCategorieArticleEntities() {
        return findCategorieArticleEntities(true, -1, -1);
    }

    public List<CategorieArticle> findCategorieArticleEntities(int maxResults, int firstResult) {
        return findCategorieArticleEntities(false, maxResults, firstResult);
    }

    private List<CategorieArticle> findCategorieArticleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from CategorieArticle as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CategorieArticle findCategorieArticle(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CategorieArticle.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategorieArticleCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from CategorieArticle as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
