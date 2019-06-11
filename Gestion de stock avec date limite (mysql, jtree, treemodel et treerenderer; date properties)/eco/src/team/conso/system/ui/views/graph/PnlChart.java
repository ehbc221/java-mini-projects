package team.conso.system.ui.views.graph;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import team.conso.data.BDDEco;
import team.conso.data.DataEvent;
import team.conso.data.DataListener;

/**
 * cette class permet d afficher un graph de type X Y
 * a partir des donnees contenus ds 2 colonnes d un vector
 * ces donnees doivent etre des nombres valide
 * pour simplifier g calculer les extrems mais on peu imaginer de les rendre parametrable
 * pour aficher des morceau du graphs ou meem faire des zoom
 * 
 * 
 * Pour l instatn je l ai brider sur lafichage du stok x = temps
 * y = nombre sur une cate gorie
 */
public class PnlChart extends JPanel
{
    private static final String VIEW_NAME = "Graphiques";

    private static final int MARGE_X = 20; // en points
    private static final int MARGE_Y = 20; // en points
    private Image offScreen = null;
    private boolean triggerDrawFired = true;
    private boolean triggerDataFired = true;
    private boolean useMouseCross = true;
    private boolean useBuble = true;
    private boolean useNode = true;
    private boolean bubleOnMouse = false;
    private boolean nodeOnLine = false;
    private boolean abscisseDate = true;
    private String xLibelle = "x";
    private String yLibelle = "y";

    private double ratioX = 0;
    private double ratioY = 0;
    private double maxX = Double.MIN_VALUE;
    private double minX = Double.MAX_VALUE;
    private double maxY = Double.MIN_VALUE;
    private double minY = Double.MAX_VALUE;
    private int radiusNode = 2; // en points
    private int limitTextBuble = 4; // en nombre de digit
    
    private Point mouseCoords = null; 
    
    private Color colorLine = Color.blue;
    private Color colorText = Color.magenta;
    private Color colorNode = Color.red;
    private Color colorAxes = Color.black;
    private Color colorCross = Color.cyan;
    private Vector data = null;
    private BDDEco bdd = null;
    
    /**
     * ici on surcharge le constructeur pour fait les init necessaire 
     * au fonctionnement de la class
     */
    public PnlChart()
    {
        super();
        // permet le retaillage du chart en cas de resize du panel
        addComponentListener( new Resizer() );
        // met en place les listner de souris pour l affichage des coords
        Mouse m = new Mouse();
        addMouseMotionListener( m );
        addMouseListener( m );
        // un ptt peu de personalisation ;o)
        setCursor( new Cursor( Cursor.CROSSHAIR_CURSOR ) );
    }
    
    /**
     * ben, methode surcharger pour dessiner le graph
     * et on respect le principe de l offscreen ;o)
     * on pourrai aussi englober les dessin lier a la souris ds l offscreen
     * et gerer une image suplementaire pour le graph mais bon c vraiment pas util... ;o)
     */
    public void paint( Graphics g )
    {
        Dimension d = getSize();
        // si les donne on changer
        if( triggerDrawFired )
        {   // on crer l image ds lakel on va dessiner
            offScreen = createImage( d.width, d.height );
            // on fait notre dessin
            drawOffScreen( (Graphics2D)offScreen.getGraphics(), d );
        }
        // maintenant tous ce qui suis sera dessiner par dessu le graph 
        // sans que celui ci soit redessiner
        if ( offScreen != null ) // maintenant on affiche notre image
            g.drawImage( offScreen, 0, 0, this );
        // chui pas sr que sa serve a grd chose ici ;o) mais par habitude ... ;o)
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        if ( isUseMouseCross() )
            drawMouse( g2, d );
        if ( isUseBuble() )
            drawBulle( g2, d );
    }

    /**
     * Method drawBulle.
     * ici on dessine les coord de la mouse c sympa non!!? ;o)
     * @param graphics2D
     * @param d
     */
    private void drawBulle( Graphics2D g, Dimension d)
    {
        // si la souris n est pas ds la fenetre oute
        if ( mouseCoords == null ) return;
        g.setColor( colorText );
        double x = (mouseCoords.x - MARGE_X)/ratioX+minX;
        double y = (d.height-mouseCoords.y - MARGE_Y)/ratioY+ minY;
        if ( isBubleOnMouse() )
        {   // ici je le met en dur mais biensur on peu le calculer en fonctino de la police utiliser
            int decPolice = 15; 
            g.drawString( isAbscisseDate()?getFormatedDate( x ):getFormatedString(""+x), mouseCoords.x+2, mouseCoords.y - decPolice );
            g.drawString( getFormatedString(""+y), mouseCoords.x+2, mouseCoords.y - 1 );
        }
        else
        {   // ici je le met en dur mais biensur on peu le calculer en fonctino de la police utiliser
            int decPolice = 15; 
            g.drawString( (isAbscisseDate()?getFormatedDate( x ):getXLibelle()+" = " + getFormatedString(""+x)) + " , " + getYLibelle()+" = " + getFormatedString(""+y), MARGE_X+2, MARGE_Y );
        }
        
    }
    
