package fr.julien.chat.ihm;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import fr.julien.chat.gestionfichiers.GestionnaireFichiers;
import fr.julien.chat.metier.Message;
import fr.julien.chat.metier.Utilisateur;
import fr.julien.chat.util.FichierConversation;

public class FenetreChat extends Thread {
	
	private static final long serialVersionUID = 6675169156730596352L;
	private JEditorPane zoneDialogue;
	private JTextArea zoneEdition;
	private Utilisateur utilisateur;
	
	public FenetreChat(Utilisateur utilisateur){
		JFrame f = new JFrame();
		this.utilisateur=utilisateur;
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500, 600);
		f.add(makeContentPane());
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	private JPanel makeContentPane(){
		JPanel res = new JPanel();
		res.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx=2;
		c.weighty=1;
		c.insets = new Insets(20, 20, 20, 20);
		c.fill=GridBagConstraints.BOTH;
		c.gridx=0;
		c.gridy=0;
		zoneDialogue = new JEditorPane();
		zoneDialogue.setEditable(false);
		zoneDialogue.setContentType("text/html");
		res.add(new JScrollPane(zoneDialogue), c);
		c.gridx=0;
		c.gridy=1;
		zoneEdition = new JTextArea();
		zoneEdition.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					GestionnaireFichiers.getInstance().append(FichierConversation.DOSSIER_PARTAGE+"\\"+FichierConversation.NOM_FICHIER, new Message(utilisateur, zoneEdition.getText()).toHTML());
					zoneEdition.setText("");
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		res.add(new JScrollPane(zoneEdition), c);
		return res;
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(2000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			zoneDialogue.setText(GestionnaireFichiers.getInstance().recupererContenu(FichierConversation.DOSSIER_PARTAGE+"\\"+FichierConversation.NOM_FICHIER));
		}
		
	}

}
