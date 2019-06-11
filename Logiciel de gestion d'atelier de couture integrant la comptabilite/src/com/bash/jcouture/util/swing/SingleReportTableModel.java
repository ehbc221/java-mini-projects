/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.util.swing;

import com.bash.jcouture.entities.model.Report;
import com.bash.jcouture.util.ReportBuilder;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author bashizip
 */
public class SingleReportTableModel extends AbstractTableModel{

    private ReportBuilder rb;
    private Report report;

    public SingleReportTableModel(Report report) {
        this.report=report;
    }


 public Object getValueAt(int rowIndex, int columnIndex) {

        Object toReturn = null;

        switch (columnIndex) {
            case 0:
                toReturn =report.getIdmouvement_compte();
                break;
            case 1:
                toReturn = report.getEntree();
                break;
            case 2:
                toReturn = report.getPourcentage();
                break;
            default:
                toReturn = report.getIdarticle();
        }

        return toReturn;

    }

    public int getRowCount() {
        return 1;
    }

    public int getColumnCount() {
     return 1;

    }


}
