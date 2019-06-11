/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bash.jcouture.util;

import com.bash.jcouture.entities.model.Report;
import java.lang.Object;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author bashizip
 */
public class ReportBuilder {

    private List<Report> reports;

    String requette = "select article.idarticle,client.idclient,facture.montant,"
            + "Compte.libele,mouvement_compte.entree,"
            + "(select 100*mouvement_compte.entree/facture.montant) as Pourcentage"
            + " from article,client,facture,mouvement_compte,compte where"
            + " article.idclient=client.idclient and"
            + " article.idfacture=facture.idfacture and"
            + " mouvement_compte.idcompte=compte.idcompte and"
            + " mouvement_compte.idfacture=article.idfacture and"
            + " article.etat='cloturé' and"
            + " mouvement_compte.date between ? and ? order by idarticle";

    
    private EntityManagerFactory emf = null;
    

    public ReportBuilder() {
        emf = Persistence.createEntityManagerFactory("JCouturePU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public double getTotalEntree() {
        double somme = 0;
        if (reports != null) {
            for (Report r : reports) {
                somme += r.getMontant();
                //reports.remove(r);
            }
        }
        return somme;
    }

    public List<Report> findReportEntities(Date debut, Date fin) {
        EntityManager em = getEntityManager();
        List<Report> list = new ArrayList<Report>();

        try {
            Query q = em.createNativeQuery(requette);

//            q.setParameter( )
//            if (!all) {
//                q.setMaxResults(maxResults);
//                q.setFirstResult(firstResult);
//            }
            q.setParameter(1, debut);
            q.setParameter(2, fin);

            for (Object obj : q.getResultList()) {

                Vector rep = (Vector) obj;
                Report areport = new Report();

                areport.setIdarticle(Integer.valueOf(rep.get(0).toString()));
                areport.setClient(rep.get(1).toString());
                areport.setMontant(Double.valueOf(rep.get(2).toString()));
                areport.setCompte(rep.get(3).toString());
                areport.setEntree(Double.valueOf(rep.get(4).toString()));
                areport.setPourcentage(Double.valueOf(rep.get(5).toString()));

                list.add(areport);

            }
            this.reports = list;
            return reports;
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
     ReportBuilder test=new ReportBuilder();
     test.findReportEntities(new Date(), null);
    }
}
