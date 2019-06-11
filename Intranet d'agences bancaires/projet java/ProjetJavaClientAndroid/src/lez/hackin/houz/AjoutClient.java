package lez.hackin.houz;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import Modele.Client;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AjoutClient extends Activity implements OnClickListener {

	private OutputStream os;
	private ObjectOutputStream oos;
	private InputStream is;
	private ObjectInputStream ois;
	private TextView txtNumAgence;
	private TextView txtNomAgence;
	private TextView txtNumAgenceValue;
	private TextView txtNomAgenceValue;
	private EditText edtNomClient;
	private EditText edtPrenomClient;
	private EditText edtAdresseClient; 
	private Button btnAjouter;
	private Button btnAnnuler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ajout_client_layout);
		
		Bundle extra = this.getIntent().getExtras();
		if(extra != null){
			String numAgence = extra.getString("numAgence");
			String nomAgence = extra.getString("nomAgence");
			//        Toast.makeText(this, numAgence+" "+nomAgence, Toast.LENGTH_SHORT).show();
			txtNumAgenceValue = (TextView)findViewById(R.id.txtNumAgenceValue);
			txtNumAgenceValue.setText(numAgence);
			txtNomAgenceValue = (TextView)findViewById(R.id.txtNomAgenceValue);
			txtNomAgenceValue.setText(nomAgence);
			edtNomClient = (EditText)findViewById(R.id.edtNomClient);
			edtPrenomClient = (EditText)findViewById(R.id.edtPrenomClient);
			edtAdresseClient = (EditText)findViewById(R.id.edtAdresseClient);
			
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
			String action = "nouveauClient";
			boolean res;
			try{
				Client client = new Client();
				client.setNom(edtNomClient.getText().toString());
				client.setPrenoms(edtPrenomClient.getText().toString());
				client.setAdresseCli(edtAdresseClient.getText().toString());
				client.setNomAgence(txtNomAgenceValue.getText().toString());
				client.setCodeAgence(Integer.parseInt(txtNumAgenceValue.getText().toString()));
				os = Main.serveur.getOutputStream();
				oos = new ObjectOutputStream(os);
				oos.writeObject(action);
				oos.flush();
				oos = new ObjectOutputStream(os);
				oos.writeObject(client);
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
					edtNomClient.setText("");
					edtPrenomClient.setText("");
					edtAdresseClient.setText("");
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
