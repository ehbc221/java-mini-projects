package rahim.kherrata.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rahim.kherrata.connexion.MaConnexion;

public class RESULTATS extends JFrame {
	private JLabel numCL =new JLabel();
	private JLabel numeleCL =new JLabel();
	private JLabel pointCL =new JLabel();

	private JComboBox numCC =new JComboBox();
	private JComboBox  numeleCC =new JComboBox();
	private JTextField pointCC =new JTextField();
	
	private Font fenC = new Font("Garamond", 0, 16);
	private Font fenC1 = new Font("Garamond", 0, 16);
	
	private JPanel panC = new JPanel();
	private JPanel panp = new JPanel();
	
	private JButton ok = new JButton();
	private JButton annuler = new JButton();

	private String req1C = " select DISTINCT NUMCOURS from  COURS";
	private String req2E = " select DISTINCT numeleve from ELEVES";
 public RESULTATS(){
	 setTitle("Fen-Ajout-ActivitésPratiquées");
		setSize(450, 200);
		setVisible(true);
		setLocationRelativeTo(null);
		initLabel();
		initCombo();
		initButton();
		initPanel();
	 
 }
 private void initLabel(){
	 numCL.setText("Non du cours:");
	 numCL.setPreferredSize(new Dimension(150, 25));
	 numCL.setFont(fenC1);
		
	 numeleCL.setText("Numero eleve:");
	 numeleCL.setPreferredSize(new Dimension(150, 25));
	 numeleCL.setFont(fenC1);
		
	 pointCL.setText("Points:");
	 pointCL.setPreferredSize(new Dimension(150, 25));
	 pointCL.setFont(fenC1);
		
	}
 private void initCombo(){
	 numCC.setPreferredSize(new Dimension(150, 25));
	 numCC.setFont(fenC);
	 numCC.setBackground(Color.WHITE);
		 Statement state;ResultSet res;
		try {
			state = MaConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			state.executeQuery(req1C);
			
			res = state.getResultSet();
			while(res.next()){
				numCC.addItem(res.getString("NUMCOURS"));
			}res.close();state.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		
		numeleCC.setPreferredSize(new Dimension(150, 25));
		numeleCC.setFont(fenC);
		numeleCC.setBackground(Color.WHITE);
		try {
			state = MaConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			state.executeQuery(req2E);
			
			res = state.getResultSet();
			while(res.next()){
				numeleCC.addItem(res.getString("numeleve"));
			}res.close();state.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pointCC.setPreferredSize(new Dimension(150, 25));
		pointCC.setFont(fenC);
		pointCC.setBackground(Color.WHITE);
		
	}
 private void initButton(){
		ok.setText("OK");
		ok.setPreferredSize(new Dimension( 100, 25));
		ok.setBackground(Color.WHITE);
		ok.setFont(fenC1);
		ok.addActionListener( new ActionListener( ) {
			public void actionPerformed( ActionEvent arg0) {
				 
			      String numcour = (String) numCC.getSelectedItem();
			       String  numele=(String) numeleCC.getSelectedItem();
			      float nbr =  Float.valueOf(pointCC.getText());
			
				
				String requete = "INSERT INTO RESULTATS VALUES ('"+numcour+"','"+numele+"',"+nbr+")";
				   Statement state = null;
					try {
						
						state = MaConnexion.getInstance().createStatement();
					  int res =  state.executeUpdate(requete);
					  if(res!=0){
						 //state.execute(requete);
							setVisible( false) ;
							JOptionPane.showMessageDialog(null, "Eneregistrement ajouté avec succès", "info", JOptionPane.INFORMATION_MESSAGE);
							}
							//res.close();
							state.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e.getMessage(), "info", JOptionPane.INFORMATION_MESSAGE);
						e.printStackTrace();
					}
			     
			}});
		
		annuler.setText("Annuler");
		annuler.setFont(fenC1);
		annuler.setPreferredSize(new Dimension( 100, 25));
		annuler.setBackground(Color.WHITE);
		annuler.addActionListener( new ActionListener( ) {
			public void actionPerformed( ActionEvent arg0) {
				 setVisible(false);
			 //On exécute la requête
			     
			}});
	}
 private void initPanel(){
		panC.setBackground(Color.WHITE);
		panC. setPreferredSize( new Dimension( 400, 180) ) ;
		panC.setBorder( BorderFactory. createTitledBorder( "Information d'activités : ") ) ;
		
		panC.add(numCL);
		panC.add(numCC);
		
		panC.add(numeleCL);
		panC.add(numeleCC);
		
		panC.add(pointCL);
		panC.add(pointCC);
		
		panC.add(ok);
		panC.add(annuler);
		setContentPane(panC);
		
		
	
	}
}
