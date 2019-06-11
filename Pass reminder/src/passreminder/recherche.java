package passreminder;

import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class recherche extends javax.swing.JFrame {

 ArrayList listeDate = new ArrayList();
 ArrayList listeJour = new ArrayList(); 
 ArrayList listeLettre = new ArrayList(); 
 ArrayList listeTaille = new ArrayList();
    
   
    public recherche() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(255, 255, 204));
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(0, 0, 255));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("Resultats de Votre Recherche : ");

        jLabel2.setText("Critère de Recherche ");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Date de Création", "Jour de Création", "Contient une Lettre", "Taille du Mot de Passe" }));

        jButton1.setText("Lancer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Ok");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jMenu1.setText("Options");

        jMenuItem1.setText("Retour");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Quitter");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                        .addGap(44, 44, 44)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTextArea1.setText("");
        String data = (String)jComboBox1.getSelectedItem();
      
       // Recherche sur la date de création
       if (data.equals("Date de Création"))
       {
         try 
         {
          String day = JOptionPane.showInputDialog(this, "Entrez la Date au format jj/mm/aa");
          Statement state = link.getInstance().createStatement();
          ResultSet res = state.executeQuery("SELECT pass FROM passTable where dating='"+day+"';");
         
          // Ajout du resultat obtenu dans le Tableau 
          while(res.next())
          {
              listeDate.add(res.getString("pass"));
          }
                  
          for(int i=0;i<listeDate.size();i++) // Parcours du tableau pour l'affichage
          {
              jTextArea1.append(listeDate.get(i)+"\n");
          }
          
         }catch(HeadlessException | SQLException e){}
         
       }
       
       // Recherche sur le jour de création
       if (data.equals("Jour de Création"))
       {
         try 
         {
          String jour = JOptionPane.showInputDialog(this, "Entrez le  Jour de Création");
          Statement state = link.getInstance().createStatement();
          ResultSet res = state.executeQuery("SELECT pass FROM passTable where jour='"+jour.toLowerCase()+"';");
          while(res.next())
          {
               listeJour.add(res.getString("pass"));
          } 
          
          for(int i=0;i<listeJour.size();i++) // Parcours du tableau pour l'affichage
          {
              jTextArea1.append(listeJour.get(i)+"\n");
          }
          
         }catch(HeadlessException | SQLException e){}
         
       }
        
       
       // Recherche sue la contenance des caracteres
       if (data.equals("Contient une Lettre"))
       {
         try 
         {
          String lettre = JOptionPane.showInputDialog(this, "Entrez une lettre contenue dans le Mot de Passe");
          Statement state = link.getInstance().createStatement();
          ResultSet res = state.executeQuery("SELECT pass FROM passTable where pass like '%"+lettre+"%';");
         
          // Ajout du resultat obtenu dans le Tableau 
          while(res.next())
          {
               listeLettre.add(res.getString("pass"));
          } 
          
          for(int i=0;i<listeLettre.size();i++) // Parcours du tableau pour l'affichage
          {
              jTextArea1.append(listeLettre.get(i)+"\n");
          }
          
         }catch(HeadlessException | SQLException e){}
         
       }        
       
       // Recherche sur la Taille du mot de Passe 
       if (data.equals("Taille du Mot de Passe"))
       {
         try 
         {
           int taille = Integer.parseInt(JOptionPane.showInputDialog(this, "Entrez la taille"));
           Statement state = link.getInstance().createStatement();
           ResultSet res = state.executeQuery("SELECT pass FROM passTable where length(pass) = "+taille+" ;");
         
            // Ajout du resultat obtenu dans le Tableau 
            while(res.next())
            {
                listeTaille.add(res.getString("pass"));
            } 
          
            for(int i=0;i<listeTaille.size();i++) // Parcours du tableau pour l'affichage
            {
               jTextArea1.append(listeTaille.get(i)+"\n");
            }
          
         }catch(HeadlessException | SQLException e){}
         
       }     
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        this.dispose();
        new index().setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
       this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTextArea1.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