    private String getFormatedDate( double x )
    {
        try
        {
            Calendar cld = Calendar.getInstance();
            cld.setTime( new Date( (long)x ) );
            String s = cld.get( Calendar.DAY_OF_MONTH ) + 
            "/" + cld.get( Calendar.MONTH ) +
            "/" + cld.get( Calendar.YEAR );
            return s;
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.views.PnlChart.getFormatedDate() : " + ex );
        }
        return "";
    }

    public String getViewName()
    {
        return VIEW_NAME;
    }
    
    public void refresh()
    {
        repaint();
    }
    
    public void setOrdonne( String categorie )
    {
        try
        {
            if ( bdd != null )
            {
	            data = bdd.getAchat( categorie );
	            calculateData(); // met en place les cord afichable
	            fireAllTriggers();
            }
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.views.PnlChart.setOrdonne() : " + ex );
        }
    }
    
    
    public void setBDDStock( BDDEco bdd )
    {
        this.bdd = bdd;
    }

    
    /**
     * coupe la string pour pas kel soit tro longue a l afichage
     */
    private String getFormatedString( String s )
    {
        if ( s.length() > limitTextBuble )
            return s.substring( 0, limitTextBuble );
        return s;
    }
    
    /**
     * Methode daffichage du repere 
     * bon la je fait simple ;o)
     * mais avec un peu plus de pacience on peu aussi afficher les graduation... ;o)
     * 
     * @param graphics
     * @param d
     */
    private void drawMouse( Graphics2D g, Dimension d )
    {
        // si la souris n est pas ds la fenetre out
        if ( mouseCoords == null ) return;
        g.setColor( colorCross );
        // ligne horizontale
        if ( mouseCoords.y < (d.height-MARGE_Y ) && mouseCoords.y > MARGE_Y )
            g.drawLine( MARGE_X, mouseCoords.y, d.width - MARGE_X, mouseCoords.y );
        // ligne verticale
        if ( mouseCoords.x < (d.width-MARGE_X ) && mouseCoords.x > MARGE_X )
            g.drawLine( mouseCoords.x, MARGE_Y, mouseCoords.x, d.height - MARGE_Y );
    }

    /**
     * Method drawOffScreen.
     * 
     * on admet ici que le model utliser pour la jtable et un deriver de 
     * DefaultTableModel et dedans i a des double ;o)
     * euu sinon sa mrche pas pour cette exemple
     * @param graphics
     * @param d
     */
    private void drawOffScreen( Graphics2D g, Dimension d )
    {
        // si pas de data on dessine rien
        if ( data == null ) return;
        // on prepare le rendu ;) c plus joli comme sa
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        // calcule des ratios
        ratioX = ( d.width  - (MARGE_X*2) ) / ( maxX - minX );
        ratioY = ( d.height - (MARGE_Y*2) ) / ( maxY - minY );

        // si on veut eviter que le node n efface un moceau du segment il faut faire une 2nd boucle
        // autrement le tracer des nodes peut ce faire ds la meme boucle que les segments
        // ici dc je choisi d afficher les node sous les segments
        if ( isShowNode()&&!isNodeOnLine() )
        {
            g.setColor( colorNode );
            Point pt = new Point();
            for( int j = 0; j < doubleX.length; j++ )
            {   // calcul di 2nd point
                pt.x = (int)((doubleX[ j ]-minX)*ratioX) + MARGE_X;
                pt.y = d.height - (int)((doubleY[ j ]- minY)*ratioY + MARGE_Y );
                // tracer du point si necessaire
                g.fillOval( pt.x-radiusNode, pt.y-radiusNode, radiusNode*2+1, radiusNode*2+1 );
            }
        }
        // calcule coord du premier point
        Point pt1 = new Point( (int)((doubleX[0]-minX)*ratioX), (int)((doubleY[0]-minY)*ratioY) );
        pt1.x += MARGE_X;
        pt1.y = d.height - pt1.y - MARGE_Y; // en y le repere est renverser
        Point pt2 = new Point( 0, 0 );
        // tracer de la courbe
        for( int j = 1; j < doubleX.length; j++ )
        {
            // calcul di 2nd point
            pt2.x = (int)((doubleX[ j ]-minX)*ratioX) + MARGE_X;
            pt2.y = d.height - (int)((doubleY[ j ]- minY)*ratioY + MARGE_Y );
            // tracer du segment
            g.setColor( colorLine );
            g.drawLine( pt1.x, pt1.y, pt2.x, pt2.y );
            pt1.x = pt2.x; // on passe au point suivant
            pt1.y = pt2.y;
        }

        // ici dc je choisi d afficher les node sur les segments
        if ( isShowNode()&&isNodeOnLine() )
        {
            g.setColor( colorNode );
            Point pt = new Point();
            for( int j = 0; j < doubleX.length; j++ )
            {   // calcul di 2nd point
                pt.x = (int)((doubleX[ j ]-minX)*ratioX) + MARGE_X;
                pt.y = d.height - (int)((doubleY[ j ]- minY)*ratioY + MARGE_Y );
                // tracer du point si necessaire
                g.fillOval( pt.x-radiusNode, pt.y-radiusNode, radiusNode*2+1, radiusNode*2+1 );
            }
        }
        
        // tracer du repere
        g.setColor( colorAxes );
        // axe X
        g.drawLine( MARGE_X, d.height-MARGE_Y, d.width-MARGE_X, d.height-MARGE_Y );
        // axe Y
        g.drawLine( MARGE_X, d.height-MARGE_Y, MARGE_X, MARGE_Y );
        
        // l offscreen est dessine pas besoin de le refaire si pas de changement demander
        // (resize ou changement de donner)
        triggerDrawFired = false;
    }
    // j utilise des refs extern a la methode de calcul pour me simplifier la vie ;o)
    // on peu metre lensemble de la methode de calcul ds la methode de drawoffscreen
    // si on ne veu pas conserver de ref sur les donnees
    // mais ici sa permet aussi de ne pas les recalculer si pas necessaire
    double[] doubleX = null;
    double[] doubleY = null;
    
