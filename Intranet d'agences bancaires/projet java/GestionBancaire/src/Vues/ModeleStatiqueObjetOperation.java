package Vues;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import Modele.Compte;
import Modele.Operation;

public class ModeleStatiqueObjetOperation extends AbstractTableModel {

	private Operation operations[];
	private final String[] entetes = {"Num. operation","Libele","Sens","Montant","Date"};
	
	public ModeleStatiqueObjetOperation(ArrayList<Operation> listeOperation){
		super();
		operations = new Operation[listeOperation.size()];
		int i = 0;
		for(Operation op : listeOperation){
			operations[i] = new Operation(op.getIdOp(), op.getLibOp(), op.getSensOp(), op.getMontantOp(), op.getDateOp());
			i++;
			}
	}

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return entetes.length;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return operations.length;
	}
	
	public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }

	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch(columnIndex){
        	case 0:
        		return operations[rowIndex].getIdOp();
        	case 1:
        		return operations[rowIndex].getLibOp();
        	case 2:
        		return operations[rowIndex].getSensOp();
        	case 3:
        		return operations[rowIndex].getMontantOp();
        	case 4:
        		return operations[rowIndex].getDateOp();
        	default:
        		return null; //Ne devrait jamais arriver
		}
	}

}
