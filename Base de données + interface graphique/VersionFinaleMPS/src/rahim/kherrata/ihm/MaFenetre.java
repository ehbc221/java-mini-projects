package rahim.kherrata.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import rahim.kherrata.connexion.MaConnexion;

public class MaFenetre extends JFrame {
	static long time1 = (new Date()).getTime();
	static long time2 = (new Date()).getTime();
	  /**
	  * ToolBar pour le lancement des requêtes
	  */
	  private JToolBar tool = new JToolBar();
	 
	  /**
	  * Le bouton
	  */
	  private JButton reqExu = new JButton();
	  private JButton reqEffac = new JButton();
	  private JButton b = new JButton("Liste des elèves");
	  private JButton b1 = new JButton("Liste des cours");
	  private JButton b2 = new JButton("Liste des professeurs");
	  private JButton b3 = new JButton("Liste des activités");
	  private JButton b4 = new JButton("Les resultats");
	  private JButton b5 = new JButton("Les activités pratiquées");
	  private JButton b6 = new JButton("Liste des cours et professeurs");
	  
	  private JButton b01 = new JButton("table elèves");
	  private JButton b11 = new JButton("table des cours");
	  private JButton b21 = new JButton("table des professeurs");
	  private JButton b31 = new JButton("table des activités");
	  private JButton b41 = new JButton("table resultats");
	  private JButton b51 = new JButton("table activités pratiquées");
	  private JButton b61 = new JButton("table Charger");
	  
	  private JButton b010 = new JButton();
	  private JButton b111 = new JButton();
	  private JButton b212 = new JButton();
	  private JButton b313 = new JButton();
	  private JButton b414 = new JButton();
	  private JButton b515 = new JButton();
	  private JButton b616 = new JButton();
	  private JButton b117 = new JButton();
	  private JButton b218 = new JButton();
	  private JButton b319 = new JButton();
	  private JButton b4110 = new JButton();
	  private JButton b5111 = new JButton();
	  private JButton b6113 = new JButton();
	  private JButton b1114 = new JButton();
	  private JButton b2115 = new JButton();
	  private JButton b3116 = new JButton();
	 
	  /**
	  * Le délimiteur
	  */
	  private JSplitPane split, split1;
	 
	  /**
	  * Le conteneur de résultat
	  */
	  private JPanel result = new JPanel();
	  private JLabel img = new JLabel(new ImageIcon("images/plus.png"));
	  private JButton lab12 = new JButton();
	  /**
	   * Le conteneur de recherche
	  */
	private JTabbedPane onglet = new JTabbedPane();
	
	private JPanel recherche = new JPanel();
	private JPanel recherche1 = new JPanel();
	private JPanel recherche2 = new JPanel();
	private JPanel recherche3 = new JPanel();
	private JPanel operation = new JPanel();
	
	private int i = 0;
	
	/**
	   * jlapel de haut
	  */
	private JPanel panH = new JPanel();
	  /**
	  * Requête par défaut pour le démarrage
	  */
	 
