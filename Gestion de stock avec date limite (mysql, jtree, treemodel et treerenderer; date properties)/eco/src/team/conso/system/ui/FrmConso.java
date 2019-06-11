package team.conso.system.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import team.conso.data.BDDEco;
import team.conso.system.core.Exitable;
import team.conso.system.core.Helpable;
import team.conso.system.ui.views.PnlBilan;
import team.conso.system.ui.views.PnlCatProd;
import team.conso.system.ui.views.PnlGraph;
import team.conso.system.ui.views.PnlHistoGram;
import team.conso.system.ui.views.PnlIventory;
import team.conso.system.ui.views.PnlSaisieStock;
import team.conso.system.ui.views.PnlTest;
import team.conso.system.util.HtmlBrowser;

/**
 * class principale de l application qui constitue l IHM de base
 * 
 * 
 * team.conso.system.ui
 * 
 * 
 */
public class FrmConso extends JFrame implements Exitable, Helpable
{
    public static final String HELP_PAGE = ".\\data\\index.htm";
    private static final String MENU_MNEMO_PREF = "&";
    private static final String MENU_ACTION = "action";
    private static final String MENU_SEPARATOR = ",";
    // cle des preferences 
    private static final String PREF_X = "x";
    private static final String PREF_Y = "y";
    private static final String PREF_W = "width";
    private static final String PREF_H = "height";
    private static final String PREF_VIEWS = "views";
    // cle des proprietee de l appli
    private static final String KEY_TITRE = "titre";
    private static final String KEY_ICON = "icone";
    private static final String KEY_MENU = "menu";
    
    private Hashtable<String,ViewPanel> viewMnemo = new Hashtable<String,ViewPanel>();
    /**
     * resources de parametrage de la fenetre 
     */
    private ResourceBundle resources; // = ResourceBundle.getBundle( this.getClass().getName() );
    private Preferences prefs; // = Preferences.userNodeForPackage( this.getClass() );
    
    // conserve une reference sur le tabbged pane qui defini la visu
    public TabbedViewPane mainPane = new TabbedViewPane();
    
    private BDDEco bdd = null;

    private static FrmConso appli = null;
    public static FrmConso getInstance()
    {
        if ( appli == null )
            appli = new FrmConso();
        return appli;
    }
    
