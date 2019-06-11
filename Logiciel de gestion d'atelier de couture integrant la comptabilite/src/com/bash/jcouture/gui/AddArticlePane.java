/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AddArticlePane.java
 *
 * Created on 7 janv. 2011, 23:40:25
 */
package com.bash.jcouture.gui;

import com.bash.jcouture.controler.ArticleJpaController;
import com.bash.jcouture.controler.CategorieArticleJpaController;
import com.bash.jcouture.controler.ClientJpaController;
import com.bash.jcouture.controler.ModeleJpaController;
import com.bash.jcouture.controler.TypeArticleJpaController;
import com.bash.jcouture.entities.model.Article;
import com.bash.jcouture.entities.model.CategorieArticle;
import com.bash.jcouture.entities.model.Client;
import com.bash.jcouture.entities.model.Facture;
import com.bash.jcouture.entities.model.Mesure;
import com.bash.jcouture.entities.model.Modele;
import com.bash.jcouture.entities.model.TypeArticle;
import com.bash.jcouture.util.ImageFileHandler;
//import com.bash.jcouture.util.MyStringUtils;
import com.bash.jcouture.util.swing.MyStringUtils;

import java.awt.Cursor;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author bashizip
 */
public class AddArticlePane extends javax.swing.JPanel {

    private Article article;
    private ArticleJpaController artControl;
    private ClientJpaController clientControl;
    private TypeArticleJpaController taControl;
    private ModeleJpaController moControl;
    private Mesure mesures;
    private Facture facture;
    private Client client;
    private CategorieArticle categorie;
    FileInputStream fis;

    /** Creates new form AddArticlePane */
    public AddArticlePane() {

        initComponents();
        initComponents2();
        btn_save.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/save.png")));
    }

    public AddArticlePane(String idclient) {
        this();
        comb_client.setModel(new DefaultComboBoxModel(new String[]{idclient}));
    }

    public AddArticlePane(Client client) {
        this();
        this.client = client;
        comb_client.setModel(new DefaultComboBoxModel(new String[]{client.getIdclient()}));
    }

    void initComponents2() {

        clientControl = new ClientJpaController();
        taControl = new TypeArticleJpaController();
        moControl = new ModeleJpaController();

        List<Client> clients = clientControl.findClientEntities();
        Vector<String> ids = new Vector<String>();

        for (Client c : clients) {
            ids.add(c.getIdclient() + ": " + c.getNom());
        }

        List<TypeArticle> tarts = taControl.findTypeArticleEntities();
        Vector<String> types = new Vector<String>();

        for (TypeArticle ta : tarts) {
            types.add(ta.getIdtypeArticle() + ": " + ta.getNom());
        }
        List<Modele> modeles = moControl.findModeleEntities();
        Vector<String> mods = new Vector<String>();

        for (Modele m : modeles) {
            mods.add(m.getIdmodele() + ": " + m.getNom());
        }
        List<CategorieArticle> categories = new CategorieArticleJpaController().findCategorieArticleEntities();

        Vector<String> cats = new Vector<String>();

        for (CategorieArticle m : categories) {
            cats.add(m.getIdcategorie() + ":" + m.getLabel());
        }
        comb_client.setModel(new DefaultComboBoxModel(ids));
        comb_type_art.setModel(new DefaultComboBoxModel(types));
        comb_modele.setModel(new DefaultComboBoxModel(mods));
        cb_categorie.setModel(new DefaultComboBoxModel(cats));
    }