    /**
     * calcule les donnee apartir du vector x,y
     * 
     */
    private void calculateData()
    {
        try
        {
            // on ne refai pas le calcul si pas demander
            if ( !triggerDataFired ) return;
            // obtient ref sur les donnee
            doubleX = new double[ data.size() ];
            doubleY = new double[ data.size() ];
            int i = 0;
            maxX = Double.MIN_VALUE;
            minX = Double.MAX_VALUE;
            maxY = Double.MIN_VALUE;
            minY = Double.MAX_VALUE;
            // calcule des maxi
            for( Iterator iter = data.iterator(); iter.hasNext(); i++ )
            {
                Vector elt = (Vector) iter.next();
                doubleX[ i ] = (new Double(elt.elementAt( 0 ).toString())).doubleValue();
                doubleY[ i ] = (new Double(elt.elementAt( 1 ).toString())).doubleValue();
                maxX = Math.max( maxX, doubleX[ i ] );
                minX = Math.min( minX, doubleX[ i ] );
                maxY = Math.max( maxY, doubleY[ i ] );
                minY = Math.min( minY, doubleY[ i ] );
            }
            triggerDataFired = false;
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.views.PnlChart.calculateData() : " + ex );
        }
    }

    /**
     */
    public Vector getData()
    {
        return data;
    }

    /**
     * Sets the table.
     * @param table The table to set
     */
    public void setBdd( BDDEco  bdd )
    {
        this.bdd = bdd;
        this.bdd.addDataListener( new UpdateListener() );
    }
    
    class UpdateListener implements DataListener
    {
        public void changeUpdate(DataEvent dataEvent)
        {
            // pour l instan le type d event ne ns interresse pas 
            refresh();     // repaint le pnl
        }
    }
    
    /**
     * Sets the triggerDrawFired.
     * @param triggerDrawFired The triggerDrawFired to set
     */
    public void fireTriggerDraw()
    {
        this.triggerDrawFired = true;
    }
    
    /**
     * Sets the triggerDataFired.
     * @param triggerDataFired The triggerDataFired to set
     */
    public void fireTriggerData()
    {
        this.triggerDataFired = true;
    }
    
    /**
     * set tou les trigger en meem temps ...
     */
    public void fireAllTriggers()
    {
        this.triggerDataFired = true;
        this.triggerDrawFired = true;
    }

    
    /**
     * class adapter ;o)
     * ki permet de refaire le graph qd on resize la fenetre
     */
    class Resizer extends ComponentAdapter
    {
        public void componentResized(ComponentEvent e)
        {
            fireTriggerDraw();
            repaint();
        }
    }
    
    /**
     * class adapter permettan d intercepter les event souris
     * ici on s en ser pour rafraichir le dessin du curseur 
     * (cross et coords)
     */
    class Mouse extends MouseInputAdapter
    {
        public void mouseMoved(MouseEvent e)
        {
            super.mouseMoved(e);
            mouseCoords = new Point( e.getX(), e.getY() );
            repaint();
        }
        /**
         * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
         */
        public void mouseExited(MouseEvent e)
        {
            super.mouseExited(e);
            mouseCoords = null;
            repaint();
        }
    }
    
