/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ModelesPanes.java
 *
 * Created on 28 janv. 2011, 11:07:14
 */
package com.bash.jcouture.gui;

import com.bash.jcouture.controler.ModeleJpaController;
import com.bash.jcouture.entities.model.Modele;
import com.bash.jcouture.util.swing.ImageCellRenderer;
import com.bash.jcouture.util.swing.MyStringUtils;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;

/**
 *
 * @author bashizip
 */
public class ModelesPanes extends javax.swing.JPanel {

    private ModeleJpaController control;
    List<Modele> all;
    private Modele modele;

    public ModelesPanes() {
        initComponents();
        control = new ModeleJpaController();
        all = control.findModeleEntities();
        initComponents2();

    }

    public void initComponents2() {


        list.setModel(new AbstractListModel() {

            public int getSize() {
                return control.getModeleCount();
            }

            public Object getElementAt(int index) {
                return all.get(index).getIdmodele()+"."+all.get(index).getNom();
            }
        });

        list.setCellRenderer(new ImageCellRenderer(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/view_sale_highlight.png"))));

    }

    void cibleModele() {

        modele = control.findModele(Integer.valueOf(MyStringUtils.returnJustID(list.getSelectedValue().toString(),".")));

        if (modele.getImage() != null) {
            try {
                image.setIcon(new ImageIcon(modele.getImage()));
            } catch (Exception e) {
            }
        } else {
            image.setIcon(new ImageIcon(getClass().getResource("/com/bash/jcouture/images/Profile.png")));
        }
        ta_desc.setText(modele.getDescription());

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        image = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        ta_desc = new javax.swing.JTextArea();

        image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jScrollPane1.setViewportView(image);

        list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(list);

        ta_desc.setColumns(20);
        ta_desc.setRows(5);
        jScrollPane3.setViewportView(ta_desc);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(215, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(123, 123, 123)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void listValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listValueChanged
        cibleModele();
    }//GEN-LAST:event_listValueChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel image;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList list;
    private javax.swing.JTextArea ta_desc;
    // End of variables declaration//GEN-END:variables
}
