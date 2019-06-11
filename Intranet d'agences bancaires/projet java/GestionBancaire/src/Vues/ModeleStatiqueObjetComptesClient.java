package Vues;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import Modele.Compte;

@SuppressWarnings("serial")
public class ModeleStatiqueObjetComptesClient extends AbstractTableModel{

	private Compte comptes[];
	private final String[] entetes = {"Num. compte","Libele compte","Solde","Sens"};
	
	public ModeleStatiqueObjetComptesClient(ArrayList<Compte> listeComptesClient){
		super();
		comptes = new Compte[listeComptesClient.size()];
		int i = 0;
		for(Compte cpt : listeComptesClient){
			comptes[i] = new Compte(cpt.getNumCompte(), cpt.getLibeleCompte(), cpt.getSensCompte(), cpt.getSoldeCompte());
			i++;
			}
	}

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return entetes.length;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return comptes.length;
	}
	
	public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }

	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch(columnIndex){
        	case 0:
        		return comptes[rowIndex].getNumCompte();
        	case 1:
        		return comptes[rowIndex].getLibeleCompte();
        	case 2:
        		return comptes[rowIndex].getSoldeCompte();
        	case 3:
        		return comptes[rowIndex].getSensCompte();
        	default:
        		return null; //Ne devrait jamais arriver
		}
	}

}
