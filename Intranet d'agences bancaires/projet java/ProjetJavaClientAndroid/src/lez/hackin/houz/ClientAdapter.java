package lez.hackin.houz;

import java.util.List;
import Modele.Client;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClientAdapter extends BaseAdapter {
	private List<Client> listeClient;
	private Context oContext;
	private LayoutInflater oInflater;
	
	public ClientAdapter(Context pContext, List<Client> pListClient){
		
		 oContext = pContext;
		 listeClient = pListClient;
		 oInflater = LayoutInflater.from(oContext);
	}
	
	@Override
	public int getCount() {
		
		return listeClient.size();
	}

	@Override
	public Object getItem(int pPosition) {
		
		return listeClient.get(pPosition);
	}

	@Override
	public long getItemId(int pPosition) {
		
		return pPosition;
	}

	@Override
	public View getView(int pPosition, View pConvertView, ViewGroup pParent) {

		LinearLayout lLayoutItem;
		
		if(pConvertView == null){
			lLayoutItem = (LinearLayout) oInflater.inflate(R.layout.clients_item_layout, pParent, false);
		}
		else {
		  	lLayoutItem = (LinearLayout) pConvertView;
		  }
				
		TextView txtNumClient = (TextView)lLayoutItem.findViewById(R.id.txtNumClient);
		TextView txtNom = (TextView)lLayoutItem.findViewById(R.id.txtNomClient);
		TextView txtPrenom = (TextView)lLayoutItem.findViewById(R.id.txtPrenomClient);
		TextView txtAdresse = (TextView)lLayoutItem.findViewById(R.id.txtAdresse);
		TextView txtAgence = (TextView)lLayoutItem.findViewById(R.id.txtNomAgenceClient);
		
		txtNumClient.setText(String.valueOf(listeClient.get(pPosition).getNumClient()));
		txtNom.setText(listeClient.get(pPosition).getNom().trim());
		txtPrenom.setText(listeClient.get(pPosition).getPrenoms().trim());
		txtAdresse.setText(listeClient.get(pPosition).getAdresseCli().trim());
		txtAgence.setText(listeClient.get(pPosition).getNomAgence().trim());
		
		return lLayoutItem;
	}


}
