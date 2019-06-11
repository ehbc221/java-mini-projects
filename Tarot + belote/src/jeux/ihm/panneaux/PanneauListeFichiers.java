package jeux.ihm.panneaux;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import jeux.*;
import jeux.ihm.dialogues.*;
import jeux.ihm.etiquettes.selection.*;
/**
 *
 */
public class PanneauListeFichiers extends Panneau {
	private static final long serialVersionUID = 1L;
	private static String ch_v=Lettre.ch_v;
	private static String unite=DialogueFichiers.unite;
	/**Tabulation*/
	private static String separateur=ch_v+DialogueFichiers.separateur;
	private static String anti_slash_separateur=ch_v+Lettre.as+Lettre.t;
	/**Liste de fichiers*/
	public PanneauListeFichiers(String nom_dossier, boolean selectionUnique){
		String nom_fichier;
		File[] fichiers=new File(nom_dossier).listFiles();
		setLayout(new FlowLayout());
		DefaultListModel modeleListeFichiers=new DefaultListModel();
		if(!nom_dossier.equals(Fichier.dossier_sauvegarde))
		{
			for (File fichier:fichiers)
			{
				nom_fichier=fichier.getName();
				if(nom_fichier.endsWith(Fichier.extension_partie))
				{
					modeleListeFichiers.addElement(nom_fichier+separateur+Lettre.es+Lettre.es+Lettre.es+DialogueFichiers.derniere_modif(fichier)+Lettre.es+Lettre.es+Lettre.es+fichier.length()+unite);
				}
			}
		}
		JList liste=new JList(modeleListeFichiers);
		setSelection(selectionUnique,liste);
		liste.setVisibleRowCount(8);
		liste.setCellRenderer(new FichierCellRenderer());
		add(new JScrollPane(liste));
	}
	public JList getListeFichiers()
	{
		return getListe();
	}
	/**faire attention aux chaines ayant en plus la date de modification*/
	public void supprimerFichiers(String[] fichiers,String dossier)
	{
		for (String fichier:fichiers)
		{
			supprimerFichier(fichier);
			if(new File(dossier+File.separator+fichier).delete());
		}
	}
	private void supprimerFichier(String fichier)
	{
		DefaultListModel fichiers=(DefaultListModel)getListeFichiers().getModel();
		String nom;
		for(int indice_fichier=0;indice_fichier<fichiers.size();)
		{
			nom=(String)fichiers.get(indice_fichier);
			if(nom.split(anti_slash_separateur)[0].equals(fichier))
			{
				fichiers.removeElementAt(indice_fichier);
				break;
			}
			indice_fichier++;
		}
	}
	/**Change le contenu en fonction du dossier visite*/
	public void modifierLeContenu(String nouveau)
	{
		DefaultListModel liste_fichiers=(DefaultListModel)getListeFichiers().getModel();
		int size=liste_fichiers.size();
		for (int i = 0; i < size; i++)
			liste_fichiers.removeElementAt(0);
		File f=new File(nouveau);
		String nom_fichier;
		File[] fichiers=f.listFiles();
		if(f.isDirectory())
		{
			for (File fichier:fichiers)
			{
				nom_fichier=fichier.getName();
				if(nom_fichier.endsWith(Fichier.extension_partie))
					ajouterFichier(nom_fichier+separateur+Lettre.es+Lettre.es+Lettre.es+DialogueFichiers.derniere_modif(fichier)+Lettre.es+Lettre.es+Lettre.es+fichier.length()+unite);
			}
		}
	}
	public void ajouterFichier(String fichier) {
		// recuperer les valeurs dans le formulaire
		// et ajouter le nouveau fichier corrrespondant
		((DefaultListModel)getListeFichiers().getModel()).addElement(fichier);
	}
	public String[] getFichiersSelectionnes()
	{
		if(getListeFichiers().isSelectionEmpty())
			return null;
		Object[] fichiers_selectionnes=getListeFichiers().getSelectedValues();
		//if(Fichier.extension_paquet.equals(""));
		String[] fichiers=new String[fichiers_selectionnes.length];
		for (int i = 0; i < fichiers.length; i++)
			fichiers[i]=((String)fichiers_selectionnes[i]).contains(separateur)?((String)fichiers_selectionnes[i]).split(anti_slash_separateur)[0]:((String)fichiers_selectionnes[i]);
		return fichiers;
	}
	public String getFichierSelectionne()
	{
		if(getListeFichiers().isSelectionEmpty())
			return null;
		Object fichier=getListeFichiers().getSelectedValue();
		return ((String)fichier).contains(separateur)?((String)fichier).split(anti_slash_separateur)[0]:((String)fichier);
	}
	public void supprimerToutesLignes()
	{
		((DefaultListModel)getListeFichiers().getModel()).removeAllElements();
	}
	public void ajouterLignes(Vector<String> noms)
	{
		for(String fichier:noms)
		{
			((DefaultListModel)getListeFichiers().getModel()).addElement(fichier);
		}
	}
}