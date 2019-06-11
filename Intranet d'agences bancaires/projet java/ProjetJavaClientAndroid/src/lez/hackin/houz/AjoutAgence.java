package lez.hackin.houz;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import Modele.Agence;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AjoutAgence extends Activity implements OnClickListener {

	private OutputStream os;
	private ObjectOutputStream oos;
	private InputStream is;
	private ObjectInputStream ois;
	private EditText edtNomAgence;
	private EditText edtAdresseAgence; 
	private Button btnAjouter;
	private Button btnAnnuler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ajout_agence_layout);
		
		btnAjouter = (Button)findViewById(R.id.btnAjouter);
    	btnAnnuler = (Button)findViewById(R.id.btnAnnuler);
    	edtNomAgence = (EditText)findViewById(R.id.edtNomAgence);
    	edtAdresseAgence = (EditText)findViewById(R.id.edtAdresseAgence);
    	
    	btnAjouter.setOnClickListener(this);
    	btnAnnuler.setOnClickListener(this);
    	    	
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.btnAjouter:
			String action = "nouvelleAgence";
			boolean res;
			try {
				Agence agence = new Agence(edtNomAgence.getText().toString(), edtAdresseAgence.getText().toString());
				Log.i("LezAndroidAgence", agence.getLibAgence());
				os = Main.serveur.getOutputStream();
				oos = new ObjectOutputStream(os);
				oos.writeObject(action);
				oos.flush();
				oos = new ObjectOutputStream(os);
				oos.writeObject(agence);
				oos.flush();
				
				is = Main.serveur.getInputStream();
		        ois = new ObjectInputStream(is);
		        String raction = (String)ois.readObject();
		        if(action.equals(raction)){
		        	ois = new ObjectInputStream(is);
		        	res = (Boolean)ois.readObject();
		        	if(res == true){
		        		Toast.makeText(this, "Sauvegarde réussie!", Toast.LENGTH_LONG).show();
		        	}
		        	else{
		        		Toast.makeText(this, "Echec de la sauvegarde!", Toast.LENGTH_LONG).show();
		        	}
		        	edtNomAgence.setText("");
		        	edtAdresseAgence.setText("");
		        }
			}catch(Exception ex){
				//ex.printStackTrace();
				Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}
}
