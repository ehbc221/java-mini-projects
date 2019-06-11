package Vues;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import Modele.Client;

@SuppressWarnings("serial")
public class ModeleStatiqueObjetClient extends AbstractTableModel {

	private Client clients[];
	private final String[] entetes = {"Num. client","Nom","Prenom","Adresse","Nom agence"};
	
	public ModeleStatiqueObjetClient(ArrayList<Client> listeClients){
		super();
		clients = new Client[listeClients.size()];
		int i = 0;
		for(Client cli : listeClients){
			clients[i] = new Client(cli.getNumClient(), cli.getNom(), cli.getPrenoms(),cli.getAdresseCli(),cli.getNomAgence());
			i++;
			}
	}

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return entetes.length;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return clients.length;
	}
	
	public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }

	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch(columnIndex){
        	case 0:
        		return clients[rowIndex].getNumClient();
        	case 1:
        		return clients[rowIndex].getNom();
        	case 2:
        		return clients[rowIndex].getPrenoms();
        	case 3:
        		return clients[rowIndex].getAdresseCli();
        	case 4:
        		return clients[rowIndex].getNomAgence();
        	default:
        		return null; //Ne devrait jamais arriver
		}
	}
}
