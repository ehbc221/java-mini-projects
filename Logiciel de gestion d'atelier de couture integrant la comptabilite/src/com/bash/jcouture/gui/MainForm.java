/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainForm.java
 *
 * Created on 6 janv. 2011, 15:08:06
 */
package com.bash.jcouture.gui;

//import com.bash.jcouture.util.MySwingUtilities;
import com.bash.jcouture.util.swing.MySwingUtilities;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXHyperlink;

/**
 *
 * @author bashizip
 */
public class MainForm extends javax.swing.JFrame {

    private JXHyperlink hynewclient, hydaillyreports,
            hyclientsList, hyedit_models, hymesure,
            hyarticles, hylogout, hycomCloturees,
            hycomptes, hybalance, hymodels;
    private MySwingUtilities sUtils = new MySwingUtilities();
    private WelcomeLoginPane welcomePane;

    /** Creates new form MainForm */
    public MainForm() {
        //setLocationRelativeTo(null);
        initComponents();
        welcomePane = new WelcomeLoginPane(this);
        setTitle("JCouture 1.0");
        createTaskPane();
        loginMode();
    }

    void createTaskPane() {

        hynewclient = new JXHyperlink(new_client_action);
        hynewclient.setText("Nouveau Client");

        hynewclient.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/Add-24_2.png")));
        tp_main.add(hynewclient);

        hycomCloturees = new JXHyperlink(com_cloturees_action);
        hycomCloturees.setText("Comandes cloturées");
        hycomCloturees.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/Profile.png")));
        tp_story.add(hycomCloturees);

        hynewclient.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/Add-24_2.png")));
        tp_main.add(hynewclient);

        hydaillyreports = new JXHyperlink(dailly_report_action);
        hydaillyreports.setText("Rapport Journalier");

        hydaillyreports.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/Modify.png")));
        tp_reports.add(hydaillyreports);


        hymesure = new JXHyperlink(new_article_action);
        hymesure.setText("Nouvel Article");

        hymesure.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/Add-24_2.png")));
        tp_main.add(hymesure);



        hyarticles = new JXHyperlink(articles_action);
        hyarticles.setText("Commandes");

        hyarticles.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/magasin2.png")));
        tp_main.add(hyarticles);

        hyclientsList = new JXHyperlink(client_list_action);
        hyclientsList.setText("Liste de clients");

        hyclientsList.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/Profile.png")));
        tp_story.add(hyclientsList);

        hyedit_models = new JXHyperlink(edit_models_action);
        hyedit_models.setText("Editions des Models");

        hyedit_models.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/cay.png")));
        tp_admin.add(hyedit_models);


        hylogout = new JXHyperlink(logout_action);
        hylogout.setText("Quitter");

        hylogout.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/Exit.png")));
        tp_admin.add(hylogout);

        hycomptes = new JXHyperlink(comptes_action);
        hycomptes.setText("Comptes");

        hycomptes.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/Pie Chart-24.png")));
        tp_compta.add(hycomptes);

        hybalance = new JXHyperlink(balance_action);
        hybalance.setText("Balance");

        hybalance.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/Bar Chart24.png")));
        tp_compta.add(hybalance);


        hymodels = new JXHyperlink(modeles_action);
        hymodels.setText("Mes modèles");

        hymodels.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/perform_new_sale_highlight.png")));
        tp_main.add(hymodels);
//--------------------------------------------------------------------------------------
        tp_admin.setCollapsed(true);
        tp_compta.setCollapsed(true);
        tp_stock.setCollapsed(true);

    }
    ///// ////////////////////////////////////////////////////////////////////////////////
    Action new_client_action = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            addClientMode();
            System.out.println("Nouveau Client Crée");
        }
    };
    Action comptes_action = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            comptesMode();
            // System.out.println("Nouveau Client Crée");
        }
    };
    Action com_cloturees_action = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            comClotureMode();
            // System.out.println("Nouveau Client Crée");
        }
    };
    Action logout_action = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            if (hyarticles.isEnabled()) {
                loginMode();
            } else {
                if (JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment quitter?") == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    return;
                }
            }
        }
    };
    Action new_article_action = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            addArticleMode();

        }
    };
    Action dailly_report_action = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            daillyReportMode();
            System.out.println("Dailly report");
        }
    };
    Action client_list_action = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            clientListMode();

        }
    };
    Action edit_models_action = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            editModelsMode();

        }
    };
    Action articles_action = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            articlesMode();

        }
    };
    Action balance_action = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            balanceMode();

        }
    };
    Action modeles_action = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {

            modelesMode();
        }
    };

