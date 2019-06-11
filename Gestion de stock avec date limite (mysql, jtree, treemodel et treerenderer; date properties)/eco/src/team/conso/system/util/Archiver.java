package team.conso.system.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * class permetan de crer un fichier d archive de type Zip
 * ou de le lire
 */
public class Archiver
{
    public static final int MODE_CREATE = 0;
    public static final int MODE_READ   = 1;
    public static final int MODE_LIST   = 2;
    
    private int mode = MODE_CREATE;
    private String archiveName = "";
    private String restorePath = ".\\";
    private boolean useRep = true;
    
    private Vector files;

    /**
     * 
     * parcour un repertoire de fichier et effectue une action par fichier
     * @param files
     * */
    private int readRep( File files )
    {
        int cpt = 0;
        try
        {
            if ( files.isDirectory() )
            {
                File[] f = files.listFiles();// new FilterFileJava() );
                int c = 0;
                for ( int i = 0; i < f.length; i++ ) 
                {
                    File file = f[ i ];
                    readRep( file );
                }
            }
            else
            {
                // action sur file
//                System.out.println( files.getPath() );
                addToVector( files );
            }
        }
        catch (Exception e)
        {
            System.out.println( " linesInFiles  eror : " + e ); //$NON-NLS-1$
        }
        return cpt;
    }

    /**
     * Method makeAction.
     * @param file
     */
    private void addToVector( File file )
    {
        files.add( file );
    }
    
    
    private byte[] buffer; 
    
    /**
     * 
     * construi larchive a partir des fichier du chemin specifier
     * ds le fichier archive
     */
    public void buildArchive( String path )
    {
        add( path );
        buildArchive();
    }
    
    /**
     * 
     * construi larchive a partir de la liste des fichier 
     * ds le fichier archive
     * 
     */
    public void buildArchive()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream( getArchiveName() );
            BufferedOutputStream bos = new BufferedOutputStream( fos );
            ZipOutputStream zos = new ZipOutputStream( bos );
            
