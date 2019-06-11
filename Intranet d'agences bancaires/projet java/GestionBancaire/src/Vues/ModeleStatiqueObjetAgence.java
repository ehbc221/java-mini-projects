package Vues;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import Modele.Agence;

@SuppressWarnings("serial")
public class ModeleStatiqueObjetAgence extends AbstractTableModel {
	
	private Agence agences[];
	private final String[] entetes = {"Code agence","Nom agence","Adresse"};
	
	public ModeleStatiqueObjetAgence(ArrayList<Agence> listeAgences){
		super();
		agences = new Agence[listeAgences.size()];
		int i = 0;
		for(Agence ag : listeAgences){
			agences[i] = new Agence(ag.getCodeAgence().toString(), ag.getLibAgence(), ag.getAdresseAgence());
			i++;
			}
	}

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return entetes.length;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return agences.length;
	}
	
	public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }

	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch(columnIndex){
        	case 0:
        		return agences[rowIndex].getCodeAgence();
        	case 1:
        		return agences[rowIndex].getLibAgence();
        	case 2:
        		return agences[rowIndex].getAdresseAgence();
        	default:
        		return null; //Ne devrait jamais arriver
		}
	}
}
