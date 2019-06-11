import java.awt.Color;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;


public class LabelListCellRenderer extends DefaultListCellRenderer{

	/**
	 * @author  Bile Bernard
	 */
	private static final long serialVersionUID = 1L;
     
	//private static final long                serialVersionUID    = 4019004513369374447L;
    Color coulexpediteur=null,couldestinataire=null;
    public LabelListCellRenderer(Color coulexpediteur,Color couldestinataire){
        super();
        this.coulexpediteur=coulexpediteur;
        this.couldestinataire=couldestinataire;
    }
    
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean hasFocus) {
        
        if(JLabel.class.isInstance(value)){
        	JLabel label=(JLabel)value;
        	if(label.getText().startsWith("Vous"))label.setBackground(this.coulexpediteur);
        	else
        		label.setBackground(this.couldestinataire);
        	if( isSelected){
        		label.setBackground(new Color(220,225,123));
        		label.setFont(new Font("Serif",Font.BOLD,25));
        	label. setForeground(Color.pink);
        	}else
        	{
        		label.setFont(new Font("Serif",Font.BOLD,15));
        		label.setBackground(list.getBackground());
            	label.setForeground(list.getForeground());
        	}
            return label;
        }
        return super.getListCellRendererComponent(list, value,
                index, isSelected, hasFocus);
    }
}  
