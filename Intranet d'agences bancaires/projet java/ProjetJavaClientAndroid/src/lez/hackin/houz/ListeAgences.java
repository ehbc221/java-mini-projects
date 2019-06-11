package lez.hackin.houz;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import Modele.Agence;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListeAgences extends Activity implements OnItemClickListener {

	private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;
	private ArrayList<Agence> listeAgences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_agence_layout);
		
		String action = "listeAgence";
		
		//j'envoie les données au serveur pour exécution de la requête {étape 1}
		try {
	        os = Main.serveur.getOutputStream();
	        oos = new ObjectOutputStream(os);
	        oos.writeObject(action);
	        
	        oos.flush();
	        
	        is = Main.serveur.getInputStream();
	        ois = new ObjectInputStream(is);
	        String raction = (String)ois.readObject();
	        if(action.equals(raction)){
	        	ois = new ObjectInputStream(is);
	        	listeAgences = (ArrayList<Agence>)ois.readObject();
	        	if(listeAgences.size() == 0){
	        		Toast.makeText(this, "Il n'y a aucune agence!", Toast.LENGTH_LONG).show();
	        	}
		        
		        AgenceAdapter adapter = new AgenceAdapter(this, listeAgences);
		        ListView listeVueAgence = (ListView)findViewById(R.id.listAgences1);
		        
		        listeVueAgence.setAdapter(adapter);
		        
		        listeVueAgence.setOnItemClickListener(this);
	        }
		}catch(IOException ex){
//			System.out.println("°_° "+ex.getMessage());
			Toast.makeText(this, "Echec de l'affichage!", Toast.LENGTH_LONG);
		}
		catch (ClassNotFoundException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View pVue, int pPosition, long id) {
		
		String numAgence = listeAgences.get(pPosition).getCodeAgence();
		String nomAgence = listeAgences.get(pPosition).getLibAgence();
		Intent intentAjoutClient = new Intent(this, AjoutClient.class);
		intentAjoutClient.putExtra("numAgence", numAgence.toString());
		intentAjoutClient.putExtra("nomAgence", nomAgence);
		startActivity(intentAjoutClient);
	}
}
