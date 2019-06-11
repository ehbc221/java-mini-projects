/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bash.jcouture.util.swing;

import com.bash.jcouture.entities.model.Report;
import com.bash.jcouture.util.ReportBuilder;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author bashizip
 */
@Deprecated
public class ReportTableModel extends AbstractTableModel {

    private List<Report> reports;
    private String[] colones = {"idarticle", "Client", "Montant", "Compte", "Entree", "Pourcentage"};
    private ReportBuilder rb=new ReportBuilder();

    public ReportTableModel(Date d, Date f) {
        reports = rb.findReportEntities(d, f);
        
        System.out.println(rb.getTotalEntree()+"");

    }

    public int getRowCount() {
        return reports.size();
    }

    public int getColumnCount() {
        return colones.length;
    }

    @Override
    public String getColumnName(int column) {
        return colones[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        Report r = reports.get(rowIndex);
        Object toReturn = null;

        switch (columnIndex) {
            case 0:
                toReturn = r.getIdarticle();
                break;
            case 1:
                toReturn = r.getClient();
                break;
            case 2:
                toReturn = r.getMontant();
                break;
            case 3:
                toReturn = r.getCompte();
                break;

            case 4:
                toReturn = r.getEntree();
                break;
            case 5:
                toReturn = r.getPourcentage();
                break;
            default:
                toReturn = r.getIdarticle();
        }

        return toReturn;

    }
}