/////////////////////////////////////////////////////////////////////////////////////////////
    void editModelsMode() {
        MySwingUtilities.setContentPane(parent_pane, new EditModelPane());
    }

    void addArticleMode() {
        MySwingUtilities.setContentPane(parent_pane, new AddArticlePane());
    }

    void clientListMode() {

        MySwingUtilities.setContentPane(parent_pane, new ClientListPane());
    }

    void daillyReportMode() {
        MySwingUtilities.setContentPane(parent_pane, new WeeklyReportPane());
    }

    void addClientMode() {
        MySwingUtilities.setContentPane(parent_pane, new AddClientPane());

    }

    void articlesMode() {
        MySwingUtilities.setContentPane(parent_pane, new CommandesPane());
    }

    void comClotureMode() {
        MySwingUtilities.setContentPane(parent_pane, new ComadesCloturesPane());
    }

    void comptesMode() {
        MySwingUtilities.setContentPane(parent_pane, new ComptesPane());
    }

    void balanceMode() {
    }

    void modelesMode () {
        MySwingUtilities.setContentPane(parent_pane  ,new ModelesPanes());
    }

    void enableDesableHyperLinks(boolean enableOrDesable) {
        Component[] c = this.getComponents();
        for (int i = 0; i < c.length; i++) {
            if (c[i].getClass().equals(JXHyperlink.class)) {
                c[i].setEnabled(enableOrDesable);
            }
        }
    }

    void loginMode() {

        MySwingUtilities.setContentPane(parent_pane, welcomePane);

        hyarticles.setEnabled(false);
        //enableDesableHyperLinks(false);

        hyclientsList.setEnabled(false);
        hydaillyreports.setEnabled(false);
        hymesure.setEnabled(false);
        hyedit_models.setEnabled(false);
        hynewclient.setEnabled(false);
        jXTaskPaneContainer1.setVisible(false);
    }

    public void connectedMode() {
        // MySwingUtilities.setContentPane(welcomePane, mycontentpane);
        // jXTaskPaneContainer1.setVisible(true);

        // enableDesableHyperLinks(true);
        hyarticles.setEnabled(true);
        hyclientsList.setEnabled(true);
        hydaillyreports.setEnabled(true);
        hymesure.setEnabled(true);
        hyedit_models.setEnabled(true);
        hynewclient.setEnabled(true);


        addArticleMode();

        jXTaskPaneContainer1.setVisible(true);

        mycontentpane.repaint();
        mycontentpane.revalidate();

        welcomePane.stopBusy();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jXHeader1 = new org.jdesktop.swingx.JXHeader();
        mycontentpane = new javax.swing.JPanel();
        jXTaskPaneContainer1 = new org.jdesktop.swingx.JXTaskPaneContainer();
        tp_main = new org.jdesktop.swingx.JXTaskPane();
        tp_story = new org.jdesktop.swingx.JXTaskPane();
        tp_reports = new org.jdesktop.swingx.JXTaskPane();
        tp_admin = new org.jdesktop.swingx.JXTaskPane();
        tp_compta = new org.jdesktop.swingx.JXTaskPane();
        tp_stock = new org.jdesktop.swingx.JXTaskPane();
        parent_pane = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jXHeader1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tp_main.setTitle("Principal");
        jXTaskPaneContainer1.add(tp_main);

        tp_story.setTitle("Historique");
        jXTaskPaneContainer1.add(tp_story);

        tp_reports.setTitle("Rapports");
        jXTaskPaneContainer1.add(tp_reports);

        tp_admin.setTitle("Administration");
        jXTaskPaneContainer1.add(tp_admin);

        tp_compta.setToolTipText("");
        tp_compta.setTitle("Comptabilité");
        jXTaskPaneContainer1.add(tp_compta);

        tp_stock.setTitle("Gestion de stock");
        jXTaskPaneContainer1.add(tp_stock);

        parent_pane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout parent_paneLayout = new javax.swing.GroupLayout(parent_pane);
        parent_pane.setLayout(parent_paneLayout);
        parent_paneLayout.setHorizontalGroup(
            parent_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 684, Short.MAX_VALUE)
        );
        parent_paneLayout.setVerticalGroup(
            parent_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 487, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout mycontentpaneLayout = new javax.swing.GroupLayout(mycontentpane);
        mycontentpane.setLayout(mycontentpaneLayout);
        mycontentpaneLayout.setHorizontalGroup(
            mycontentpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mycontentpaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(parent_pane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        mycontentpaneLayout.setVerticalGroup(
            mycontentpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mycontentpaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mycontentpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(parent_pane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jXHeader1, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mycontentpane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jXHeader1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mycontentpane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXHeader jXHeader1;
    private org.jdesktop.swingx.JXTaskPaneContainer jXTaskPaneContainer1;
    private javax.swing.JPanel mycontentpane;
    private javax.swing.JPanel parent_pane;
    private org.jdesktop.swingx.JXTaskPane tp_admin;
    private org.jdesktop.swingx.JXTaskPane tp_compta;
    private org.jdesktop.swingx.JXTaskPane tp_main;
    private org.jdesktop.swingx.JXTaskPane tp_reports;
    private org.jdesktop.swingx.JXTaskPane tp_stock;
    private org.jdesktop.swingx.JXTaskPane tp_story;
    // End of variables declaration//GEN-END:variables
}
