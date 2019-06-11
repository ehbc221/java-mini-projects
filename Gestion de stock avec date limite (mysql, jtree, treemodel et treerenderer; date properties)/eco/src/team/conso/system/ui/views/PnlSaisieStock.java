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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.DateFormatter;
import javax.swing.text.MaskFormatter;

import team.conso.data.BDDEco;
import team.conso.data.DataEvent;
import team.conso.data.DataListener;
import team.conso.system.ui.ViewPanel;


/**
 * 
 */
public class PnlSaisieStock extends ViewPanel
{
    private static final String VIEW_NAME = "Saisie";

    JTable table = null;
    private BDDEco bddEco = null;
    
    private JButton  btnValidate = new JButton( "Validation" );
    private JComboBox cmbProd = new JComboBox();
    private JTextField txtQuant = new JTextField();
    private JTextField txtPrix = new JTextField();
    private JFormattedTextField txtDatLim = null;//new JFormattedTextField();
    private JFormattedTextField txtDat = null;//new JFormattedTextField();
    {
        // preparation d un masque de saisie pour les dates
        // voila 2 facon differente de gerer simplement un champ date
        // ( y a mieu mais c bcp plus compliker ;o)
        try {
            MaskFormatter mf = new MaskFormatter( "##/##/20##" ); 
            mf.setPlaceholderCharacter('0');       
            txtDat = new JFormattedTextField( new DateFormatter() );
            txtDatLim    = new JFormattedTextField( mf );
        } catch (Exception e) {}
    }

    public PnlSaisieStock()
    {
        super();
        setLayout( new GridBagLayout() );
        add( new JLabel( "produit : ", JLabel.RIGHT), 
        		new GridBagConstraints( 
                0,0,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,20,0,2),
                0,0 
                ) );
        add( cmbProd, 
        		new GridBagConstraints( 
                1,0,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,2,0,20),
                0,0 
                ) );
        
        add( new JLabel( "Quantité : ", JLabel.RIGHT ), 
        		new GridBagConstraints( 
                0,1,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,20,0,2),
                0,0 
                ) );
        add( txtQuant , 
        		new GridBagConstraints( 
                1,1,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,2,0,20),
                0,0 
                ) );

        add( new JLabel( "Prix : ", JLabel.RIGHT ), 
        		new GridBagConstraints( 
                0,2,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,20,0,2),
                0,0 
                ) );
        add( txtPrix , 
        		new GridBagConstraints( 
                1,2,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,2,0,20),
                0,0 
                ) );
        
        add( new JLabel( "Date limite : ", JLabel.RIGHT ), 
        		new GridBagConstraints( 
                0,3,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,20,0,2),
                0,0 
                ) );
        add( txtDatLim , 
        		new GridBagConstraints( 
                1,3,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,2,0,20),
                0,0 
                ) );
        
        add( new JLabel( "Date d'achat : ", JLabel.RIGHT ), 
        		new GridBagConstraints( 
                0,4,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,20,0,2),
                0,0 
                ) );
        add( txtDat, 
        		new GridBagConstraints( 
                1,4,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,2,0,20),
                0,0 
                ) );
        add( btnValidate, 
        		new GridBagConstraints( 
                0,5,
                2,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,20,0,20),
                0,0 
                ) );
        preInitFields();
        btnValidate.addActionListener( new AL() );
    }
    
    public void setBDDStock( BDDEco bdd )
    {
        bddEco = bdd;
        // rempli la combo en fonction de la base 
        cmbProd.setModel( new ComboProdModel( bddEco.getProdList() ) );
        // en cas de changement il faut reinit la combo 
        // on ne test pas le type de changement pour l instant ( plus simple )
	    bdd.addDataListener( new DataListener() 
	            {
            public void changeUpdate( DataEvent dataEvent ) { setCmbProdList(); }
	            } 			);
    }
    
    /**
     * juste pour faire jolie
     *
     */
    public void preInitFields()
    {
        txtDat.setValue( new Date() );
        txtDatLim.setText( "01/01/2004" );
        txtQuant.setText( "1" );
        txtPrix.setText( "0" );
    }

    public void setCmbProdList()
    {
		((ComboProdModel)cmbProd.getModel()).setVector( bddEco.getProdList() );
    }
    
    public String getViewName()
    {
        return VIEW_NAME;
    }
    public String getMnemo()
    {
        return "S";
    }

    public void refresh()
    {
        repaint();
    }
    public void close()
    {
    }
   
    public void addStock()
    {
        try
        {
            // faire les control d integrite des donnee ici ou en meme temp que l extraction des val
            String d = txtDat.getText();
            Date date = DateFormat.getDateInstance().parse( d );
            Calendar cld = Calendar.getInstance();
            cld.setTime( date );
            d = BDDEco.convertDateToBdd( cld ); // cld.get( Calendar.YEAR ) + "-" + cld.get( Calendar.MONTH ) + "-" + cld.get( Calendar.DAY_OF_MONTH );
            String dl = BDDEco.convertDateToBdd( txtDatLim.getText() );
            String q = txtQuant.getText();
            String p = txtPrix.getText();
            String n = (String)((ComboProdModel)cmbProd.getModel()).getSelectedKey();
            bddEco.addStok( n, q, dl, d, p );
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.views.PnlSaisieStock.addStock() : " + ex );
        }
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
            return (String)keys.get( (String)getSelectedItem() );
        }
        
        public String getKey( String prod ) 
        {
            return (String)keys.get( prod );
        }
    }
    
    class AL implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            Object source = e.getSource();
            if ( source == btnValidate )
            {
                addStock();
                preInitFields();
            }
        }
    }

}