    /**
     * Returns the useMouseCross.
     * @return boolean
     */
    public boolean isUseMouseCross()
    {
        return useMouseCross;
    }

    /**
     * Sets the useMouseCross.
     * @param useMouseCross The useMouseCross to set
     */
    public void setUseMouseCross(boolean useMouseCross)
    {
        this.useMouseCross = useMouseCross;
    }

    /**
     * Returns the useNode.
     * @return boolean
     */
    public boolean isShowNode()
    {
        return useNode;
    }

    /**
     * Sets the useNode.
     * @param useNode The useNode to set
     */
    public void setShowNode( boolean useNode )
    {
        this.useNode = useNode;
    }

    /**
     * Returns the radiusNode.
     * @return int
     */
    public int getRadiusNode()
    {
        return radiusNode;
    }

    /**
     * Sets the radiusNode.
     * regle la largeur d un point du graph en pixels
     * @param radiusNode The radiusNode to set
     */
    public void setRadiusNode(int radiusNode)
    {
        this.radiusNode = radiusNode;
    }

    /**
     * Returns the colorAxes.
     * @return Color
     */
    public Color getColorAxes()
    {
        return colorAxes;
    }

    /**
     * Returns the colorCross.
     * @return Color
     */
    public Color getColorCross()
    {
        return colorCross;
    }

    /**
     * Returns the colorLine.
     * @return Color
     */
    public Color getColorLine()
    {
        return colorLine;
    }

    /**
     * Returns the colorNode.
     * @return Color
     */
    public Color getColorNode()
    {
        return colorNode;
    }

    /**
     * Returns the colorText.
     * @return Color
     */
    public Color getColorText()
    {
        return colorText;
    }

    /**
     * Sets the colorAxes.
     * @param colorAxes The colorAxes to set
     */
    public void setColorAxes(Color colorAxes)
    {
        this.colorAxes = colorAxes;
    }

    /**
     * Sets the colorCross.
     * @param colorCross The colorCross to set
     */
    public void setColorCross(Color colorCross)
    {
        this.colorCross = colorCross;
    }

    /**
     * Sets the colorLine.
     * @param colorLine The colorLine to set
     */
    public void setColorLine(Color colorLine)
    {
        this.colorLine = colorLine;
    }

    /**
     * Sets the colorNode.
     * @param colorNode The colorNode to set
     */
    public void setColorNode(Color colorNode)
    {
        this.colorNode = colorNode;
    }

    /**
     * Sets the colorText.
     * @param colorText The colorText to set
     */
    public void setColorText(Color colorText)
    {
        this.colorText = colorText;
    }

    /**
     * Returns the limitTextBuble.
     * @return int
     */
    public int getLimitTextBuble()
    {
        return limitTextBuble;
    }

    /**
     * Sets the limitTextBuble.
     * @param limitTextBuble The limitTextBuble to set
     */
    public void setLimitTextBuble(int limitTextBuble)
    {
        this.limitTextBuble = limitTextBuble;
    }

    /**
     * Returns the bubleOnMouse.
     * @return boolean
     */
    public boolean isBubleOnMouse()
    {
        return bubleOnMouse;
    }

    /**
     * Sets the bubleOnMouse.
     * @param bubleOnMouse The bubleOnMouse to set
     */
    public void setBubleOnMouse(boolean bubleOnMouse)
    {
        this.bubleOnMouse = bubleOnMouse;
    }

    /**
     * Returns the useBuble.
     * @return boolean
     */
    public boolean isUseBuble()
    {
        return useBuble;
    }

    /**
     * Sets the useBuble.
     * @param useBuble The useBuble to set
     */
    public void setUseBuble(boolean useBuble)
    {
        this.useBuble = useBuble;
    }

    /**
     * Returns the nodeOnLine.
     * @return boolean
     */
    public boolean isNodeOnLine()
    {
        return nodeOnLine;
    }

    /**
     * Sets the nodeOnLine.
     * @param nodeOnLine The nodeOnLine to set
     */
    public void setNodeOnLine(boolean nodeOnLine)
    {
        this.nodeOnLine = nodeOnLine;
    }

    public boolean isAbscisseDate()
    {
        return abscisseDate;
    }
    public void setAbscisseDate( boolean abscisseDate )
    {
        this.abscisseDate = abscisseDate;
    }
    public BDDEco getBdd()
    {
        return bdd;
    }
    public String getXLibelle()
    {
        return xLibelle;
    }
    public void setXLibelle(String libelle)
    {
        xLibelle = libelle;
    }
    public String getYLibelle()
    {
        return yLibelle;
    }
    public void setYLibelle(String libelle)
    {
        yLibelle = libelle;
    }
}
