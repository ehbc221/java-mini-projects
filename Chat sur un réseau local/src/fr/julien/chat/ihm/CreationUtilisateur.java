package fr.julien.chat.ihm;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.julien.chat.metier.Couleur;
import fr.julien.chat.metier.Utilisateur;

public class CreationUtilisateur extends JFrame {
	
	private static final long serialVersionUID = -6027597315757919534L;
	private JTextField pseudo;
	private JTextField couleur;
	
	public CreationUtilisateur(){
		setSize(300, 150);
		add(makeContentPane());
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private JPanel makeContentPane(){
		JPanel res = new JPanel(new BorderLayout());
		JPanel centre = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx=2;
		c.weighty=2;
		c.fill=GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx=0;
		c.gridy=0;
		centre.add(new JLabel("Pseudo"), c);
		c.gridx=1;
		c.gridy=0;
		pseudo = new JTextField();
		centre.add(pseudo, c);
		c.gridx=0;
		c.gridy=1;
		centre.add(new JLabel("Couleur du texte"), c);
		c.gridx=1;
		c.gridy=1;
		couleur = new JTextField();
		centre.add(couleur, c);
		JButton valider = new JButton("Valider");
		valider.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new FenetreChat(new Utilisateur(pseudo.getText(), Couleur.valueOf(couleur.getText().toUpperCase()))).start();
				setVisible(false);
			}
		});
		res.add(centre, BorderLayout.CENTER);
		res.add(valider, BorderLayout.SOUTH);
		return res;
	}

}
