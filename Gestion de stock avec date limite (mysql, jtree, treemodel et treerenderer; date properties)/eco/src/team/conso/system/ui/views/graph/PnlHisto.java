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
 * 
 * 
 */
public class PnlHisto extends JPanel 
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
    public PnlHisto()
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
            g.drawString( (isAbscisseDate()?getFormatedDate( x ):"x = " + getFormatedString(""+x)) + " , y = " + getFormatedString(""+y), MARGE_X+2, MARGE_Y );
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
//        buildData( bdd.getStock() );
//        calculateData();
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
            }
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.views.PnlChart.setOrdonne() : " + ex );
        }
    }
    
    
    public void setBDDStock( BDDEco bdd )
    {
        this.bdd = bdd;
//        bdd.addDataListener( new UpdateListener() );
    }

//    private void buildData( Vector tree )
//    {
//        for ( Iterator iter = tree.iterator(); iter.hasNext(); )
//        {
//            DataElement elt = (DataElement)iter.next();
//            if ( elt instanceof Categorie )
//            {
//                Categorie cat = (Categorie)elt;
//                currentCat = new DefaultMutableTreeNode( cat );
////                currentCat = new DefaultMutableTreeNode( cat.getLibelle() );
//                root.add( currentCat );
//            }
//            else if ( elt instanceof Produit )
//            {
//                Produit pro = (Produit)elt;
//                currentPro = new DefaultMutableTreeNode( pro );
////                currentPro = new DefaultMutableTreeNode( pro.getLibelle() );
//                currentCat.add( currentPro );
//            }
//            else if ( elt instanceof Stock )
//            {
//                Stock st = (Stock)elt;
////                String s = "Date limite = " + st.getDateLimit();
////                currentPro.add( new DefaultMutableTreeNode( s ) );
//                DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode( st );
//                currentPro.add( dmtn );
//                dmtn.add( new DefaultMutableTreeNode( "quantité restante: " + st.getNombre() ) );
//                StringTokenizer sgt = new StringTokenizer( st.getDate(), "-" );
//                String y = sgt.nextToken(); String m = sgt.nextToken(); String d = sgt.nextToken();
////                dmtn.add( new DefaultMutableTreeNode( "date limite : " + m + "/" + m + "/" + y ) );
//                dmtn.add( new DefaultMutableTreeNode( "date limite : " + st.getDateLimit() ) );
//                dmtn.add( new DefaultMutableTreeNode( "date d'achat : " + m + "/" + m + "/" + y ) );
//            }
//        } // end for
//    }
    
    /**
     * coupe la string pour pas kel soit tro longue a l afichage
     */
    private String getFormatedString( String s )
    {
        if ( s.length() > limitTextBuble )
            return s.substring( 0, limitTextBuble );
        return s;
    }
    
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
//        calculateData();

//        // obtient ref sur les donnee
//        Vector data = ((DefaultTableModel)table.getModel()).getDataVector();
//        double[] doubleX = new double[ data.size() ];
//        double[] doubleY = new double[ data.size() ];
//        int i = 0;
//        maxX = Double.MIN_VALUE;
//        minX = Double.MAX_VALUE;
//        maxY = Double.MIN_VALUE;
//        minY = Double.MAX_VALUE;
//        // calcule des maxi
//        for( Iterator iter = data.iterator(); iter.hasNext(); i++ )
//        {
//            Vector elt = (Vector) iter.next();
//            doubleX[ i ] = (new Double(elt.elementAt( 0 ).toString())).doubleValue();
//            doubleY[ i ] = (new Double(elt.elementAt( 1 ).toString())).doubleValue();
//            maxX = Math.max( maxX, doubleX[ i ] );
//            minX = Math.min( minX, doubleX[ i ] );
//            maxY = Math.max( maxY, doubleY[ i ] );
//            minY = Math.min( minY, doubleY[ i ] );
//        }
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

    public Vector getData()
    {
        return data;
    }

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
    
    public void fireTriggerDraw()
    {
        this.triggerDrawFired = true;
    }
    
    public void fireTriggerData()
    {
        this.triggerDataFired = true;
    }
    
    public void fireAllTriggers()
    {
        this.triggerDataFired = true;
        this.triggerDrawFired = true;
    }

    
    class Resizer extends ComponentAdapter
    {
        public void componentResized(ComponentEvent e)
        {
            fireTriggerDraw();
            repaint();
        }
    }
    
    class Mouse extends MouseInputAdapter
    {
        public void mouseMoved(MouseEvent e)
        {
            super.mouseMoved(e);
            mouseCoords = new Point( e.getX(), e.getY() );
            repaint();
        }
        public void mouseExited(MouseEvent e)
        {
            super.mouseExited(e);
            mouseCoords = null;
            repaint();
        }
    }
    
    public boolean isUseMouseCross()
    {
        return useMouseCross;
    }

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

    public void setShowNode( boolean useNode )
    {
        this.useNode = useNode;
    }

    public int getRadiusNode()
    {
        return radiusNode;
    }

    public void setRadiusNode(int radiusNode)
    {
        this.radiusNode = radiusNode;
    }

    public Color getColorAxes()
    {
        return colorAxes;
    }

    public Color getColorCross()
    {
        return colorCross;
    }

    public Color getColorLine()
    {
        return colorLine;
    }

    public Color getColorNode()
    {
        return colorNode;
    }

    public Color getColorText()
    {
        return colorText;
    }

    public void setColorAxes(Color colorAxes)
    {
        this.colorAxes = colorAxes;
    }

    public void setColorCross(Color colorCross)
    {
        this.colorCross = colorCross;
    }

    public void setColorLine(Color colorLine)
    {
        this.colorLine = colorLine;
    }

    public void setColorNode(Color colorNode)
    {
        this.colorNode = colorNode;
    }

    public void setColorText(Color colorText)
    {
        this.colorText = colorText;
    }

    public int getLimitTextBuble()
    {
        return limitTextBuble;
    }

    public void setLimitTextBuble(int limitTextBuble)
    {
        this.limitTextBuble = limitTextBuble;
    }

    public boolean isBubleOnMouse()
    {
        return bubleOnMouse;
    }

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

    public void setUseBuble(boolean useBuble)
    {
        this.useBuble = useBuble;
    }

    public boolean isNodeOnLine()
    {
        return nodeOnLine;
    }

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
}
