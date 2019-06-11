package team.conso.system.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import team.conso.data.BDDEco;
import team.conso.data.DataEvent;
import team.conso.data.DataListener;
import team.conso.data.bdd.DataFilter;


/**
 * IHM permettan de definir un ensemble de filtre a fournir au document pour
 * des requete de donnee
 */
public class PnlFilter extends JPanel
{
    DataFilter filtre = new DataFilter();
    PnlLister listCate = new PnlLister();
    PnlLister listProd = new PnlLister();
    BDDEco bdd = null;
    JCheckBox chkValide = new JCheckBox( "produits consommable" );
    JCheckBox chkAlerte = new JCheckBox( "produits presque hors limite" );
    JCheckBox chkPerime = new JCheckBox( "produits hors limite" );
    JButton btnSave = new JButton( "Sauver" );
    JButton btnLoad = new JButton( "Charger" );
    JButton btnOk = new JButton( "Appliquer" );
    JButton btnCancel = new JButton( "Annuler" );
    /**
     * 
     */
    public PnlFilter()
    {
        super();
        setLayout( new GridBagLayout() );
        add( new JLabel( "Filtre sur inventaire" ), 
        		new GridBagConstraints( 
                        0,0,
                        2,1,
                        1.0,1.0,
                        java.awt.GridBagConstraints.CENTER,
                        java.awt.GridBagConstraints.BOTH,
                        new Insets(0,2,0,2),
                        0,0 
                        ) );
        add( chkValide, 
        		new GridBagConstraints( 
                        0,1,
                        2,1,
                        1.0,1.0,
                        java.awt.GridBagConstraints.CENTER,
                        java.awt.GridBagConstraints.BOTH,
                        new Insets(0,2,0,2),
                        0,0 
                        ) );
        add( chkAlerte, 
        		new GridBagConstraints( 
                        0,2,
                        2,1,
                        1.0,1.0,
                        java.awt.GridBagConstraints.CENTER,
                        java.awt.GridBagConstraints.BOTH,
                        new Insets(0,2,0,2),
                        0,0 
                        ) );
        add( chkPerime, 
        		new GridBagConstraints( 
                        0,3,
                        2,1,
                        1.0,1.0,
                        java.awt.GridBagConstraints.CENTER,
                        java.awt.GridBagConstraints.BOTH,
                        new Insets(0,2,0,2),
                        0,0 
                        ) );
        listCate.setLabel( "Catégories" );
        add( listCate, 
    		new GridBagConstraints( 
                    0,4,
                    1,1,
                    1.0,1.0,
                    java.awt.GridBagConstraints.CENTER,
                    java.awt.GridBagConstraints.BOTH,
                    new Insets(2,2,2,2),
                    0,0 
                    ) );
        listProd.setLabel( "Produits" );
        add( listProd, 
    		new GridBagConstraints( 
                    1,4,
                    1,1,
                    1.0,1.0,
                    java.awt.GridBagConstraints.CENTER,
                    java.awt.GridBagConstraints.BOTH,
                    new Insets(2,2,2,2),
                    0,0 
                    ) );
        add( btnLoad, 
    		new GridBagConstraints( 
                    0,5,
                    1,1,
                    1.0,1.0,
                    java.awt.GridBagConstraints.CENTER,
                    java.awt.GridBagConstraints.HORIZONTAL,
                    new Insets(0,2,5,2),
                    0,0 
                    ) );
        add( btnSave, 
    		new GridBagConstraints( 
                    1,5,
                    1,1,
                    1.0,1.0,
                    java.awt.GridBagConstraints.CENTER,
                    java.awt.GridBagConstraints.HORIZONTAL,
                    new Insets(0,2,5,2),
                    0,0 
                    ) );
        add( btnCancel, 
        		new GridBagConstraints( 
                        0,6,
                        1,1,
                        1.0,1.0,
                        java.awt.GridBagConstraints.CENTER,
                        java.awt.GridBagConstraints.HORIZONTAL,
                        new Insets(5,2,0,2),
                        0,0 
                        ) );
        add( btnOk, 
    		new GridBagConstraints( 
                    1,6,
                    1,1,
                    1.0,1.0,
                    java.awt.GridBagConstraints.CENTER,
                    java.awt.GridBagConstraints.HORIZONTAL,
                    new Insets(5,2,0,2),
                    0,0 
                    ) );
        ActionBtn ab = new ActionBtn();
        btnLoad.addActionListener( ab );
        btnSave.addActionListener( ab );
    }

    
    public void addOkActionListener( ActionListener al )
    {
        btnOk.addActionListener( al );
    }
    public void addCancelActionListener( ActionListener al )
    {
        btnCancel.addActionListener( al );
    }
    
    public void setBDDStock( BDDEco bdd )
    {
        this.bdd= bdd;
        setValues();
        bdd.addDataListener( new UpdateListener() );
        
    }
    
    public void setValues()
    {
        if ( bdd == null ) return;
        listProd.setData( bdd.getProdList( false ) );
        listCate.setData( bdd.getCategories( true ) );
    }
    
    class ActionBtn implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            Object source = e.getSource();
            if ( source == btnLoad )
                loadFiltre();
            if ( source == btnSave )
                saveFiltre();
        }
    }
    
    public void loadFiltre()
    {
        try
        {
            JFileChooser jfc = new JFileChooser( System.getProperty( "user.dir" ) );
            jfc.setDialogType( JFileChooser.OPEN_DIALOG );
            if ( jfc.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION ) 
            {
                File inFile = jfc.getSelectedFile();
                FileInputStream fis = new FileInputStream(inFile );            
                ObjectInputStream ois = new ObjectInputStream( fis );
                DataFilter filtre = (DataFilter)ois.readObject();
                ois.close();
                fis.close();
                setFiltre( filtre );
            }
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.pnlFilter.load : " + ex );
        }
        
    }
    public void saveFiltre()
    {
        // get filtre
        try
        {
            JFileChooser jfc = new JFileChooser( System.getProperty( "user.dir" ) );
            jfc.setDialogType( JFileChooser.SAVE_DIALOG );
            if ( jfc.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION ) 
            {
                File outFile = jfc.getSelectedFile();
                FileOutputStream fos = new FileOutputStream( outFile );            
                ObjectOutputStream oos = new ObjectOutputStream( fos );
                oos.writeObject( getFiltre() );
                oos.close();
                fos.close();
                setFiltre( filtre );
            }
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.pnlFilter.save : " + ex );
        }
        
    }
    
    class UpdateListener implements DataListener
    {
        public void changeUpdate(DataEvent dataEvent)
        {
            try
            {
                setValues();
            } catch (Exception e)
            {
                System.out.println( "change update  : " + e);
            }
        }
    }
    

    public DataFilter getFiltre()
    {
        // on sassure ke le filtre est init ;o)
        filtre.setValide( chkValide.isSelected() );
        filtre.setPerime( chkPerime.isSelected() );
        filtre.setAlarme( chkAlerte.isSelected() );
        filtre.setCate( listCate.getCheckedItems() );
        filtre.setProd( listProd.getCheckedItems() );
        return filtre;
    }
    public void setFiltre(DataFilter filtre)
    {
        this.filtre = filtre;
        chkAlerte.setSelected( filtre.isAlarme() );
        chkPerime.setSelected( filtre.isPerime() );
        chkValide.setSelected( filtre.isValide() );
        listCate.setCheckedItems( filtre.getCate() );
        listProd.setCheckedItems( filtre.getProd() );
    }
}
