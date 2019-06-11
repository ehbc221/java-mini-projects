package team.conso.system.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import team.conso.data.BDDEco;
import team.conso.system.ui.FrmConso;
import team.conso.system.util.Archiver;


/**
 * action permettan l import d une base de donne deja exporter
 */
public class Import extends AbstractAction
{
    String tmpPath  = System.getProperty( "java.io.tmpdir" );
    String tmpExtend = "tbl";
    String comment = "Archived by Eco prog ;o)";

    public Import()
    {
        super();
    }
 
    public void actionPerformed( ActionEvent e )
    {
        try
        {
            FrmConso appli = (FrmConso)getValue( "appli" );
            JFileChooser jfc = new JFileChooser( System.getProperty( "user.dir" ) );
            jfc.setDialogType( JFileChooser.OPEN_DIALOG );
            if ( jfc.showOpenDialog( appli ) == JFileChooser.APPROVE_OPTION ) 
            {
                File inFile = jfc.getSelectedFile();
                if ( inFile != null )
                {
                    Archiver ar = new Archiver( inFile.getPath(), Archiver.MODE_READ );
                    ar.setArchiveWithRep( false );
                    ar.setRestorePath( tmpPath );
                    File[] files = getFiles( ar );
                    // rempli la base avec les fichier provenant de l archive 
                    BDDEco bdd =appli.getBdd();
                    for ( int i = 0; i < BDDEco.tablesName.length; i++ ) 
                    { 
                        bdd.loadDataInFile( BDDEco.tablesName[ i ], files[i].getPath() );
                        files[i].delete();
                    }
                    ar.buildArchive();
                    // efface les fichier temporarie qui on servi a la creation de l archive
                    for ( int i = 0; i < files.length; i++ ) { files[ i ].delete(); }
                }
                JOptionPane.showConfirmDialog( appli, "Archivage dans " + inFile.getPath() + " terminé.", "Export de base", JOptionPane.INFORMATION_MESSAGE );
            }
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
    public File[] getFiles( Archiver ar )
    {
        File[] files = null;//new File[ bdd.tablesName.length ];
        try
        {
            files = new File[ BDDEco.tablesName.length ];
            for (int i = 0; i < BDDEco.tablesName.length; i++) 
            {
                String fileName = BDDEco.tablesName[ i ] + tmpExtend;
                files[ i ] = ar.extractFile( fileName );
            }
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.actions.Export.getFiles() : " + ex );
        }
        return files;
    }
 }