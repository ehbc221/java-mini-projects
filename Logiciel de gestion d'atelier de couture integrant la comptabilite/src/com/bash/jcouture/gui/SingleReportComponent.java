/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SingleReportComponent.java
 *
 * Created on 16 févr. 2011, 09:44:16
 */
package com.bash.jcouture.gui;

import com.bash.jcouture.entities.model.Report;

/**
 *
 * @author bashizip
 */
public class SingleReportComponent extends javax.swing.JPanel {

    private Report report;


    public SingleReportComponent(Report report) {
        initComponents();
        this.report = report;

        lab_client.setText("Client: " + report.getClient());
        lab_produit.setText("NO. Produit: " + report.getIdarticle() + "");
        lab_montant.setText("Prix: " + report.getMontant());

       //tab_report.setModel(new SingleReportComponent(report));
    }



    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tab_report = new javax.swing.JTable();
        lab_produit = new javax.swing.JLabel();
        lab_client = new javax.swing.JLabel();
        lab_montant = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        tab_report.setModel(new javax.swing.table.DefaultTableModel(
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
        tab_report.setShowHorizontalLines(false);
        jScrollPane1.setViewportView(tab_report);

        lab_produit.setText("Produit");

        lab_client.setText("Client");

        lab_montant.setText("Montant total");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_client, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_montant, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_produit, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lab_produit)
                .addGap(18, 18, 18)
                .addComponent(lab_client)
                .addGap(22, 22, 22)
                .addComponent(lab_montant)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lab_client;
    private javax.swing.JLabel lab_montant;
    private javax.swing.JLabel lab_produit;
    private javax.swing.JTable tab_report;
    // End of variables declaration//GEN-END:variables
}
