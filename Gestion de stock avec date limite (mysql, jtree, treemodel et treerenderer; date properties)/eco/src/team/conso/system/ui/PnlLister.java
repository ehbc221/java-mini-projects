package team.conso.system.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;


/**
 * class permettan de listter un ensemble de donne String 
 * fourni sous forme de vector leur representation se fait
 * sous la forme d une liste de checkbox
 * et renvoi un vector de string sur les checked
 */
public class PnlLister extends JPanel
{
    
    private JLabel lbl = new JLabel();
    private JList liste = new JList();
    private JButton btnAll = new JButton( "Tous" );
    private JButton btnNo = new JButton( "Rien" );
    private CellRenderer checkListRenderer = new CellRenderer();
    /**
     * 
     */
    public PnlLister()
    {
        super();
        setLayout( new BorderLayout() );
        JScrollPane scrll = new JScrollPane();
        scrll.setAutoscrolls( true );
		add( scrll, BorderLayout.CENTER );
        scrll.getViewport().add( liste );
        JPanel pnl = new JPanel();
        pnl.setLayout( new GridLayout( 1, 2 ) );
        pnl.add( btnAll );
        pnl.add( btnNo );
        add( pnl, BorderLayout.SOUTH );
        add( lbl, BorderLayout.NORTH );
        liste.setCellRenderer( checkListRenderer );
        liste.addMouseListener( new MouseListe() );
        liste.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        ActionBtn ab = new ActionBtn();
        btnAll.addActionListener( ab );
        btnNo.addActionListener( ab );
    }
    
    public PnlLister( Vector data )
    {
        this();
        setData( data );
    }

    public void setLabel( String name )
    {
        lbl.setText( name );
    }
    public void setCheckedItems( Vector items )
    {
        try
        {
            checkListRenderer.setCheckedVector( false );
            int max = liste.getModel().getSize(); 
            for ( int i = 0; i < max; i++ )
            {
                String s = (String)liste.getModel().getElementAt( i );
                for (Iterator iter = items.iterator(); iter.hasNext(); ) 
                {
                    String elt = (String) iter.next();
                    if ( elt.equals( s ) )
                    {
                        checkListRenderer.getCheck().setElementAt( new Boolean( true ), i );
                        break;
                    }
                }
            }
        } catch (Exception e)
        {
            System.out.println( "Error : " + e);
        }

    }
    public Vector getCheckedItems()
    {
        Vector result = new Vector();
        try
        {
            Vector idx = checkListRenderer.getCheck();
            int i = 0;
            for ( Iterator iter = idx.iterator(); iter.hasNext(); i++ )
            {
                Boolean elt = (Boolean) iter.next();
                if ( elt.booleanValue() )
                    result.add( liste.getModel().getElementAt( i ) );
            }
        } catch (Exception e)
        {
            System.out.println( "Error : " + e);
        }
        return result.isEmpty()?null:result;
    }
    
    public void setData( Vector data )
    {
        liste.setListData( data );
        checkListRenderer.setCheckedVector( data.size(), false );
    }
    
    class MouseListe extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
            if ( liste.getSelectedIndex() >= 0 )
            {
                int i = liste.getSelectedIndex();
                boolean b = ((Boolean)checkListRenderer.getCheck().elementAt( i )).booleanValue();
                checkListRenderer.getCheck().setElementAt( new Boolean( !b ), i );
                liste.repaint();
            }
        }
    }
    
    class CellRenderer extends JCheckBox implements ListCellRenderer
    {
        Vector check = null;
        
        public CellRenderer()
        {
            check = new Vector();
        }

        public CellRenderer( Vector check )
        {
            this.check = check;
        }
        
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus)
        {
            this.setText( value.toString() );
            boolean b = false;
            if ( index < check.size() )
                 b = ((Boolean)check.elementAt( index )).booleanValue();
            this.setSelected( b );
            this.setBackground( list.getBackground() );
            this.setForeground( list.getForeground() );
            
            return this;
        }
        public Vector getCheck()
        {
            return check;
        }
        public void setCheck( Vector check )
        {
            this.check = check;
        }

        public void setCheckedVector( boolean b )
        {
            for ( int i = 0; i < check.size(); i++ )
                check.setElementAt( new Boolean( b ), i );
        }
        /**
         * use for init size and values when size is unknown ;o) c qd mem plus joli en anglai beurk ;o)
         * @param size
         * @param b
         */
        public void setCheckedVector( int size, boolean b )
        {
            Vector v = new Vector( size );
            for ( int i = 0; i < size; i++ ) 
                v.add( new Boolean( false ) );
            setCheck( v );
        }
    }
    
    class ActionBtn implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if ( source == btnNo )
                actionNo( e );
            else if ( source == btnAll )
                actionAll( e );
        }

    }

    /**
     * @param e
     */
    private void actionAll(ActionEvent e)
    {
        checkListRenderer.setCheckedVector( true );
        liste.repaint();
    }

    /**
     * @param e
     */
    private void actionNo(ActionEvent e)
    {
        checkListRenderer.setCheckedVector( false );
        liste.repaint();
    }
}
