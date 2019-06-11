
package fr.achat.view;

import java.time.LocalDate;
import fr.achat.Appli;
import fr.achat.model.Achat;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

public class OverviewController 
{
	//Ajout de champs et méthodes pour faire le lien avec notre affichage : tableau et étiquettes.
	//Les @ servent à faire aux FXML d'acceder à ces champs et méthodes qui sont privés
	
	@FXML
	private TableView<Achat> achatTable;
	
	@FXML
	private TableColumn<Achat, LocalDate> dateColonne;
	
	@FXML
	private TableColumn<Achat, Float> prixColonne;
	
	@FXML
	private TableColumn<Achat, String> magasinColonne;
	
	@FXML
	private Label descriptionLabel;
	
	@FXML
	private Label categorieLabel;
	
	//On met une référence pour l'application principal : Appli.
	private Appli app;
	
	//Constructeur:
	public OverviewController()
	{
		
	}
	
	//Methode qui est appelée automatiquement après le chargement du fichier Overview.fxml dans notre class Appli
	@FXML
	private void initialize()
	{
		//On affecte à la colonne dateColonne notre getter dateAchatProperty() de notre class Achat... de même pour l'autre
		dateColonne.setCellValueFactory(cellData -> cellData.getValue().dateAchatProperty()); 
		magasinColonne.setCellValueFactory(cellData -> cellData.getValue().magasinProperty());
		prixColonne.setCellValueFactory(cellData -> cellData.getValue().prixProperty().asObject());
		
		//Pour effacer les détails d'un achat : 
		afficheAchatDetails(null);
		
		//Pour se tenir au courant des changements dans la liste d'achat et permet de voir le détail à droite
		achatTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> afficheAchatDetails(newValue));
		/*
		 * addListener va servir a notifier à chaque fois qu'il y a changement dans notre Observable
		 */
		

		
	}
	
	//Méthode pour afficher la partie droite de notre application : les informations concernant l'achat
	private void afficheAchatDetails(Achat achat)
	{
		if(achat != null)
		{
			//Le label a une méthode pour pouvoir changer sa valeur : setText("") ou setText(Integer.toString())
			//S'il y avait un champ de type int il aurait fallu le convertir
			descriptionLabel.setText(achat.getDescription());
			categorieLabel.setText(achat.getCategorie());
		}
		
		else
		{
			descriptionLabel.setText("");
			categorieLabel.setText("");
		}
	}
	
	
	//***************** Setter pour notre champ : Appli app***************************************
	//Sera appelé par la class Appli qui donnera accès à l'objet de Appli et notre List des Achat
	public void setAppli(Appli app)
	{
		this.app = app;
		
		//On rajoute notre ObservableList de notre class Appli via son getter à nos données du tableau achatTable :
		achatTable.setItems(app.getDonneeAchat());
	}
	//***********************************************************************************************
	//Méthodes Button
	
	//Méthode de Suppression
	
	public void supprimer()
	{
		/*
		 * On va enregistrer le champs sélectionné
		 * puis lui passe en paramètre cet enregistrement (ici selection)
		 */
		int selection = achatTable.getSelectionModel().getSelectedIndex();
		if(selection >= 0)
		{
			achatTable.getItems().remove(selection);
		}
		else
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(app.getPrimaryStage());
			alert.setTitle("Attention");
			alert.setHeaderText("Aucune selection");
			alert.setContentText("Selectionner un champ pour pouvoir supprimer");
			
			alert.showAndWait();
		}
	}
	
	
	/*Sera appelé quand l'utilisateur appuira sur nouveau.
	 * on a fait un lien entre cette méthode et Appli qui cette dernire va charger 
	 * notre petite de fenetre d'edition d'Achat
	 */
	@FXML
	private void appuiSurNouveau()
	{
		Achat achat = new Achat();
		boolean cliqueOK = app.showAchatEditDialog(achat);
		if(cliqueOK)
		{
			app.getDonneeAchat().add(achat); //Car private --> on passe par le getter 
			//--> on va rajouter un nouvel objet de type Achat dans notre list
		}
		
	}
	
	@FXML
	private void appuiSurEdit()
	{
		Achat selectionAchat = achatTable.getSelectionModel().getSelectedItem();
		if(selectionAchat != null)
		{
			boolean cliqueOK = app.showAchatEditDialog(selectionAchat);
			if(cliqueOK)
			{
				afficheAchatDetails(selectionAchat);
			}
		}
		else
		{
			Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(app.getPrimaryStage());
	        alert.setTitle("Pas de sélection");
	        alert.setHeaderText("Aucun achat n'est sélectionné");
	        alert.setContentText("Selectionnez un achat.");

	        alert.showAndWait();
		}
		
	}
	
	
	
	
	
	
	
}
