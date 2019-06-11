package lez.hackin.houz;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import Modele.Client;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListeClients extends Activity implements OnItemClickListener {
	
	private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;
	private ArrayList<Client> listeClients;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_client_layout);
		
		String action = "listeClient";
		
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
	        	listeClients = (ArrayList<Client>)ois.readObject();
	        	if(listeClients.size() == 0){
	        		Toast.makeText(this, "Il n'y a aucun client!", Toast.LENGTH_LONG).show();
	        	}
		        
		        ClientAdapter adapter = new ClientAdapter(this, listeClients);
		        ListView listeVueClient = (ListView)findViewById(R.id.listClients1);
		        
		        listeVueClient.setAdapter(adapter);
		        
		        listeVueClient.setOnItemClickListener(this);
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
		
		String numClient = String.valueOf(listeClients.get(pPosition).getNumClient());
		String nomClient = listeClients.get(pPosition).getNom();
		String prenomClient = listeClients.get(pPosition).getPrenoms();
		String adresseClient = listeClients.get(pPosition).getAdresseCli();
		String nomAgence = listeClients.get(pPosition).getNomAgence();
		Intent intentAjoutCompte = new Intent(this, AjoutCompte.class);
		intentAjoutCompte.putExtra("numClient", numClient.toString());
		intentAjoutCompte.putExtra("nomAgence", nomAgence);
		startActivity(intentAjoutCompte);
	}
}
