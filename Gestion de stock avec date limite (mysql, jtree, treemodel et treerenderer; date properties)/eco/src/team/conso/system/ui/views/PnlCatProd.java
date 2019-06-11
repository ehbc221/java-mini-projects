package team.conso.system.ui.views;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import team.conso.data.BDDEco;
import team.conso.data.DataEvent;
import team.conso.data.DataListener;
import team.conso.data.bdd.Categorie;
import team.conso.data.bdd.DataElement;
import team.conso.system.ui.FrmConso;
import team.conso.system.ui.ViewPanel;

/**
 * team.conso.system.ui.views
 * 
 */
public class PnlCatProd extends ViewPanel
{
    private static final String VIEW_NAME = "Nomenclature";

    private JButton btnNewCat = new JButton( "Nouv. Categorie");
    private JButton btnNewPro = new JButton( "Nouv. Produit" );
    private JButton btnFile = new JButton( "Explorer..." );
    private JTextField txtNameProd = new JTextField();
    private JComboBox cmbCate = new JComboBox();
//    private JTextField txtNameCate = new JTextField();
    private JTextField txtFile = new JTextField();
    private BDDEco bddEco = null;

    public PnlCatProd()
    {
        super();
        setLayout( new GridBagLayout() );
        add( new JLabel( "Libellé Produit ou catégorie : " ), 
        		new GridBagConstraints( 
                0,0,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,2,0,2),
                0,0 
                ) );
        add( cmbCate, 
        		new GridBagConstraints( 
                0,1,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,2,0,2),
                0,0 
                ) );
        add( new JLabel( "Image :" ), 
        		new GridBagConstraints( 
                0,2,
                1,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,2,0,2),
                0,0 
                ) );
        add( txtNameProd, 
        		new GridBagConstraints( 
                1,0,
                2,1,
                1.0,1.0,
                java.awt.GridBagConstraints.CENTER,
                java.awt.GridBagConstraints.HORIZONTAL,
                new Insets(0,2,0,2),
                0,0 
                ) );