	  private String requete = "SELECT  * FROM eleves order by  nom asc";
	  private String requete1 = "SELECT  * FROM cours order by nomcours asc";
	  private String requete2 = "SELECT  * FROM Professeurs order by nomprof asc";
	  private String requete3 = "SELECT  * FROM ACTIVITES";
	  private String requete4 = "SELECT  * FROM RESULTATS";
	  private String requete5 = "SELECT  * FROM ACTIVITESPRATIQUES";
	  private String requete6 = "SELECT  * FROM CHARGE";
	 
	  
	  private String requete01 = "SELECT COLUMN_NAME as 'NOM de colonne', DATA_TYPE as 'TYPE', CHARACTER_MAXIMUM_LENGTH as 'TAILLE' FROM information_schema.COLUMNS WHERE table_name = 'eleves'";
	  private String requete11 = "SELECT COLUMN_NAME as 'NOM de colonne', DATA_TYPE as 'TYPE', CHARACTER_MAXIMUM_LENGTH as 'TAILLE' FROM information_schema.COLUMNS WHERE table_name = 'cours'";
	  private String requete21 = "SELECT COLUMN_NAME as 'NOM de colonne', DATA_TYPE as 'TYPE', CHARACTER_MAXIMUM_LENGTH as 'TAILLE' FROM information_schema.COLUMNS WHERE table_name = 'professeurs' ";
	  private String requete31 = "SELECT COLUMN_NAME as 'NOM de colonne', DATA_TYPE as 'TYPE', CHARACTER_MAXIMUM_LENGTH as 'TAILLE' FROM information_schema.COLUMNS WHERE table_name = 'Activites' ";
	  private String requete41 = "SELECT COLUMN_NAME as 'NOM de colonne', DATA_TYPE as 'TYPE', CHARACTER_MAXIMUM_LENGTH as 'TAILLE' FROM information_schema.COLUMNS WHERE table_name = 'resultats' ";
	  private String requete51 = "SELECT COLUMN_NAME as 'NOM de colonne', DATA_TYPE as 'TYPE', CHARACTER_MAXIMUM_LENGTH as 'TAILLE' FROM information_schema.COLUMNS WHERE table_name = 'ACTIVITESPRATIQUES' ";
	  private String requete61 = "SELECT COLUMN_NAME as 'NOM de colonne', DATA_TYPE as 'TYPE', CHARACTER_MAXIMUM_LENGTH as 'TAILLE' FROM information_schema.COLUMNS WHERE table_name = 'CHARGE' ";
	  