    void saveArticle() {

        artControl = new ArticleJpaController();
        article = new Article();
        //article.setIdarticle(0);
        try {
            article.setClient(clientControl.findClient(MyStringUtils.returnJustID(comb_client.getSelectedItem().toString(),":")));

            article.setTypeArticle(taControl.findTypeArticle(Integer.valueOf(MyStringUtils.returnJustID(comb_type_art.getSelectedItem().toString(),":"))));
            article.setModele(moControl.findModele(Integer.valueOf(MyStringUtils.returnJustID(comb_modele.getSelectedItem().toString(),":"))));
           article.setCategorieArticle(new CategorieArticleJpaController().findCategorieArticle(Integer.valueOf(MyStringUtils.returnJustID(cb_categorie.getSelectedItem().toString(),":"))));
            article.setFacture(facture);
            article.setEtat(tf_etat.getText());
            article.setDateEntree(tf_date_entree.getDate());
            article.setDateSortie(tf_date_sortie.getDate());
            article.setCommentaire(tf_comment.getText());

            byte[] buffer = new byte[fis.available()];


            fis.read(buffer);
            article.setPhoto(buffer);

            article.setMesure(mesures);
            article.setPriorite(Integer.valueOf(comb_priority.getSelectedItem().toString()));

            artControl.create(article);

             JOptionPane.showMessageDialog(null,
                "Article crée avec success ",
                "Confirmation",JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Erreur des données, certains champs sont vides !",
                "Erreur !",JOptionPane.ERROR_MESSAGE);

            e.printStackTrace();
        }

    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public Mesure getMesures() {
        return mesures;
    }

    public void setMesures(Mesure mesures) {
        this.mesures = mesures;
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btn_save = new javax.swing.JButton();
        btn_echant = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        comb_client = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        tf_date_sortie = new org.jdesktop.swingx.JXDatePicker();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tf_etat = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tf_comment = new javax.swing.JTextArea();
        tf_date_entree = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        comb_type_art = new javax.swing.JComboBox();
        comb_modele = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        comb_priority = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        cb_categorie = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        imagepanel = new javax.swing.JLabel();

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Comic Sans MS", 1, 18));
        jLabel6.setForeground(new java.awt.Color(0, 102, 204));
        jLabel6.setText("Nouvel article");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(490, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        btn_save.setText("Sauver");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        btn_echant.setText("Echantillon");
        btn_echant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_echantActionPerformed(evt);
            }
        });

        jButton3.setText("Mesures");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));

        comb_client.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Date sortie :");

        jLabel3.setText("Facture :");
        jLabel3.setToolTipText("");

        jLabel7.setText("Date d'entrée :");

        tf_etat.setText("En conception");

        jLabel4.setText("Modèle d'article :");

        tf_comment.setColumns(20);
        tf_comment.setRows(5);
        jScrollPane1.setViewportView(tf_comment);

        jLabel1.setText("Client :");

        jLabel8.setText("Etat :");

        jLabel2.setText("Type d'article :");

        jLabel9.setText("Commentaire :");

        comb_type_art.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        comb_modele.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setText("Priorité :");

        comb_priority.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3"}));

        jButton1.setText("Modifier");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cb_categorie.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel11.setText("Catégorie :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cb_categorie, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comb_priority, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tf_date_entree, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                    .addComponent(tf_date_sortie, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                    .addComponent(comb_modele, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comb_type_art, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comb_client, javax.swing.GroupLayout.Alignment.LEADING, 0, 242, Short.MAX_VALUE)
                    .addComponent(tf_etat, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comb_client, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comb_type_art, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(comb_modele, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_categorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tf_date_entree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_date_sortie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_etat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(comb_priority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(imagepanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_echant, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59))))
            .addGroup(layout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(455, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(btn_echant)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addGap(14, 14, 14))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)))
                .addGap(18, 18, 18)
                .addComponent(btn_save)
                .addGap(36, 36, 36))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_echantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_echantActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        File file = ImageFileHandler.chooseImageFile(this);

        setCursor(Cursor.getDefaultCursor());
        // ne fais rien sinon
        if (file == null) {
            return;
        }
        try {
            fis = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }


        imagepanel.setIcon(new ImageIcon(file.getAbsolutePath()));
        // imagepanel.setImage(new ImageIcon(file.getAbsolutePath()));
    }//GEN-LAST:event_btn_echantActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new MesuresDLG(this).setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        saveArticle();        // TODO add your handling code here:
    }//GEN-LAST:event_btn_saveActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new FactureDLG(this).setVisible(true);

    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_echant;
    private javax.swing.JButton btn_save;
    private javax.swing.JComboBox cb_categorie;
    private javax.swing.JComboBox comb_client;
    private javax.swing.JComboBox comb_modele;
    private javax.swing.JComboBox comb_priority;
    private javax.swing.JComboBox comb_type_art;
    private javax.swing.JLabel imagepanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea tf_comment;
    private org.jdesktop.swingx.JXDatePicker tf_date_entree;
    private org.jdesktop.swingx.JXDatePicker tf_date_sortie;
    private javax.swing.JTextField tf_etat;
    // End of variables declaration//GEN-END:variables
}
