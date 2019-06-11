package team.conso.system.ui.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.text.DateFormatter;

import team.conso.data.BDDEco;
import team.conso.data.DataEvent;
import team.conso.data.DataListener;
import team.conso.data.bdd.Categorie;
import team.conso.system.ui.ViewPanel;

/**
 * cete class permet d afficher plusieur bilan financier
 * j en ai trouver que 2 pour l instant
 * 
 */
public class PnlBilan extends ViewPanel
{
    private static final String VIEW_NAME = "Bilan";

    private JComboBox cmbProd = new JComboBox();
    private JComboBox cmbCate = new JComboBox();

    private JLabel lblProd = new JLabel();
    private JLabel lblCate = new JLabel();
    private JLabel lblTotal = new JLabel();
    private JButton btnTotal = new JButton( "Total des depences : " );
    
    private JFormattedTextField txtDatDeb = new JFormattedTextField( new DateFormatter() );
    private JFormattedTextField txtDatFin = new JFormattedTextField( new DateFormatter() );
    
    private BDDEco bddEco = null;
    
    public PnlBilan ()
    {
        super();
        setLayout( new GridBagLayout() );
        add( cmbProd, 
        		new GridBagConstraints( 
                0,0,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,20,0,2),
                0,0 
                ) );
        add( lblProd, 
        		new GridBagConstraints( 
                1,0,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,10,0,20),
                0,0 
                ) );
        
        add( cmbCate, 
        		new GridBagConstraints( 
                0,1,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,20,0,2),
                0,0 
                ) );
        add( lblCate, 
        		new GridBagConstraints( 
                1,1,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,10,0,20),
                0,0 
                ) );
        add( btnTotal, 
        		new GridBagConstraints( 
                0,2,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,20,0,2),
                0,0 
                ) );
        add( lblTotal, 
        		new GridBagConstraints( 
                1,2,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,10,0,20),
                0,0 
                ) );

        add( txtDatDeb, 
        		new GridBagConstraints( 
                0,3,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,10,0,20),
                0,0 
                ) );
        add( txtDatFin, 
        		new GridBagConstraints( 
                1,3,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,10,0,20),
                0,0 
                ) );

        CmbAction ca = new CmbAction();
        cmbCate.addActionListener( ca );
        cmbProd.addActionListener( ca );
        btnTotal.addActionListener( ca );
        preInitFields();
    }
    
    public void setBDDStock( BDDEco bdd )
    {
        try
        {
            bddEco = bdd;
            // rempli les combo en fonction de la base 
            cmbProd.setModel( new ComboProdModel( bddEco.getProdList() ) );
            cmbCate.setModel( new ComboCateModel( bddEco.getCategoriesEnStock() ) );
            // en cas de changement il faut reinit la combo 
            // on ne test pas le type de changement pour l instant ( plus simple )
            bdd.addDataListener( new DataListener() 
                    {
                public void changeUpdate( DataEvent dataEvent ) { setValues(); upDateLbl(); }
                    }           );
            setValues();
        } catch ( Exception e )
        {
            System.out.println( " error stock " + e );
        }
    }
    
    public void setValues()
    {
		((ComboProdModel)cmbProd.getModel()).setVector( bddEco.getProdList() );
		((ComboCateModel)cmbCate.getModel()).setVector( bddEco.getCategoriesEnStock() );
    }

    public void preInitFields()
    {
        txtDatDeb.setValue( new Date() );
        txtDatFin.setValue( new Date() );
    }
    /**
	 * met a jour le contenu des labels
     */
    public void upDateLbl()
    {
        try
        {
            if ( bddEco == null ) return;
            Date date = DateFormat.getDateInstance().parse( txtDatDeb.getText() );
            Calendar dateDeb = Calendar.getInstance();
            dateDeb.setTime( date );
            date = DateFormat.getDateInstance().parse( txtDatFin.getText() );
            Calendar dateFin = Calendar.getInstance();
            dateFin.setTime( date );
            if ( dateFin.before( dateDeb ) ) 
            {
                lblCate.setText("");
                lblProd.setText("");
                return;
            }   
            if ( cmbCate.getSelectedItem() != null )
                lblCate.setText( bddEco.getPrixCate( cmbCate.getSelectedItem().toString(), BDDEco.convertDateToBdd( dateDeb ), BDDEco.convertDateToBdd( dateFin ) ) );
          
            if ( ((ComboProdModel)cmbProd.getModel()).getSelectedKey() != null )
            {
                lblProd.setText( bddEco.getPrixProd( ((ComboProdModel)cmbProd.getModel()).getSelectedKey(), BDDEco.convertDateToBdd( dateDeb ), BDDEco.convertDateToBdd( dateFin ) ) );
            }
            lblTotal.setText( bddEco.getPrixProd( "", BDDEco.convertDateToBdd( dateDeb ), BDDEco.convertDateToBdd( dateFin ) ) );
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.views.PnlBilan.upDateLbl() : " + ex );
        }
    }
    
    public String getViewName()
    {
        return VIEW_NAME;
    }
    
    public void refresh()
    {
        repaint();
    }
    public String getMnemo()
    {
        return "B";
    }
    public void close()
    {
        
    }
    
    /**
     * model permettan e stoker la cle du produi c plus simple a retrouver comme ca 
     * pas besoin de requette
     *
     */
    class ComboProdModel extends DefaultComboBoxModel
    {
        Hashtable keys = new Hashtable();
        public ComboProdModel( Vector list )
        {
            setVector( list );
        }
        
        public void setVector( Vector list )
        {
            removeAllElements();

            for (Iterator iter = list.iterator(); iter.hasNext();)
            {
                String elt = (String) iter.next();
                String key = (String) iter.next();
                addElement( elt );
                keys.put( elt, key );
            }
        }
        
        public String getSelectedKey()
        {
            
            return (String)keys.get( (String)(getSelectedItem()==null?"null":(String)getSelectedItem()) );
        }
        
        public String getKey( String prod ) 
        {
            return (String)keys.get( prod );
        }
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
           removeAllElements();
           for (Iterator iter = list.iterator(); iter.hasNext();)
           {
               Categorie elt = (Categorie) iter.next();
               addElement( elt.getLibelle() );
           }
       }
   }
   
   class CmbAction implements ActionListener
   {
	    public void actionPerformed( ActionEvent e )
	    {
	        upDateLbl();
	    }
   }

}
