package jeux.ihm.dialogues;
import java.awt.event.*;
import javax.swing.*;
import jeux.enumerations.Jeu;
import jeux.ihm.*;
import jeux.ihm.panneaux.*;
/**
 *
 */
class Dialogue extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTabbedPane jt=new JTabbedPane();
	Dialogue(String titre,Fenetre fenetre,boolean mod)
	{
		super(fenetre,titre,mod);
		setLocationRelativeTo(fenetre);
		setResizable(false);
	}
	JTabbedPane getJt()
	{
		return jt;
	}
	void modifierListePourTri(String s)
	{
		if(s.startsWith("<="))
			//Retirer du tri
		{
			if(getTitle().equals(Jeu.Belote.toString()))
			{
				JPanel onglet=(JPanel)jt.getComponentAt(1);
				PanneauListeCouleurs panneau_couleurs=(PanneauListeCouleurs)onglet.getComponent(2);
				JComboBox liste=(JComboBox)onglet.getComponent(0);
				if(panneau_couleurs.nombreDeCouleurs()<4||liste.getItemCount()<4)
				{
					String[] couleurs=panneau_couleurs.getCouleursSelectionnees();
					if(couleurs!=null)
					{
						panneau_couleurs.supprimerCouleurs(couleurs);
						for(String couleur:couleurs)
						{
							liste.addItem(couleur);
						}
					}
				}
				else
				{
					panneau_couleurs.toutSupprimer();
				}
			}
			else//cas du Tarot
			{
				JPanel onglet=(JPanel)jt.getComponentAt(1);
				PanneauListeCouleurs panneau_couleurs=(PanneauListeCouleurs)onglet.getComponent(2);
				JComboBox liste=(JComboBox)onglet.getComponent(0);
				if(panneau_couleurs.nombreDeCouleurs()<5||liste.getItemCount()<5)
				{
					String[] couleurs=panneau_couleurs.getCouleursSelectionnees();
					if(couleurs!=null)
					{
						panneau_couleurs.supprimerCouleurs(couleurs);
						for (String couleur:couleurs)
						{
							liste.addItem(couleur);
						}
					}
				}
				else
				{
					panneau_couleurs.toutSupprimer();
				}
			}
		}
		else//Ajouter dans le tri
		{
			if(getTitle().equals(Jeu.Belote.toString()))
			{
				JPanel onglet=(JPanel)jt.getComponentAt(1);
				PanneauListeCouleurs panneau_couleurs=(PanneauListeCouleurs)onglet.getComponent(2);
				JComboBox liste=(JComboBox)onglet.getComponent(0);
				if(panneau_couleurs.nombreDeCouleurs()==4&&liste.getItemCount()==4)
				{
					panneau_couleurs.toutSupprimer();
				}
				panneau_couleurs.ajouterCouleur((String)liste.getSelectedItem());
				liste.removeItemAt(liste.getSelectedIndex());
			}
			else//cas du Tarot
			{
				JPanel onglet=(JPanel)jt.getComponentAt(1);
				JComboBox liste=(JComboBox)onglet.getComponent(0);
				PanneauListeCouleurs panneau_couleurs=(PanneauListeCouleurs)onglet.getComponent(2);
				if(panneau_couleurs.nombreDeCouleurs()==5&&liste.getItemCount()==5)
				{
					panneau_couleurs.toutSupprimer();
				}
				panneau_couleurs.ajouterCouleur((String)liste.getSelectedItem());
				liste.removeItemAt(liste.getSelectedIndex());
			}
		}
	}
	class EcouteBoutonsTri implements ActionListener{
		private JButton bouton;
		public EcouteBoutonsTri(JButton pbouton)
		{
			bouton=pbouton;
		}
		public void actionPerformed(ActionEvent e) {
			String nom=bouton.getText();
			if(!nom.startsWith("Deplacer"))
			{
				modifierListePourTri(nom);
			}
			else
			{
				((Editeur)Dialogue.this).deplacerCartes();
			}
		}
	}
	class EcouteListe implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(Dialogue.this instanceof DialogueJeux)
			{
				((DialogueJeux)Dialogue.this).modifierListe();
			}
			else
			{
				((Editeur)Dialogue.this).modifierListeMode();
			}
		}
	}
}