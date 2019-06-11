
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


class Mpasse extends JFrame implements ActionListener  {
	JPasswordField mpasse;
	JTextField login;
	JLabel lmp;
	JLabel log;
	JButton valide, annule;
	public Mpasse()
	{
		mpasse= new JPasswordField("user");
		login= new JTextField("user");
		lmp= new JLabel("Mot De Passe");
		log= new JLabel("Entrer Login");
		valide= new JButton("valider");
		annule= new JButton("annuler");
		JPanel p1,p2,p3, p4,p5;
		
		p1= new JPanel();
		p2= new JPanel();
		p3= new JPanel();
		p4= new JPanel();
		p5= new JPanel();
		p1.setLayout(new GridLayout(1,2,10,10));
		p1.add(log);
		p1.add(login);
		p2.setLayout(new GridLayout(1,2,10,10));
		p2.add(lmp);
		p2.add(mpasse);
		p5.add(valide);
		p5.add(annule);
		p3.setLayout(new GridLayout(4,1,10,10));
		p3.add(p4);
		p3.add(p1);
		p3.add(p2);
		p3.add(p5);
	valide.addActionListener(this);
	annule.addActionListener(this);
//	mpasse = new JPasswordField(10);
//mpasse.setActionCommand("ok");
mpasse.addActionListener(this);

		Container c= this.getContentPane();
	c.add(p3); 

   	this.setTitle("authentification");
     this.setSize(400,300);
      this.show();
		
		
	}
	  boolean isPasswordCorrect(char[] input) {
    boolean isCorrect = true;
    char[] correctPassword = { 'u', 's', 'e', 'r'};

    if (input.length != correctPassword.length) {
        isCorrect = false;
    } else {
        isCorrect = Arrays.equals (input, correctPassword);
    }

   
    Arrays.fill(correctPassword,'0');

    return isCorrect;
}

	
	public void actionPerformed(ActionEvent e)
	{ 

		if(e.getSource()==valide)
		{	
	     System.out.println(login.getText());
		char[] input = mpasse.getPassword();
        if (isPasswordCorrect(input))
        { if(login.getText().compareTo("user")==0)
        {
		 
		 this.dispose();
		 principale p= new principale();
		 p.setVisible(true);
		 }
		 else
		 	JOptionPane.showMessageDialog(null,"verifier votre mot de passe ");
        }
		else 
			JOptionPane.showMessageDialog(null,"verifier votre mot de passe");
	}
	if(e.getSource()==annule)
	{ login.setText("");
	mpasse.setText("");
	}
	
	}
	public static void main(String[] args){
		
		
		Mpasse mp=new Mpasse();
		mp.setVisible(true);
	}
	
	
}