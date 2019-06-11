


import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.sql.*;

class interface_2 extends JPanel implements ActionListener
{ base b;
boolean DEBUG = false;
       boolean ALLOW_COLUMN_SELECTION = true;
       boolean ALLOW_ROW_SELECTION = true;
	JButton chercher;
	JButton valider,annuler ;
	JTextField txt;
	JRadioButton b1,b2;
	JLabel l,l2;
	JTable jt;//=null;
	JPanel p2;
	JPanel p6;
static	String datD;
static	String datF;
	
	public interface_2()
	{b=new base();
		//sp=new JScrollPane();
		 
		l= new JLabel("Identificateur de l'employé");
		l2= new JLabel("décision du congé");
		chercher=new JButton("demandes de congés");
		valider= new JButton("valider");
		annuler=new JButton("annuler");
	txt=new JTextField();
	b1= new JRadioButton("accepter");
	b2= new JRadioButton("refuser");
	ButtonGroup b=new ButtonGroup();
	b.add(b1);
	b.add(b2);
   Font titreFont=new Font("Arial",2, 20);
   Font titre=new Font("Arial",2, 30);
   l2.setFont(titre);
   l2.setForeground(Color.magenta);
   l.setFont(titreFont);
   valider.setFont(titreFont);
   chercher.setFont(titreFont);
   annuler.setFont(titreFont);
   b1.setFont(titreFont);
   b2.setFont(titreFont);
   b1.setSelected(true);
	
	chercher.addActionListener(this);
	valider.addActionListener(this);
	annuler.addActionListener(this);
	b1.addActionListener(this);
	b2.addActionListener(this);
	JPanel p= new JPanel();
	p.add(chercher);
 p2= new JPanel();// va contenir le JTable
// p2.add(jt);
	JPanel p3= new JPanel();
	p3.setLayout(new GridLayout(2,2));
	p3.add(l);
	p3.add(txt);
	JPanel p4= new JPanel();
	//p4.setLayout(new GridLayout(1,2));//,10,10));
	p4.add(l2);
	p3.add(b1);
	p3.add(b2);
	JPanel p5= new JPanel();
	jt=new JTable();
	p2.add(jt);
	 p6= new JPanel();
	p5.add(valider);
	p5.add(annuler);
	p6.setLayout(new GridLayout(5,1));//,10,10));
	p6.add(p4);
	p6.add(p);
	p6.add(p2);
	p6.add(p3);
//	p6.add(p4);
	p6.add(p5);

	//*********************** action sur le JTable****************
	//**************************************************************
	jt=new JTable();


	//Container c= this.getContentPane(); 
this.add(p6);
 this.setSize(700,600);
 this.setMaximumSize(new Dimension(700,600));
   this.show(); 
   	this.setName("décision congé");
     

	}// fin du constructeur
	
	
	
	//************************* implémentation de actionListener*********
	
	
	public void actionPerformed(ActionEvent e)
	{ if(e.getSource()==chercher)
	{try{
	
		String ch="select * from DemandeConge";
		
		jt=b.select_demandes();
		
		//************************************************debut jt
		//************************************************
		
		final javax.swing.table.TableModel model = jt.getModel();
	jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (ALLOW_ROW_SELECTION) { // true by default
            ListSelectionModel rowSM = jt.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    //Ignore extra messages.
                    if (e.getValueIsAdjusting()) return;

                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if (lsm.isSelectionEmpty()) {
                        System.out.println("No rows are selected.");
                    } else {
                        int selectedRow = lsm.getMinSelectionIndex();
                        System.out.println("Row " + selectedRow
                                           + " is now selected.");
                    }
                }
            });
        } else {
            jt.setRowSelectionAllowed(false);
        }
	//****pour les Rows************************************
	
	 if (ALLOW_COLUMN_SELECTION) { // false by default
            if (ALLOW_ROW_SELECTION) {
                //We allow both row and column selection, which
                //implies that we *really* want to allow individual
                //cell selection.
                jt.setCellSelectionEnabled(true);
            }
            jt.setColumnSelectionAllowed(true);
            ListSelectionModel colSM =
               jt.getColumnModel().getSelectionModel();
            colSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    //Ignore extra messages.
                    if (e.getValueIsAdjusting()) return;

                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                   /* if (lsm.isSelectionEmpty()) {
                        System.out.println("No columns are selected.");
                    } else {*/
                        int selectedCol = lsm.getMinSelectionIndex();
                        System.out.println("Column " + selectedCol
                                           + " is now selected.");
                                           
                          if(selectedCol==0){
                          
                          	txt.setText((String)model.getValueAt(jt.getSelectedRow(),0)); 
                          		datD=(String)model.getValueAt(jt.getSelectedRow(),1);
                          		datF=(String)model.getValueAt(jt.getSelectedRow(),2); 
                          		}               
                    //}// fin else
                }
            });
        }
		
		
		
		//************************************************fin jt
 jt.setMaximumSize(new Dimension(this.getWidth()-10,100));
	 jt.setPreferredScrollableViewportSize(new Dimension(this.getWidth()-10,100));
	   p2.removeAll();

	p2.add(new JScrollPane(jt));

		
	this.validate();
	
	//p2.show(true);
		//sp.add(jt);
	//	p2.add(jt);}
	}
		catch (Exception f){System.out.println(f.getMessage());
		}
	}
	
	if(e.getSource()==valider)
	{ if(b1.isSelected())
	{	try{
		
	 b.executer_req(Integer.parseInt(txt.getText()),datD,datF);
	 b.effacer(Integer.parseInt(txt.getText()));
	 	}
	catch( NumberFormatException nb){JOptionPane.showMessageDialog(null,nb.getMessage());}
	 catch( SQLException ex){JOptionPane.showMessageDialog(null,ex.getMessage());}
	 catch( NullPointerException nx){JOptionPane.showMessageDialog(null,nx.getMessage());}
	 
	 }
		
	}
	
	
	if(e.getSource()==annuler)
	{ txt.setText("");
	b1.setSelected(true);
	}
		
	} 
	
	
	
	

}
	

