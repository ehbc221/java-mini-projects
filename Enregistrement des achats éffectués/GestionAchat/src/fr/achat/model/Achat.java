package fr.achat.model;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import fr.achat.util.LocalDateAdapter;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Achat 
{
	private StringProperty magasin;	
	private FloatProperty prix;
	private ObjectProperty<LocalDate> dateAchat;
	private StringProperty categorie;
	private StringProperty description;
	private int nCategorie;
	
	
	//******************** Constructeurs *****************************************
	
	//Constructeur par défaut
	public Achat()
	{
		this(null, null, 0);

	}
	
	//exhaustif
	public Achat(String pMag, LocalDate pDate, int pPrix)
	{
		this.magasin = new SimpleStringProperty(pMag); //Ici pas comme d'hab --> il faut appeler le constructeur de StringProperty
		this.prix = new SimpleFloatProperty(pPrix);
		this.dateAchat = new SimpleObjectProperty<LocalDate>(pDate); //LocalDate sera ici de type Object
		this.description = new SimpleStringProperty("");
		this.categorie = new SimpleStringProperty("");
		this.setNcategorie(this.getNcategorie());
	}
	
	//******************** Getters **********************************************
	
	

	public String getMagasin()
	{
		return this.magasin.get();
	}
	
	public StringProperty magasinProperty() 
	{
		return this.magasin;

	}
	
	
	public float getPrix()
	{
		return this.prix.get();
	}
	
	public FloatProperty prixProperty()
	{
		return this.prix;
	}
	
	
	public String getDescription()
	{
		return this.description.get();
	}
	
	public StringProperty descriptionProperty()
	{
		return this.description;
	}
	
	
	public String getCategorie()
	{
		return this.categorie.get();
	}
	
	public StringProperty categorieProperty()
	{
		return this.categorie;
	}
	
	
	public int getNcategorie() 
	{
		return this.nCategorie;
	}
	
	
	
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getDateAchat()
	{
		return dateAchat.get();
	}	
	
	public ObjectProperty<LocalDate> dateAchatProperty() //un peu comme Object<LocalDate> d = ...
	{
		return this.dateAchat;
	}
	

	
	//******************** Setters ***************************************************
	
	public void setMagasin(String magasin)
	{
		this.magasin.set(magasin);
	}
	
	public void setPrix(float f)
	{
		this.prix.set(f);
	}
	
	public void setCategorie(String categorie)
	{
		this.categorie.set(categorie);
	}

	public void setDescription(String description) 
	{
		this.description.set(description);
	}
	
	public void setDateAchat(LocalDate date)
	{
		this.dateAchat.set(date);
		
	}
		
	public void setNcategorie(int a) 
	{
		this.nCategorie = a;
		
	}
	


	
	//*********************************************************************************
	
	/*public void afficheConsole()
	{
		System.out.println("Categorie : " +this.getCategorie()+ "Num catégorie : " +this.getNcategorie());
	}
	*/
	
	
	
	
	
}
