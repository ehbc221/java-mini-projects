package fr.achat.view;

import java.io.File;

import fr.achat.Appli;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class RootLayoutController 
{

	private Appli app;
	
	public void setAppli(Appli app)
	{
		this.app = app;
	}
	
	//***************************************************************************************************
	//Gestion des bouttons Barre des menus
	@FXML
	private void appuiNouveauFichier()
	{
		app.getDonneeAchat().clear();
		app.setCheminFichierAchat(null);
	}
	
	@FXML 
	private void appuiOuvrir()
	{
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(app.getPrimaryStage());

        if (file != null) 
        {
            app.chargementDonneeDepuisFichier(file);
        }
        	
    }
	
	@FXML
	private void appuiEnregistrer()
	{
		File fichierAchat = app.getCheminFichierAchat();
		if(fichierAchat != null)
		{
			app.enregistrementDonneeVersFichier(fichierAchat);
		}
		
		else
		{
			appuiEnregistrerEnTtQue();
		}

	}

	
	@FXML
	private void appuiEnregistrerEnTtQue() 
	{
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(exFilter);
		
		File file = fileChooser.showSaveDialog(app.getPrimaryStage());
		
		if(file != null)
		{	
			//Pout être sur que notre fichier sera enregistré en xml
			if(!file.getPath().endsWith(".xml"))
			{
				file = new File(file.getPath() +".xml");
			}
			app.enregistrementDonneeVersFichier(file);
		}
	}
	
	@FXML
    private void appuiAbout() 
	{
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Gestion des Achats");
        alert.setHeaderText("About");
        alert.setContentText("Petite application personnelle dans le but d'apprendre le JavaFX inspiré par le tutoriel de Marc Jacob sur son site http://code.makery.ch/library/javafx-8-tutorial/fr/ ");

        alert.showAndWait();
    }
	
	
	//Affichage des Stats si l'on clique sur stat par mois et par catégorie
	@FXML
	private void appuiSurStatsMois()
	{
		app.vuStatsDepenseMois();
		
	}
	
	@FXML
	private void appuiStatsCategorie()
	{
		app.vuStatsDepenseCategorie();
	}
	
	//ferme l'application
	@FXML
    private void appuiExit()
	{
        System.exit(0);
    }
	
	
	
	
}
