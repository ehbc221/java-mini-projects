package lez.hackin.houz;

import java.util.List;

import Modele.Agence;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AgenceAdapter extends BaseAdapter {

	private List<Agence> listeAgence;
	private Context oContext;
	private LayoutInflater oInflater;
	
	public AgenceAdapter(Context pContext, List<Agence> pListAgence){
		
		 oContext = pContext;
		 listeAgence = pListAgence;
		 oInflater = LayoutInflater.from(oContext);
	}
	
	@Override
	public int getCount() {
		
		return listeAgence.size();
	}

	@Override
	public Object getItem(int pPosition) {
		
		return listeAgence.get(pPosition);
	}

	@Override
	public long getItemId(int pPosition) {
		
		return pPosition;
	}

	@Override
	public View getView(int pPosition, View pConvertView, ViewGroup pParent) {

		LinearLayout lLayoutItem;
		
		if(pConvertView == null){
			lLayoutItem = (LinearLayout) oInflater.inflate(R.layout.agences_item_layout, pParent, false);
		}
		else {
		  	lLayoutItem = (LinearLayout) pConvertView;
		  }
		
		TextView txtNumAgence = (TextView)lLayoutItem.findViewById(R.id.txtNumAgence);
		TextView txtLibele = (TextView)lLayoutItem.findViewById(R.id.txtLibele);
		TextView txtAdresse = (TextView)lLayoutItem.findViewById(R.id.txtAdresse);
		
		txtNumAgence.setText(listeAgence.get(pPosition).getCodeAgence().trim());
		txtLibele.setText(listeAgence.get(pPosition).getLibAgence().trim());
		txtAdresse.setText(listeAgence.get(pPosition).getAdresseAgence().trim());
		
		return lLayoutItem;
	}

}
