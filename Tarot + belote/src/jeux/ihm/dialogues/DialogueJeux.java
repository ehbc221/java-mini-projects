package jeux.ihm.dialogues;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import jeux.*;
import jeux.enumerations.*;
import jeux.ihm.*;
import jeux.ihm.panneaux.*;
/**Boite de dialogue permettant de modifier les informations relatives a un jeu ou ou fonctionnement du logiciel*/
public class DialogueJeux extends Dialogue {
	private static final long serialVersionUID = 1L;
	/**Informations relatives a un jeu ou ou fonctionnement du logiciel
	 * ou aux joueurs*/
	private Vector<String> infos;
	/**Met en place la boite de dialogue modale selon l'option choisie*/
	public DialogueJeux(String titre,Fenetre fenetre) {
		super(titre,fenetre,true);
		if(titre.equals(Fenetre.sous_menus_utilises3[0]))
		{
			infos=fenetre.getInfos().lastElement();
		}
		else if(titre.equals(Fenetre.sous_menus_utilises3[1])||titre.equals(Fenetre.sous_menus_utilises3[2])||titre.equals(Fenetre.sous_menus_utilises3[3]))
		{//Lancement du logiciel
			infos=fenetre.getInfos().get(0);
		}
		else
		{//Selon le jeu on affecte une partie des parametres dans la variable infos
			infos=fenetre.getInfos().get(Jeu.valueOf(titre).ordinal()+1);
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	/**A la fermeture de la boite de dialogue on rcupere les informations relatives a un jeu ou ou fonctionnement du logiciel*/
	public Vector<String> getInfos()
	{
		setVisible(true);
		return infos;
	}
	/**Met en place le contenu de la boite de dialogue
	 * Pour les jeux et les joueurs on a besoin d'onglets pour utiliser moins de place sur l'ecran*/
	public void setDialogue()
	{
		Container container=new Container();
		container.setLayout(new BorderLayout());
		if(!getTitle().startsWith(Fenetre.sous_menus_utilises3[1])&&!getTitle().startsWith(Fenetre.sous_menus_utilises3[2])&&!getTitle().startsWith(Fenetre.sous_menus_utilises3[3]))
		{
			if(getTitle().equals(Jeu.Belote.toString()))
			{
				JPanel panneau=new JPanel();
				panneau.setLayout(new GridLayout(0,2));
				//Sous - panneau Battre les cartes
				panneau.add(new JLabel("Battre les cartes"));
				Vector<String> liste=new Vector<String>();
				JComboBox listeChoix=new JComboBox();
				for (ChoixBattreCartes choix:ChoixBattreCartes.values()) {
					listeChoix.addItem(choix.toString());
				}
				listeChoix.setSelectedItem(infos.get(1).split(":")[1]);
				panneau.add(new JLabel("Sens"));
				panneau.add(listeChoix);
				//Panneau Distribution
				JCheckBox caseCroix=new JCheckBox(Sens.Horaire.toString());
				caseCroix.setSelected(infos.get(2).contains(Sens.Horaire.toString()));
				panneau.add(caseCroix);
				getJt().add("Distribution",panneau);
				//Panneau Tri avant enchere
				panneau=new JPanel();
				panneau.setLayout(new GridLayout(0,4));
				listeChoix=new JComboBox();
				for (Couleur couleur:Couleur.values())
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
				caseCroix=new JCheckBox("Trier par ordre croissant");
				caseCroix.setSelected(infos.get(3).contains(Monotonie.Croissant.toString()));
				sous_2_panneau.add(caseCroix);
				panneau.add(sous_2_panneau);
				for (String chaine:infos.get(3).split(":")[1].split(";")[0].split(Parametres.separateur_tiret_slash)) {
					liste.addElement(chaine);
				}
				PanneauListeCouleurs plc=new PanneauListeCouleurs(liste,4);
				liste.removeAllElements();
				panneau.add(plc);
				//Panneau Tri avant enchere (Atout)
				JPanel sous_panneau=new JPanel();
				sous_panneau.setLayout(new GridLayout(0,1));
				sous_panneau.add(new JLabel("Tri avant contrat"));
				caseCroix=new JCheckBox("Ordre d\'atout");
				caseCroix.setSelected(infos.get(4).split(":")[1].equals(Ordre.Atout.toString()));
				sous_panneau.add(caseCroix);
				panneau.add(sous_panneau);
				getJt().add("Tri",panneau);
				panneau=new JPanel(new GridLayout(0,2));
				//Panneau Annonces autorisees
				sous_panneau=new JPanel();
				sous_panneau.setLayout(new GridLayout(0,1));
				caseCroix=new JCheckBox("Obligation de sous-couper adversaire");
				caseCroix.setSelected(infos.get(6).endsWith(Reponse.oui.toString()));
				sous_panneau.add(caseCroix);
				sous_panneau.add(new JLabel("Obligation de couper sur partenaire"));
				listeChoix=new JComboBox();
				for(BeloteCoupePartenaire choix:BeloteCoupePartenaire.values())
				{
					listeChoix.addItem(choix.toString());
				}
				listeChoix.setSelectedItem(infos.get(7).split(":")[1]);
				sous_panneau.add(listeChoix);
				panneau.add(sous_panneau);
				getJt().add("Regles du jeu",panneau);
				//Panneau Calcul des scores
				sous_panneau=new JPanel();
				sous_panneau.setLayout(new GridLayout(0,1));
				sous_panneau.add(new JLabel("Calcul des scores"));
				caseCroix=new JCheckBox("162 - 0, pour la defense lorsqu\'elle gagne");
				caseCroix.setSelected(infos.lastElement().contains("classique"));
				sous_panneau.add(caseCroix);
				getJt().add("Fin de partie",sous_panneau);
			}
			else if(getTitle().equals(Jeu.Tarot.toString()))
			{
				JPanel panneau=new JPanel();
				panneau.setLayout(new GridLayout(0,2));
				//Panneau Battre les cartes
				panneau.add(new JLabel("Battre les cartes"));
				Vector<String> liste=new Vector<String>();
				JComboBox listeChoix=new JComboBox();
				for (ChoixBattreCartes choix:ChoixBattreCartes.values()) {
					listeChoix.addItem(choix.toString());
				}
				listeChoix.setSelectedItem(infos.get(1).split(":")[1]);
				panneau.add(new JLabel("Distribution"));
				panneau.add(listeChoix);
				//Panneau Distribution
				JCheckBox caseCroix=new JCheckBox("Dans le sens horaire");
				caseCroix.setSelected(infos.get(2).contains(Sens.Horaire.toString()));
				panneau.add(caseCroix);
				getJt().add("Distribution",panneau);
				//Panneau Tri
				JPanel sous_panneau=new JPanel();
				sous_panneau.setLayout(new GridLayout(0,3));
				listeChoix=new JComboBox();
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
				caseCroix=new JCheckBox("Trier par ordre croissant");
				caseCroix.setSelected(infos.get(3).contains(Monotonie.Croissant.toString()));
				sous_2_panneau.add(caseCroix);
				sous_panneau.add(sous_2_panneau);
				for(String couleur_tri:infos.get(3).split(":")[1].split(";")[0].split(Parametres.separateur_tiret_slash))
				{
					liste.addElement(couleur_tri);
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
				String annonces=infos.get(4);
				for(Poignees poignee:Poignees.values())
				{
					caseCroix=new JCheckBox(poignee.toString());
					caseCroix.setSelected(annonces.contains(poignee.toString()));
					sous_panneau.add(caseCroix);
				}
				panneau.add(sous_panneau);
				//Panneau Miseres
				sous_panneau=new JPanel();
				sous_panneau.setLayout(new GridLayout(0,1));
				sous_panneau.add(new JLabel("Miseres autorisees"));
				for (Miseres annonce:Miseres.values())
				{
					caseCroix=new JCheckBox(annonce.toString());
					caseCroix.setSelected(annonces.contains(annonce.toString()));
					sous_panneau.add(caseCroix);
				}
				panneau.add(sous_panneau);
				getJt().add("Annonces",panneau);
				//Panneau Chelem
				panneau=new JPanel();
				panneau.setLayout(new GridLayout(0,2));
				sous_panneau=new JPanel();
				caseCroix=new JCheckBox("Chelem contrat");
				caseCroix.setSelected(infos.get(5).contains(Reponse.oui.toString()));
				sous_panneau.add(caseCroix);
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
				listeChoix.setSelectedItem(infos.get(6).split(":")[1]);
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
				listeChoix.setSelectedItem(infos.get(8).split(":")[1]);
				sous_panneau.add(listeChoix);
				panneau.add(sous_panneau);
				sous_panneau=new JPanel();
				sous_panneau.setLayout(new GridLayout(0,1));
				listeChoix=new JComboBox();
				sous_panneau.add(new JLabel("5 joueurs"));
				for (Repartition5Joueurs mode:Repartition5Joueurs.values()) {
					listeChoix.addItem(mode.toString());
				}
				listeChoix.setSelectedItem(infos.lastElement().split(":")[1]);
				sous_panneau.add(listeChoix);
				panneau.add(sous_panneau);
				getJt().add("Repartition",panneau);
			}
			else//Joueurs
			{
				//Panneau pseudo du joueur
				JPanel sous_panneau=new JPanel();
				sous_panneau.setLayout(new GridLayout(0,1));
				sous_panneau.add(new JLabel("Votre pseudo"));
				JTextField pseudo=new JTextField(30);
				pseudo.setText(infos.get(1).substring(infos.get(1).indexOf(':')+1));
				sous_panneau.add(pseudo);
				getJt().add("Pseudo",sous_panneau);
				//Panneau pseudos des joueurs belote
				sous_panneau=new JPanel();
				sous_panneau.setLayout(new GridLayout(0,1));
				String[] pseudos=infos.get(4).split("\t");
				for (int i = 0; i < 3; i++) {
					pseudo=new JTextField(30);
					pseudo.setText(pseudos[i]);
					sous_panneau.add(new JLabel("Pseudo du joueur "+(i+1)));
					sous_panneau.add(pseudo);
				}
				getJt().add("Pseudos pour la belote",sous_panneau);
				//Panneau pseudos des joueurs tarot
				sous_panneau=new JPanel();
				sous_panneau.setLayout(new GridLayout(0,1));
				pseudos=infos.get(6).split("\t");
				for (int i = 0; i < 4; i++) {
					pseudo=new JTextField(30);
					pseudo.setText(pseudos[i]);
					sous_panneau.add(new JLabel("Pseudo du joueur "+(i+1)));
					sous_panneau.add(pseudo);
				}
				getJt().add("Pseudos pour le tarot",sous_panneau);
			}
			container.add(getJt(),BorderLayout.CENTER);
		}
		else if(getTitle().startsWith(Fenetre.sous_menus_utilises3[1]))
			//Lancement du logiciel
		{
			String info=infos.get(0).split(":")[1];
			JPanel panneau=new JPanel();
			panneau.setLayout(new FlowLayout());
			JComboBox liste=new JComboBox(new String[]{"Lancement du logiciel",Jeu.Belote.toString(),Jeu.Tarot.toString()});
			JComboBox liste2=new JComboBox();
			String info2=null;
			String[] infos_lancement=null;
			if(!info.startsWith(Lancements.Bienvenue_dans_les_jeux_de_cartes.toString()))
			{
				infos_lancement=info.split("\\\\");
				info2=infos_lancement[0];
				liste.setSelectedItem(info2);
				if(info2.equals(Jeu.Belote.toString()))
				{
					String[] choix={"menu","Avec surcontrat","Sans surcontrat"};
					for (int i = 0; i < choix.length; i++) {
						liste2.addItem(choix[i]);
					}
				}
				else if(info2.equals(Jeu.Tarot.toString()))
				{
					String[] choix=new String[4];
					choix[0]="menu";
					for (int i = 1; i < choix.length; i++) {
						choix[i]=(i+2)+" joueurs";
					}
					for (int i = 0; i < choix.length; i++) {
						liste2.addItem(choix[i]);
					}
				}
				else
				{
					liste2.addItem("menu");
				}
				if(info.indexOf('\\')<0)
				{
					liste2.setSelectedIndex(0);
				}
				else
				{
					liste2.setSelectedItem(infos_lancement[1]);
				}
			}
			else
			{
				liste2.addItem("menu");
			}
			liste.addActionListener(new EcouteListe());
			panneau.add(liste);
			panneau.add(liste2);
			if(Jeu.Tarot.toString().equals(info2))
			{
				JComboBox liste3=new JComboBox();
				liste3.addItem("menu");
				for (String choix:Jouer.modes2) {
					liste3.addItem(choix.replace(Lettre._, Lettre.es));
				}
				if(infos_lancement!=null&&info.indexOf('\\')<info.lastIndexOf('\\'))
				{
					liste3.setSelectedItem(infos_lancement[2]);
				}
				panneau.add(liste3);
			}
			container.add(panneau,BorderLayout.CENTER);
		}
		else if(getTitle().startsWith(Fenetre.sous_menus_utilises3[2]))
		{
			JPanel panneau=new JPanel(new GridLayout(0,1));
			int nombre_infos=infos.size()-4;
			int[][] min_max={{300,2000},{300,2000},{500,3000},{300,2000},{300,2000},{500,3000}};
			String[] chaines;
			for(int indice_info=1;indice_info<=nombre_infos;indice_info++)
			{
				chaines=infos.get(indice_info).split(":");
				int valeur=Integer.parseInt(chaines[1]);
				int min_valeur=min_max[indice_info-1][0];
				int max_valeur=min_max[indice_info-1][1];
				panneau.add(new JLabel(chaines[0]+"(min="+min_valeur+" ms,max="+max_valeur+" ms):"+valeur+" ms"));
				JSlider barre=new JSlider(min_valeur,max_valeur);
				barre.setValue(valeur);
				barre.addChangeListener(new EcouteChangeSlide((byte)(indice_info-1)));
				panneau.add(barre);
			}
			for(String info:infos.get(nombre_infos+1).split(";"))
			{
				JCheckBox caseCroix=new JCheckBox(info.split(":")[0]);
				caseCroix.setSelected(info.endsWith(Reponse.oui.toString()));
				panneau.add(caseCroix);
			}
			panneau.setPreferredSize(new Dimension(400,800));
			container.add(panneau,BorderLayout.CENTER);
		}
		else
		{
			JPanel panneau=new JPanel(new GridLayout(0,1));
			for(String info:infos.get(infos.size()-2).split(";"))
			{
				JCheckBox caseCroix=new JCheckBox(info.split(":")[0]);
				caseCroix.setSelected(info.endsWith(Reponse.oui.toString()));
				panneau.add(caseCroix);
			}
			container.add(panneau,BorderLayout.CENTER);
		}
		JButton bouton=new JButton("Valider");
		bouton.addActionListener(new EcouteValider());
		container.add(bouton,BorderLayout.SOUTH);
		setContentPane(container);
		pack();
	}
	private class EcouteChangeSlide implements ChangeListener{
		private byte numero;
		private EcouteChangeSlide(byte pnumero)
		{
			numero=pnumero;
		}
		public void stateChanged(ChangeEvent e)
		{
			JSlider slide=(JSlider)((JPanel)getContentPane().getComponent(0)).getComponent(2*numero+1);
			JLabel etiquette=(JLabel)((JPanel)getContentPane().getComponent(0)).getComponent(2*numero);
			String texte=etiquette.getText().split(":")[0];
			/*On eclate la chaine texte en trois chaines avec pour separateur une chaine "nombre ms"*/
			String[] morceau=texte.split("([0-9])+ ms");
			int min=slide.getMinimum();
			int max=slide.getMaximum();
			String nouveau=morceau[0]+min+" ms"+morceau[1]+max+" ms"+morceau[2]+":"+slide.getValue()+" ms";
			etiquette.setText(nouveau);
		}
	}
	/**Modifie la deuxieme liste deroulante lorsque l'utilisateur change l'objet selectionne de la premiere liste
	 * pour l'option de lancement*/
	void modifierListe()
	{
		JPanel panneau=(JPanel)getContentPane().getComponent(0);
		JComboBox liste2=((JComboBox)panneau.getComponent(1));
		String rep=(String)((JComboBox)panneau.getComponent(0)).getSelectedItem();
		if(rep.equals("Lancement du logiciel"))
		{
			liste2.removeAllItems();
			liste2.addItem("menu");
			if(panneau.getComponentCount()==3)
			{
				panneau.remove(2);
			}
		}
		else if(rep.equals(Jeu.Belote.toString()))
		{
			liste2.removeAllItems();
			String[] tab1={"menu","Avec surcontrat","Sans surcontrat"};
			for (int i = 0; i < tab1.length; i++) {
				liste2.addItem(tab1[i]);
			}
			if(panneau.getComponentCount()==3)
			{
				panneau.remove(2);
			}
		}
		else
		{
			String[] tab1=new String[4];
			tab1[0]="menu";
			for (int i = 1; i < tab1.length; i++) {
				tab1[i]=(i+2)+" joueurs";
			}
			liste2.removeAllItems();
			for (int i = 0; i < tab1.length; i++) {
				liste2.addItem(tab1[i]);
			}
			if(panneau.getComponentCount()==2)
			{
				JComboBox liste3=new JComboBox();
				String[] choix=new String[6];
				choix[0]="menu";
				for (int i = 1; i < choix.length; i++) {
					choix[i]=Jouer.modes2.get(i-1);
				}
				for (int i = 0; i < choix.length; i++) {
					liste3.addItem(choix[i].replace(Lettre._, Lettre.es));
				}
				panneau.add(liste3);
			}
		}
		pack();
	}
	/**Classe d'ecouteur speciale a ce type de boite de dialogue*/
	private class EcouteValider implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			valider();
		}
		/**Enregistre les informations dans une variable et ferme la boite de dialogue*/
		private void valider()
		{
			if(!getTitle().startsWith(Fenetre.sous_menus_utilises3[1])&&!getTitle().startsWith(Fenetre.sous_menus_utilises3[2])&&!getTitle().startsWith(Fenetre.sous_menus_utilises3[3]))
			{
				if(getTitle().equals(Jeu.Belote.toString()))
				{
					if(((PanneauListeCouleurs)((JPanel)getJt().getComponentAt(1)).getComponent(2)).nombreDeCouleurs()<4)
					{
						JOptionPane.showMessageDialog(DialogueJeux.this,"Il manque au moins une couleur pour enregistrer les informations.","Erreur de sauvegarde",JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						JPanel onglet=(JPanel)getJt().getComponentAt(0);
						String info=(String) ((JComboBox)onglet.getComponent(2)).getSelectedItem();
						infos.setElementAt("Battre les cartes:"+info,1);
						info="Sens de distribution:";
						if(((JCheckBox)onglet.getComponent(3)).isSelected())
							info+=Sens.Horaire;
						else
							info+=Sens.Anti_horaire;
						infos.setElementAt(info,2);
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
						infos.setElementAt(info,3);
						info="Ordre avant enchere:";
						if(((JCheckBox)((JPanel)onglet.getComponent(3)).getComponent(1)).isSelected())
							info+=Ordre.Atout;
						else
							info+=Ordre.Couleur;
						infos.setElementAt(info,4);
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
						infos.setElementAt(info,5);
						info="Obligation avec le partenaire:"+((JComboBox)((JPanel)onglet.getComponent(0)).getComponent(2)).getSelectedItem();
						infos.setElementAt(info,6);
						onglet=(JPanel)getJt().getComponentAt(3);
						info="Calcul des scores a la fin:";
						jc=(JCheckBox)onglet.getComponent(1);
						if(jc.isSelected())
							info+="classique";
						else
							info+="varie";
						infos.setElementAt(info,7);
						dispose();
					}
				}
				else if(getTitle().equals(Jeu.Tarot.toString()))
				{
					if(((PanneauListeCouleurs)((JPanel)getJt().getComponentAt(1)).getComponent(2)).nombreDeCouleurs()<5)
					{
						JOptionPane.showMessageDialog(DialogueJeux.this,"Il manque au moins une couleur pour enregistrer les informations.","Erreur de sauvegarde",JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						JPanel onglet=(JPanel)getJt().getComponentAt(0);
						infos.setElementAt("Battre les cartes:"+((JComboBox)onglet.getComponent(2)).getSelectedItem(),1);
						String info="Sens de distribution:";
						if(((JCheckBox)onglet.getComponent(3)).isSelected())
							info+=Sens.Horaire;
						else
							info+=Sens.Anti_horaire;
						infos.setElementAt(info,2);
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
						infos.setElementAt(info,3);
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
						infos.setElementAt(info,4);
						onglet=(JPanel)getJt().getComponentAt(3);
						jcb=(JCheckBox)((JPanel)onglet.getComponent(0)).getComponent(0);
						info="Chelem contrat:";
						if(jcb.isSelected())
							info+=Reponse.oui;
						else
							info+=Reponse.non;
						infos.setElementAt(info,5);
						info="Regle du demi-point:";
						info+=((JComboBox)((JPanel)onglet.getComponent(1)).getComponent(1)).getSelectedItem();
						infos.setElementAt(info,6);
						onglet=(JPanel)getJt().getComponentAt(4);
						info="4 joueurs:";
						info+=((JComboBox)((JPanel)onglet.getComponent(0)).getComponent(1)).getSelectedItem();
						infos.setElementAt(info,8);
						info="5 joueurs:";
						info+=((JComboBox)((JPanel)onglet.getComponent(1)).getComponent(1)).getSelectedItem();
						infos.setElementAt(info,9);
						dispose();
					}
				}
				else
				{
					if(unChampVidePresent())
					{
						JOptionPane.showMessageDialog(DialogueJeux.this,"Un des champs est vide.","Erreur de sauvegarde",JOptionPane.ERROR_MESSAGE);
					}
					else if(tabulationPresente())
					{
						JOptionPane.showMessageDialog(DialogueJeux.this,"Les tabulations sont interdites.","Erreur de sauvegarde",JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						JPanel onglet =(JPanel)getJt().getComponentAt(0);
						String info="Pseudo:"+((JTextField)onglet.getComponent(1)).getText();
						infos.setElementAt(info, 1);
						onglet =(JPanel)getJt().getComponentAt(1);
						info=((JTextField)onglet.getComponent(1)).getText();
						for (int i = 3; i < 6; i+=2) {
							info+="\t"+((JTextField)onglet.getComponent(i)).getText();
						}
						infos.setElementAt(info, 4);
						onglet =(JPanel)getJt().getComponentAt(2);
						info=((JTextField)onglet.getComponent(1)).getText();
						for (int i = 3; i < 8; i+=2) {
							info+="\t"+((JTextField)onglet.getComponent(i)).getText();
						}
						infos.setElementAt(info, 6);
						dispose();
					}
				}
			}
			else if(getTitle().startsWith(Fenetre.sous_menus_utilises3[1]))
			{
				JPanel panneau=(JPanel)getContentPane().getComponent(0);
				String rep=((String)((JComboBox)panneau.getComponent(0)).getSelectedItem());
				String rep2=((String)((JComboBox)panneau.getComponent(1)).getSelectedItem());
				if(rep.equals("Lancement du logiciel"))
				{
					infos.setElementAt("Lancement:"+Lancements.Bienvenue_dans_les_jeux_de_cartes, 0);
				}
				else if(rep2.equals("menu"))
				{
					infos.setElementAt("Lancement:"+rep, 0);
				}
				else
				{
					if(!rep.equals(Jeu.Tarot.toString()))
					{
						infos.setElementAt("Lancement:"+rep+"\\"+rep2, 0);
					}
					else
					{
						String rep3=((String)((JComboBox)panneau.getComponent(2)).getSelectedItem());
						if(rep3.equals("menu"))
						{
							infos.setElementAt("Lancement:"+rep+"\\"+rep2, 0);
						}
						else
						{
							infos.setElementAt("Lancement:"+rep+"\\"+rep2+"\\"+rep3, 0);
						}
					}
				}
				dispose();
			}
			else if(getTitle().startsWith(Fenetre.sous_menus_utilises3[2]))
			{
				JPanel panneau=(JPanel)getContentPane().getComponent(0);
				int nombre_infos=infos.size()-4;
				for(int indice_info=1;indice_info<=nombre_infos;indice_info++)
				{
					JSlider slide=(JSlider)panneau.getComponent(2*indice_info-1);
					JLabel etiquette=(JLabel)panneau.getComponent(2*indice_info-2);
					String texte=etiquette.getText().split(":")[0];
					/*On eclate la chaine texte en trois chaines avec pour separateur une chaine "nombre ms"*/
					String[] morceau=texte.split("([0-9])+ ms")[0].split("\\(");
					infos.setElementAt(morceau[0]+":"+slide.getValue(),indice_info);
				}
				String[] chaines=infos.get(nombre_infos+1).split(";");
				String ajout="";
				for(int indice_info=0;indice_info<chaines.length;indice_info++)
				{
					JCheckBox caseCroix=(JCheckBox)panneau.getComponent(2*nombre_infos+indice_info);
					ajout+=chaines[indice_info].split(":")[0]+":";
					if(caseCroix.isSelected())
					{
						ajout+=Reponse.oui;
					}
					else
					{
						ajout+=Reponse.non;
					}
					if(indice_info<chaines.length-1)
					{
						ajout+=";";
					}
				}
				infos.setElementAt(ajout,nombre_infos+1);
				dispose();
			}
			else
			{
				JPanel panneau=(JPanel)getContentPane().getComponent(0);
				int indice=infos.size()-2;
				String[] chaines=infos.get(indice).split(";");
				String ajout="";
				for(int indice_info=0;indice_info<chaines.length;indice_info++)
				{
					JCheckBox caseCroix=(JCheckBox)panneau.getComponent(indice_info);
					ajout+=chaines[indice_info].split(":")[0]+":";
					if(caseCroix.isSelected())
					{
						ajout+=Reponse.oui;
					}
					else
					{
						ajout+=Reponse.non;
					}
					if(indice_info<chaines.length-1)
					{
						ajout+=";";
					}
				}
				infos.setElementAt(ajout,indice);
				dispose();
			}
		}
		/**Retourne vrai si et seulement si il existe un nom de joueur ayant une tabulation*/
		private boolean tabulationPresente()
		{
			JPanel onglet =((JPanel)(getJt().getComponentAt(0)));
			boolean tabulation=((JTextField)onglet.getComponent(1)).getText().contains("\t");
			if(tabulation)
				return true;
			onglet =((JPanel)(getJt().getComponentAt(1)));
			for (int i = 1; i < 6; i+=2) {
				tabulation|=((JTextField)onglet.getComponent(i)).getText().contains("\t");
			}
			if(tabulation)
				return true;
			onglet =((JPanel)(getJt().getComponentAt(2)));
			for (int i = 1; i < 8; i+=2) {
				tabulation|=((JTextField)onglet.getComponent(i)).getText().contains("\t");
			}
			return tabulation;
		}
		/**Retourne vrai si et seulement si il existe un champ vide parmi les champs de texte*/
		private boolean unChampVidePresent()
		{
			JPanel onglet =((JPanel)(getJt().getComponentAt(0)));
			boolean vide=((JTextField)onglet.getComponent(1)).getText().isEmpty();
			if(vide)
				return true;
			onglet =((JPanel)(getJt().getComponentAt(1)));
			for (int i = 1; i < 6; i+=2) {
				vide|=((JTextField)onglet.getComponent(i)).getText().isEmpty();
			}
			if(vide)
				return true;
			onglet =((JPanel)(getJt().getComponentAt(2)));
			for (int i = 1; i < 8; i+=2) {
				vide|=((JTextField)onglet.getComponent(i)).getText().isEmpty();
			}
			return vide;
		}

	}
	
}