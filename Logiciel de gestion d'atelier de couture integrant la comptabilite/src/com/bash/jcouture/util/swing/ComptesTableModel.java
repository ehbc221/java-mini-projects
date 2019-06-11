/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bash.jcouture.util.swing;

import com.bash.jcouture.entities.model.Compte;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author bashizip
 */
public class ComptesTableModel extends AbstractTableModel {

    private List<com.bash.jcouture.entities.model.Compte> data;
    private String[] colones={"code","Libelé","Designation","Exercice"};

    public ComptesTableModel(List<com.bash.jcouture.entities.model.Compte> data) {
        this.data = data;
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return colones.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Compte c = data.get(rowIndex);

        Object toReturn = null;

        switch (columnIndex) {
            case 0:
                toReturn = c.getIdcompte();
                break;
            case 1:
                toReturn = c.getLibele();
                break;
            case 2:
                toReturn = c.getDescription();
                break;
                case 3:
                toReturn = c.getExercice();
                break;


        }
        return toReturn;

    }

      @Override
    public String getColumnName(int column) {
        return colones[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
