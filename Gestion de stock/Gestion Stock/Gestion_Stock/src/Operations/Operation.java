/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Operations;

/**importation des packages necessaires*/
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import Beans.*;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author Ibrahima
 */
public class Operation {

    /*Declaration des variables*/
    Connection con = conect();
    ResultSet rs;
    PreparedStatement stat;
    Statement s;
    Vector<Mail> mail;
    static final String PROPERTY_FILE = "database.properties";
    /*Methode Permettant detablir la connectio a la base de donnees
    qui return un objet de type Connection */

    public Connection conect() {
        try {
            /*Chargement du pilote*/
            // Utilisation du fichier .properties pour la connection a la base donnees
            Properties prop = new Properties();
            prop.load(new FileInputStream(PROPERTY_FILE));
            String driver = prop.getProperty("driver");
            String url = prop.getProperty("url");
            String user = prop.getProperty("user");
            String pass = prop.getProperty("password");
            ///
            Class.forName(driver);
            /*Etablissement de la connection*/
            con = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur de connection a la base de donnees :" + e);
        }
        /*retourne un object con de type Connection*/
        return con;
    }

    /*Methode permettant de passer une commande
    dinserer des donnees dans la table Commande*/
    public void commander_article(Commander c) {
        try {
            /*initialisation de lobject stat de type prepareStatement*/
            stat = con.prepareStatement("insert into Commande values(?,?,?,?)");
            /*initialisation des parametres sql*/
            stat.setInt(1, c.getId_Commande());
            stat.setString(2, c.getNom_Article());
            stat.setString(3, c.get_Categorie());
            stat.setInt(4, c.getQuantite());
            int i = stat.executeUpdate();
            if (i > 0) {
                JOptionPane.showMessageDialog(null, "Insertion reussie !");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur Insertion \n" + e.getMessage());
        }
    }

    /*Methode permettant de passer une commande
    dinserer des donnees dans la table Commande*/
    public void entrer_fournisseur(Fournisseur f) {
        try {
            /*initialisation de lobject stat de type prepareStatement*/
            stat = con.prepareStatement("insert into Fournisseur values(?,?,?,?)");
            /*initialisation des parametres sql*/
            stat.setInt(1, f.getId_fournisseur());
            stat.setString(2, f.getNom());
            stat.setString(3, f.getCategorie());
            stat.setString(4, f.getAdresse());
            int i = stat.executeUpdate();
            if (i > 0) {
                JOptionPane.showMessageDialog(null, "Insertion reussie !");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur Insertion \n" + e.getMessage());
        }
    }
    /*Methose pour la visualisation des donnees*/

    public Object[][] consulter_stock() {
        Object[][] donnes = new Object[100][4];

        try {
            /*initialisation de l'object con de type Connection et etablissement de la connection*/
            con = conect();
            String r = "select * from Article_Stock";
            /*initialisation de lobjet stat de type prepareStatement*/
            s = con.createStatement();
            rs = s.executeQuery(r);
            int i = 0;
            while (rs.next()) {
                donnes[i][0] = rs.getString("ID_Article");
                donnes[i][1] = rs.getString("Nom_Article");
                donnes[i][2] = rs.getString("Categorie");
                donnes[i][3] = rs.getString("Quantite");
                i++;
            }
        } catch (Exception e) {
        }
        return donnes;
    }

    public Object[][] consulter_commande() {
        Object[][] donnes = new Object[100][4];

        try {
            /*initialisation de l'object con de type Connection et etablissement de la connection*/
            con = conect();
            /*initialisation de lobjet stat de type prepareStatement*/
            s = con.createStatement();
            rs = s.executeQuery("select * from Commande");
            int i = 0;
            while (rs.next()) {
                donnes[i][0] = rs.getString("ID_Com");
                donnes[i][1] = rs.getString("Nom_Article");
                donnes[i][2] = rs.getString("Categorie");
                donnes[i][3] = rs.getString("Quantite");
                i++;
            }
        } catch (Exception e) {
        }
        return donnes;
    }

    public Object[][] consulter_fournisseur() {
        Object[][] donnes = new Object[100][4];

        try {
            /*initialisation de l'object con de type Connection et etablissement de la connection*/
            con = conect();
            /*initialisation de lobjet stat de type prepareStatement*/
            s = con.createStatement();
            rs = s.executeQuery("select * from Fournisseur");
            int i = 0;
            while (rs.next()) {
                donnes[i][0] = Integer.parseInt(rs.getString("ID_Fournisseur"));
                donnes[i][1] = rs.getString("Nom_Fournisseur");
                donnes[i][2] = rs.getString("Categorie_Fournie");
                donnes[i][3] = rs.getString("Adresse");
                i++;
            }
        } catch (Exception e) {
        }
        return donnes;
    }

    /*Methode pour Sortir un article du  stock (supprimer de la table Article_Stock) 
    et et de le remettre au client (inserer dans la table Employe)
    La methode prend un bean Employe comme paramettre d'entrée,
    recupere ses membres et les insere dans la table Employe*/
    public void sortir_acticle(Employe c) {
        /*recuperation des membres du bean Employe deja rempli*/
        int id_client = c.getId_Client();
        int id_article = c.getId_Article();
        String nom_article = c.getNom_Article();
        String categorie = c.getCategorie();
        int quantite = c.getQuantite();

        try {
            /*initialisation de lobjet stat de type prepareStatement,
            creation de la requete pour linsertion dans la table Employe*/
            stat = con.prepareStatement("insert into Client values (?,?,?,?,?)");
            /*initialisation des valeur de lobjet prepareStatement avec les
            valeures recuperéés a partir du bean Employe accepté comme parametre dentree*/
            stat.setInt(1, id_client);
            stat.setInt(2, id_article);
            stat.setString(3, nom_article);
            stat.setString(4, categorie);
            stat.setInt(5, quantite);
            /*execution de la requete*/
            int i = stat.executeUpdate();
            if (i != 0) {
                JOptionPane.showMessageDialog(null, "Article Remis au client N°:" + id_client);
            } else {
                JOptionPane.showMessageDialog(null, "Erreur de traitement");
            }
            /**Creation de la requete pour supprimer la ligne recuperée de la
            table Article_Stock et inserée das la table Employe*/
            stat = con.prepareStatement("delete from Article_Stock where ID_Article=?");
            stat.setInt(1, c.getId_Article());
            /**execution de la requete*/
            int ex = stat.executeUpdate();
            if (ex != 0) {
                JOptionPane.showMessageDialog(null, "Supression Article N°:" + id_article + "\nDe la table Article");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur de traitement");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur Suppression :" + e);
        }
    }
    /*Methode pour Sortir un article du  stock (supprimer de la table Commande) 
    et et de le remettre au client (inserer dans la table Article_Stock)
    La methode prend comme parametre dentree un bean de type Article
    recupere ses membre et les insere dans la base*/

    public void rentrer_article(Article a) {
        /*recuperation des membres du bean Article deja rempli*/
        int id_article = a.getId_Article();
        String nom_article = a.getNom_Article();
        String categorie = a.getCategorie();
        int quantite = a.getQuantite();
        try {
            /*creation de la requete pour linsertion dans la table Article_Stock*/
            stat = con.prepareStatement("insert into Article_Stock values (?,?,?,?)");
            /*initialisation des valeur de lobjet prepareStatement avec les
            valeures recuperéés a partir du bean Article accepté comme parametre dentree*/
            stat.setInt(1, id_article);
            stat.setString(2, nom_article);
            stat.setString(3, categorie);
            stat.setInt(4, quantite);
            /*execution de la requete*/
            int i = stat.executeUpdate();
            if (i != 0) {
                JOptionPane.showMessageDialog(null, "Article N°:" + id_article + "\nEn Stock ");

            } else {
                JOptionPane.showMessageDialog(null, "Erreur de traitement");
            }
            /*Creation de la requete pour supprimer la ligne recuperée de la
            table Commande et inserée das la table Article_Stock*/
            stat = con.prepareStatement("delete from Commande where ID_Com=?");
            stat.setInt(1, id_article);
            int ex = stat.executeUpdate();
            if (ex != 0) {
                JOptionPane.showMessageDialog(null, "Supression Article N°:" + id_article + "\nDe la table Commande");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur de traitement");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public int donnes_diagramme_article(String nom_article) {
        int i = 0;
        try {
            String str = "{call Donnees_Diagramme_Article(?,?)}";
            CallableStatement call = con.prepareCall(str);
            call.setString(1, nom_article);
            call.registerOutParameter(2, Types.INTEGER);
            call.execute();
            i = call.getInt("Quantite");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur Operation \n" + e.getMessage());
        }
        return i;
    }

    public int donnes_diagramme_categorie(String categorie) {
        int i = 0;
        try {
            String str = "{call Donnees_Diagramme_Categorie(?,?)}";
            CallableStatement call = con.prepareCall(str);
            call.setString(1, categorie);
            call.registerOutParameter(2, Types.INTEGER);
            call.execute();
            i = call.getInt("Quantite");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur Operation \n" + e.getMessage());
        }
        return i;
    }

}
