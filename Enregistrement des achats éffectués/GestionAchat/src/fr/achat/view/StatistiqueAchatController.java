package fr.achat.view;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.achat.model.Achat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class StatistiqueAchatController 
{
	
	@FXML
	private BarChart<String, Float> barChart;
	
	@FXML
	private CategoryAxis xAxe;
	
	@FXML
	private NumberAxis yAxis = new NumberAxis();
	
	private ObservableList<String> dateAbs = FXCollections.observableArrayList();
	
	@FXML
	private void initialize()
	{
		String date[] = DateFormatSymbols.getInstance(Locale.FRENCH).getMonths();
		dateAbs.addAll(Arrays.asList(date));
				
	}
	
	public void setAchatDonnee(List<Achat> listAchat)
	{
		try{

			float somme[] = new float [12];
			
			for(int i= 0; i<listAchat.size(); i++)
			{			
				int mois = listAchat.get(i).getDateAchat().getMonthValue()-1; //va m'obtenir la valeur du mois de 0 à 11. 
				somme[mois] = (somme[mois] + listAchat.get(i).getPrix());
			}

			
			XYChart.Series<String, Float> series = new XYChart.Series<>(); 
			//on va faire une série où il y aura 
			//des String à prendre en compte et des Float pour les coûts
			//Maintenant on crée notre objet pour chaque mois en fonction de nos dépenses
			
			for (int i = 0; i<somme.length ; i++)
			{
				series.getData().add(new XYChart.Data<>( dateAbs.get(i), somme[i] ));
				
				//System.out.println("Tour : " +i);
			}
			
			
			barChart.getData().add(series);	
			
		}catch(IndexOutOfBoundsException e)
		{
			e.printStackTrace();
			System.out.println("Pb pour stats par mois ");
		}
		
		
	}
	
	
	
	
	
	
	

}
