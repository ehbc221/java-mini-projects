/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Abonnement;
import entity.ChargeCompte;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author khaled.medjhoum
 */
public class AbonnementFacade extends AbstractSQL {

    public AbonnementFacade() {
    }

    public List<Abonnement> getListMailClient() {
        List<Abonnement> lcs = new ArrayList<Abonnement>();
        String query = "SELECT DATEDIFF(a.date_fin,CURDATE()) as reste, a.id_abonnement, a.id_client, a.date_debut, a.date_fin, a.etat_abonnement, c.id , c.Nom , c.Email , c.Contact ";
        query += "FROM abonnement a JOIN clients c ON c.id = a.id_client ";
        query += "WHERE a.etat_abonnement = 1 AND ( ";
        query += "1 OR DATE_ADD( CURDATE( ) , INTERVAL 15 DAY ) = date_fin ";
        query += "OR DATE_ADD( CURDATE( ) , INTERVAL 10 DAY ) = date_fin ";
        query += "OR DATE_ADD( CURDATE( ) , INTERVAL 5 DAY ) = date_fin ";
        query += "OR DATE_ADD( CURDATE( ) , INTERVAL 2 DAY ) = date_fin ";
        query += "OR DATE_ADD( CURDATE( ) , INTERVAL 12 DAY ) = date_fin ";
        query += "OR DATE_ADD( CURDATE( ) , INTERVAL 4 DAY ) = date_fin ";
        query += ") ";

        if (connect()) {
            try {
                ResultSet rs = execResult(query);
                if (rs != null) {
                    while (rs.next()) {
                        lcs.add(new Abonnement(rs.getInt("reste"), rs.getInt("id_abonnement"), rs.getInt("id_client"), rs.getDate("date_debut"), rs.getDate("date_fin"), rs.getInt("etat_abonnement"), rs.getString("Nom"), rs.getString("Email"), rs.getString("Contact"),0,0,0,""));
                    }
                }
            } catch (SQLException ex) {
            }
        } else {
            System.out.println("Mysql connection failed !!!");
        }
        close();

        return lcs;
    }

    public List<Abonnement> getListClient(int idContact, int day, boolean beforeEndAbonnement) {
        List<Abonnement> lcs = new ArrayList<Abonnement>();
        String query = "SELECT DATEDIFF(a.date_fin,CURDATE()) as reste, a.id_abonnement, a.id_client, a.date_debut, a.date_fin, a.etat_abonnement, c.id , c.Nom , c.Email , c.Contact, a.couper_client, a.send_mail_client, a.facture_regle, a.observation, 'connexion' as Connexion ";
        query += "FROM abonnement a JOIN clients c ON c.id = a.id_client JOIN abonnement_contact ac ON ac.id_abonnement = a.id_abonnement ";
        query += "WHERE ac.id_contatct = " + idContact;
//        query += " AND ";
//        if (beforeEndAbonnement){
//            query += "DATE_ADD( CURDATE( ) , INTERVAL "+day+" DAY ) = date_fin ";
//        }else{
//            query += "DATE_SUB( CURDATE( ) , INTERVAL "+day+" DAY ) = date_debut ";
//        }
        if (connect()) {
            try {
                ResultSet rs = execResult(query);
                if (rs != null) {
                    while (rs.next()) {
                        lcs.add(new Abonnement(rs.getInt("reste"), rs.getInt("id_abonnement"), rs.getInt("id_client"), rs.getDate("date_debut"), rs.getDate("date_fin"), rs.getInt("etat_abonnement"), rs.getString("Nom"), rs.getString("Email"), rs.getString("Contact"), rs.getInt("couper_client"), rs.getInt("send_mail_client"), rs.getInt("facture_regle"), rs.getString("Connexion")));
                    }
                }
            } catch (SQLException ex) {
            }
        } else {
            System.out.println("Mysql connection failed !!!");
        }
        close();
        return lcs;
    }

    public List<ChargeCompte> getListMailChargeClientPourRappelEtablierFacture() {
        return getListMailChargeClient(35, true);
    }

    public List<ChargeCompte> getListMailChargeClientPourRappelReglementFacture() {
        return getListMailChargeClient(15, true);
    }

    public List<ChargeCompte> getListMailChargeClientPourRappelIntroduireFacture() {
        return getListMailChargeClient(30, true);
    }

    public List<ChargeCompte> getListMailChargeClientPourDebutAbonnement() {
        return getListMailChargeClient(0, false);
    }

    public List<ChargeCompte> getListMailChargeClientPourRelanceReglementFacture() {
        return getListMailChargeClient(15, false);
    }

    public List<ChargeCompte> getListMailChargeClientPourReppelDeCoupure() {
        return getListMailChargeClient(20, false);
    }

    public List<ChargeCompte> getListMailChargeClientPourCoupure() {
        return getListMailChargeClient(21, false);
    }
    
    private List<ChargeCompte> getListMailChargeClient(int day, boolean beforeEndAbonnement){
        List<ChargeCompte> lcs = new ArrayList<ChargeCompte>();
        String query = "SELECT id_contatct , Nom , Prenom , Portable , Email ";
        query += "FROM abonnement_contact a ";
        query += "JOIN charge_compte u ON a.id_contatct = u.id ";
        query += "GROUP BY a.id_contatct ";
        if (connect()) {
            try {
                ResultSet rs = execResult(query);
                if (rs != null) {
                    while (rs.next()) {
                        lcs.add(new ChargeCompte(rs.getString("Nom"), rs.getString("Prenom"), rs.getString("Email"), rs.getString("Portable"), getListClient(rs.getInt("id_contatct"), day, beforeEndAbonnement)));
                    }
                }
            } catch (SQLException ex) {}          
        } else {
            System.out.println("Mysql connection failed !!!");
        }
        close();
        return lcs;
    }
}
