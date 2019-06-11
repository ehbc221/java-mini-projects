package team.conso.system.ui.views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import team.conso.system.ui.ViewPanel;

/**
 * permet d afficher un histograme des depence
 * 
 */
public class PnlHistoGram extends ViewPanel
{
    private static final String VIEW_NAME = "Histogramme";

    JList lstProd = new JList();
    JComboBox cmbPeriod = new JComboBox();
    JComboBox cmbType = new JComboBox();
    JButton btnNext = new JButton( "Suivant" );
    JButton btnPrev = new JButton( "Precedent" );
    
    public PnlHistoGram()
    {
        super();
        setLayout( new BorderLayout() );
        JPanel pnlCommand = new JPanel();
        pnlCommand.setLayout( new BorderLayout() );
        add( pnlCommand, BorderLayout.CENTER );
        JScrollPane scrll = new JScrollPane();
        scrll.setAutoscrolls( true );
		add( scrll, BorderLayout.CENTER );
        scrll.getViewport().add( lstProd );
    }

    public String getViewName()
    {
        return VIEW_NAME;
    }
    
    public String getMnemo()
    {
        return "H";
    }
    public void refresh()
    {
        repaint();
    }
    public void close()
    {
    }
  
    


}
