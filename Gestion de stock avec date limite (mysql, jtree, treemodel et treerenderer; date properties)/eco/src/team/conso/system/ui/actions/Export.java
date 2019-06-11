package team.conso.system.ui.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.UIManager;

import team.conso.data.BDDEco;
import team.conso.system.ui.FrmConso;


/**
 * action d exporter une base existatnte
 */
public class Export extends AbstractAction
{
    String tmpPath  = System.getProperty( "java.io.tmpdir" );
    String tmpExtend = "tbl";
    String comment = "Archived by Eco prog ;o)";

    public Export ()
    {
        super();
    }
    int idxOld = -1;
    public void actionPerformed( ActionEvent e )
    {
        try
        {
            FrmConso appli = (FrmConso)getValue( "appli" );
            appli.mainPane.setBackground( Color.blue );
            int idx = appli.mainPane.getSelectedIndex();
            if ( idxOld >= 0 )
                appli.mainPane.setBackgroundAt( idxOld, appli.mainPane.getBackground() );
            System.out.println( "bck col = " + appli.mainPane.getBackground() );
//            appli.mainPane.setBackgroundAt( idx, Color.red );
//            appli.mainPane.getSelectedComponent().
//    	    UIManager.getDefaults().put("TabbedPane.selected", Color.cyan );
    	    UIManager.getLookAndFeel().getDefaults().put( "TabbedPane.selected", Color.red );
//    	    UIManager.getLookAndFeel().initialize();
//    	    System.out.println( "col = " + c );
//            appli.mainPane.getUI();
//            SelectedComponent().setBackground( Color.green );
            idxOld = idx;
            
//            JFileChooser jfc = new JFileChooser( System.getProperty( "user.dir" ) );
//            jfc.setDialogType( JFileChooser.SAVE_DIALOG );
//            if ( jfc.showOpenDialog( appli ) == JFileChooser.APPROVE_OPTION ) 
//            {
//                File outFile = jfc.getSelectedFile();
//                if ( outFile != null )
//                {
//                    Archiver ar = new Archiver( outFile.getPath(), Archiver.MODE_CREATE );
//                    ar.setComment( comment );
//                    ar.setArchiveWithRep( false );
//                    File[] files = getFiles();
//                    // archive les fichier
//                    for ( int i = 0; i < files.length; i++ ) { ar.addToArchive( files[ i ] ); }
//                    ar.buildArchive();
//                    // efface les fichier temporarie qui on servi a la creation de l archive
//                    for ( int i = 0; i < files.length; i++ ) { files[ i ].delete(); }
//                }
//                JOptionPane.showConfirmDialog( appli, "Archivage dans " + outFile.getPath() + " terminé.", "Export de base", JOptionPane.INFORMATION_MESSAGE );
//            }
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.actions.Export.actionPerformed() : " + ex );
        }
    }
    
    /**
     * retourne des fichier temporaire pour l archivage
     * s assurer que les fichier seron effacer
     * @return
     */
    public File[] getFiles()
    {
        File[] files = null;//new File[ bdd.tablesName.length ];
        try
        {
            BDDEco bdd = ((FrmConso)getValue( "appli" )).getBdd();
            files = new File[ BDDEco.tablesName.length ];
            for (int i = 0; i < BDDEco.tablesName.length; i++) 
            {
                String fileName = BDDEco.tablesName[ i ] + tmpExtend;
                bdd.selectIntoOutFile( BDDEco.tablesName[ i ], tmpPath, fileName );
                files[ i ] = new File( tmpPath + fileName );
            }
            
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.actions.Export.getFiles() : " + ex );
        }
        return files;
    }
    
}