//        add( txtNameCate, 
//        		new GridBagConstraints( 
//                1,2,
//                2,1,
//                1.0,1.0,
//                java.awt.GridBagConstraints.CENTER,
//                java.awt.GridBagConstraints.HORIZONTAL,
//                new Insets(0,2,0,2),
//                0,0 
//                ) );
//        add( txtFile, 
//        		new GridBagConstraints( 
//                1,1,
//                1,1,
//                1.0,1.0,
//                java.awt.GridBagConstraints.CENTER,
//                java.awt.GridBagConstraints.HORIZONTAL,
//                new Insets(0,2,0,2),
//                0,0 
//                ) );
        JPanel pnl = new JPanel();
        pnl.setLayout( new BorderLayout() );
        pnl.add( txtFile, BorderLayout.CENTER );
        pnl.add( btnFile, BorderLayout.EAST );
        
	    add( pnl, 
				new GridBagConstraints( 
		        1,2,
		        2,1,
		        1.0,1.0,
		        java.awt.GridBagConstraints.CENTER,
		        java.awt.GridBagConstraints.HORIZONTAL,
		        new Insets(0,2,0,2),
		        0,0 
		        ) );
	    pnl = new JPanel();
	    add( pnl, 
				new GridBagConstraints( 
		        0,3,
		        3,1,
		        1.0,1.0,
		        java.awt.GridBagConstraints.CENTER,
		        java.awt.GridBagConstraints.HORIZONTAL,
		        new Insets(0,2,0,2),
		        0,0 
		        ) );
	    pnl.setLayout( new GridLayout( 1, 2 ) );
	    pnl.add( btnNewCat );  
	    pnl.add( btnNewPro );  
	    
        AL al = new AL();
        btnFile.addActionListener( al );
        btnNewCat.addActionListener( al );
        btnNewPro.addActionListener( al );
    }

    public String getViewName()
    {
        return VIEW_NAME;
    }
    public void refresh()
    {
    }
    public void close()
    {
    }
    public String getMnemo()
    {
        return "N";
    }

    class AL implements ActionListener
    {
    	public void actionPerformed( ActionEvent e )
        {
    	    Object source = e.getSource();
    	    if ( source == btnFile )
    	    {
    	        String s = getFile();
    	        if ( s != null )
    	            txtFile.setText( s );
    	    }
    	    else if ( source == btnNewCat )
    	    {
    	        addCategorie();
    	    }
    	    else if ( source == btnNewPro )
    	    {
    	        addProduit();
    	    }
        }
    }
    
    public void setBDDStock( BDDEco bdd )
    {
        bddEco = bdd;
		// en cas de changement il faut reinit la combo 
		cmbCate.setModel( new ComboCateModel( bddEco.getCategories() ) );
        // on ne test pas le type de changement pour l instant ( plus simple )
	    bdd.addDataListener( new DataListener() 
	            {
            public void changeUpdate( DataEvent dataEvent ) { setValues(); }
	            } 			);
    }
    
    public void setValues()
    {
		((ComboCateModel)cmbCate.getModel()).setVector( bddEco.getCategories() );
    }
    

    private void addCategorie()
    {
        String s = checkFields( "cat" );
        if ( s != null )
        {
            bddEco.addCate( txtNameProd.getText(), s );
        }
    }
    
    private void addProduit()
    {
        String s = checkFields( "pro" );
        if ( s != null )
            bddEco.addProd( txtNameProd.getText(), s, ""+(cmbCate.getSelectedIndex()+1) );
    }
    
    private String checkFields( String fields )
    {
        if ( txtNameProd.getText().trim().length() == 0 ) 
        {
            JOptionPane.showMessageDialog( this, "La zone 'Nom' doit etre remplit.", "Controle de saisie", JOptionPane.WARNING_MESSAGE );
            return null;
        }
        
        // ajouter le control du contenu ici ( le champ existe il deja ? )
        
        if ( txtFile.getText().trim().length() != 0 )
            return copyImageFile( txtFile.getText() );
        else
            return "";
    }
    
    /**
     * use pour copier les fichier image ds un repertoire local a l appli
     * @return
     */
    private String copyImageFile( String path )
    {
        String newPath = null;
        try
        {
			// rajout du numero de lavion au nom du fichier pour eviter les duplication de nom
			StringTokenizer stk = new StringTokenizer(path, "\\" );
			String nameFile = "";
			while( stk.hasMoreTokens() )
			{ nameFile = stk.nextToken(); }
			
			nameFile =  DataElement.IMAGE_ROOT + nameFile;
			
			if ( !copyFile( path, nameFile ) ) return null;
			newPath = nameFile;
        } catch (Exception e)
        {
            System.out.println( " exception  : " + e );
        }
        return newPath;
    }
    
    /**
     * copy un fichier de source vers dest .. ;o)
     * @param source
     * @param dest
     * @return
     */
    private boolean copyFile( String source, String dest )
    {
        try
        {
			File fi = new File( source );
			File fo = new File( dest );
//			fo.mkdirs(); // crer le repertoire si necessaire 
			// on li tou le fichier en 1 seul foi ... ;o) c un peu bourrin 
			// mais ca le fai tre bien ;o)
			byte[] b = new byte[(int) fi.length()];
			FileInputStream fis = new FileInputStream( fi );
			// crer le fichier 
			FileOutputStream fos = new FileOutputStream( dest );
			fis.read(b);
			fos.write(b);
			fos.close();
			fis.close();
        } catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    private String getFile()
    {
        String result = "";
        try
        {
        	java.awt.FileDialog openFileDialog = new java.awt.FileDialog( FrmConso.getInstance() );
    		openFileDialog.setMode( FileDialog.LOAD );
    		openFileDialog.setTitle( "Recherche d'un fichier" );
    		String rep  = System.getProperty( "user.dir" ); // par defau on le met sur ce rep
    	    openFileDialog.setDirectory( rep );
    		openFileDialog.setVisible( true );
    		String s  = openFileDialog.getFile();
    		String d  = openFileDialog.getDirectory();
    		result = d+s;
            
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.views.PnlCatProd.getFile(  )  : " + ex );
        }
        return result;
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
}
