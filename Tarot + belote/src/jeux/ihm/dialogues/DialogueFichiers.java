package jeux.ihm.dialogues;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import jeux.*;
import jeux.enumerations.*;
import jeux.ihm.*;
import jeux.ihm.panneaux.*;
/**Classe permettant de sauvegarder, de charger, ou de supprimer une ou plusieurs parties
 * Une boite de dialogue apparait alors.*/
public class DialogueFichiers extends JDialog {
	private static final long serialVersionUID = 1L;
	/**Fichier a charger*/
	private String fichier;
	/**Premier = racine, Deuxieme = jeu, Troisieme = mode du jeu, Quatrieme (Tarot Deuxieme mode, Ailleurs Type), Cinquieme (au Tarot) Type*/
	private Vector<String> dossiers_ouverts=new Vector<String>();
	private boolean passage;
	private final static char ea=Lettre.ea;
	private static char as=Lettre.as;
	private static char es=Lettre.es;
	private static char st=Lettre.st;
	private static char _=Lettre._;
	private static final String ch_v=Lettre.ch_v;
	/**Tabulation*/
	public static char separateur=Lettre.tb;
	private static String inf=" <";
	private static String sup=" >";
	private static String nom="Nom";
	private static String taille="Taille";
	private static String annuler="Annuler";
	private static String date="Date";
	private static String p_v=ch_v+Lettre.pv;
	private static String sauvegarder="Sauvegarder";
	public static String unite=ch_v+es+Lettre.o;
	private static String supprimer="Supprimer";
	private static String anti_slash_point=ch_v+as+Lettre.pt;
	private static String description="Fichier "+Fichier.extension_partie+" a "+sauvegarder.toLowerCase();
	private static String message_erreur="Aucun fichier n'est s"+ea+"lectionn"+ea;
	private static final String erreur_sauvegarde;
	private static final String crochets="[ ]";
	private static final String chapeau="^(";
	private static final String pipe="|";
	private static final String plus=")+(";
	private static final String parentheses=")(";
	private static final String fin_etoile=")*$";
	private static final String fin_plus=")+$";
	private static final String avertissement="Avertissement";
	private static final String avertissement_fichier="Le fichier ";
	private static final String avertissement_existe=" existe deja,"+st;
	private static final String avertissement_ecrase="voulez-vous l'ecraser?";
	private static String titre_erreur="Erreur";
	private static String titre_erreur_sauvegarde="Erreur de sauvegarde";
	public static String titre_sauvegarde=sauvegarder+" une partie";
	public static String titre_chargement="Charger une partie";
	static{
		String erreur_sauvegarde_1="Les caracteres"+st;
		String erreur_sauvegarde_2="1 < > ? \" * / \\ | : ."+st;
		String erreur_sauvegarde_3="2 espace en debut de chaine"+st;
		String erreur_sauvegarde_4="3 tabulation"+st;
		String erreur_sauvegarde_5="ne sont pas autorises";
		erreur_sauvegarde=erreur_sauvegarde_1+erreur_sauvegarde_2+erreur_sauvegarde_3+erreur_sauvegarde_4+erreur_sauvegarde_5;
	}
	public static String titre_suppression=Fenetre.sous_menus_utilises1[4];
	private DialogueFichiers(Frame fenetre,String titre)
	{
		super(fenetre,titre,true);
		setResizable(false);
		setLocationRelativeTo(fenetre);
	}
	/**Chargement d'un fichier ou suppression de fichier(s)*/
	public DialogueFichiers(Fenetre fenetre,String titre)
	{
		this((Frame)fenetre,titre);
		initChoixSupprimerCharger();
		setVisible(titre.startsWith(titre_suppression));
	}
	/**Sauvegarder une partie editee*/
	public DialogueFichiers(Editeur editeur,String[] dossiers)
	{
		super(editeur,titre_sauvegarde,true);
		setResizable(false);
		setLocationRelativeTo(editeur);
		initChoixSauvegardes(dossiers);
	}
	/**Sauvegarder une partie jouee*/
	public DialogueFichiers(Fenetre fenetre,String titre,String[] dossiers)
	{
		this((Frame)fenetre,titre);
		initChoixSauvegardes(dossiers);
		setVisible(true);
	}
	private void initChoixSauvegardes(String[] dossiers)
	{
		String chemin=dossiers[0].replace(es,_);
		String nom_dossier;
		dossiers_ouverts.addElement(Fichier.dossier_sauvegarde);
		dossiers_ouverts.addElement(chemin);
		for(int indice_dossier=1;indice_dossier<dossiers.length;indice_dossier++)
		{
			nom_dossier=dossiers[indice_dossier].replace(es,_);
			chemin+=File.separator+nom_dossier;
			dossiers_ouverts.addElement(nom_dossier);
		}
		File f=new File(Fichier.dossier_sauvegarde+File.separator+chemin);
		File[] fichiers=f.listFiles();
		Vector<String> noms_fichiers=new Vector<String>();
		Vector<Date> dates_modif=new Vector<Date>();
		Vector<Long> tailles=new Vector<Long>();
		String nom_fichier;
		JTextArea jta=new JTextArea(ch_v);
		jta.setEditable(false);
		jta.setFocusable(false);
		for (File fichier_2:fichiers)
		{
			nom_fichier=fichier_2.getName();
			if(nom_fichier.endsWith(Fichier.extension_partie))
			{
				noms_fichiers.addElement(nom_fichier.split(anti_slash_point)[0]);
				dates_modif.addElement(derniere_modif(fichier_2));
				tailles.addElement(fichier_2.length());
			}
		}
		int nombre_sauvegardes=noms_fichiers.size();
		for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
		{
			for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
			{
				if(noms_fichiers.get(indice_fichier2).compareTo(noms_fichiers.get(indice_fichier))<0)
				{
					noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
					dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
					tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
				}
			}
		}
		for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
		{
			jta.append(noms_fichiers.get(indice_fichier)+Fichier.extension_partie+separateur+dates_modif.get(indice_fichier)+separateur+tailles.get(indice_fichier)+unite+st);
		}
		jta.setRows(4);
		Container c=new Container();
		c.setLayout(new BorderLayout());
		c.add(new JLabel(Fichier.dossier_sauvegarde+File.separator+chemin,JLabel.CENTER),BorderLayout.NORTH);
		JPanel panneau=new JPanel(new BorderLayout());
		JPanel panneau2=new JPanel();
		JButton bouton;
		bouton=new JButton(nom+inf);
		bouton.addActionListener(new EcouteBouton(bouton.getText()));
		panneau2.add(bouton);
		bouton=new JButton(date+inf);
		bouton.addActionListener(new EcouteBouton(bouton.getText()));
		panneau2.add(bouton);
		bouton=new JButton(taille+inf);
		bouton.addActionListener(new EcouteBouton(bouton.getText()));
		panneau2.add(bouton);
		panneau.add(panneau2,BorderLayout.NORTH);
		panneau.add(new JScrollPane(jta),BorderLayout.CENTER);
		c.add(panneau,BorderLayout.CENTER);
		bouton=new JButton(sauvegarder);
		bouton.addActionListener(new EcouteBouton(bouton.getText()));
		panneau=new JPanel(new GridLayout(0,2));
		JTextField jtf=new JTextField(20);
		jtf.addKeyListener(new EcouteClavier(bouton.getText()));
		panneau.add(jtf);
		panneau.add(bouton);
		bouton=new JButton(annuler);
		bouton.addActionListener(new EcouteBouton(bouton.getText()));
		panneau.add(new JLabel(description,JLabel.CENTER));
		panneau.add(bouton);
		c.add(panneau,BorderLayout.SOUTH);
		setContentPane(c);
		pack();
		addWindowListener(new FermetureFenetre());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	private void initChoixSupprimerCharger()
	{
		Container c=new Container();
		c.setLayout(new BorderLayout());
		JPanel panneau=new JPanel();
		panneau.setLayout(new GridLayout(0,1));
		panneau.add(new JLabel(new File(Fichier.dossier_sauvegarde).getName(),JLabel.CENTER));
		JPanel panneau3=new JPanel();
		JPanel panneau2=new JPanel();
		JButton bouton;
		bouton=new JButton(nom+inf);
		bouton.addActionListener(new EcouteBouton(bouton.getText()));
		panneau2.add(bouton);
		bouton=new JButton(date+inf);
		bouton.addActionListener(new EcouteBouton(bouton.getText()));
		panneau2.add(bouton);
		bouton=new JButton(taille+inf);
		bouton.addActionListener(new EcouteBouton(bouton.getText()));
		panneau2.add(bouton);
		panneau3.add(panneau2);
		panneau.add(panneau3);
		c.add(panneau,BorderLayout.NORTH);
		PanneauListeFichiers panneau_liste=new PanneauListeFichiers(Fichier.dossier_sauvegarde,getTitle().startsWith(titre_chargement));
		if(getTitle().startsWith(titre_chargement))
		{
			panneau_liste.getListeFichiers().addListSelectionListener(new EcouteurListe());
		}
		c.add(panneau_liste, BorderLayout.CENTER);
		DefaultMutableTreeNode root=new DefaultMutableTreeNode(Fichier.dossier_sauvegarde);
		for(Jeu jeu:Jeu.values())
		{
			DefaultMutableTreeNode root_2=new DefaultMutableTreeNode(jeu.toString().replace(Lettre.ea,Lettre.e));
			for(String mode:Jouer.modes1.get(jeu.ordinal()))
			{
				DefaultMutableTreeNode root_3=new DefaultMutableTreeNode(mode);
				if(jeu==Jeu.Tarot)
				{
					for(String mode2:Jouer.modes2)
					{
						DefaultMutableTreeNode root_4=new DefaultMutableTreeNode(mode2);
						for(Type t:Type.values())
						{
							root_4.add(new DefaultMutableTreeNode(t.toString()));
						}
						root_3.add(root_4);
					}
				}
				else
				{
					for(Type t:Type.values())
					{
						root_3.add(new DefaultMutableTreeNode(t.toString()));
					}
				}
				root_2.add(root_3);
			}
			root.add(root_2);
		}
		JTree arbre=new JTree(root);
		arbre.addTreeSelectionListener(new EcouteCliqueArbre());
		c.add(new JScrollPane(arbre),BorderLayout.WEST);
		panneau=new JPanel();
		if(getTitle().startsWith(titre_chargement))
		{
			bouton=new JButton(annuler);
			bouton.addActionListener(new EcouteBouton(bouton.getText()));
			panneau.add(bouton);
			c.add(panneau,BorderLayout.SOUTH);
		}
		else
		{
			bouton=new JButton(annuler);
			bouton.addActionListener(new EcouteBouton(bouton.getText()));
			panneau.add(bouton);
			bouton=new JButton(supprimer);
			bouton.addActionListener(new EcouteBouton(bouton.getText()));
			panneau.add(bouton);
			c.add(panneau,BorderLayout.SOUTH);
		}
		setContentPane(c);
		addWindowListener(new FermetureFenetre());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
	}
	private class EcouteCliqueArbre implements TreeSelectionListener
	{
		public void valueChanged(TreeSelectionEvent e) {
			JTree arbre=(JTree)((JScrollPane)getContentPane().getComponent(2)).getViewport().getComponent(0);
			Object o=arbre.getLastSelectedPathComponent();
			if(o!=null)
			{
				dossiers_ouverts=new Vector<String>();
				for(Object o2:e.getPath().getPath())
				{
					dossiers_ouverts.addElement(o2.toString());
				}
				ouvrirDossier();
			}
		}
	}
	private void setTexte(String f)
	{
		fichier=f;
	}
	public String getTexte()
	{
		setVisible(true);
		if(fichier!=null)
		{
			return fichier;
		}
		return ch_v;
	}
	/**On donne le nom du fichier ainsi que le dossier que le contient*/
	public String[] getPartie()
	{
		String[] infos=new String[2];
		infos[1]=getTexte();
		infos[0]=dossiers_ouverts.isEmpty()?ch_v:dossiers_ouverts.get(0);
		for(int indice_dossier=1;indice_dossier<dossiers_ouverts.size();indice_dossier++)
		{
			infos[0]+=File.separator+dossiers_ouverts.get(indice_dossier);
		}
		return infos;
	}
	/**Grace a cet ecouteur on n'a a cliquer qu'une fois pour ouvrir un dossier, charger un fichier ou supprimer un fichier*/
	private class EcouteurListe implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting())
			{
				fichier=((PanneauListeFichiers)getContentPane().getComponent(1)).getFichierSelectionne();
				dispose();
			}
		}
	}
	/**Appelee lorsqu'on cherche a ouvrir un dossier*/
	private void ouvrirDossier()
	{
		String dossier=dossiers_ouverts.get(0);
		for(int indice_dossier=1;indice_dossier<dossiers_ouverts.size();indice_dossier++)
		{
			dossier+=File.separator+dossiers_ouverts.get(indice_dossier);
		}
		PanneauListeFichiers p2=(PanneauListeFichiers)getContentPane().getComponent(1);
		p2.modifierLeContenu(dossier);
		((JLabel)((JPanel)getContentPane().getComponent(0)).getComponent(0)).setText(dossier);
		pack();
	}
	public String fichier()
	{
		return fichier;
	}
	private void supprimerFichier()
	{
		String[] fichiers=((PanneauListeFichiers)getContentPane().getComponent(1)).getFichiersSelectionnes();
		String dossier=((JLabel)((JPanel)getContentPane().getComponent(0)).getComponent(0)).getText();
		if(fichiers!=null)
		{
			((PanneauListeFichiers)getContentPane().getComponent(1)).supprimerFichiers(fichiers,dossier);
		}
		else
		{
			JOptionPane.showMessageDialog(this,message_erreur,titre_erreur,JOptionPane.ERROR_MESSAGE);
		}
		pack();
	}
	private void choixBouton(String texte)
	{
		if(texte.startsWith(nom)||texte.startsWith(date)||texte.startsWith(taille))
		{
			String nom_dossier=dossiers_ouverts.get(0);
			for(int indice_dossier=1;indice_dossier<dossiers_ouverts.size();indice_dossier++)
			{
				nom_dossier+=File.separator+dossiers_ouverts.get(indice_dossier);
			}
			trier(texte,texte.endsWith(sup),new File(nom_dossier));
		}
		else if(texte.equals(supprimer))
		{//On supprime un ou plusieurs fichiers
			supprimerFichier();
		}
		else if(texte.equals(annuler))
		{
			fichier=ch_v;
			dispose();
		}
		else if(texte.equals(sauvegarder))
		{
			String dossier=((JLabel)getContentPane().getComponent(0)).getText();
			String regulier;
			Window possesseur=getOwner();
			if(possesseur instanceof Fenetre)
			{
				regulier=((Fenetre)possesseur).getInfos().get(0).lastElement().split(p_v)[1];
			}
			else
			{
				regulier=((Fenetre)possesseur.getOwner()).getInfos().get(0).lastElement().split(p_v)[1];
			}
			Pattern pattern=Pattern.compile(chapeau+regulier+parentheses+crochets+pipe+regulier+fin_etoile);
			Pattern pattern2=Pattern.compile(chapeau+crochets+plus+regulier+fin_plus);
			String texteFichier=((JTextField)((JPanel)getContentPane().getComponent(2)).getComponent(0)).getText();
			boolean convient=pattern.matcher(texteFichier).matches();
			if(convient||pattern2.matcher(texteFichier).matches())
			{
				File fichier_sauvegarder;
				if(!convient)
				{
					for(;texteFichier.charAt(0)==es;texteFichier=texteFichier.substring(1));
				}
				//On elimine les espaces successifs dans la chaine jusqu'au premier caractere sans espace
				fichier_sauvegarder=new File(dossier+File.separator+texteFichier+Fichier.extension_partie);
				setTexte(texteFichier);
				if(!fichier_sauvegarder.exists())
				{
					//enregistrement d'une partie dans un fichier non existant
					if(possesseur instanceof Fenetre)
						((Fenetre)possesseur).getPartie().sauvegarder(dossier+File.separator+texteFichier+Fichier.extension_partie);
					dispose();
				}
				else
				{
					String message=avertissement_fichier+texteFichier+Fichier.extension_partie+avertissement_existe;
					message+=avertissement_ecrase;
					int choix=JOptionPane.showConfirmDialog(this,message,avertissement, JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
					if(choix==JOptionPane.YES_OPTION)
					{
						if(possesseur instanceof Fenetre)
							((Fenetre)possesseur).getPartie().sauvegarder(dossier+File.separator+texteFichier+Fichier.extension_partie);
						dispose();
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this,erreur_sauvegarde,titre_erreur_sauvegarde,JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	private class FermetureFenetre extends WindowAdapter{
		public void windowClosing(WindowEvent e) {
			fichier=ch_v;
			dispose();
		}
	}
	private class EcouteClavier extends KeyAdapter{
		private String texte;
		private EcouteClavier(String txt) {
			texte=txt;
		}
		private void taper(KeyEvent e)
		{
			if(e.getKeyCode()==KeyEvent.VK_ENTER)
			{//On modifie la fenetre de gestions des fichiers en fonction du nom du bouton
				choixBouton(texte);
			}
		}
		public void keyReleased(KeyEvent e) {
			taper(e);
		}
		public void keyTyped(KeyEvent e) {
			taper(e);
		}
	}
	private class EcouteBouton implements ActionListener{
		private String texte;
		private EcouteBouton(String txt) {
			texte=txt;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			choixBouton(texte);
			if(texte.endsWith(sup))
			{
				if(texte.startsWith(nom))
				{
					passage=false;
					texte=nom+inf;
				}
				else if(texte.startsWith(date))
				{
					passage=false;
					texte=date+inf;
				}
				else
				{
					passage=true;
					texte=taille+inf;
				}
			}
			else if(texte.endsWith(inf))
			{
				if(texte.startsWith(nom))
				{
					passage=false;
					texte=nom+sup;
				}
				else if(texte.startsWith(date))
				{
					passage=false;
					texte=date+sup;
				}
				else
				{
					passage=true;
					texte=taille+sup;
				}
			}
		}
	}
	/**Trie les fichiers de sauvegarde de partie soit par nom soit par date
	 * dans l'ordre croissant ou decroissant
	 * @param ordre_tri est une chaine prenant les valeurs Nom, Date et Taille
	 * par ordre chronologique
	 * @param croissant est vrai si et seulement si pour un tri de nom on suit le sens a -> z ou pour un tri de date
	 * on part du plus ancien au plus recent
	 * @param dossier le dossier dans lequel il faut trier les fichiers*/
	private void trier(String ordre_tri,boolean croissant,File dossier)
	{
		File[] fichiers=dossier.listFiles();
		JPanel panneau=(JPanel)getContentPane().getComponent(1);
		JButton bouton;
		String nom_fichier;
		Vector<String> noms_fichiers=new Vector<String>();
		Vector<Date> dates_modif=new Vector<Date>();
		Vector<String> resultats=new Vector<String>();
		Vector<Long> tailles=new Vector<Long>();
		JPanel panneau2;
		int nombre_sauvegardes;
		for(File fichier_2:fichiers)
		{
			nom_fichier=fichier_2.getName();
			if(nom_fichier.endsWith(Fichier.extension_partie))
			{
				noms_fichiers.addElement(nom_fichier.split(anti_slash_point)[0]);
				dates_modif.addElement(derniere_modif(fichier_2));
				tailles.addElement(fichier_2.length());
			}
		}
		if(getTitle().equals(DialogueFichiers.titre_sauvegarde))
		{
			panneau2=(JPanel)panneau.getComponent(0);
		}
		else
		{
			panneau2=(JPanel)((JPanel)((JPanel)getContentPane().getComponent(0)).getComponent(1)).getComponent(0);
		}
		nombre_sauvegardes=noms_fichiers.size();
		if(ordre_tri.startsWith(nom))
		{
			if(passage)
			{
				bouton=(JButton)panneau2.getComponent(2);
				boolean local=bouton.getText().endsWith(inf);
				if(local)
				{/*Ordre croissant de la taille des fichiers*/
					if(croissant)
					{
						for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
						{
							for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
							{
								if(tailles.get(indice_fichier)>tailles.get(indice_fichier2))
								{
									noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
									dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
									tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
								}
								else if(tailles.get(indice_fichier)==tailles.get(indice_fichier2))
								{
									if(noms_fichiers.get(indice_fichier2).compareTo(noms_fichiers.get(indice_fichier))<0)
									{
										noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
										dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
										tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
									}
								}
							}
						}
					}
					else
					{
						for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
						{
							for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
							{
								if(tailles.get(indice_fichier)>tailles.get(indice_fichier2))
								{
									noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
									dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
									tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
								}
								else if(tailles.get(indice_fichier)==tailles.get(indice_fichier2))
								{
									if(noms_fichiers.get(indice_fichier2).compareTo(noms_fichiers.get(indice_fichier))>0)
									{
										noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
										dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
										tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
									}
								}
							}
						}
					}
				}
				else
				{
					if(croissant)
					{
						for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
						{
							for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
							{
								if(tailles.get(indice_fichier)<tailles.get(indice_fichier2))
								{
									noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
									dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
									tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
								}
								else if(tailles.get(indice_fichier)==tailles.get(indice_fichier2))
								{
									if(noms_fichiers.get(indice_fichier2).compareTo(noms_fichiers.get(indice_fichier))<0)
									{
										noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
										dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
										tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
									}
								}
							}
						}
					}
					else
					{
						for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
						{
							for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
							{
								if(tailles.get(indice_fichier)<tailles.get(indice_fichier2))
								{
									noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
									dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
									tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
								}
								else if(tailles.get(indice_fichier)==tailles.get(indice_fichier2))
								{
									if(noms_fichiers.get(indice_fichier2).compareTo(noms_fichiers.get(indice_fichier))>0)
									{
										noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
										dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
										tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
									}
								}
							}
						}
					}
				}
			}
			else
			{
				if(croissant)
				{
					for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
					{
						for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
						{
							if(noms_fichiers.get(indice_fichier2).compareTo(noms_fichiers.get(indice_fichier))<0)
							{
								noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
								dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
								tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
							}
						}
					}
				}
				else
				{
					for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
					{
						for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
						{
							if(noms_fichiers.get(indice_fichier2).compareTo(noms_fichiers.get(indice_fichier))>0)
							{
								noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
								dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
								tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
							}
						}
					}
				}
			}
		}
		else if(ordre_tri.startsWith(date))
		{
			if(passage)
			{
				bouton=(JButton)panneau2.getComponent(2);
				boolean local=bouton.getText().endsWith(inf);
				if(local)
				{/*Ordre croissant de la taille des fichiers*/
					if(croissant)
					{
						for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
						{
							for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
							{
								if(tailles.get(indice_fichier)>tailles.get(indice_fichier2))
								{
									noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
									dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
									tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
								}
								else if(tailles.get(indice_fichier)==tailles.get(indice_fichier2))
								{
									if(dates_modif.get(indice_fichier2).plusVieuxQue(dates_modif.get(indice_fichier)))
									{
										noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
										dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
										tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
									}
								}
							}
						}
					}
					else
					{
						for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
						{
							for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
							{
								if(tailles.get(indice_fichier)>tailles.get(indice_fichier2))
								{
									noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
									dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
									tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
								}
								else if(tailles.get(indice_fichier)==tailles.get(indice_fichier2))
								{
									if(dates_modif.get(indice_fichier).plusVieuxQue(dates_modif.get(indice_fichier2)))
									{
										noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
										dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
										tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
									}
								}
							}
						}
					}
				}
				else
				{
					if(croissant)
					{
						for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
						{
							for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
							{
								if(tailles.get(indice_fichier)<tailles.get(indice_fichier2))
								{
									noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
									dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
									tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
								}
								else if(tailles.get(indice_fichier)==tailles.get(indice_fichier2))
								{
									if(dates_modif.get(indice_fichier2).plusVieuxQue(dates_modif.get(indice_fichier)))
									{
										noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
										dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
										tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
									}
								}
							}
						}
					}
					else
					{
						for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
						{
							for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
							{
								if(tailles.get(indice_fichier)<tailles.get(indice_fichier2))
								{
									noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
									dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
									tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
								}
								else if(tailles.get(indice_fichier)==tailles.get(indice_fichier2))
								{
									if(dates_modif.get(indice_fichier).plusVieuxQue(dates_modif.get(indice_fichier2)))
									{
										noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
										dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
										tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
									}
								}
							}
						}
					}
				}
			}
			else
			{
				if(croissant)
				{
					for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
					{
						for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
						{
							if(dates_modif.get(indice_fichier2).plusVieuxQue(dates_modif.get(indice_fichier)))
							{
								noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
								dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
								tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
							}
						}
					}
				}
				else
				{
					for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
					{
						for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
						{
							if(dates_modif.get(indice_fichier).plusVieuxQue(dates_modif.get(indice_fichier2)))
							{
								noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
								dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
								tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
							}
						}
					}
				}
			}
		}
		else
		{
			if(croissant)
			{
				for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
				{
					for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
					{
						if(tailles.get(indice_fichier)>tailles.get(indice_fichier2))
						{
							noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
							dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
							tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
						}
					}
				}
			}
			else
			{
				for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
				{
					for(int indice_fichier2=indice_fichier+1;indice_fichier2<nombre_sauvegardes;indice_fichier2++)
					{
						if(tailles.get(indice_fichier)<tailles.get(indice_fichier2))
						{
							noms_fichiers.setElementAt(noms_fichiers.set(indice_fichier,noms_fichiers.get(indice_fichier2)),indice_fichier2);
							dates_modif.setElementAt(dates_modif.set(indice_fichier, dates_modif.get(indice_fichier2)),indice_fichier2);
							tailles.setElementAt(tailles.set(indice_fichier, tailles.get(indice_fichier2)),indice_fichier2);
						}
					}
				}
			}
		}
		if(getTitle().equals(DialogueFichiers.titre_sauvegarde))
		{
			for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
			{
				resultats.addElement(noms_fichiers.get(indice_fichier)+Fichier.extension_partie+separateur+dates_modif.get(indice_fichier)+separateur+tailles.get(indice_fichier)+unite);
			}
			if(ordre_tri.startsWith(nom))
			{
				bouton=(JButton)panneau2.getComponent(0);
				if(croissant)
				{
					bouton.setText(nom+inf);
				}
				else
				{
					bouton.setText(nom+sup);
				}
			}
			else if(ordre_tri.startsWith(date))
			{
				bouton=(JButton)panneau2.getComponent(1);
				if(croissant)
				{
					bouton.setText(date+inf);
				}
				else
				{
					bouton.setText(date+sup);
				}
			}
			else
			{
				bouton=(JButton)panneau2.getComponent(2);
				if(croissant)
				{
					bouton.setText(taille+inf);
				}
				else
				{
					bouton.setText(taille+sup);
				}
			}
			JTextArea zone=(JTextArea)((JScrollPane)panneau.getComponent(1)).getViewport().getComponent(0);
			zone.setText(ch_v);
			for(String ligne:resultats)
			{
				zone.append(ligne+st);
			}
		}
		else
		{
			for(int indice_fichier=0;indice_fichier<nombre_sauvegardes;indice_fichier++)
			{
				resultats.addElement(noms_fichiers.get(indice_fichier)+Fichier.extension_partie+separateur+Lettre.es+Lettre.es+Lettre.es+dates_modif.get(indice_fichier)+Lettre.es+Lettre.es+Lettre.es+tailles.get(indice_fichier)+unite);
			}
			if(ordre_tri.startsWith(nom))
			{
				bouton=(JButton)panneau2.getComponent(0);
				if(croissant)
				{
					bouton.setText(nom+inf);
				}
				else
				{
					bouton.setText(nom+sup);
				}
			}
			else if(ordre_tri.startsWith(date))
			{
				bouton=(JButton)panneau2.getComponent(1);
				if(croissant)
				{
					bouton.setText(date+inf);
				}
				else
				{
					bouton.setText(date+sup);
				}
			}
			else
			{
				bouton=(JButton)panneau2.getComponent(2);
				if(croissant)
				{
					bouton.setText(taille+inf);
				}
				else
				{
					bouton.setText(taille+sup);
				}
			}
			PanneauListeFichiers fichiers_liste=(PanneauListeFichiers)getContentPane().getComponent(1);
			fichiers_liste.supprimerToutesLignes();
			fichiers_liste.ajouterLignes(resultats);
		}
	}
	/**Calcule la date de modification d'un fichier passe en parametre dans le fuseau horaire de la France
	 * @param f le fichier pour lequel il faut recuperer la date de modification a la milliseconde pres
	 * @return la date de modification du fichier*/
	public static Date derniere_modif(File f)
	{
		long temps=f.lastModified();
		int[] mois_nb_jour;
		int millisecondes=(int)(temps%1000L);
		int secondes=(int)((temps/1000L)%60L);
		int minutes=(int)((temps/60000L)%60L);
		int heures=(int)((temps/3600000L)%24L);
		long total_jours=temps/86400000L;
		int ans=0;
		int an=0;
		int jour=0;
		int mois=0;
		int indice_mois=0;
		int nb_annee_bis=0;
		int nb_annee=0;
		int numero_jour=0;
		int position_jour=5;
		for(;ans<31;ans++)
		{
			if(ans%4==2)
			{
				total_jours-=366L;
			}
			else
			{
				total_jours-=365L;
			}
		}
		ans=2001;
		for(;total_jours>365L;ans++)
		{
			if(ans%4==0&&ans%100!=0||ans%400==0)
			{
				total_jours-=366L;
			}
			else
			{
				total_jours-=365L;
			}
		}
		if(total_jours==365L&&(ans%4!=0||ans%100==0&&ans%400!=0))
		{
			ans++;
			total_jours=0;
		}
		an=ans;
		mois_nb_jour=new int[]{31,(ans%4==0&&ans%100!=0||ans%400==0)?29:28,31,30,31,30,31,31,30,31,30,31};
		for(;mois<12;mois++)
		{
			if(total_jours<mois_nb_jour[mois])
			{
				jour=(int)(total_jours+1);
				break;
			}
			total_jours-=mois_nb_jour[mois];
		}
		ans=2000;
		for(;ans<an;ans++)
		{
			if(ans%4==0&&ans%100!=0||ans%400==0)
			{
				nb_annee_bis++;
			}
		}
		nb_annee=an-2000;
		position_jour=nb_annee+nb_annee_bis+position_jour;
		for(;indice_mois<mois;indice_mois++)
		{
			numero_jour+=mois_nb_jour[indice_mois];
		}
		numero_jour+=jour-1;
		position_jour=(position_jour+numero_jour)%7;
		return new Date(an,mois,jour,heures,minutes,secondes,millisecondes,mois_nb_jour[mois],position_jour);
	}
}