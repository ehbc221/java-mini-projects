package team.conso.system.util;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
/**
 * permet de visioner un fichier html ( use pour l aide )
 */
public class HtmlBrowser extends JFrame 
{
    JEditorPane html;
    String path = "";//".\\data\\index.htm";
    Vector oldPages = new Vector(); // sauve les path des page deja passer
    int currentPageIdx = 0;
    
    private static HtmlBrowser browser = null;
    
    public static HtmlBrowser getInstance()
    {
        if ( browser == null) { browser = new HtmlBrowser(); }
        return browser;
    }
    
    public static void show( String page )
    {
        getInstance().setPath( page );
        getInstance().setVisible( true );
    }
    
    /**
     * Constructor for HtmlBrowser.
     * @throws HeadlessException
     */
    private HtmlBrowser() 
    {
        super();
        setSize( 500, 500 );

        JPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        JPanel2.setBounds(0,0,790,29);
        JToolBar1.setAlignmentY( 0.222222F );
        JPanel2.add(JToolBar1);
        JToolBar1.setBounds(0,0,410,29);
        JToolBar1.add(btnPrev);
        JToolBar1.add(btnNext);
        btnPrev.setText("Page precédente");
        btnNext.setText("Page suivante");
        
        Action aIcareAction = new Action();
        btnPrev.addActionListener( aIcareAction );
        btnNext.addActionListener( aIcareAction );
        btnNext.setFocusPainted( false );
        btnPrev.setFocusPainted( false );

//        getContentPane().setLayout(new GridBagLayout());
        btnNext.setEnabled( false );
        btnPrev.setEnabled( false );

        try
        {
            URL url = getURL( path );
            html = new JEditorPane( url );
            html.setEditable(false);
            html.addHyperlinkListener( createHyperLinkListener() );
            html.setBounds( 0, 0, 500, 500 );
//            html.setVisible(true);
            
            JScrollPane scroller = new JScrollPane();
            scroller.setVisible( true );
            JViewport vp = scroller.getViewport();
            vp.add( html );
            getContentPane().add( JPanel2, BorderLayout.NORTH );
            getContentPane().add( scroller, BorderLayout.CENTER);
            html.setBackground( this.getBackground() );
//            setTitle( "" );
            
        }
        catch (Exception e)
        {
            System.out.println("eror : " + e );
        }
        
        
//mise en place du racoursi cclavier sur l aide
        LudoAideAction anAction = new LudoAideAction();
        javax.swing.KeyStroke keystr = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0 );
        getRootPane().registerKeyboardAction( anAction,
                                "ludoaction",
                                keystr,
                                JRootPane.WHEN_IN_FOCUSED_WINDOW ); // met en place la touche daide
    }

    class LudoAideAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) { aideActionPerformed( event ); } }
    public void aideActionPerformed( java.awt.event.ActionEvent event ) { this.setVisible( false ); /*this.dispose();*/ }
    
        
    JPanel JPanel2 = new JPanel();
    JToolBar JToolBar1 = new JToolBar();
    JButton btnPrev = new JButton();
    JButton btnNext = new JButton();
    
    class Action implements java.awt.event.ActionListener
    {
        public void actionPerformed(java.awt.event.ActionEvent event)
        {
            Object object = event.getSource();
            if (object == btnNext )
                btnNextActionPerformed(event);
            else if (object == btnPrev)
                btnPrevActionPerformed(event);
        }
    }
    
    private void btnNextActionPerformed(java.awt.event.ActionEvent event)
    {
        try {
            if( currentPageIdx++ < oldPages.size() ) 
            {
                String p = (String)oldPages.elementAt( currentPageIdx );
                URL url = getURL( p );
                html.setPage( url );
            }
            checkBtn();            
        } catch (Exception e) 
        {
            System.out.println( " btnNext_actionPerformed : " + e );
        }
    }

    private void btnPrevActionPerformed(java.awt.event.ActionEvent event)
    {
        try {
            if( currentPageIdx > 0 && !oldPages.isEmpty() ) 
            {
                String p = (String)oldPages.elementAt( --currentPageIdx );
                URL url = getURL( p );
                html.setPage( url );
            }
            checkBtn();            
        } catch (Exception e) 
        {
            System.out.println( " btnNext_actionPerformed : " + e );
        }
    }

    private void initTitre( String p )
    {
        String sep = "¤";
        if ( p.indexOf( "/" ) > 0 ) sep = "/";
        else if ( p.indexOf( "\\" ) > 0 ) sep = "\\";
        StringTokenizer st = new StringTokenizer( p, sep );
        int n = st.countTokens();
        String s = "";
        while( st.hasMoreTokens() ) { s = st.nextToken(); }
        if ( s.indexOf(".") > 0 ) s = s.substring( 0, s.indexOf( "." ) );
        this.setTitle( "Aide : " + s );
    }
    

    /**
     * Constructor for HtmlBrowser.
     * @param titre
     * @throws HeadlessException
     */
    public HtmlBrowser( String titre ) throws HeadlessException
    {
        this();
        this.setTitle( titre );
    }

    public static void main(String[] args)
    {
//        HtmlBrowser b = new HtmlBrowser( "Browser de moi " );
        HtmlBrowser b = new HtmlBrowser( );
        b.setLocation( new Point( 0, 0 ) );
        b.setVisible( true );
    }

    private void checkBtn()
    {
        if ( oldPages == null || oldPages.size() == 0 )
        {
            btnNext.setEnabled( false );
            btnPrev.setEnabled( false );
        }
        else 
        {
            if ( currentPageIdx > 0 )
                btnPrev.setEnabled( true );
            else 
                btnPrev.setEnabled( false );
            if ( (currentPageIdx+1) < oldPages.size() )
                btnNext.setEnabled( true );
            else 
                btnNext.setEnabled( false );
        }
    }
    
    private HyperlinkListener createHyperLinkListener()
    {
        return new HyperlinkListener()
                            {
                                public void hyperlinkUpdate(HyperlinkEvent e)
                                {
                                    if ( e.getEventType() == HyperlinkEvent.EventType.ACTIVATED )
                                    {
                                        if (e instanceof HTMLFrameHyperlinkEvent)
                                        {
                                            ((HTMLDocument) html
                                                    .getDocument())
                                                    .processHTMLFrameHyperlinkEvent(
                                                (HTMLFrameHyperlinkEvent) e);
                                        }
                                        else
                                        {
                                            try
                                            {
//                                                System.out.println(" e.getURL() = " + e.getURL() );
                                                path = e.getURL().getPath();
                    //                            initTitre( path );
                                                html.setPage( e.getURL() );
                                                addHisto( path );
                                            }
                                            catch (IOException ioe)
                                            {
                                                System.out.println("IOE: " + ioe);
                                            }
                                        }
                                    }
                                }
                            };
    }
    
    private void addHisto( String path )
    {
        oldPages.add( path );
        currentPageIdx = oldPages.size() - 1;
        if ( oldPages.size() > 25 ) 
        {
            oldPages = new Vector();
            oldPages.add( path );
            currentPageIdx = oldPages.size() - 1;
        }
        checkBtn();
    }
    
    /**
     * Returns le path de la page en cour de lecture.
     * @return String
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Sets le path de la page html a lire.
     * @param path de la page html
     */
    public void setPath(String path)
    {
        try 
        {
            this.path = path;
            URL url = getURL( path );
            html.setPage( url );
            addHisto( path );
            setTitle( path );
        } 
        catch (Exception e) 
        {
            System.out.println("setPath  : " + e );
        }

    }
    
    public void setDocument( String doc )
    {
        html.setText( doc );
    }
    
    public static URL getURL( String file ) throws MalformedURLException
	{
	    URL documentBase = new URL("file:///" + System.getProperty("user.dir") + "/");
	    return new URL( documentBase, file );
	}

}
