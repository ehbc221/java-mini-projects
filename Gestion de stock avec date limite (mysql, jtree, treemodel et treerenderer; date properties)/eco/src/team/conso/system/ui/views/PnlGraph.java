package team.conso.system.ui.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import team.conso.data.BDDEco;
import team.conso.data.DataEvent;
import team.conso.data.DataListener;
import team.conso.data.bdd.Categorie;
import team.conso.system.ui.ViewPanel;
import team.conso.system.ui.views.graph.PnlChart;


/**
 */
public class PnlGraph extends ViewPanel
{
    private static final String VIEW_NAME = "Graph d'achat";

    PnlChart pnlChart = new PnlChart();
    JComboBox cmbCate  = new JComboBox();
    
    /**
     * 
     */
    public PnlGraph()
    {
        super();
        setLayout( new BorderLayout() );
        add( pnlChart, BorderLayout.CENTER );
        JPanel pnlCommand = new JPanel();
        add( pnlCommand, BorderLayout.NORTH );
        pnlCommand.setLayout( new FlowLayout( FlowLayout.RIGHT ) ); //new BorderLayout() );
        pnlCommand.add( cmbCate ); //, BorderLayout.CENTER );
        cmbCate.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e )
            {
                setOrdonne( cmbCate.getSelectedItem().toString() ); 
            }
        } );
    }

    public String getViewName()
    {
        return VIEW_NAME;
    }
    public String getMnemo()
    {
        return "G";
    }
    
    public void refresh()
    {
        repaint();
    }
    public void close()
    {
    }

    public void setBDDStock( BDDEco bdd )
    {
        try
        {
            pnlChart.setBDDStock( bdd );
            // rempli la combo en fonction de la base 
            cmbCate.setModel( new ComboCateModel( pnlChart.getBdd().getCategoriesEnStock() ) );
            cmbCate.setSelectedIndex( 0 );
                      // en cas de changement il faut reinit la combo 
            // on ne test pas le type de changement pour l instant ( plus simple )
            bdd.addDataListener( new DataListener() 
                    {
                public void changeUpdate( DataEvent dataEvent ) { updateProd(); }
                    }           );
        } catch ( Exception e )
        {
            System.out.println( " stock e = " + e );
        }
    }
    
    public void updateProd()
    {
        try
        {
            Vector v = pnlChart.getBdd().getCategoriesEnStock();
            
    		((ComboCateModel)cmbCate.getModel()).setVector( v );
        } catch (Exception e)
        {
            System.out.println( "error " + e );
        }
    }
    

    /**
     * @param string
     */
    public void setOrdonne( String ord )
    {
        pnlChart.setOrdonne( ord );
        pnlChart.setYLibelle( ord );
        
        
    }
    /**
     *
     */
    class ComboCateModel extends DefaultComboBoxModel
    {
        public ComboCateModel( Vector list )
        {
            setVector( list );
        }
        public void setVector( Vector list )
        {
            try
            {
                try
                {
                    removeAllElements();
                } catch (Exception e)
                {
                }
                for (Iterator iter = list.iterator(); iter.hasNext();)
                {
                    Categorie elt = (Categorie) iter.next();
                    addElement( elt.getLibelle() );
                }
            } catch (Exception e)
            {
                System.out.println("remove error : " + e);
            }
        }
    }
    
    
    
    
}
