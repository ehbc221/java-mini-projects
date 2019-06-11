package team.conso.system;

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import team.conso.data.BDDEco;
import team.conso.system.ui.FrmConso;

/**
 * class servant de point d entrer a l aplication 
 * avec 2 options
 * lf = true   : pour utiliser le look & feel de l appli
 * test = true : pour utilise le jeu d essai contenu ds le repertoire data comme donnee de base
 * team.conso.system
 * 
 */
public class Econome
{
    /**
     * Point d entree de l application 
     */
    public static void main( String[] args )
    {
//        System.getProperties().list( System.out ); // c pour avoir la liste des proprietees system
        if ( args.length > 0 )
        {
            for ( int i = 0; i < args.length; i++ )
            {
                if ( args[i].toLowerCase().startsWith( "lf" ) )
                {
                    if ( args[i].substring( args[i].indexOf( "=" )+1 ).trim().compareToIgnoreCase( "true" ) == 0 )
                        setLFDecorated( true );
                }
                else if ( args[i].toLowerCase().startsWith( "test" ) )
                {
                    if ( args[i].substring( args[i].indexOf( "=" )+1 ).trim().compareToIgnoreCase( "true" ) == 0 )
                        BDDEco.setTest( true );
                }
            }
        }
        
	    UIManager.getLookAndFeel().getDefaults().put( "TabbedPane.selected", Color.red );
        UIManager.getLookAndFeel().initialize();
        // instancie la class principale(fenetre)
        FrmConso.getInstance().setVisible( true );//  rend visible la fenetre de l appli
    }
    

    /**
     * change la decoration des fenetre et des boite de dialogue
     * @param LFDecorated
     */
    protected static void setLFDecorated( boolean LFDecorated )
    {
        if ( LFDecorated )
        {
	        JFrame.setDefaultLookAndFeelDecorated( true );
	        JDialog.setDefaultLookAndFeelDecorated( true );
        }
    }
    
    
}