            for (Iterator iter = files.iterator(); iter.hasNext();)
            {
                File file = (File) iter.next();
                FileInputStream fis = new FileInputStream( file );
                BufferedInputStream buf = new BufferedInputStream( fis );
                ZipEntry ze = new ZipEntry( getNameEntry( file ) );
                zos.putNextEntry( ze );
                int n = 0;
                while ((n=buf.read(buffer))>0)
                    zos.write( buffer, 0, n );
                buf.close();
                zos.closeEntry();
            }
            if ( comment != null )
                zos.setComment( comment );
            zos.close();
            
        }
        catch (Exception e)
        {
            System.out.println("Erreur : " + e);
        }
    }
    
    ZipOutputStream archiveIn = null; 
    ZipFile archiveOut = null;
    /**
     * ouvre un fichier d archive pre  a etre rempli fichier apres fichier
     * ne pasq oublier de le fermer apres
     * 
     * @see addFileToArchive, closeArchive
     */
    public void openArchive()
    {
        try
        {
            if ( mode == MODE_READ )
            {
                archiveOut = new ZipFile( getArchiveName() );
            }
            else
            {
                FileOutputStream fos = new FileOutputStream( getArchiveName() );
                BufferedOutputStream bos = new BufferedOutputStream( fos );
                archiveIn = new ZipOutputStream( bos );
            }
        }
        catch (Exception e)
        {
            System.out.println("Erreur : " + e);
        }
    }
    
    public File extractFile( String name )
    {
        try
        {
            return extractFile( name, null ); 
        }
        catch (Exception e)
        {
            System.out.println("Erreur : " + e);
        }
        return null;
    }
    
    public File extractFile( String name, File file )
    {
        if ( mode != MODE_READ ) return null;
        if ( archiveOut == null ) openArchive();
        try
        {
            ZipEntry ze = archiveOut.getEntry( name );
            String path = ze.getName();
            if ( file == null )
            {
                if ( !useRep )
                {
                    File f = new File( path );
                    path = f.getName();
                }
                file = new File( restorePath + path );
                if ( !file.getParentFile().exists() )
                    file.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream( file );
            InputStream is = archiveOut.getInputStream( ze );
            int n = 0;
            while ((n=is.read(buffer))>0)
                fos.write( buffer, 0, n );
            is.close();
            fos.close();
//                    v.add( ((ZipEntry)enum.nextElement()).getName() );
            return file;
        }
        catch (Exception e)
        {
            System.out.println("Erreur : " + e);
        }
        return null;
    }
    
    /**
     * 
     * 
     * @see openArchive, closeArchive
     */
    public void addFileToArchive( File file, String archivedName )
    {
        try
        {
            FileInputStream fis = new FileInputStream( file );
            BufferedInputStream buf = new BufferedInputStream( fis );
            ZipEntry ze = new ZipEntry( archivedName );
            archiveIn.putNextEntry( ze );
            int n = 0;
            while ((n=buf.read(buffer))>0)
                archiveIn.write( buffer, 0, n );
            buf.close();
            archiveIn.closeEntry();
        }
        catch (Exception e)
        {
            System.out.println("Erreur addFileToArchive: " + e);
            System.out.println( "file = " + file.getPath() );
            System.out.println( "achive = " + archivedName );
        }
    }

    /**
     * 
     * 
     * @see openArchive, closeArchive
     */
    public void addObjectToArchive( Object obj, String archivedName )
    {
//        try
//        {
////            File file 
////            FileInputStream fis = new FileInputStream( file );
//            
////            ObjectInputStream io = new ObjectInputStream();
////            ObjectOutputStream oo = new ObjectOutputStream(  );
//            InputStream is = new InputStream()
//            {
//                public int read() throws IOException
//                {
//                    return 0;
//                }
//                public int read(byte[] b, int off, int len) throws IOException
//                {
//                    return 0;
//                }
//            };
//            BufferedInputStream buf = new BufferedInputStream( is );
//            
//            
//            ZipEntry ze = new ZipEntry( archivedName );
//            archive.putNextEntry( ze );
//            int n = 0;
//            while ((n=buf.read(buffer))>0)
//                archive.write( buffer, 0, n );
//            buf.close();
//            archive.closeEntry();
//        }
//        catch (Exception e)
//        {
//            System.out.println("Erreur : " + e);
//        }
    }

    
    String comment = null;
    public void setComment( String comment )
    {
        this.comment = comment;
    }
    
    /**
     * 
     * 
     * @see openArchive, addFileToArchive
     */
    public void closeArchive()
    {
        try
        {
            if ( comment != null )
                archiveIn.setComment( comment );
            archiveIn.close();
        }
        catch (Exception e)
        {
            System.out.println("Erreur closeArchive : " + e);
        }
    }
    
    public void setArchiveWithRep( boolean f )
    {
        useRep = f;
    }
    
    private String getNameEntry( File f )
    {
        String s = f.getPath();
        if ( !useRep )
            s = f.getName();
        else
            s = s.substring( s.indexOf( ":" ) + 2 );
        return s;
    }
    
    /**
     * extrait l archive ds le chemin de l'archive
     */
    public void extract()
    {
        try
        {
            ZipFile zp = new ZipFile( getArchiveName() );
            Enumeration enu = zp.entries();
            while ( enu.hasMoreElements() )
            {
                ZipEntry ze = (ZipEntry)enu.nextElement();
                String path = ze.getName();

                if ( !useRep )
                {
                    File f = new File( path );
                    path = f.getName();
                }
                File f = new File( restorePath + path );
                if ( !f.getParentFile().exists() )
                    f.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream( f );
                InputStream is = zp.getInputStream( ze );
                int n = 0;
                while ((n=is.read(buffer))>0)
                    fos.write( buffer, 0, n );
                is.close();
                fos.close();
//                    v.add( ((ZipEntry)enum.nextElement()).getName() );
            }
        }
        catch (Exception e)
        {
            System.out.println("Erreur : " + e);
        }
    }

    /**
     * extrait l archive ds le chemin specifier
     */
    public void extract( String path )
    {
        try
        {
            ZipFile zp = new ZipFile( getArchiveName() );
            Enumeration enu = zp.entries();
            while ( enu.hasMoreElements() )
            {
                ZipEntry ze = (ZipEntry)enu.nextElement();
                String pathEntry = ze.getName();

                if ( !useRep )
                {
                    File f = new File( pathEntry );
                    pathEntry = f.getName();
                }
                
                File f = new File( path + pathEntry );
                if ( !f.getParentFile().exists() )
                    f.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream( f );
                InputStream is = zp.getInputStream( ze );
                int n = 0;
                while ((n=is.read(buffer))>0)
                    fos.write( buffer, 0, n );
                is.close();
                fos.close();
//                            v.add( ((ZipEntry)enum.nextElement()).getName() );
            }
        }
        catch (Exception e)
        {
            System.out.println("Erreur : " + e);
        }
    }
    
    public Archiver()
    {
        buffer = new byte[ 8092 ];
        files = new Vector();
    }
    
    public Archiver( String archiveName )
    {
        this();
        setArchiveName( archiveName );
    }

    public Archiver( String archiveName, int archiveMode )
    {
        this( archiveName );
        this.mode = archiveMode;
    }
    
    /**
     * retourne une list des fichier de l'archive
     */
    public Vector getList()
    {
        Vector v = new Vector();
        try
        {
            if ( mode == MODE_CREATE )
            {
                for (Iterator iter = files.iterator(); iter.hasNext();)
                    v.add( ((File)iter.next()).getPath() );
            }
            else
            {
                ZipFile zp = new ZipFile( getArchiveName() );
                Enumeration enu = zp.entries();
                while ( enu.hasMoreElements() )
                    v.add( ((ZipEntry)enu.nextElement()).getName() );
            }
        }
        catch (Exception e)
        {
            System.out.println("Erreur : " + e);
        }
        return v;
    }
    
    /**
     * ajoute un chemin de fichier a l archive
     * 
     */
    public void addToArchive( String path )
    {
        add( path );
    }

    /**
     * ajoute un fichier a l archive
     * 
     */
    public void addToArchive( File file )
    {
        readRep( file ); // ajoute tou les fichier a la list
    }
    
    /**
     * ajoute un chemin de fichier a l archive
     * 
     */
    public void add( String path )
    {
        try
        {
            File f = new File( path );
            readRep( f ); // ajoute tou les fichier a la list
        }
        catch (Exception e)
        {
            System.out.println("Erreur : " + e);
        }
    }

    /**
     * Returns the archiveName.
     * @return String
     */
    public String getArchiveName()
    {
        return archiveName;
    }

    /**
     * Returns the mode.
     * @return int
     */
    public int getMode()
    {
        return mode;
    }

    /**
     * Sets the archiveName.
     * @param archiveName The archiveName to set
     */
    public void setArchiveName(String archiveName)
    {
        this.archiveName = archiveName;
        File f = new File( archiveName );
        if ( f.isDirectory() ) 
            restorePath = new String( archiveName );
        else
            restorePath = f.getParent();
        if ( !restorePath.endsWith( "\\" ) ) restorePath += "\\";
//System.out.println( "restorePath =" + restorePath );
    }

    /**
     * Sets the mode.
     * @param mode The mode to set
     */
    public void setMode(int mode)
    {
        this.mode = mode;
    }

    /**
     * Returns the restorePath.
     * @return String
     */
    public String getRestorePath()
    {
        return restorePath;
    }

    /**
     * Sets the restorePath.
     * @param restorePath The restorePath to set
     */
    public void setRestorePath(String restorePath)
    {
        this.restorePath = restorePath;
    }

}
