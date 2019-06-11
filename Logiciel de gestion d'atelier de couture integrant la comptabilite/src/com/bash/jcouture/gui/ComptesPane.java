/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ComptesPane.java
 *
 * Created on 18 janv. 2011, 12:26:18
 */
package com.bash.jcouture.gui;

import com.bash.jcouture.controler.CompteJpaController;
import com.bash.jcouture.controler.exceptions.NonexistentEntityException;
import com.bash.jcouture.entities.model.Compte;
import com.bash.jcouture.util.ComptesBuilder;
import com.bash.jcouture.util.swing.ComptesListModel;

import com.bash.jcouture.util.swing.ComptesTableModel;
import com.bash.jcouture.util.swing.ImageCellRenderer;
import com.bash.jcouture.util.swing.MySwingUtilities;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author bashizip
 */
public class ComptesPane extends javax.swing.JPanel {

    private CompteJpaController controler;
    private static boolean cansave = true;
    private Compte compte;
    private Thread busyingThread;


    /** Creates new form ComptesPane */
    public ComptesPane() {
        initComponents();
        controler = new CompteJpaController();
        initComponets2();
    }

    void initComponets2() {
        tab_comptes.setModel(new ComptesTableModel(controler.findCompteEntities()));
        pan_new.setVisible(false);
        list_comptes.setCellRenderer(new ImageCellRenderer(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/manager.png"))));
        list_comptes.setModel(new ComptesListModel());
//spinner_exerc.setModel(new SpinnerDateModel(Calendar.get(Calendar.YEAR), null, WIDTH, WIDTH));
   spinner_exerc.setValue(2011);
    }

    void save() {

        compte = new Compte(Integer.valueOf(tf_code.getText()), list_comptes.getSelectedValue().toString(), ta_desc.getText(),spinner_exerc.getValue().toString());
        try {
            controler.create(compte);
        } catch (Exception e) {
            e.printStackTrace();
        }


        but_save.setEnabled(false);

        tab_comptes.setModel(new ComptesTableModel(controler.findCompteEntities()));

        pan_new.setVisible(false);

    }

    private void showCode() {
        tf_code.setText(ComptesBuilder.codes[list_comptes.getSelectedIndex()]);
    }

    private void delete() {
        if (MySwingUtilities.showConfirmDialog("Supprimer ce compte?") == JOptionPane.YES_OPTION) {
            ;

            try {
                controler.destroy(Integer.valueOf(tab_comptes.getValueAt(tab_comptes.getSelectedRow(), 0).toString()));
            } catch (NonexistentEntityException ex) {
                ex.printStackTrace();
            }
            tab_comptes.setModel(new ComptesTableModel(controler.findCompteEntities()));
        }


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab_comptes = new javax.swing.JTable();
        but_save = new javax.swing.JButton();
        but_new = new javax.swing.JButton();
        pan_new = new javax.swing.JPanel();
        tf_code = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_comptes = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        ta_desc = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        spinner_exerc = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        lab_busy = new org.jdesktop.swingx.JXBusyLabel();
        but_del = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Comic Sans MS", 1, 18));
        jLabel4.setText("Edition des Comptes");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(553, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        tab_comptes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tab_comptes);

        but_save.setText("Sauver");
        but_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                but_saveActionPerformed(evt);
            }
        });

        but_new.setText("Nouveau");
        but_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                but_newActionPerformed(evt);
            }
        });

        pan_new.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        jLabel1.setText("code :");

        jLabel2.setText("Libel� :");

        list_comptes.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list_comptes.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list_comptesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(list_comptes);

        ta_desc.setColumns(20);
        ta_desc.setRows(5);
        jScrollPane3.setViewportView(ta_desc);

        jLabel3.setText("Description :");

        jLabel5.setText("Exercice :");

        javax.swing.GroupLayout pan_newLayout = new javax.swing.GroupLayout(pan_new);
        pan_new.setLayout(pan_newLayout);
        pan_newLayout.setHorizontalGroup(
            pan_newLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_newLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(pan_newLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pan_newLayout.createSequentialGroup()
                        .addGroup(pan_newLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_code, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(100, 100, 100)
                .addGroup(pan_newLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_newLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spinner_exerc, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_newLayout.setVerticalGroup(
            pan_newLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_newLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pan_newLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_newLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(tf_code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan_newLayout.createSequentialGroup()
                        .addGroup(pan_newLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spinner_exerc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_newLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pan_newLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                    .addComponent(jScrollPane3))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        but_del.setText("Supprimer");
        but_del.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                but_delActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(288, 288, 288)
                .addComponent(but_new, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(but_save, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(but_del)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(lab_busy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pan_new, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 726, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pan_new, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(but_new)
                        .addComponent(but_del)
                        .addComponent(but_save))
                    .addComponent(lab_busy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void but_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_but_newActionPerformed

        but_save.setEnabled(true);
        pan_new.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_but_newActionPerformed

    private void but_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_but_saveActionPerformed
        lab_busy.setBusy(true);

        save();

        if (lab_busy.isBusy()) {
            lab_busy.setBusy(false);

        }
        //   lab_busy.setBusy(false);
    }//GEN-LAST:event_but_saveActionPerformed

    private void list_comptesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list_comptesValueChanged
        showCode();
    }//GEN-LAST:event_list_comptesValueChanged

    private void but_delActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_but_delActionPerformed
        delete();        // TODO add your handling code here:
    }//GEN-LAST:event_but_delActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton but_del;
    private javax.swing.JButton but_new;
    private javax.swing.JButton but_save;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private org.jdesktop.swingx.JXBusyLabel lab_busy;
    private javax.swing.JList list_comptes;
    private javax.swing.JPanel pan_new;
    private javax.swing.JSpinner spinner_exerc;
    private javax.swing.JTextArea ta_desc;
    private javax.swing.JTable tab_comptes;
    private javax.swing.JTextField tf_code;
    // End of variables declaration//GEN-END:variables
}