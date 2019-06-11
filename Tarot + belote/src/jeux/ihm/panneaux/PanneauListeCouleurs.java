package jeux.ihm.panneaux;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import jeux.ihm.etiquettes.selection.*;
/**
 *
 */
public class PanneauListeCouleurs extends Panneau {
	private static final long serialVersionUID = 1L;
	public PanneauListeCouleurs(Vector<String> couleurs,int nb)
	{
		JLabel titrePanneau = new JLabel("Couleurs", JLabel.CENTER);
		add(titrePanneau, BorderLayout.NORTH);
		DefaultListModel modeleListeCouleurs=new DefaultListModel();
		for (String couleur : couleurs)
			modeleListeCouleurs.addElement(couleur);
		JList liste=new JList(modeleListeCouleurs);
		liste.setCellRenderer(new CouleurCellRenderer());
		setSelection(false, liste);
		//On peut selectionner plusieurs elements dans la liste listeCouleurs en
		//utilisant "ctrl + A", "ctrl", "maj+clic", comme dans explorer
		liste.setVisibleRowCount(nb);
		add(new JScrollPane(liste),BorderLayout.CENTER);
	}
	public String[] getCouleurs()
	{
		String[] retour=new String[nombreDeCouleurs()];
		for (int i = 0; i < retour.length; i++) {
			retour[i]=(String)((DefaultListModel)getListe().getModel()).elementAt(i);
		}
		return retour;
	}
	/**Retourne le nombre de
	 * couleurs dans la liste
	 * (selectionnees ou non)*/
	public int nombreDeCouleurs()
	{
		return ((DefaultListModel)getListe().getModel()).size();
	}
	public void ajouterCouleur(String couleur) {
		// recuperer les valeurs dans le formulaire
		// et ajouter le nouvel etudiant corrrespondant
		((DefaultListModel)getListe().getModel()).addElement(couleur);
	}
	public void supprimerCouleurs(String[] couleurs)
	{
		for (int i = 0; i < couleurs.length; i++)
			if(((DefaultListModel)getListe().getModel()).contains(couleurs[i]))
				((DefaultListModel)getListe().getModel()).removeElementAt(((DefaultListModel)getListe().getModel()).indexOf(couleurs[i]));
	}
	public void toutSupprimer()
	{
		((DefaultListModel)getListe().getModel()).removeAllElements();
	}
	public String[] getCouleursSelectionnees()
	{
		if(getListe().isSelectionEmpty())
			return null;
		int l=getListe().getSelectedValues().length;
		String[] valeurs=new String[l];
		for (int i = 0; i < l; i++) {
			valeurs[i]=(String)getListe().getSelectedValues()[i];
		}
		//Appelee 2 fois
		 return valeurs;
	}
}