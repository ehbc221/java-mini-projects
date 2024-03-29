import javax.swing.*;
import java.util.*;
/*
 * Acceuil.java
 *
 * Created on 17 novembre 2005, 09:12
 */

/**
 *
 * @author  Sébastien
 */
public class Acceuil extends javax.swing.JFrame {
    
    /** Creates new form Acceuil */
    public Acceuil() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        desktop = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFichier = new javax.swing.JMenu();
        menuOuvrir = new javax.swing.JMenuItem();
        menuSauver = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        itemParcourir = new javax.swing.JMenuItem();
        itemRechercher = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        itemQuitter = new javax.swing.JMenuItem();
        menuFactureCee = new javax.swing.JMenu();
        itemNouvelleFactureCee = new javax.swing.JMenuItem();
        menuFactureExport = new javax.swing.JMenu();
        itemNouvelleFactureExport = new javax.swing.JMenuItem();

        getContentPane().setLayout(new java.awt.GridLayout(1, 1, 5, 5));

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        getContentPane().add(desktop);

        menuFichier.setText("Fichier");
        menuOuvrir.setText("Ouvrir");
        menuOuvrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOuvrirActionPerformed(evt);
            }
        });

        menuFichier.add(menuOuvrir);

        menuSauver.setText("Sauver");
        menuSauver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSauverActionPerformed(evt);
            }
        });

        menuFichier.add(menuSauver);

        menuFichier.add(jSeparator1);

        itemParcourir.setText("Parcourir");
        itemParcourir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemParcourirActionPerformed(evt);
            }
        });

        menuFichier.add(itemParcourir);

        itemRechercher.setText("Rechercher");
        itemRechercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemRechercherActionPerformed(evt);
            }
        });

        menuFichier.add(itemRechercher);

        menuFichier.add(jSeparator2);

        itemQuitter.setText("Quitter");
        itemQuitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemQuitterActionPerformed(evt);
            }
        });

        menuFichier.add(itemQuitter);

        jMenuBar1.add(menuFichier);

        menuFactureCee.setText("Facture CEE");
        itemNouvelleFactureCee.setText("Nouvelle facture");
        itemNouvelleFactureCee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNouvelleFactureCeeActionPerformed(evt);
            }
        });

        menuFactureCee.add(itemNouvelleFactureCee);

        jMenuBar1.add(menuFactureCee);

        menuFactureExport.setText("Facture Export");
        itemNouvelleFactureExport.setText("Nouvelle facture");
        itemNouvelleFactureExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNouvelleFactureExportActionPerformed(evt);
            }
        });

        menuFactureExport.add(itemNouvelleFactureExport);

        jMenuBar1.add(menuFactureExport);

        setJMenuBar(jMenuBar1);

        setBounds(0, 0, 1000, 700);
    }//GEN-END:initComponents

    private void itemRechercherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemRechercherActionPerformed
        Rechercher_Facture rechercherFacture = new Rechercher_Facture();
        desktop.add(rechercherFacture,javax.swing.JLayeredPane.DEFAULT_LAYER);
        rechercherFacture.show();
    }//GEN-LAST:event_itemRechercherActionPerformed

    private void menuSauverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSauverActionPerformed
        String baseName = "";
        JFileChooser save = new JFileChooser();
        save.setDialogTitle("Enregistrer la base sous...");
        int retour = save.showSaveDialog(this);
        if(retour == save.APPROVE_OPTION)
        {
            baseName = save.getSelectedFile().getAbsolutePath();
            Exception e = baseDeDonneeFacture.saveBase(baseName);
            if(e != null)
            {
                String msg = "Erreur de sauvegarde de la base : " + e;
                JOptionPane.showMessageDialog(getParent(),msg,"Erreur de sauvegarde...",JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_menuSauverActionPerformed

    private void menuOuvrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOuvrirActionPerformed
        String baseName = "";
        JFileChooser open = new JFileChooser();
        open.setDialogTitle("Ouvrir la base...");
        int retour = open.showOpenDialog(this);
        if(retour == open.APPROVE_OPTION)
        {
            baseName = open.getSelectedFile().getAbsolutePath();
            Exception e = baseDeDonneeFacture.openBase(baseName);
            if(e != null)
            {
                String msg = "Erreur de chargement de la base : " + e;
                JOptionPane.showMessageDialog(getParent(),msg,"Erreur de chargement...",JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_menuOuvrirActionPerformed

    private void itemParcourirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemParcourirActionPerformed
        Parcourir_Facture parcourirFacture = new Parcourir_Facture();
        desktop.add(parcourirFacture,javax.swing.JLayeredPane.DEFAULT_LAYER);
        parcourirFacture.show();
    }//GEN-LAST:event_itemParcourirActionPerformed

    private void itemNouvelleFactureExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNouvelleFactureExportActionPerformed
        Nouvelle_Facture_Export nouvelleFactureExport = new Nouvelle_Facture_Export();
        desktop.add(nouvelleFactureExport,javax.swing.JLayeredPane.DEFAULT_LAYER);
        nouvelleFactureExport.show();
    }//GEN-LAST:event_itemNouvelleFactureExportActionPerformed

    private void itemNouvelleFactureCeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNouvelleFactureCeeActionPerformed
        Nouvelle_Facture_Cee nouvelleFactureCee = new Nouvelle_Facture_Cee();
        desktop.add(nouvelleFactureCee,javax.swing.JLayeredPane.DEFAULT_LAYER);
        nouvelleFactureCee.show();
    }//GEN-LAST:event_itemNouvelleFactureCeeActionPerformed

    private void itemQuitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemQuitterActionPerformed
        System.exit(0);
    }//GEN-LAST:event_itemQuitterActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static Base_de_donnee_facture baseDeDonneeFacture;
    public static void main(String args[]) {
        new Acceuil().show();
        baseDeDonneeFacture = new Base_de_donnee_facture();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JMenuItem itemNouvelleFactureCee;
    private javax.swing.JMenuItem itemNouvelleFactureExport;
    private javax.swing.JMenuItem itemParcourir;
    private javax.swing.JMenuItem itemQuitter;
    private javax.swing.JMenuItem itemRechercher;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JMenu menuFactureCee;
    private javax.swing.JMenu menuFactureExport;
    private javax.swing.JMenu menuFichier;
    private javax.swing.JMenuItem menuOuvrir;
    private javax.swing.JMenuItem menuSauver;
    // End of variables declaration//GEN-END:variables
    
}
