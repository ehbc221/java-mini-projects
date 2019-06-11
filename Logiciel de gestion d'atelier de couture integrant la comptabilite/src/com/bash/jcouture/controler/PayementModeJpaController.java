/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.controler;

import com.bash.jcouture.controler.exceptions.IllegalOrphanException;
import com.bash.jcouture.controler.exceptions.NonexistentEntityException;
import com.bash.jcouture.entities.model.PayementMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.bash.jcouture.entities.model.Facture;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bashizip
 */
public class PayementModeJpaController {

    public PayementModeJpaController() {
        emf = Persistence.createEntityManagerFactory("JCouturePU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PayementMode payementMode) {
        if (payementMode.getFactureList() == null) {
            payementMode.setFactureList(new ArrayList<Facture>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Facture> attachedFactureList = new ArrayList<Facture>();
            for (Facture factureListFactureToAttach : payementMode.getFactureList()) {
                factureListFactureToAttach = em.getReference(factureListFactureToAttach.getClass(), factureListFactureToAttach.getIdfacture());
                attachedFactureList.add(factureListFactureToAttach);
            }
            payementMode.setFactureList(attachedFactureList);
            em.persist(payementMode);
            for (Facture factureListFacture : payementMode.getFactureList()) {
                PayementMode oldPayementModeOfFactureListFacture = factureListFacture.getPayementMode();
                factureListFacture.setPayementMode(payementMode);
                factureListFacture = em.merge(factureListFacture);
                if (oldPayementModeOfFactureListFacture != null) {
                    oldPayementModeOfFactureListFacture.getFactureList().remove(factureListFacture);
                    oldPayementModeOfFactureListFacture = em.merge(oldPayementModeOfFactureListFacture);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PayementMode payementMode) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PayementMode persistentPayementMode = em.find(PayementMode.class, payementMode.getIdpayementMode());
            List<Facture> factureListOld = persistentPayementMode.getFactureList();
            List<Facture> factureListNew = payementMode.getFactureList();
            List<String> illegalOrphanMessages = null;
            for (Facture factureListOldFacture : factureListOld) {
                if (!factureListNew.contains(factureListOldFacture)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Facture " + factureListOldFacture + " since its payementMode field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Facture> attachedFactureListNew = new ArrayList<Facture>();
            for (Facture factureListNewFactureToAttach : factureListNew) {
                factureListNewFactureToAttach = em.getReference(factureListNewFactureToAttach.getClass(), factureListNewFactureToAttach.getIdfacture());
                attachedFactureListNew.add(factureListNewFactureToAttach);
            }
            factureListNew = attachedFactureListNew;
            payementMode.setFactureList(factureListNew);
            payementMode = em.merge(payementMode);
            for (Facture factureListNewFacture : factureListNew) {
                if (!factureListOld.contains(factureListNewFacture)) {
                    PayementMode oldPayementModeOfFactureListNewFacture = factureListNewFacture.getPayementMode();
                    factureListNewFacture.setPayementMode(payementMode);
                    factureListNewFacture = em.merge(factureListNewFacture);
                    if (oldPayementModeOfFactureListNewFacture != null && !oldPayementModeOfFactureListNewFacture.equals(payementMode)) {
                        oldPayementModeOfFactureListNewFacture.getFactureList().remove(factureListNewFacture);
                        oldPayementModeOfFactureListNewFacture = em.merge(oldPayementModeOfFactureListNewFacture);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = payementMode.getIdpayementMode();
                if (findPayementMode(id) == null) {
                    throw new NonexistentEntityException("The payementMode with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PayementMode payementMode;
            try {
                payementMode = em.getReference(PayementMode.class, id);
                payementMode.getIdpayementMode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The payementMode with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Facture> factureListOrphanCheck = payementMode.getFactureList();
            for (Facture factureListOrphanCheckFacture : factureListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PayementMode (" + payementMode + ") cannot be destroyed since the Facture " + factureListOrphanCheckFacture + " in its factureList field has a non-nullable payementMode field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(payementMode);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PayementMode> findPayementModeEntities() {
        return findPayementModeEntities(true, -1, -1);
    }

    public List<PayementMode> findPayementModeEntities(int maxResults, int firstResult) {
        return findPayementModeEntities(false, maxResults, firstResult);
    }

    private List<PayementMode> findPayementModeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from PayementMode as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public PayementMode findPayementMode(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PayementMode.class, id);
        } finally {
            em.close();
        }
    }

    public int getPayementModeCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from PayementMode as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
