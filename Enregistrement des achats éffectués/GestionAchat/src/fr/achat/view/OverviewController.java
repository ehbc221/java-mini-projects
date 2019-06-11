
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
	//Ajout de champs et m�thodes pour faire le lien avec notre affichage : tableau et �tiquettes.
	//Les @ servent � faire aux FXML d'acceder � ces champs et m�thodes qui sont priv�s
	
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
	
	//On met une r�f�rence pour l'application principal : Appli.
	private Appli app;
	
	//Constructeur:
	public OverviewController()
	{
		
	}
	
	//Methode qui est appel�e automatiquement apr�s le chargement du fichier Overview.fxml dans notre class Appli
	@FXML
	private void initialize()
	{
		//On affecte � la colonne dateColonne notre getter dateAchatProperty() de notre class Achat... de m�me pour l'autre
		dateColonne.setCellValueFactory(cellData -> cellData.getValue().dateAchatProperty()); 
		magasinColonne.setCellValueFactory(cellData -> cellData.getValue().magasinProperty());
		prixColonne.setCellValueFactory(cellData -> cellData.getValue().prixProperty().asObject());
		
		//Pour effacer les d�tails d'un achat : 
		afficheAchatDetails(null);
		
		//Pour se tenir au courant des changements dans la liste d'achat et permet de voir le d�tail � droite
		achatTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> afficheAchatDetails(newValue));
		/*
		 * addListener va servir a notifier � chaque fois qu'il y a changement dans notre Observable
		 */
		

		
	}
	
	//M�thode pour afficher la partie droite de notre application : les informations concernant l'achat
	private void afficheAchatDetails(Achat achat)
	{
		if(achat != null)
		{
			//Le label a une m�thode pour pouvoir changer sa valeur : setText("") ou setText(Integer.toString())
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
	//Sera appel� par la class Appli qui donnera acc�s � l'objet de Appli et notre List des Achat
	public void setAppli(Appli app)
	{
		this.app = app;
		
		//On rajoute notre ObservableList de notre class Appli via son getter � nos donn�es du tableau achatTable :
		achatTable.setItems(app.getDonneeAchat());
	}
	//***********************************************************************************************
	//M�thodes Button
	
	//M�thode de Suppression
	
	public void supprimer()
	{
		/*
		 * On va enregistrer le champs s�lectionn�
		 * puis lui passe en param�tre cet enregistrement (ici selection)
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
	
	
	/*Sera appel� quand l'utilisateur appuira sur nouveau.
	 * on a fait un lien entre cette m�thode et Appli qui cette dernire va charger 
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
	        alert.setTitle("Pas de s�lection");
	        alert.setHeaderText("Aucun achat n'est s�lectionn�");
	        alert.setContentText("Selectionnez un achat.");

	        alert.showAndWait();
		}
		
	}
	
	
	
	
	
	
	
}
