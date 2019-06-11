package fr.achat;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import fr.achat.model.Achat;
import fr.achat.model.AchatJaxb;
import fr.achat.view.AchatEditController;
import fr.achat.view.OverviewController;
import fr.achat.view.RootLayoutController;
import fr.achat.view.StatistiqueAchatController;
import fr.achat.view.StatsCategorie;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Appli extends Application 
{
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	//Collection d'objet FX
	private ObservableList<Achat> donneeAchat = FXCollections.observableArrayList();
	
	//************************ Constructeur de Appli ************************************************************* 
	public Appli() 
	{
		//Ajout dans la collection ArrayList donneeAchat
		//donneeAchat.add(new Achat("Mango", LocalDate.of(2016, Month.JANUARY, 20), 50, 0));
	}
	
	//****** Getter de notre List: ObservableList<Achat> donneeAchat *******************************************
	public ObservableList<Achat> getDonneeAchat()
	{
		return this.donneeAchat; 
	}
	//***********************************************************************************************************
	
	
	@Override
	public void start(Stage primaryStage) 
	{
		this.primaryStage = primaryStage; //Tous ce qu'on a fait sur Scene Builder
		this.primaryStage.setTitle("Visualisation des Achats"); // Mets le titre
		
		initRootLayout(); //La fenêtre où l'on va mettre notre Overview
		showOverview();
	}
	
	//********************** Méthode pour initialiser le RootLayout.fxml (ce qui va contenir notre Overview)***
	
	public void initRootLayout()
	{
		try
		{
		//On va charger le fichier fxml RootLayout
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Appli.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane)loader.load();
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			//On va donner à notre main l'accès au RootLayoutController
			RootLayoutController controller = loader.getController();
			controller.setAppli(this);	
			
			primaryStage.show();
				
			
			
		}catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("ERREUR pour le chargement RootLayout");
		}
		
		File file = getCheminFichierAchat();
		if(file != null)
		{
			chargementDonneeDepuisFichier(file);
		}
				
	}
	
	//******************************* Méthode pour charger notre fichier Overview.fxml dans notre fenetre RootLayout************************
	public void showOverview()
	{
		try
		{
			//On va charger le ficher Overview --> presque même chose que la méthode précedente
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Appli.class.getResource("view/Overview.fxml"));
			AnchorPane overview = (AnchorPane)loader.load(); // ce que j'ai fait sur Scene Builder je le charge
															 //et je le met dans overview qui est de type AnchorPane
			
			//on met le fichier overview dans notre rootLayout au centre
			rootLayout.setCenter(overview);
			
			
			//On va donner à notre controller "OverviewController" l'accès à la classe Appli. 
			//Il faut faire le lien entre les 2.
			OverviewController controller = loader.getController();
			controller.setAppli(this);
			
			
		}catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("ERREUR pour le chargement Overview");
		}
	}
	
	// *******************Méthode charger notre fichier AchatEdit.fxml quand on appui sur modifier ou nouveau ******
	
	public boolean showAchatEditDialog(Achat achat)
	/*
	 * De type boolean car si l'utilisateur clique sur OK
	 * --> on mets à jour les changements et un sera retourné
	*/
	{
		//Chargement de noter fichier fxml
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Appli.class.getResource("view/AchatEditFenetre.fxml"));
			AnchorPane page = (AnchorPane)loader.load();
			
			//Stage
			Stage dialogeStage = new Stage();
			dialogeStage.setTitle("Edition");
			dialogeStage.initModality(Modality.WINDOW_MODAL);
			dialogeStage.initOwner(primaryStage);
			
			//Scene pour le fichier fxml
			Scene scene = new Scene(page); //notre scene sera notre fichier fxml qu'on a mit dans page qu'on dans la "scene" qui est l'objet scene de type Scene
			dialogeStage.setScene(scene);
			
			AchatEditController controller = loader.getController(); //lien entre notre fichier controller et son fxml
			controller.setStageAjout(dialogeStage);
			controller.setAchat(achat);
			
			dialogeStage.showAndWait();
			
			
			return controller.cliqueOk();
			
		}catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("ERREUR pour le chargement de l'AchatEditFenetre");
			return false;
		}
	
	}	
	
	
	//***********************************************************************************************************
	//Méthode pour le chargement de fenetre pour les stats
	//Connexion de la vue/controller avec le main Appli
	public void vuStatsDepenseMois()
	{
		try
		{
			//Chargement dans loader
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Appli.class.getResource("view/Statistics.fxml"));
			AnchorPane page = (AnchorPane)loader.load();
			
			//Stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Statistique par Mois");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			
			//Scene
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			//liaison avec le StatistiqueAchatController
			StatistiqueAchatController controller = loader.getController();
			controller.setAchatDonnee(donneeAchat);
			
			dialogStage.showAndWait();
			
		}catch(IOException  e)
		{
			e.printStackTrace();
			System.out.println("Pb de chargement pour la fenêtre stats");
		}
	}
	
	
	
	public void vuStatsDepenseCategorie()
	{
		try
		{
			//Chargement dans loader
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Appli.class.getResource("view/StatsCategorie.fxml"));
			AnchorPane page = (AnchorPane)loader.load();
			
			//Stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Statistique par catégorie");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			
			//Scene
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			//liaison avec le StatistiqueAchatController
			StatsCategorie controller = loader.getController();
			controller.setAchatDonnee(donneeAchat);
			
			dialogStage.showAndWait();
			
		}catch(IOException  e)
		{
			e.printStackTrace();
			System.out.println("Pb de chargement pour la fenêtre stats");
		}
	}
	
	
	//***********************************************************************************************************
	//Méthode pour les préférence qui vont nous permettre de retrouver le fichier enregistré ou de fixer le chemin
	
	public File getCheminFichierAchat()
	{
		Preferences pref = Preferences.userNodeForPackage(Appli.class);
		String cheminFichier = pref.get("cheminFichier", null);
		if(cheminFichier != null)
		{
			return new File(cheminFichier);
		}
		else 
			return null;
	}
	
	
	public void setCheminFichierAchat(File file)
	{
		Preferences pref = Preferences.userNodeForPackage(Appli.class); 
		if(file != null)
		{
			pref.put("cheminFichier", file.getPath() );
			primaryStage.setTitle("Visualisation des Achats - " +file.getName() );
		}
		else
		{
			pref.remove("cheminFichier");
			primaryStage.setTitle("Visualisation des Achats");
		}
			
	}
	
	//******************************** Unmarshall et Marshall **********************************************
	
	public void chargementDonneeDepuisFichier(File file)
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance(AchatJaxb.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			
			//Lecture du fichier XML et unmarshalling
			AchatJaxb achatJaxb = (AchatJaxb) unmarshaller.unmarshal(file);
			
			donneeAchat.clear(); // J'efface tous ce qu'il y a dans mon ObservableList actuel 
			donneeAchat.addAll(achatJaxb.getAchats());
			
			//Enregistre le chemin du fichier dans le registre
			
		}catch(Exception e)//Intercept toutes exception qui peut se produire
		{
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Erreur");
	        alert.setHeaderText("Erreur de chargement du fichier");
	        alert.setContentText("Impossbile de charger le fichier depuis le chemin :\n" + file.getPath());
	        alert.showAndWait();
		}
	}
	
	public void enregistrementDonneeVersFichier(File file)
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance(AchatJaxb.class);
			Marshaller marsha = jc.createMarshaller();
			marsha.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			AchatJaxb aJ = new AchatJaxb();
			aJ.setAchats(donneeAchat);
			//on va "marshaller" (marshalling) notre fichier et le sauver 
			marsha.marshal(aJ, file);
			
			//on sauvegarde le chemin du fichier dans le registre
			setCheminFichierAchat(file);
			
		}catch(JAXBException e)
		{
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Erreur");
	        alert.setHeaderText("Erreur enregistrement le fichier");
	        alert.setContentText("Impossible d'enregistrer le fichier:\n" + file.getPath());
	        alert.showAndWait();
		}
	}
	
	//************************************************************************************************
	
	
	
	
	public Stage getPrimaryStage()
	{
		return primaryStage;
		
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
	

	
}
