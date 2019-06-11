/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.controler;

import com.bash.jcouture.controler.exceptions.NonexistentEntityException;
import com.bash.jcouture.entities.model.TypeArticle;
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
public class TypeArticleJpaController {

    public TypeArticleJpaController() {
        emf = Persistence.createEntityManagerFactory("JCouturePU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TypeArticle typeArticle) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(typeArticle);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TypeArticle typeArticle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            typeArticle = em.merge(typeArticle);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = typeArticle.getIdtypeArticle();
                if (findTypeArticle(id) == null) {
                    throw new NonexistentEntityException("The typeArticle with id " + id + " no longer exists.");
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
            TypeArticle typeArticle;
            try {
                typeArticle = em.getReference(TypeArticle.class, id);
                typeArticle.getIdtypeArticle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The typeArticle with id " + id + " no longer exists.", enfe);
            }
            em.remove(typeArticle);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TypeArticle> findTypeArticleEntities() {
        return findTypeArticleEntities(true, -1, -1);
    }

    public List<TypeArticle> findTypeArticleEntities(int maxResults, int firstResult) {
        return findTypeArticleEntities(false, maxResults, firstResult);
    }

    private List<TypeArticle> findTypeArticleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from TypeArticle as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TypeArticle findTypeArticle(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TypeArticle.class, id);
        } finally {
            em.close();
        }
    }

    public int getTypeArticleCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from TypeArticle as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
