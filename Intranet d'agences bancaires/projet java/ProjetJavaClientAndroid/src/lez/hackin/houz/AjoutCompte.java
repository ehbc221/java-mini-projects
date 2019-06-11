package lez.hackin.houz;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import Modele.Compte;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AjoutCompte extends Activity implements OnClickListener {

	private OutputStream os;
	private ObjectOutputStream oos;
	private InputStream is;
	private ObjectInputStream ois;
	private TextView txtNumClient;
	private TextView txtNomAgence;
	private EditText edtNumCompte;
	private TextView txtSens;
	private EditText edtSolde; 
	private Button btnAjouter;
	private Button btnAnnuler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ajout_compte_layout);
		
		Bundle extra = this.getIntent().getExtras();
		if(extra != null){
			String numAgence = extra.getString("numClient");
			String nomAgence = extra.getString("nomAgence");
			//        Toast.makeText(this, numAgence+" "+nomAgence, Toast.LENGTH_SHORT).show();
			txtNumClient = (TextView)findViewById(R.id.txtNumClientValue);
			txtNumClient.setText(numAgence);
			txtNomAgence = (TextView)findViewById(R.id.txtNomAgenceValue);
			txtNomAgence.setText(nomAgence);
			edtNumCompte = (EditText)findViewById(R.id.edtNumCompte);
			txtSens = (TextView)findViewById(R.id.txtSensValue);
			txtSens.setText("CR");
			edtSolde = (EditText)findViewById(R.id.edtSolde);
			
			btnAjouter = (Button)findViewById(R.id.btnAjouter);
			btnAnnuler = (Button)findViewById(R.id.btnAnnuler);
			btnAjouter.setOnClickListener(this);
			btnAnnuler.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btnAjouter:
			String action = "nouveauCompte";
			boolean res;
			try{
				Compte compte = new Compte();
				compte.setNumClient(Integer.parseInt(txtNumClient.getText().toString()));
				compte.setNumComte(edtNumCompte.getText().toString());
				compte.setSensCompte(txtSens.getText().toString());
				compte.setSoldeCompte(Integer.parseInt(edtSolde.getText().toString()));
				os = Main.serveur.getOutputStream();
				oos = new ObjectOutputStream(os);
				oos.writeObject(action);
				oos.flush();
				oos = new ObjectOutputStream(os);
				oos.writeObject(compte);
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
					edtNumCompte.setText("");
					edtSolde.setText("");
				}
		}catch(Exception ex){
//			ex.printStackTrace();
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
			break;

		default:
			break;
		}
		
	}
}