	  private String requetett1 = "select  NOM, PRENOM, DATENAISSANCE from ELEVES";
	  private String requetett2 = "select *from ACTIVITES ";
	  private String requetett3 = "select DISTINCT SPECIALITE from PROFESSEURS";
	  private String requetett4 = "select NOM,prenom from ELEVES,ACTIVITESPRATIQUES ,ACTIVITES \n where \n poids < '50' \n and ELEVES.NUMELEVE = ACTIVITESPRATIQUES.NUMELEVE \n and ACTIVITESPRATIQUES.NIVEAU=ACTIVITES.NIVEAU \n and (ACTIVITES.NIVEAU='premier année' or ACTIVITES.NIVEAU='deuxieme année')";
	  private String requetett5 = "select NUMELEVE,NOM,PRENOM from ELEVES \n where \n poids between '50'and '60'   ";
	  private String requetett6 = " select * from activ_max \n where \n total_inscrit = (select MAX(total_inscrit) from activ_max)";
	  private String requetett7 = "select * from cours_max \n where \n nbr_total =  (select MAX(nbr_total) from cours_max)    ";
	  private String requetett8 = " select EQUIPE ,NOM,PRENOM ,poids from ACTIVITES ,ACTIVITESPRATIQUES,ELEVES \n where \n ELEVES.NUMELEVE=ACTIVITESPRATIQUES.NUMELEVE \n and \n ACTIVITES.NIVEAU = ACTIVITESPRATIQUES.NIVEAU  \n and \n ELEVES.PRENOM = 'ali'";
	  private String requetett9 = "select  PROFESSEURS.SALAIRE ,PROFESSEURS.NOMPROF,PROFESSEURS.NUMPROF from nbr_cours ,PROFESSEURS \n where \n nombr_cour = '2' \n and \n PROFESSEURS.NUMPROF = nbr_cours.NUMPROF ";
	  private String requetett10 = "select PROFESSEURS.SPECIALITE,PROFESSEURS.NUMPROF,PROFESSEURS.NOMPROF, nbr_heures_max from  charge_lourd4  ,PROFESSEURS \n where \n PROFESSEURS.NUMPROF=charge_lourd4.NUMPROF \n and \n nbr_heures_max=(select MAX(nbr_heures_max) from charge_lourd4)";
	  private String requetett11 = "select COURS.NOMCOURS from COURS ,ELEVES ,RESULTATS, ACTIVITESPRATIQUES where ELEVES.NUMELEVE=ACTIVITESPRATIQUES.NUMELEVE and COURS.NUMCOURS = RESULTATS.NUMCOURS \n  and \n RESULTATS.NUMELEVE =ELEVES.NUMELEVE \n and \n ACTIVITESPRATIQUES.NOMACTPRATIQUE='football'";
	  private String requetett12 = " select ELEVES.NUMELEVE, ELEVES.NOM, ELEVES.PRENOM ,RESULTATS.POINTES,RESULTATS.NUMCOURS from ELEVES , RESULTATS \n where \n ELEVES.NUMELEVE = RESULTATS.NUMELEVE \n and \n POINTES =(select MAX(POINTES) from RESULTATS ,COURS \n where \n RESULTATS.NUMCOURS = COURS.NUMCOURS \n and \n COURS.NUMCOURS ='14m01') \n and \n RESULTATS.NUMCOURS  ='14m01'";
	  private String requetett13 = "select NOMCOURS , COUNT(ELEVES.NUMELEVE)as nbr_inscrit from COURS ,ELEVES , RESULTATS \n where \n ELEVES.NUMELEVE =RESULTATS.NUMELEVE \n and \n RESULTATS.NUMCOURS=COURS.NUMCOURS \n GROUP BY NOMCOURS";
	  private String requetett14 = "select ELEVES.NUMELEVE,ELEVES.NOM,ELEVES.PRENOM,ACTIVITESPRATIQUES.NOMACTPRATIQUE from ELEVES ,ACTIVITESPRATIQUES \n where \n poids between '50' and '60' \n and \n ELEVES.NUMELEVE =ACTIVITESPRATIQUES.NUMELEVE \n and \n ACTIVITESPRATIQUES.NOMACTPRATIQUE='gymnastique' ";
	  private String requetett15 = "select COURS.NUMCOURS,COURS.NOMCOURS from COURS ,ELEVES,RESULTATS \n where\n  ELEVES.NUMELEVE = RESULTATS.NUMELEVE \n and \n RESULTATS.NUMCOURS = COURS.NUMCOURS \n and \n COURS.ANNEE='2014' \n and \n ELEVES.PRENOM ='kamel'";
	  private String requetett16 = "select distinct * from ELEVES,intervalle_rrr";
	  /**
	  * Le composant dans lequel taper la requête
	  */
	  private JTextArea text = new JTextArea();
	
	    
	  JMenuBar barMenu = new JMenuBar();
	  JMenu fichierM = new JMenu();
	  JMenu affichageM = new JMenu();
	  JMenu apropos = new JMenu();
	  JMenu editionM = new JMenu();
	  JMenuItem quit = new JMenuItem(); 
	  JMenu structT = new JMenu();
	  JMenu contentT = new JMenu(); 
	  JMenu nouveauT = new JMenu(); 
	  JMenuItem s1 = new JMenuItem();
	  JMenuItem s2 = new JMenuItem();
	  JMenuItem s3 = new JMenuItem();
	  JMenuItem s4 = new JMenuItem();
	  JMenuItem s5 = new JMenuItem();
	  JMenuItem s6 = new JMenuItem();
	  JMenuItem s7 = new JMenuItem();
	  JMenuItem c1 = new JMenuItem();
	  JMenuItem c2 = new JMenuItem();
	  JMenuItem c3 = new JMenuItem();
	  JMenuItem c4 = new JMenuItem();
	  JMenuItem c5 = new JMenuItem();
	  JMenuItem c6 = new JMenuItem();
	  JMenuItem c7 = new JMenuItem();
	  JMenuItem ap = new JMenuItem();
	  JMenuItem n1 = new JMenuItem();
	  JMenuItem n2 = new JMenuItem();
	  JMenuItem n3 = new JMenuItem();
	  JMenuItem n4 = new JMenuItem();
	  JMenuItem n5 = new JMenuItem();
	  JMenuItem n6 = new JMenuItem();
	  JMenuItem n7 = new JMenuItem();
	 
	  /**
	  * Constructeur
	  */
	  public MaFenetre(){
	    setSize(900, 700);
	    setTitle("MPS");
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    initbarMenu();
	    initrecherche();
	    initToolbar();
	    initContent();
	   // initpaneloperation();
	   result.setBackground(Color.white);
	    
	   // initTable(requete);
	  }
	 
