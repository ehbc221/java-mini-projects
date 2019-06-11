package jeux.ihm.dialogues;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import jeux.enumerations.*;
import jeux.*;
import jeux.parties.*;
import jeux.cartes.*;
import jeux.ihm.*;
import jeux.ihm.panneaux.*;
import jeux.mains.*;
/**Classe permettant de creer ses propres parties*/
public class Editeur extends Dialogue
{
	private static final long serialVersionUID = 1L;
	private static final String jeu="Jeu";
	private static final String ch_v=Lettre.ch_v;
	private String rep;
	private String rep2;
	private boolean partie_sauvegardee;
	private Partie partie;
	private int nombre_cartes_selectionnees;
	private int nombre_cartes_selectionnees_precedent;
	private Vector<String> infos=new Vector<String>();
	public Editeur(Fenetre fenetre)
	{
		super(jeu,fenetre,true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new FermetureFenetre());
	}
	public Partie getPartie()
	{
		setVisible(true);
		return partie;
	}
	public void setDialogue()
	{
		setTitle(jeu);
		Container c=new Container();
		getJt().removeAll();
		c.setLayout(new GridLayout(0,1));
		JPanel panneau=new JPanel();
		panneau.setLayout(new FlowLayout());
		JComboBox liste=new JComboBox(new String[]{Jeu.Belote.toString(),Jeu.Tarot.toString()});
		JComboBox liste2=new JComboBox();
		String info2=(String) liste.getSelectedItem();
		if(info2.equals(Jeu.Belote.toString()))
		{
			String[] choix={ModeBelote.Avec_surcontrat.toString(),ModeBelote.Sans_surcontrat.toString()};
			for (int i = 0; i < choix.length; i++)
				liste2.addItem(choix[i]);
		}
		else
		{
			for (int i = 0; i < 3; i++)
				liste2.addItem((i+3)+" joueurs");
		}
		liste.addActionListener(new EcouteListe());
		panneau.add(liste);
		panneau.add(liste2);
		c.add(panneau);
		panneau=new JPanel();
		JButton bouton=new JButton("Suivant =>");
		bouton.addActionListener(new EcouteValider("Suivant =>"));
		panneau.add(bouton);
		c.add(panneau);
		setContentPane(c);
		pack();
	}
	private JPanel panneauCartes()
	{
		return (JPanel)((JPanel)getContentPane().getComponent(1)).getComponent(0);
	}
	private String pseudoEgalite(JPanel pansCartes)throws Exception
	{
		PanneauListeCartes plc=(PanneauListeCartes)pansCartes.getComponent(1);
		int max=plc.getMax();
		int nombreDeMains=pansCartes.getComponentCount();
		boolean pseudoegaux=true;
		int i;
		pseudoegaux=max-2<plc.taille();
		for (i=pseudoegaux?2:1; i < nombreDeMains; i++) {
			plc=(PanneauListeCartes)pansCartes.getComponent(i);
			max=plc.getMax();
			pseudoegaux&=pseudoegaux&&plc.taille()>max-2;
			if(!pseudoegaux)
				break;
		}
		if(pseudoegaux)
		{
			String fichier=new DialogueFichiers(this,new String[]{partie.jeu().name(),partie.getMode().replace(Lettre.ea,Lettre.e),partie.getType().toString()}).getTexte();
			if(fichier!=null&&!fichier.equals(ch_v))
			{
				validerSauvegarde(fichier);
			}
			return fichier;
		}
		throw new Exception("Erreur de repartition mains");
	}
	private String validerEgalite()
	{
		String[] dossiers;
		if(!(partie instanceof PartieTarot))
		{
			dossiers=new String[]{partie.jeu().name(),partie.getMode().replace(Lettre.ea,Lettre.e),partie.getType().toString()};
		}
		else
		{
			dossiers=new String[]{partie.jeu().name(),partie.getMode(),infos.get(7).split(":")[1],partie.getType().toString()};
		}
		String fichier=new DialogueFichiers(this,dossiers).getTexte();
		if(fichier!=null&&!fichier.equals(ch_v))
		{
			validerSauvegarde(fichier);
		}
		return fichier;
	}
	private class FermetureFenetre extends WindowAdapter{
		public void windowClosing(WindowEvent e) {
			partie=null;
			dispose();
		}
	}
	private void erreur(PanneauListeCartes plc)
	{
		JOptionPane.showMessageDialog(this,"Il reste "+plc.taille()+" cartes a placer","Erreur de repartition", JOptionPane.ERROR_MESSAGE);
	}
	private class EcouteValider implements ActionListener{
		private String nom;
		EcouteValider(String pnom)
		{
			nom=pnom;
		}
		private String sauvegarder()throws Exception
		{
			PanneauListeCartes plc;
			JPanel pansCartes=panneauCartes();
			plc=(PanneauListeCartes)pansCartes.getComponent(0);
			if(plc.taille()==0)
			{
				if(rep.equals(Jeu.Belote.toString())||rep.equals(Jeu.Tarot.toString()))
				{
					return validerEgalite();
				}
				return pseudoEgalite(pansCartes);
			}
			throw new Exception("Erreur de repartition de la pile de distribution");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(nom.endsWith("=>"))
			{
				if(getTitle().equals(jeu))
				{
					validerChoixJeu();
				}
				else
				{
					validerInformationJeu();
				}
			}
			else if(nom.startsWith("<="))
			{
				nombre_cartes_selectionnees=nombre_cartes_selectionnees_precedent=0;
				partie_sauvegardee=false;
				if(getTitle().equals(rep+" "+rep2)||getTitle().equals(rep2))
				{
					setDialogue();
				}
				else
				{
					validerChoixJeu();
				}
			}
			else if(nom.endsWith("1"))
			{/*Si on veut sauvegarder une partie et on veut en creer une autre*/
				if(!partie_sauvegardee)
				{
					setPartie();
					try {
						String fichier=sauvegarder();
						if(fichier!=null&&!fichier.isEmpty())
						{
							partie_sauvegardee=true;
							partie=null;
						}
					} catch (Exception exception) {
						releverErreurs(exception);
					}//Methode permet de sauvegarder une partie et de relever d'eventuelles erreurs
				}
				else
				{
					JOptionPane.showMessageDialog(Editeur.this,"Partie deja sauvegardee","Erreur de sauvegarde",JOptionPane.ERROR_MESSAGE);
				}
			}
			else if(nom.endsWith("2"))
			{/*Si on veut sauvegarder une partie puis la jouer et fermer l'editeur*/
				setPartie();
				try {
					String fichier=sauvegarder();
					if(fichier!=null&&!fichier.isEmpty())
					{
						dispose();
					}
				} catch (Exception exception) {
					releverErreurs(exception);
				}
			}
			else if(nom.endsWith("3"))
			{/*Si on veut jouer une partie sans la sauvegarder et fermer l'editeur*/
				setPartie();
				dispose();
			}
			else if(nom.endsWith("4"))
			{/*Si on veut sauvegarder une partie sans la jouer et fermer l'editeur*/
				try {
					String fichier=sauvegarder();
					if(fichier!=null&&!fichier.isEmpty())
					{
						dispose();
					}
				} catch (Exception exception) {
					releverErreurs(exception);
				}
			}
		}
		private void releverErreurs(Exception exception)
		{
			exception.printStackTrace();
			if(exception.getMessage().equals("Erreur de repartition de la pile de distribution"))
			{
				erreur((PanneauListeCartes)panneauCartes().getComponent(0));
			}
			else
			{
				JPanel pansCartes=panneauCartes();
				PanneauListeCartes plc=(PanneauListeCartes)pansCartes.getComponent(1);
				int max=plc.getMax();
				int nombreDeMains=pansCartes.getComponentCount();
				boolean pseudoegaux=true;
				int i;
				pseudoegaux=max-2<plc.taille();
				for (i=pseudoegaux?2:1; i < nombreDeMains; i++) {
					plc=(PanneauListeCartes)pansCartes.getComponent(i);
					max=plc.getMax();
					pseudoegaux&=pseudoegaux&&plc.taille()>max-2;
					if(!pseudoegaux)
						break;
				}
				String mes;
				mes="Le talon (la pile numero 1) doit posseder "+(plc.getMax())+" cartes.\n";
				mes+="Les piles numero de 2 a "+nombreDeMains+" doivent posseder "+(plc.getMax()-1)+" ou "+(plc.getMax())+" cartes.\n";
				if(i==1)
				{
					mes+="Raison de l'erreur: le talon possede "+plc.taille()+" cartes.";
				}
				else
				{
					mes+="Raison de l'erreur: la pile numero "+i+" possede "+plc.taille()+" cartes.";
				}
				JOptionPane.showMessageDialog(Editeur.this,mes,"Erreur de repartition", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	private void validerChoixJeu()
	{
		if(getTitle().equals(jeu))
		{
			JPanel panneau=(JPanel)getContentPane().getComponent(0);
			rep=(String)((JComboBox)panneau.getComponent(0)).getSelectedItem();
			rep2=(String)((JComboBox)panneau.getComponent(1)).getSelectedItem();
		}
		else
		{
			getJt().removeAll();
		}
		Container container=new Container();
		container.setLayout(new BorderLayout());
		JPanel panneau=new JPanel();
		if(rep.equals(Jeu.Belote.toString()))
		{
			setTitle(rep+" "+rep2);
			panneau.setLayout(new GridLayout(0,2));
			panneau.add(new JLabel("Nombre de parties a jouer sans battre"));
			//Panneau Distribution
			panneau.add(new JLabel("Sens"));
			Byte[] tas=new Byte[100];
			//Panneau Distribution
			for(byte b=1;b<tas.length+1;b++)
				tas[b-1]=b;
			panneau.add(new JSpinner(new SpinnerListModel(tas)));
			panneau.add(new JCheckBox("Horaire"));
			getJt().add("Distribution",panneau);
			//Panneau Tri avant enchere
			panneau=new JPanel();
			panneau.setLayout(new GridLayout(0,4));
			Vector<String> liste=new Vector<String>();
			JComboBox listeChoix=new JComboBox();
			listeChoix=new JComboBox();
			for(Couleur couleur:Couleur.values())
			{
				int ordre=couleur.ordinal();
				if(ordre>0)
				{
					listeChoix.addItem(couleur.toString());
				}
			}
			panneau.add(listeChoix);
			JPanel sous_2_panneau=new JPanel();
			sous_2_panneau.setLayout(new GridLayout(0,1));
			JButton bouton=new JButton("Ajouter dans le tri =>");
			bouton.addActionListener(new EcouteBoutonsTri(bouton));
			sous_2_panneau.add(bouton);
			bouton=new JButton("<= Retirer du tri");
			bouton.addActionListener(new EcouteBoutonsTri(bouton));
			sous_2_panneau.add(bouton);
			sous_2_panneau.add(new JCheckBox("Trier par ordre croissant"));
			panneau.add(sous_2_panneau);
			for(Couleur couleur:Couleur.values())
			{
				int ordre=couleur.ordinal();
				if(ordre>0)
				{
					liste.addElement(couleur.toString());
				}
			}
			PanneauListeCouleurs plc=new PanneauListeCouleurs(liste,4);
			liste.removeAllElements();
			panneau.add(plc);
			//Panneau Tri avant enchere (Atout)
			JPanel sous_panneau=new JPanel();
			sous_panneau.setLayout(new GridLayout(0,1));
			sous_panneau.add(new JLabel("Tri avant contrat"));
			sous_panneau.add(new JCheckBox("Ordre d'atout"));
			panneau.add(sous_panneau);
			getJt().add("Tri",panneau);
			//Panneau Annonces autorisees
			panneau=new JPanel(new GridLayout(0,1));
			sous_panneau=new JPanel();
			sous_panneau.setLayout(new GridLayout(0,1));
			sous_panneau.add(new JCheckBox("Obligation de sous-couper adversaire"));
			sous_panneau.add(new JLabel("Obligation de couper sur partenaire"));
			listeChoix=new JComboBox();
			for(BeloteCoupePartenaire choix:BeloteCoupePartenaire.values())
			{
				listeChoix.addItem(choix.toString());
			}
			sous_panneau.add(listeChoix);
			panneau.add(sous_panneau);
			getJt().add("Regles du jeu",panneau);
			//Panneau Calcul des scores
			sous_panneau=new JPanel();
			sous_panneau.setLayout(new GridLayout(0,1));
			sous_panneau.add(new JLabel("Calcul des scores"));
			sous_panneau.add(new JCheckBox("162 - 0, pour la defense lorsqu'elle gagne"));
			getJt().add("Fin de partie",sous_panneau);
			container.add(getJt(),BorderLayout.CENTER);
		}
		else
		{
			setTitle(rep+" "+rep2);
			panneau.setLayout(new GridLayout(0,2));
			//Panneau Distribution
			panneau.add(new JLabel("Nombre de parties a jouer sans battre"));
			panneau.add(new JLabel("Distribution"));
			Byte[] tas=new Byte[100];
			//Panneau Distribution
			for(byte b=1;b<tas.length+1;b++)
				tas[b-1]=b;
			panneau.add(new JSpinner(new SpinnerListModel(tas)));
			panneau.add(new JCheckBox("Dans le sens horaire"));
			getJt().add("Distribution",panneau);
			//Panneau Tri
			JPanel sous_panneau=new JPanel();
			sous_panneau.setLayout(new GridLayout(0,3));
			JComboBox listeChoix=new JComboBox();
			Vector<String> liste=new Vector<String>();
			for(Couleur couleur:Couleur.values())
			{
				listeChoix.addItem(couleur.toString());
			}
			sous_panneau.add(listeChoix);
			JPanel sous_2_panneau=new JPanel();
			sous_2_panneau.setLayout(new GridLayout(0,1));
			JButton bouton=new JButton("Ajouter dans le tri =>");
			bouton.addActionListener(new EcouteBoutonsTri(bouton));
			sous_2_panneau.add(bouton);
			bouton=new JButton("<= Retirer du tri");
			bouton.addActionListener(new EcouteBoutonsTri(bouton));
			sous_2_panneau.add(bouton);
			sous_2_panneau.add(new JCheckBox("Trier par ordre croissant"));
			sous_panneau.add(sous_2_panneau);
			for(Couleur couleur:Couleur.values())
			{
				liste.addElement(couleur.toString());
			}
			PanneauListeCouleurs plc=new PanneauListeCouleurs(liste,5);
			liste.removeAllElements();
			sous_panneau.add(plc);
			getJt().add("Tri",sous_panneau);
			panneau=new JPanel();
			panneau.setLayout(new GridLayout(0,2));
			//Panneau Poignees
			sous_panneau=new JPanel();
			sous_panneau.setLayout(new GridLayout(0,1));
			sous_panneau.add(new JLabel("Poignees autorisees"));
			for (Poignees poignee:Poignees.values()) {
				sous_panneau.add(new JCheckBox(poignee.toString()));
			}
			panneau.add(sous_panneau);
			//Panneau Miseres
			sous_panneau=new JPanel();
			sous_panneau.setLayout(new GridLayout(0,1));
			sous_panneau.add(new JLabel("Miseres autorisees"));
			for (Miseres misere:Miseres.values()) {
				sous_panneau.add(new JCheckBox(misere.toString()));
			}
			panneau.add(sous_panneau);
			getJt().add("Annonces",panneau);
			//Panneau Mode
			sous_panneau=new JPanel();
			sous_panneau.setLayout(new GridLayout(0,1));
			sous_panneau.add(new JLabel("Mode de jouerie"));
			for(String mode2:Jouer.modes2)
			{
				liste.addElement(mode2.replace(Lettre._, Lettre.es));
			}
			listeChoix=new JComboBox();
			for (String mode:liste) {
				listeChoix.addItem(mode);
			}
			liste.removeAllElements();
			sous_panneau.add(listeChoix);
			getJt().add("Mode",sous_panneau);
			//Panneau Chelem
			panneau=new JPanel();
			panneau.setLayout(new GridLayout(0,2));
			sous_panneau=new JPanel();
			sous_panneau.add(new JCheckBox("Chelem contrat"));
			panneau.add(sous_panneau);
			//Panneau Regle du demi-point
			sous_panneau=new JPanel();
			sous_panneau.setLayout(new GridLayout(0,1));
			sous_panneau.add(new JLabel("Regle du demi-point"));
			liste.addElement("classique");
			liste.addElement("L'attaque gagne");
			liste.addElement("Match nul");
			listeChoix=new JComboBox();
			for (String mode:liste) {
				listeChoix.addItem(mode);
			}
			liste.removeAllElements();
			sous_panneau.add(listeChoix);
			panneau.add(sous_panneau);
			getJt().add("Regles",panneau);
			//Panneau 4-5 joueurs
			panneau=new JPanel();
			panneau.setLayout(new GridLayout(0,2));
			sous_panneau=new JPanel();
			sous_panneau.setLayout(new GridLayout(0,1));
			sous_panneau.add(new JLabel("4 joueurs"));
			listeChoix=new JComboBox();
			for (Repartition4Joueurs mode:Repartition4Joueurs.values()) {
				listeChoix.addItem(mode.toString());
			}
			sous_panneau.add(listeChoix);
			panneau.add(sous_panneau);
			sous_panneau=new JPanel();
			sous_panneau.setLayout(new GridLayout(0,1));
			listeChoix=new JComboBox();
			sous_panneau.add(new JLabel("5 joueurs"));
			for (Repartition5Joueurs mode:Repartition5Joueurs.values()) {
				listeChoix.addItem(mode.toString());
			}
			sous_panneau.add(listeChoix);
			panneau.add(sous_panneau);
			getJt().add("Repartition",panneau);
			container.add(getJt(),BorderLayout.CENTER);
		}
		panneau=new JPanel();
		JButton bouton=new JButton("<= Precedent");
		bouton.addActionListener(new EcouteValider("<= Precedent"));
		panneau.add(bouton);
		bouton=new JButton("Suivant =>");
		bouton.addActionListener(new EcouteValider("Suivant =>"));
		panneau.add(bouton);
		container.add(panneau,BorderLayout.SOUTH);
		setContentPane(container);
		pack();
	}
	private void validerInformationJeu()
	{
		infos=new Vector<String>();
		if(rep.equals(Jeu.Belote.toString()))
		{
			if(((PanneauListeCouleurs)((JPanel)getJt().getComponentAt(1)).getComponent(2)).nombreDeCouleurs()<4)
			{
				JOptionPane.showMessageDialog(this,"Il manque au moins une couleur pour enregistrer les informations.","Erreur de sauvegarde",JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				infos.addElement("Belote:");
				JPanel onglet=(JPanel)getJt().getComponentAt(0);
				byte nbp=(Byte)((JSpinner)onglet.getComponent(2)).getValue();
				infos.addElement("Battre les cartes:"+nbp);
				String info="Sens de distribution:";
				if(((JCheckBox)onglet.getComponent(3)).isSelected())
					info+=Sens.Horaire;
				else
					info+=Sens.Anti_horaire;
				infos.addElement(info);
				info="Tri au debut:";
				onglet=(JPanel)getJt().getComponentAt(1);
				String[] couleurs=((PanneauListeCouleurs)onglet.getComponent(2)).getCouleurs();
				info+=couleurs[0];
				for (int i = 1; i < couleurs.length; i++) {
					info+=Parametres.separateur_tiret+couleurs[i];
				}
				info+=";";
				if(((JCheckBox)((JPanel)onglet.getComponent(1)).getComponent(2)).isSelected())
					info+=Monotonie.Croissant;
				else
					info+=Monotonie.Decroissant;
				infos.addElement(info);
				info="Ordre avant enchere:";
				if(((JCheckBox)((JPanel)onglet.getComponent(3)).getComponent(1)).isSelected())
					info+=Ordre.Atout;
				else
					info+=Ordre.Couleur;
				infos.addElement(info);
				onglet=(JPanel)getJt().getComponentAt(2);
				JCheckBox jc;
				info="Obligation de sous-couper adversaire:";
				if(((JCheckBox)((JPanel)onglet.getComponent(0)).getComponent(0)).isSelected())
				{
					info+=Reponse.oui;
				}
				else
				{
					info+=Reponse.non;
				}
				infos.addElement(info);
				info="Obligation avec le partenaire:"+((JComboBox)((JPanel)onglet.getComponent(0)).getComponent(2)).getSelectedItem();
				infos.addElement(info);
				onglet=(JPanel)getJt().getComponentAt(3);
				info="Calcul des scores a la fin:";
				jc=(JCheckBox)onglet.getComponent(1);
				if(jc.isSelected())
					info+="classique";
				else
					info+="varie";
				infos.addElement(info);
			}
			if(((PanneauListeCouleurs)((JPanel)getJt().getComponentAt(1)).getComponent(2)).nombreDeCouleurs()>=4)
			{
				distribuer();
			}
		}
		else if(rep.equals(Jeu.Tarot.toString()))
		{
			if(((PanneauListeCouleurs)((JPanel)getJt().getComponentAt(1)).getComponent(2)).nombreDeCouleurs()<5)
			{
				JOptionPane.showMessageDialog(this,"Il manque au moins une couleur pour enregistrer les informations.","Erreur de sauvegarde",JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				JPanel onglet=(JPanel)getJt().getComponentAt(0);
				infos.addElement("Tarot:");
				byte nbp=(Byte)((JSpinner)onglet.getComponent(2)).getValue();
				infos.addElement("Battre les cartes:"+nbp);
				String info="Sens de distribution:";
				if(((JCheckBox)onglet.getComponent(3)).isSelected())
					info+=Sens.Horaire;
				else
					info+=Sens.Anti_horaire;
				infos.addElement(info);
				info="Tri:";
				onglet=(JPanel)getJt().getComponentAt(1);
				String[] couleurs=((PanneauListeCouleurs)onglet.getComponent(2)).getCouleurs();
				info+=couleurs[0];
				for (int i = 1; i < couleurs.length; i++) {
					info+=Parametres.separateur_tiret+couleurs[i];
				}
				info+=";";
				JCheckBox jcb=(JCheckBox)((JPanel)onglet.getComponent(1)).getComponent(2);
				if(jcb.isSelected())
					info+=Monotonie.Croissant;
				else
					info+=Monotonie.Decroissant;
				infos.addElement(info);
				onglet=(JPanel)getJt().getComponentAt(2);
				info="Poignees:";
				for (int i = 0; i < 3; i++) {
					jcb=(JCheckBox)((JPanel)onglet.getComponent(0)).getComponent(i+1);
					if(jcb.isSelected())
					{
						if(info.equals("Poignees:"))
							info+=jcb.getText();
						else
							info+=Parametres.separateur_tiret+jcb.getText();
					}
				}
				info+=";Miseres:";
				for (int i = 0; i < 5; i++) {
					jcb=(JCheckBox)((JPanel)onglet.getComponent(1)).getComponent(i+1);
					if(jcb.isSelected())
					{
						if(info.endsWith(";Miseres:"))
							info+=jcb.getText();
						else
							info+=Parametres.separateur_tiret+jcb.getText();
					}
				}
				infos.addElement(info);
				onglet=(JPanel)getJt().getComponentAt(4);
				jcb=(JCheckBox)((JPanel)onglet.getComponent(0)).getComponent(0);
				info="Chelem contrat:";
				if(jcb.isSelected())
					info+=Reponse.oui;
				else
					info+=Reponse.non;
				infos.addElement(info);
				info="Regle du demi-point:";
				info+=((JComboBox)((JPanel)onglet.getComponent(1)).getComponent(1)).getSelectedItem();
				infos.addElement(info);
				onglet=(JPanel)getJt().getComponentAt(3);
				info="Mode:";
				info+=((JComboBox)onglet.getComponent(1)).getSelectedItem().toString();
				infos.addElement(info);
				onglet=(JPanel)getJt().getComponentAt(5);
				info="4 joueurs:";
				info+=((JComboBox)((JPanel)onglet.getComponent(0)).getComponent(1)).getSelectedItem();
				infos.addElement(info);
				info="5 joueurs:";
				info+=((JComboBox)((JPanel)onglet.getComponent(1)).getComponent(1)).getSelectedItem();
				infos.addElement(info);
			}
			if(((PanneauListeCouleurs)((JPanel)getJt().getComponentAt(1)).getComponent(2)).nombreDeCouleurs()>=5)
			{
				distribuer();
			}
		}
	}
	private class EcouteurListe implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting())
			{
				int i=1;
				JPanel pansCartes=(JPanel)((JPanel)getContentPane().getComponent(i)).getComponent(0);
				int nombreDeMains=pansCartes.getComponentCount();
				nombre_cartes_selectionnees_precedent=nombre_cartes_selectionnees;
				for(int j=0;j<nombreDeMains;j++)
				{
					nombre_cartes_selectionnees+=((PanneauListeCartes)pansCartes.getComponent(j)).nombre_cartes_selectionnees();
				}
				nombre_cartes_selectionnees-=nombre_cartes_selectionnees_precedent;
				((JLabel)((JPanel)((JPanel)getContentPane().getComponent(i)).getComponent(1)).getComponent(2)).setText("Nombre de cartes selectionnees "+nombre_cartes_selectionnees);
			}
		}
	}
	private void distribuer()
	{
		setTitle("Distribuer les cartes");
		Container c=new Container();
		c.setLayout(new BorderLayout());
		JPanel panneau=new JPanel();
		if(rep.equals(Jeu.Belote.toString()))
		{
			panneau=new JPanel();
			panneau.add(new JLabel("Donneur :"));
			JComboBox liste=new JComboBox();
			liste.addItem("vous");
			for(int i=1;i<4;i++)
				liste.addItem("joueur "+i);
			liste.addItem("aleatoire");
			panneau.add(liste);
			c.add(panneau,BorderLayout.NORTH);
			panneau=new JPanel();
			panneau.setLayout(new BorderLayout());
			JPanel sous_panneau=new JPanel();
			MainBelote pile=new MainBelote();
			for(byte b=0;b<32;b++)
				pile.ajouter(new CarteBelote(b));
			String pcouleurs=infos.get(3);
			String pordre=infos.get(4);
			String psens=infos.get(3);
			pile.trier(pcouleurs, psens);
			PanneauListeCartes plc=new PanneauListeCartes(pile,12,32,"Distribution");
			plc.getListe().addListSelectionListener(new EcouteurListe());
			plc.setTri(pcouleurs, pordre, psens);
			sous_panneau.add(plc);
			plc=new PanneauListeCartes(5,5,"Votre main");
			plc.getListe().addListSelectionListener(new EcouteurListe());
			plc.setTri(pcouleurs, pordre, psens);
			sous_panneau.add(plc);
			for(int i=1;i<4;i++)
			{
				plc=new PanneauListeCartes(5,5,"Main du joueur "+i);
				plc.getListe().addListSelectionListener(new EcouteurListe());
				plc.setTri(pcouleurs, pordre, psens);
				sous_panneau.add(plc);
			}
			plc=new PanneauListeCartes(12,12,"Talon");
			plc.getListe().addListSelectionListener(new EcouteurListe());
			sous_panneau.add(plc);
			panneau.add(sous_panneau,BorderLayout.CENTER);
			sous_panneau=new JPanel();
			JButton bouton=new JButton("Deplacer les cartes vers la main numero");
			bouton.addActionListener(new EcouteBoutonsTri(bouton));
			sous_panneau.add(bouton);
			liste=new JComboBox();
			for(int i=0;i<6;i++)
				liste.addItem(i);
			sous_panneau.add(liste);
			sous_panneau.add(new JLabel("Nombre de cartes selectionnees "+nombre_cartes_selectionnees));
			panneau.add(sous_panneau,BorderLayout.SOUTH);
			c.add(panneau,BorderLayout.CENTER);
		}
		else
		{
			byte nbJ=Byte.parseByte(rep2.split(" ")[0]);
			byte nbCartesPJ;
			byte nbCartesC;
			if(nbJ==3)
			{
				nbCartesPJ=24;
				nbCartesC=6;
			}
			else if(nbJ==4)
			{
				nbCartesPJ=18;
				nbCartesC=6;
			}
			else if(infos.lastElement().contains("1 vs 4"))
			{
				nbCartesPJ=14;
				nbCartesC=8;
			}
			else
			{
				nbCartesPJ=15;
				nbCartesC=3;
			}
			MainTarot pile=new MainTarot();
			for(byte b=0;b<78;b++)
				pile.ajouter(new CarteTarot(b));
			panneau=new JPanel();
			panneau.add(new JLabel("Donneur :"));
			JComboBox liste=new JComboBox();
			liste.addItem("vous");
			for(int i=1;i<nbJ;i++)
				liste.addItem("joueur "+i);
			liste.addItem("aleatoire");
			panneau.add(liste);
			c.add(panneau,BorderLayout.NORTH);
			String pcouleurs=infos.get(3);
			String psens=infos.get(3);
			pile.trier(pcouleurs, psens);
			PanneauListeCartes plc=new PanneauListeCartes(pile,nbCartesPJ,78,"Pile de distribution");
			plc.getListe().addListSelectionListener(new EcouteurListe());
			plc.setTri(pcouleurs, null, psens);
			JPanel sous_panneau=new JPanel();
			sous_panneau.add(plc);
			plc=new PanneauListeCartes(nbCartesPJ,nbCartesPJ,"Votre main");
			plc.getListe().addListSelectionListener(new EcouteurListe());
			plc.setTri(pcouleurs, null, psens);
			sous_panneau.add(plc);
			for(int i=1;i<nbJ;i++)
			{
				plc=new PanneauListeCartes(nbCartesPJ,nbCartesPJ,"Main du joueur "+i);
				plc.getListe().addListSelectionListener(new EcouteurListe());
				plc.setTri(pcouleurs, null, psens);
				sous_panneau.add(plc);
			}
			plc=new PanneauListeCartes(nbCartesC,nbCartesC,"Chien");
			plc.getListe().addListSelectionListener(new EcouteurListe());
			plc.setTri(pcouleurs, null, psens);
			sous_panneau.add(plc);
			panneau=new JPanel();
			panneau.setLayout(new BorderLayout());
			panneau.add(sous_panneau,BorderLayout.CENTER);
			sous_panneau=new JPanel();
			JButton bouton=new JButton("Deplacer les cartes vers la main numero");
			bouton.addActionListener(new EcouteBoutonsTri(bouton));
			sous_panneau.add(bouton);
			liste=new JComboBox();
			for(int i=0;i<2+nbJ;i++)
				liste.addItem(i);
			sous_panneau.add(liste);
			sous_panneau.add(new JLabel("Nombre de cartes selectionnees "+nombre_cartes_selectionnees));
			panneau.add(sous_panneau,BorderLayout.SOUTH);
			c.add(panneau,BorderLayout.CENTER);
		}
		panneau=new JPanel();
		JButton bouton=new JButton("<= Precedent");
		bouton.addActionListener(new EcouteValider("<= Precedent"));
		panneau.add(bouton);
		bouton=new JButton("Sauvegarder sans fermer");
		bouton.addActionListener(new EcouteValider("Sauvegarder1"));
		panneau.add(bouton);
		bouton=new JButton("Sauvegarder et jouer");
		bouton.addActionListener(new EcouteValider("Sauvegarder2"));
		panneau.add(bouton);
		bouton=new JButton("Jouer sans sauvegarder");
		bouton.addActionListener(new EcouteValider("Sauvegarder3"));
		panneau.add(bouton);
		bouton=new JButton("Sauvegarder et fermer");
		bouton.addActionListener(new EcouteValider("Sauvegarder4"));
		panneau.add(bouton);
		c.add(panneau,BorderLayout.SOUTH);
		setContentPane(c);
		pack();
	}
	private void setPartie()
	{
		Vector<Main> mains=new Vector<Main>();
		PanneauListeCartes plc;
		JPanel pansCartes;
		Donne donne;
		pansCartes=(JPanel)((JPanel)getContentPane().getComponent(1)).getComponent(0);
		int nombreDeMains=pansCartes.getComponentCount();
		int nombreDeJoueurs;
		if(rep.equals(Jeu.Belote.toString()))
		{
			for(int i=1;i<nombreDeMains;i++)
			{
				plc=(PanneauListeCartes)pansCartes.getComponent(i);
				MainBelote m=new MainBelote(plc.getCartes());
				m.setOrdre(Ordre.valueOf(infos.get(4).split(":")[1]));
				if(i<nombreDeMains-1)
				{/*On trie toutes les mains sauf le talon car l'ordre des cartes au talon est important*/
					m.trier(infos.get(3), infos.get(3));
				}
				mains.addElement(m);
			}
		}
		else
		{
			for(int i=1;i<nombreDeMains;i++)
			{
				plc=(PanneauListeCartes)pansCartes.getComponent(i);
				MainTarot m=new MainTarot(plc.getCartes());
				m.trier(infos.get(3), infos.get(3));
				mains.addElement(m);
			}
		}
		nombreDeJoueurs=nombreDeMains-1;
		byte donneur;
		String info=(String)((JComboBox)((JPanel)getContentPane().getComponent(0)).getComponent(1)).getSelectedItem();
		if(info.startsWith("v"))
		{
			donneur=0;
		}
		else if(info.startsWith("a"))
		{
			donneur=(byte)Math.floor(nombreDeJoueurs*Math.random());
		}
		else
		{
			donneur=Byte.parseByte(info.split(" ")[1]);
		}
		donne=new Donne(rep2,infos,mains,donneur);
		if(rep.equals(Jeu.Belote.toString()))
		{
			partie=new PartieBelote(Type.Editer,donne);
		}
		else
		{
			partie=new PartieTarot(Type.Editer,donne);
		}
	}
	/**Lorsqu'on veut sauvegarder une partie*/
	private void validerSauvegarde(String s)
	{
		partie.sauvegarder(Fichier.dossier_sauvegarde+File.separator+rep+File.separator+s+Fichier.extension_partie);
	}
	void deplacerCartes()
	{
		JPanel panneau=(JPanel)getContentPane().getComponent(1);
		JPanel pansCartes=(JPanel)panneau.getComponent(0);
		int nombreDeMains=pansCartes.getComponentCount();
		Main m;
		if(rep.equals(Jeu.Belote.toString()))
		{
			m=new MainBelote(Ordre.valueOf(infos.get(4).split(":")[1]));
		}
		else
		{
			m=new MainTarot();
		}
		for(int i=0;i<nombreDeMains;i++)
		{
			Carte[] cartesSelectionnees=((PanneauListeCartes)pansCartes.getComponent(i)).getCartesSelectionnees();
			if(cartesSelectionnees!=null)
			{
				m.ajouterCartes(cartesSelectionnees);
			}
		}
		int numero=(Integer)((JComboBox)((JPanel)panneau.getComponent(1)).getComponent(1)).getSelectedItem();
		PanneauListeCartes panneau_selectionne=(PanneauListeCartes)pansCartes.getComponent(numero);
		PanneauListeCartes panneau_2;
		int taille=panneau_selectionne.taille();
		int max=panneau_selectionne.getMax();
		if(taille+m.total()<max+1)
		{
			if(!rep.equals(Jeu.Belote.toString())||numero!=nombreDeMains-1)
			{
				panneau_selectionne.ajouterCartes(m);
			}
			else
			{
				panneau_selectionne.ajouterCartesFin(m);
			}
			for(int i=0;i<nombreDeMains;i++)
			{
				panneau_2=(PanneauListeCartes)pansCartes.getComponent(i);
				Carte[] cartesSelectionnees=panneau_2.getCartesSelectionnees();
				if(cartesSelectionnees!=null)
				{
					panneau_2.supprimerCartes(cartesSelectionnees);
				}
			}
			nombre_cartes_selectionnees=0;
		}
		else
		{
			String mes=taille+m.total()+" cartes sont selectionnees,\nle nombre de places restantes est de "+(max-taille);
			mes+="\npour la main numero "+numero;
			JOptionPane.showMessageDialog(this,mes, "Erreur de placement", JOptionPane.ERROR_MESSAGE);
		}
	}
	/**Change la deuxieme liste*/
	void modifierListeMode()
	{
		JPanel panneau=(JPanel)getContentPane().getComponent(0);
		JComboBox liste=((JComboBox)panneau.getComponent(0));
		JComboBox liste2=((JComboBox)panneau.getComponent(1));
		String nrep=(String)liste.getSelectedItem();
		liste2.removeAllItems();
		if(nrep.equals(Jeu.Belote.toString()))
		{
			String[] tab1={ModeBelote.Avec_surcontrat.toString(),ModeBelote.Sans_surcontrat.toString()};
			for (int i = 0; i < tab1.length; i++)
			{
				liste2.addItem(tab1[i]);
			}
		}
		else
		{
			for (int i = 0; i < 3; i++)
			{
				liste2.addItem((i+3)+" joueurs");
			}
		}
		pack();
	}
}