    /**
     * constructeur de la class fentre principale 
     * ce constructeur est priver (singleton)
     * il faut use la methode getinstance ca permet de controler 
     * l unicite de sont instanciation ds la jvm
     * De plus toutes les class pourron acces a la reference de l appli( ca c asser pratik ;o) )
     * @param resource nom du fichier de propriete de la class
     */
    private FrmConso() 
    {
        super();
        // optention des parametres et des prefs
        try
        {
            resources = ResourceBundle.getBundle( "team.conso.system.ui.resources.frmconso" );
            setPrefs( Preferences.userNodeForPackage( this.getClass() ) );
        } catch ( Exception ex )
        {
            System.out.println( "Impossible d obtenir les ressources de l application : " + ex );
            System.exit( 0 );
        }
        setTitle( getResourceString( KEY_TITRE ) );
        // mise en place de l icone de l application
        URL ur = getResource( KEY_ICON );
        ImageIcon im = new ImageIcon( ur );
        setIconImage( im.getImage() ); 
        // regle la taille et la position de la fenetre en fonction de sa derniere ouverture
        setSize( getPrefs().getInt( PREF_W, 500 ), getPrefs().getInt( PREF_H, 500 ) );
        setLocation( getPrefs().getInt( PREF_X, 200 ), getPrefs().getInt( PREF_Y, 200 ) );
        // met a jour les prefs a chac deplacement et resize de la fenetre
        addComponentListener( new ComponentAdapter() 
                {
            public void componentMoved( ComponentEvent e )
            {
                Point pt = getLocation();
                getPrefs().putInt( PREF_X, pt.x );
                getPrefs().putInt( PREF_Y, pt.y );
            }

            public void componentResized( ComponentEvent e )
            {
                Dimension dim = getSize();
                getPrefs().putInt( PREF_W, dim.width );
                getPrefs().putInt( PREF_H, dim.height );
            }
                });
        // implemente la sorti correct de l application 
        addWindowListener( new WindowAdapter() 
                { public void windowClosing( WindowEvent e ) { exit(); } } );
        createMenuBar();
        
        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( mainPane, BorderLayout.CENTER );
        
        // cette ligne est juste pour faire jolie ;o) : faudra gere un vrai multiconte
        String userEcoBase = "Reserve de "+ System.getProperty( "user.name" );
        bdd = new BDDEco( getConnection(), userEcoBase );
        // si pas de bdd on kitte sa ser a rien ;o)
        if ( bdd.getConnection() == null ) System.exit( 0 ); // TODO
        createPanelViews();
        restoreOpenedViews();
        
        
        // actions effectuer par le menu now
//        mainPane.addViewPane( pnl1 );
//        mainPane.addViewPane( pnl2 );
//        mainPane.addViewPane( pnl3 );
//        mainPane.addViewPane( pnl4 );
//        mainPane.addViewPane( pnl5 );
        //mise en place du racoursi cclavier sur l aide
        AideAction anAction = new AideAction();
        KeyStroke keystr = KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0 );
        getRootPane().registerKeyboardAction( anAction,
                                "hlpaction",
                                keystr,
                                JRootPane.WHEN_IN_FOCUSED_WINDOW ); // met en place la touche daide
		setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE); // pour pas quitter qd on di non
    }

	class AideAction implements java.awt.event.ActionListener {
	    public void actionPerformed(java.awt.event.ActionEvent event) { browseAide(); } }
	public void browseAide()
	{
	    HtmlBrowser.show( HELP_PAGE ); 
	}

	public void showAPropo()
	{
        JOptionPane.showMessageDialog( 
                appli, 
                "Programme réalisé par Ludo... ;o)", 
                "A propos de.", 
                JOptionPane.INFORMATION_MESSAGE, 
                new ImageIcon( appli.getIconImage() ) );
	}
	
    public boolean addOrRemoveView( ViewPanel vp )
    {
        return mainPane.addOrRemove( vp );
    }

    public void addOrRemoveView( String mnemo )
    {
        // si c un affichage alors on le mmet devant
        if ( addOrRemoveView( viewMnemo.get( mnemo ) ) )
            mainPane.setSelectedComponent( viewMnemo.get( mnemo ) );
//        if ( addOrRemoveView( (ViewPanel)viewMnemo.get( mnemo ) ) )
//            mainPane.setSelectedComponent( (ViewPanel)viewMnemo.get( mnemo ) );
    }
    
    private void createPanelViews()
    {
        PnlIventory    pnl1 = new PnlIventory();
        PnlCatProd     pnl2 = new PnlCatProd();
        PnlSaisieStock pnl3 = new PnlSaisieStock();
        PnlGraph       pnl4 = new PnlGraph();
        PnlHistoGram   pnl5 = new PnlHistoGram();
        PnlBilan       pnl6 = new PnlBilan();
        PnlTest        pnl7 = new PnlTest();
        pnl1.setBDDStock( bdd );
        pnl2.setBDDStock( bdd );
        pnl3.setBDDStock( bdd );
        pnl4.setBDDStock( bdd );
//        pnl5.setBDDStock( bdd ); // pas fini
        pnl6.setBDDStock( bdd );
        // pette astuce pour simplifier le generateur de menu
        viewMnemo.put( pnl1.getMnemo(), pnl1 );
        viewMnemo.put( pnl2.getMnemo(), pnl2 );
        viewMnemo.put( pnl3.getMnemo(), pnl3 );
        viewMnemo.put( pnl4.getMnemo(), pnl4 );
        viewMnemo.put( pnl5.getMnemo(), pnl5 );
        viewMnemo.put( pnl6.getMnemo(), pnl6 );
        viewMnemo.put( pnl7.getMnemo(), pnl7 );
    }
    
    /**
     * restituje les onglet qui etati ouver a la fermeture de l application
     *
     */
    private void restoreOpenedViews()
    {
        String s = getPrefs().get( PREF_VIEWS, "I" );

        for (int i = 0; i < s.length(); i++)
        {
            addOrRemoveView( s.substring( i, i+1 ) );
        }
    }
    
    /**
     * sauve les onglet ouvert
     *
     */
    private void saveOpenedViews()
    {
        int max = mainPane.getTabCount();
        String mnemo = "";
        for (int i = 0; i < max; i++)
        {
            mnemo += mainPane.getViewPanelAt( i ).getMnemo();
        }
        getPrefs().put( PREF_VIEWS, mnemo );
    }
    
    /**
     *     obtien les connection sur la bases
     * 	   cette methode est senser n etre appeler qu un e foi ;o)
     */
    private Connection getConnection()
    {
        // mise en place du pilot MySQL
        try
        {
            Class.forName( "org.gjt.mm.mysql.Driver" ).newInstance(); 
        }
        catch ( Exception e ) 
        {
            System.out.println( " Establish driver error  : " + e );
            return null;
        }
        
        // connection a la base eco selon les paramtre du fichier de conf
    	try
        {
            String URL = getResourceString( "URL" );
            URL += getResourceString( "nomBase" );
            return DriverManager.getConnection( URL, getResourceString("user"), getResourceString("pass") ); 
        }
        catch ( Exception e ) 
        {
            System.out.println( " Frmconso.getConnections() : " + e );
        }
        return null;
    }

    /**
	 * doit etre appeller avant de quitter l application
     */
    public void exit()
    {
        if ( JOptionPane.showConfirmDialog( 
                this, 
                "Voulez-vous vraiment quitter l'application?", 
                getTitle(), 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE ) == JOptionPane.YES_OPTION )
        {
            saveOpenedViews();
            System.exit( 0 ); 
        }
    }
    /**
     * creer la menubar selon les description du fichier de proprietee
     *
     */
    protected void createMenuBar()
    {
        try
        {
            StringTokenizer menuString = new StringTokenizer ( getResourceString( KEY_MENU ), MENU_SEPARATOR );
            if ( menuString.countTokens() == 0 ) return;
            JMenuBar menuBar = new JMenuBar();
            setJMenuBar( menuBar );
//            menuBar.add( new JMenu( "toto" ) );
            // boucle sur les element de la bar de menu
            while ( menuString.hasMoreTokens() )
            {
                String menuName = menuString.nextToken();
                JMenu menu = new JMenu();
                int idxMnemo = menuName.indexOf( MENU_MNEMO_PREF );
                if ( idxMnemo >= 0 )
                {
                    menuName = menuName.substring( 0, idxMnemo ) + menuName.substring( idxMnemo + 1 );
                    menu.setMnemonic( menuName.charAt( idxMnemo ) );
                }
                menu.setText( menuName );
                menuBar.add( menu );
                createMenu( menu );
            }
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.FrmConso.initMenuBar(  )  : " + ex );
        }
    }
    
    /**
	 *  cree un menuitem selon le fichier des proprietee
	 */
    protected void createMenu( JMenu menu )
    {
        try
        {
	        StringTokenizer menuString = new StringTokenizer ( getResourceString( menu.getText() ), MENU_SEPARATOR );
	        while ( menuString.hasMoreTokens() )
	        {
	            String name = menuString.nextToken();
	            JMenuItem menuItem = new JMenuItem();
	            int idxMnemo = name.indexOf( MENU_MNEMO_PREF );
	            if ( idxMnemo >= 0 )
	            {
	                name = name.substring( 0, idxMnemo ) + name.substring( idxMnemo + 1 );
	                menuItem.setMnemonic( name.charAt( idxMnemo ) );
	            }
	            menuItem.setText( name );
	            String action = getResourceString( (menu.getText() + "." + name + "." + MENU_ACTION).replace(' ', '_' ) );
	            if ( action != null )
	            {
		            AbstractAction act = (AbstractAction)Class.forName( action ).newInstance();
		            act.putValue( "appli", this );
		            act.putValue( "bdd", this.getBdd() );
		            menuItem.addActionListener( act );
		            menuItem.setActionCommand( name );
		            menuItem.setEnabled( act.isEnabled() );
	            }
	            menu.add( menuItem );
	        }
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.FrmConso.createMenu( menu )  : " + ex );
        }
    }
    
    protected String getResourceString( String nm )
    {
        String str;
        try
        {
            str = resources.getString( nm  );
        } catch ( MissingResourceException mre )
        {
            str = null;
        }
        return str;
    }

    protected URL getResource( String key )
    {
        String name = getResourceString( key );
        if ( name != null )
        {
            URL url = this.getClass().getResource( name );
            return url;
        }
        return null;
    }

    public BDDEco getBdd()
    {
        return bdd;
    }
    public Preferences getPrefs()
    {
        return prefs;
    }
    private void setPrefs( Preferences p )
    {
        prefs = p;
    }
}