	  private void initpaneloperation(){
		  
		 // operation.setLayout(new BorderLayout());
		  operation.setSize(200, 1000);
		  operation.setBackground(Color.WHITE);
		  JButton bA = new JButton();
		  bA.setPreferredSize(new Dimension(150,30));
		  bA.setBackground(Color.WHITE);
		  bA.setText("Nouveau");
		  bA.setFont(new Font("Garamond", 0, 16));
		  JButton bM = new JButton();
		  bM.setPreferredSize(new Dimension(150,30));
		  bM.setBackground(Color.WHITE);
		  bM.setText("Mdifier");
		  bM.setFont(new Font("Garamond", 0, 16));
		  JButton bS = new JButton();
		  bS.setPreferredSize(new Dimension(150,30));
		  bS.setBackground(Color.WHITE);
		  bS.setText("Supprimer");
		  bS.setFont(new Font("Garamond", 0, 16));
		  JButton bR = new JButton();
		  bR.setPreferredSize(new Dimension(150,30));
		  bR.setBackground(Color.WHITE);
		  bR.setText("Rechercher");
		  bR.setFont(new Font("Garamond", 0, 16));
		  operation.add(bA, BorderLayout.PAGE_START);
		  operation.add(bM, BorderLayout.PAGE_START);
		  operation.add(bS, BorderLayout.PAGE_START);
		  operation.add(bR, BorderLayout.PAGE_START);
		  
	  }
	  private void initbarMenu(){
		 
		  fichierM.setText("Fichier");
		  n1.setText("Nouveau Elève");
		  n1.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  Eleve el = new Eleve();
		      }
		    }); 
		  n2.setText("Nouveau cour");
		  n2.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  Cours el = new Cours();

		      }
		    }); 
		  n3.setText("Nouveau Professeur");
		  n3.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	 Professeurs prof = new Professeurs();

		      }
		    }); 
		  n4.setText("Nouveau Activité");
		  n4.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  Activites el = new  Activites();

		      }
		    }); 
		  n5.setText("Nouveau Résultats des élèves");
		  n5.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  RESULTATS el = new RESULTATS();

		      }
		    }); 
		  n6.setText("Nouveau Activités paratiquées");
		  n6.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  ACTIVITESPRATIQUES el = new   ACTIVITESPRATIQUES();

		      }
		    }); 
		  n7.setText("NouveauCode Cours-Prof");
		  n7.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete6);

		      }
		    }); 
		  quit.setText("Quiter");
		  quit.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  System.exit(0);
		      }
		    }); 
		  fichierM.add(nouveauT);
		  nouveauT.setText("Nouveau");
		  nouveauT.add(n1);
		  nouveauT.add(n2);
		  nouveauT.add(n3);
		  nouveauT.add(n4);
		  nouveauT.add(n5);
		  nouveauT.add(n6);
		  fichierM.add(quit);
		 
		  s1.setText("Table Elève");
		  s1.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete01);

		      }
		    }); 
		  s2.setText("Table cours");
		  s2.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete11);

		      }
		    }); 
		  s3.setText("Table Professeurs");
		 
		  s3.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete21);	    	  
		      }
		    }); 
		  s4.setText("Table Activités");
		  s4.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete31);

		      }
		    }); 
		  s5.setText("Table Résultats");
		  s5.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete41);

		      }
		    }); 
		  s6.setText("Table Activités paratiquées");
		  s6.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete51);

		      }
		    }); 
		  s7.setText("Table Cours-Prof");
		  s7.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete61);

		      }
		    }); 
		  structT.setText("Structure des tables");
		  structT.add(s1);
		  structT.add(s2);
		  structT.add(s3);
		  structT.add(s4);
		  structT.add(s5);
		  structT.add(s6);
		  structT.add(s7);
		  c1.setText("Liste des Elève");
		  c1.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete);

		      }
		    }); 
		  c2.setText("Liste des cours");
		  c2.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete1);

		      }
		    }); 
		  c3.setText("Liste des Professeurs");
		  c3.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete2);


		      }
		    }); 
		  c4.setText("Liste des Activités");
		  c4.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete3);

		      }
		    }); 
		  c5.setText("Résultats des élèves");
		  c5.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete4);

		      }
		    }); 
		  c6.setText("Liste des Activités paratiquées");
		  c6.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete5);

		      }
		    }); 
		  c7.setText("Code Cours-Prof");
		  c7.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete6);

		      }
		    }); 
		  contentT.setText("Contenu des tables");
		  contentT.add(c1);
		  contentT.add(c2);
		  contentT.add(c3);
		  contentT.add(c4);
		  contentT.add(c5);
		  contentT.add(c6);
		  contentT.add(c7);
		  affichageM.setText("Affichage");
		  affichageM.add(structT);
		  affichageM.add(contentT);
		  ap.setText("A propos de l'application");
		  ap.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	 Apropos apro = new Apropos();

		      }
		    });
		  apropos.setText("?");
		  apropos.add(ap);
		  barMenu.add(fichierM);
		  barMenu.add(affichageM);
		  barMenu.add(apropos);
		  
		  setJMenuBar(barMenu);
	  }   
	  private void initrecherche(){
		  Font fer = new Font("Garamond",0, 16);
		  recherche.setBackground(Color.WHITE);
		  recherche1.setBackground(Color.WHITE);
		  recherche1.setBackground(Color.WHITE);
		  onglet.setSize(220, 700);
		 // onglet.setAutoscrolls(true);
		  onglet.setBorder(null);
		  onglet.setBackground(Color.WHITE);
		    b.setPreferredSize(new Dimension(170, 30));
		    b.setBorder(null);
		    b.setBackground(Color.WHITE);
		    b.setText("Liste des elèves");
		    b.setFont(fer);
		    b.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  initTable(requete);
		      }
		    }); 
		  
		    b1.setPreferredSize(new Dimension(170, 30));
		    b1.setBorder(null);
		    b1.setBackground(Color.WHITE);
		    b1.setText("Liste des cours");
		    b1.setFont(fer);
		    b1.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		        initTable(requete1);
		      }
		    }); 
		    b2.setPreferredSize(new Dimension(170, 30));
		    b2.setBorder(null);
		    b2.setBackground(Color.WHITE);
		    b2.setFont(fer);
		    b2.setText("Liste des professeurs");
		    b2.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		        initTable(requete2);
		      }
		    });  
		    b3.setPreferredSize(new Dimension(170, 30));
		    b3.setBorder(null);
		    b3.setBackground(Color.WHITE);
		    b3.setText("Liste des activités");
		    b3.setFont(fer);
		    b3.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		        initTable(requete3);
		      }
		    });
		    b4.setPreferredSize(new Dimension(170, 30));
		    b4.setBorder(null);
		    b4.setBackground(Color.WHITE);
		    b4.setText("les résultats");
		    b4.setFont(fer);
		    b4.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		        initTable(requete4);
		      }
		    });  
		    b5.setPreferredSize(new Dimension(170, 30));
		    b5.setBorder(null);
		    b5.setBackground(Color.WHITE);
		    b5.setFont(fer);
		    b5.setText("Les activités pratiquées");
		    b5.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		        initTable(requete5);
		      }
		    }); 
		
		    recherche.add(b, BorderLayout.WEST);
		    recherche.add(b1, BorderLayout.WEST);
		    recherche.add(b2, BorderLayout.WEST);
		    recherche.add(b3, BorderLayout.WEST);
		    recherche.add(b4, BorderLayout.WEST);
		    recherche.add(b5, BorderLayout.WEST);
		    
		    onglet.add("Affichage", recherche);

		    b01.setPreferredSize(new Dimension(170, 30));
		    b01.setBorder(null);
		    b01.setBackground(Color.WHITE);
		    b01.setText("Table elèves");
		    b01.setFont(fer);
		    b01.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		        initTable(requete01);
		      }
		    }); 
		  
		    b11.setPreferredSize(new Dimension(170, 30));
		    b11.setBorder(null);
		    b11.setBackground(Color.WHITE);
		    b11.setText("Table cours");
		    b11.setFont(fer);
		    b11.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		        initTable(requete11);
		      }
		    }); 
		    b21.setPreferredSize(new Dimension(170, 30));
		    b21.setBorder(null);
		    b21.setBackground(Color.WHITE);
		    b21.setText("Table professeurs");
		    b21.setFont(fer);
		    b21.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		        initTable(requete21);
		      }
		    });  
		    b31.setPreferredSize(new Dimension(170, 30));
		    b31.setBorder(null);
		    b31.setBackground(Color.WHITE);
		    b31.setText("Table activités");
		    b31.setFont(fer);
		    b31.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		        initTable(requete31);
		      }
		    });
		    b41.setPreferredSize(new Dimension(170, 30));
		    b41.setBorder(null);
		    b41.setBackground(Color.WHITE);
		    b41.setText("Table résultats");
		    b41.setFont(fer);
		    b41.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		        initTable(requete41);
		      }
		    });  
		    b51.setPreferredSize(new Dimension(170, 30));
		    b51.setBorder(null);
		    b51.setBackground(Color.WHITE);
		    b51.setText("Table activités pratiquées");
		    b51.setFont(fer);
		    b51.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		        initTable(requete51);
		      }
		    }); 
		  
		    recherche1.add(b01, BorderLayout.WEST);
		    recherche1.add(b11, BorderLayout.WEST);
		    recherche1.add(b21, BorderLayout.WEST);
		    recherche1.add(b31, BorderLayout.WEST);
		    recherche1.add(b41, BorderLayout.WEST);
		    recherche1.add(b51, BorderLayout.WEST);
			onglet.add("Structure", recherche1);
			 
			
			b010.setPreferredSize(new Dimension(170, 30));
			b010.setBorder(null);
			b010.setBackground(Color.WHITE);
			b010.setText("Requête 1");
			b010.setFont(fer);
		    b010.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett1);
			      }
			    });
		    b111.setPreferredSize(new Dimension(170, 30));
			b111.setBorder(null);
			b111.setBackground(Color.WHITE);
			b111.setText("Requête 2");
			b111.setFont(fer);
		    b111.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett2);
			      }
			    });
		    b212.setPreferredSize(new Dimension(170, 30));
			b212.setBorder(null);
			b212.setBackground(Color.WHITE);
			b212.setText("Requête 3");
			b212.setFont(fer);
		    b212.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett3);
			      }
			    });
			
		    b313.setPreferredSize(new Dimension(170, 30));
			b313.setBorder(null);
			b313.setBackground(Color.WHITE);
			b313.setText("Requête4");
			b313.setFont(fer);
		    b313.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett4);
			      }
			    });
		   
		    b414.setPreferredSize(new Dimension(170, 30));
			b414.setBorder(null);
			b414.setBackground(Color.WHITE);
			b414.setText("Requête 5");
			b414.setFont(fer);
		    b414.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett5);
			      }
			    });
		    
		    b515.setPreferredSize(new Dimension(170, 30));
			b515.setBorder(null);
			b515.setBackground(Color.WHITE);
			b515.setText("Requête 6");
			b515.setFont(fer);
		    b515.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett6);
			      }
			    });
		    b616.setPreferredSize(new Dimension(170, 30));
			b616.setBorder(null);
			b616.setBackground(Color.WHITE);
			b616.setText("Requête 7");
			b616.setFont(fer);
		    b616.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett7);
			      }
			    });
		    
		    b117.setPreferredSize(new Dimension(170, 30));
			b117.setBorder(null);
			b117.setBackground(Color.WHITE);
			b117.setText("Requête 8");
			b117.setFont(fer);
		    b117.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett8);
			      }
			    });
		    
		    b218.setPreferredSize(new Dimension(170, 30));
			b218.setBorder(null);
			b218.setBackground(Color.WHITE);
			b218.setText("Requête 9");
			b218.setFont(fer);
		    b218.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett9);
			      }
			    });
		    
		    b319.setPreferredSize(new Dimension(170, 30));
			b319.setBorder(null);
			b319.setBackground(Color.WHITE);
			b319.setText("Requête 10");
			b319.setFont(fer);
		    b319.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett10);
			      }
			    });
		    
		    b4110.setPreferredSize(new Dimension(170, 30));
			b4110.setBorder(null);
			b4110.setBackground(Color.WHITE);
			b4110.setText("Requête 11");
			b4110.setFont(fer);
		    b4110.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett11);
			      }
			    });
		    
		    b5111.setPreferredSize(new Dimension(170, 30));
			b5111.setBorder(null);
			b5111.setBackground(Color.WHITE);
			b5111.setText("Requête 12");
			b5111.setFont(fer);
		    b5111.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett12);
			      }
			    });
		    
		    b6113.setPreferredSize(new Dimension(170, 30));
			b6113.setBorder(null);
			b6113.setBackground(Color.WHITE);
			b6113.setText("Requête 13");
			b6113.setFont(fer);
		    b6113.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett13);
			      }
			    });
		    
		    b1114.setPreferredSize(new Dimension(170, 30));
			b1114.setBorder(null);
			b1114.setBackground(Color.WHITE);
			b1114.setText("Requête 14");
			b1114.setFont(fer);
		    b1114.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett14);
			      }
			    });
		    
		    b2115.setPreferredSize(new Dimension(170, 30));
		    b2115.setBorder(null);
		    b2115.setBackground(Color.WHITE);
		    b2115.setText("Requête 15");
		    b2115.setFont(fer);
		    b2115.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett15);
			      }
			    });
		    
		    b3116.setPreferredSize(new Dimension(170, 30));
		    b3116.setBorder(null);
		    b3116.setBackground(Color.WHITE);
		    b3116.setText("Requête 16");
		    b3116.setFont(fer);
		    b3116.addActionListener(new ActionListener(){
			      public void actionPerformed(ActionEvent event){
			        initTable(requetett16);
			      }
			    });
		    recherche3.add(b010, BorderLayout.WEST);
		    recherche3.add(b111, BorderLayout.WEST);
		    recherche3.add(b212, BorderLayout.WEST);
		    recherche3.add(b313, BorderLayout.WEST);
		    recherche3.add(b414, BorderLayout.WEST);
		    recherche3.add(b515, BorderLayout.WEST);
		    recherche3.add(b616, BorderLayout.WEST);
		    recherche3.add(b117, BorderLayout.WEST);
		    recherche3.add(b218, BorderLayout.WEST);
		    recherche3.add(b319, BorderLayout.WEST);
		    recherche3.add(b4110, BorderLayout.WEST);
		    recherche3.add(b5111, BorderLayout.WEST);
		    recherche3.add(b6113, BorderLayout.WEST);
		    recherche3.add(b1114, BorderLayout.WEST);
		    recherche3.add(b2115, BorderLayout.WEST);
		    recherche3.add(b3116);
		    recherche3.setBackground(Color.WHITE);
			recherche2.add(onglet);
			
			onglet.add(recherche3, "Requête");
		    //getContentPane().add(recherche2);
	  }
	  public void initContent(){
		  JLabel ne =  new JLabel(new ImageIcon("images/Fond.jpg"));
		  JLabel nea =  new JLabel();
		  nea.setText("\t \t \t \t \t "+"Bienvenu ");
			
			nea.setFont(new Font("Garamond",2 , 70));
			ne.setPreferredSize(new Dimension(200,50));
		 
			recherche2.setLayout(new BorderLayout());
		    result.setLayout(new BorderLayout());
		   // result.add(nea, BorderLayout.NORTH);
		    result.add(ne);
		    split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(text), result);
		    split.setDividerLocation(100);
		    split.setDividerSize(2);
		    split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, recherche2, split);
		    split1.setDividerLocation(220);
		    split1.setDividerSize(2);
		    getContentPane().add(split1);      
		  }
	
	  /**
	  * Initialise la toolbar
	  */
	  private void initToolbar(){
		  
	  
		   reqExu.setPreferredSize(new Dimension(150, 30));
		    reqExu.setBackground(Color.WHITE);
		    reqExu.setText("Exécuter la reqûete");
		    reqExu.setFont(new Font("Garamond", 0, 16));
		   reqExu.addActionListener(new ActionListener(){
	   
	      public void actionPerformed(ActionEvent event){
	        initTable(text.getText());
	      }
	    });
	  
	    reqEffac.setPreferredSize(new Dimension(150, 30));
	    reqEffac.setBackground(Color.WHITE);
	    reqEffac.setText("Effacer la reqûete");
	    reqEffac.setFont(new Font("Garamond", 0, 16));
	    reqEffac.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent event){
	    	  
	    	  
	      }
	    });
	 
	    tool.add( reqExu);
	   // tool.add( reqEffac);
	    
	    getContentPane().add(tool, BorderLayout.NORTH);
	  }
	     
	 
	  public void initTable(String query){
		  JLabel ne =  new JLabel();
		  ne.setFont(new Font("Garamond",2 , 35));
	    try {
	    	
			 
			  
	    //On crée un statement
	      long start = System.currentTimeMillis();
	      Statement state = MaConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	 
	      //On exécute la requête
	      ResultSet res = state.executeQuery(query);
	      //Temps d'exécution
	 
	      //On récupère les meta afin de récupérer le nom des colonnes
	      ResultSetMetaData meta = res.getMetaData();
	      //On initialise un tableau d'Object pour les en-têtes du tableau
	      Object[] column = new Object[meta.getColumnCount()];
	 
	      for(int i = 1 ; i <= meta.getColumnCount(); i++)
	        column[i-1] = meta.getColumnName(i);
	 
	      //Petite manipulation pour obtenir le nombre de lignes
	      res.last();
	      int rowCount = res.getRow();
	      Object[][] data = new Object[res.getRow()][meta.getColumnCount()];
	      //On revient au départ
	      res.beforeFirst();
	      int j = 1;
	 
	      //On remplit le tableau d'Object[][]
	      while(res.next()){
	        for(int i = 1 ; i <= meta.getColumnCount(); i++)
	          data[j-1][i-1] = res.getObject(i);
	                 
	        j++;
	      }
	 
	      //On ferme le tout                                    
	      res.close();
	      state.close();
	 
	      long totalTime = System.currentTimeMillis() - start;
	 
	      //On enlève le contenu de notre conteneur
	      result.removeAll();
	      //On y ajoute un JTable
	     
	      result.add(new JScrollPane(new JTable(data, column)));
	      text.setText(query);
	      result.add(new JLabel("La requête à été exécuter en " + totalTime + " ms et a retourné " + rowCount + " ligne(s)"), BorderLayout.SOUTH);
	      //On force la mise à jour de l'affichage
	      result.revalidate();
	             
	    } catch (SQLException e) {
	    	
	    
	      //Dans le cas d'une exception, on affiche une pop-up et on efface le contenu     
	    	if(e.getSQLState()==null){
	    		ne.setText("Operation terminer avec succès");
	    		result.removeAll();
				  result.add(ne);
				  result.revalidate();
	    	}else{
	    		ne.setText(e.getMessage());
	    		ne.setForeground(Color.RED);
	    	result.removeAll();
			  result.add(ne);
			  result.revalidate();}
	     
	    }  
	    catch (StringIndexOutOfBoundsException e) {
	    	result.removeAll();
	    	
			ne.setText("Veuillez saisir une requête puis validez.");
			
			
			  result.add(ne);
			  result.revalidate();}	
	    
	    
	  }
	 
	 
	  /**
	  * Point de départ du programme
	  * @param args
	  */
	  public static void main(String[] args){
	   
	  try {
          for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
              if ("Nimbus".equals(info.getName())) {
                  UIManager.setLookAndFeel(info.getClassName());
                  break;
              }
          }
      } catch (ClassNotFoundException ex) {
         
      } catch (InstantiationException ex) {
         
      } catch (IllegalAccessException ex) {
         
      } catch (UnsupportedLookAndFeelException ex) {
    	  JOptionPane.showMessageDialog(null, "Pattern insupportable", "informtion", NORMAL);
       
      }
	  
     
      java.awt.EventQueue.invokeLater(new Runnable() {
          public void run() {
        	
        	  
              new MaFenetre().setVisible(true);
          }
      });
  }
	  
	  
	  
	  }

