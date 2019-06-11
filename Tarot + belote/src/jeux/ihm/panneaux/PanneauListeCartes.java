package jeux.ihm.panneaux;
import java.awt.*;
import javax.swing.*;
import jeux.*;
import jeux.cartes.*;
import jeux.ihm.etiquettes.selection.*;
import jeux.mains.*;
/***/
public class PanneauListeCartes extends Panneau {
	private static final long serialVersionUID = 1L;
	private static final String ch_v=Lettre.ch_v;
	private static final String p2=ch_v+Lettre.p2;
	private static final String pv=ch_v+Lettre.pv;
	private static final String pls="Pls: ";
	private int max;
	private int nbCartesRestantes;
	private String couleurs;
	private String ordre;
	private String sens;
	/**Instancie un panneau de cartes (avec un symbole et un nombre ou une lettre) pour les mains a remplir*/
	public PanneauListeCartes(int nb,int pmax,String titre)
	{
		max=pmax;
		JLabel titrePanneau = new JLabel(titre, JLabel.CENTER);
		add(titrePanneau, BorderLayout.NORTH);
		JList liste=new JList(new DefaultListModel());
		liste.setCellRenderer(new CarteCellRenderer());
		//On peut slectionner plusieurs elements dans la liste listeCouleurs en
		//utilisant "ctrl + A", "ctrl", "maj+clic", comme dans explorer
		liste.setVisibleRowCount(nb);
		nbCartesRestantes=max;
		add(new JScrollPane(liste),BorderLayout.CENTER);
		titrePanneau = new JLabel(pls+nbCartesRestantes, JLabel.CENTER);
		add(titrePanneau, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(100,10*(nb+4)));
	}
	/**Instancie un panneau de cartes (avec un symbole et un nombre ou une lettre) pour la pile
	 * de distribution*/
	public PanneauListeCartes(Main pile,int nb,int pmax,String titre)
	{
		this(nb, pmax, titre);
		DefaultListModel modeleListeCartes=new DefaultListModel();
		for (Carte carte : pile)
			modeleListeCartes.addElement(carte);
		JList liste=getListe();
		liste.setModel(modeleListeCartes);
		nbCartesRestantes=max-taille();
		((JLabel)getComponent(2)).setText(pls+nbCartesRestantes);
		//On peut selectionner plusieurs elements dans la liste listeCouleurs en
		//utilisant "ctrl + A", "ctrl", "maj+clic", comme dans explorer
	}
	/**Utilise pour le solitaire pour reduire la largeur*/
	public void setPreference(int nb)
	{
		setPreferredSize(new Dimension(50,10*(nb+4)));
	}
	/**Retourne le nombre total de cartes*/
	public int taille() {
		return ((DefaultListModel)getListe().getModel()).size();
	}
	public int getMax()
	{
		return max;
	}
	public void setTri(String pcouleurs,String pordre,String psens)
	{
		couleurs=pcouleurs;
		ordre=pordre;
		sens=psens;
	}
	public void ajouterCartesFin(Main m)
	{
		DefaultListModel modele=(DefaultListModel)getListe().getModel();
		for(Carte c:m)
		{
			modele.addElement(c);
		}
		nbCartesRestantes-=m.total();
		((JLabel)getComponent(2)).setText(pls+nbCartesRestantes);
	}
	/**Utilisee pour ajouter des cartes en respectant le tri*/
	public void ajouterCartes(Main m)
	{
		DefaultListModel modele=(DefaultListModel)getListe().getModel();
		if(couleurs.contains(p2)&&couleurs.contains(pv))
			couleurs=couleurs.split(p2)[1].split(pv)[0];
		if(sens.contains(pv))
			sens=sens.split(pv)[1];
		for(Carte c:m)
		{
			if(modele.isEmpty()||((Triable)modele.lastElement()).vientAvant((Triable)c,sens,ordre,couleurs))
				modele.addElement(c);
			else
			{
				byte b=0;
				for(;((Triable)modele.elementAt(b)).vientAvant((Triable)c,sens,ordre,couleurs);b++);
				modele.add(b, c);
			}
			nbCartesRestantes--;
		}
		((JLabel)getComponent(2)).setText(pls+nbCartesRestantes);
	}
	public void supprimerCartes(Carte[] cs)
	{
		DefaultListModel modele=(DefaultListModel)getListe().getModel();
		int indice;
		for(Carte c:cs)
		{
			indice=modele.indexOf(c);
			if(indice>-1)
			{
				modele.removeElementAt(indice);
				nbCartesRestantes++;
			}
		}
		((JLabel)getComponent(2)).setText(pls+nbCartesRestantes);
	}
	public Carte[] getCartes()
	{
		Carte[] cs=new Carte[taille()];
		for (int i = 0; i < cs.length; i++) {
			cs[i]=(Carte)((DefaultListModel)getListe().getModel()).elementAt(i);
		}
		return cs;
	}
	/**Retourne le nombre de cartes selectionnees*/
	public int nombre_cartes_selectionnees()
	{
		return getListe().getSelectedValues().length;
	}
	public Carte[] getCartesSelectionnees()
	{
		if(getListe().isSelectionEmpty())
			return null;
		Object[] cartes=getListe().getSelectedValues();
		int l=cartes.length;
		Carte[] valeurs=new Carte[l];
		for (int i = 0; i < l; i++)
			valeurs[i]=(Carte)cartes[i];
		return valeurs;
	}
}