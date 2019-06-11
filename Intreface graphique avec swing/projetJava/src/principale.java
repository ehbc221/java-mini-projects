import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class principale extends JFrame implements ActionListener {
	JButton b1,b2,b3, b4;
	JSplitPane sp;
	public principale()
	{
		 sp= new JSplitPane();
		sp.setSize(new Dimension(800,600));
		sp.setDividerLocation(230);
		JPanel p=new JPanel();
		b1= new JButton("deposer congé");
		b2= new JButton("gerer congés");
		b3= new JButton("gerer personnel");
		
		p.setPreferredSize(new Dimension(280,300));
		p.setMaximumSize(new Dimension(280,300));
		p.setLayout(new GridLayout(6,1,20,10));
		p.add(new JPanel());
		p.add(b1);
		p.add(b2);
		p.add(b3);
		//p.add(b4);
		p.add(new JPanel());
		JPanel p2=new JPanel();
		p2.setLayout(new GridLayout(1,2,20,10));
		
		p2.add(p);	
		
		sp.setLeftComponent(p2);
		sp.setRightComponent(new deposer_conge() );
		Container c= this.getContentPane();
		this.setPreferredSize(new Dimension(900,650));
		this.setSize(new Dimension(900,650));
		c.add(sp);
		pack();
		this.setLocation(100,50);
		
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		//b4.addActionListener(this);
		
		
	}
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==b1)
			sp.setRightComponent(new deposer_conge() );
		if(e.getSource()==b2)
			sp.setRightComponent(new interface_2());
		if(e.getSource()==b3)
			sp.setRightComponent(new gererPersonnel());
	}
	
	/*public static void main(String []arg)
	{
		principale p= new principale();
		p.setVisible(true);
		
	}*/
	
	
	

}
