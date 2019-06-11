package lez.hackin.houz;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	public static Socket serveur;
	public static String ip;
	public static int port;
	private OutputStream os;
	private ObjectOutputStream oos;
	private Button btnConnexion;
	private EditText edtIp;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        edtIp = (EditText)findViewById(R.id.edtAdresseIp);
        btnConnexion = (Button)findViewById(R.id.btnConnexion);
        btnConnexion.setOnClickListener(this);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	SubMenu ssMenu1 = menu.addSubMenu(0,1,0,"Agences");
    	ssMenu1.add(1, 11, 0, "Ajouter une agence");
    	ssMenu1.add(1, 12, 0, "Afficher les agences");
    	SubMenu ssMenu2 = menu.addSubMenu(0,2,0,"Clients");
    	ssMenu2.add(2, 21, 0, "Liste des clients");
    	ssMenu2.add(2, 22, 0, "Clients par agence");
    	SubMenu ssMenu3 = menu.addSubMenu(0,3,0,"Comptes");
    	ssMenu3.add(3, 31, 0, "Comptes par client");
    	ssMenu3.add(3, 32, 0, "Tous les comptes");
    	ssMenu3.add(3, 33, 0, "Relevé par compte");
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
		case 11:
//			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			Intent intentAjoutAgence = new Intent(this, AjoutAgence.class);
			startActivity(intentAjoutAgence);
			break;
		case 12:
//			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			Intent intentListeAgences = new Intent(this, ListeAgences.class);
			startActivity(intentListeAgences);
			break;
		case 21:
//			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			Intent intentListeClients = new Intent(this, ListeClients.class);
			startActivity(intentListeClients);
			break;
		case 22:
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		case 31:
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		case 32:
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		case 33:
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}    	
    	
    	return super.onOptionsItemSelected(item);
    }

	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.btnConnexion){
			try {
	    		ip = edtIp.getText().toString();
	    		port = 2000;
	    		serveur = new Socket(ip, port);
	    		if(serveur.isConnected()){
	    			Toast.makeText(this, "Connecté", Toast.LENGTH_SHORT).show();
	    		}
	    	} catch (Throwable e) {
	    		e.printStackTrace();
	    	}
		}
	}
}