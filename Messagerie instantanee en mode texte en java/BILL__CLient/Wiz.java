import java.awt.Component;

     public class Wiz extends Thread
    {
    	 /**
    		 * @author  Bile Bernard
    		 */
    	 Component c=null;int vitesse=1, decallage=20;
    	 Wiz(Component c,int vitesse,int decallage){
    		 this.c=c;
    		 this.vitesse=vitesse;
    		 this.decallage=decallage;
    		// new JOUerSOn();
    		 start();
    	 }
    	 public void run(){
    		 creerWizz( c, vitesse, decallage) ;
    		 yield();
    	 }
   public  void creerWizz(Component c,int vitesse,int decallage)
    {
   
    int pos_x=c.getBounds().getLocation().x;
    int pos_y=c.getBounds().getLocation().y;
    
    int [] po={0,decallage};
  
    for (int i=0;i<40;i++)
    {
    
    	
    c.setBounds(pos_x+po[i%2],pos_y+po[i%2],c.getBounds().getSize().width,c.getBounds().getSize().height);
    java.awt.Toolkit.getDefaultToolkit().beep();
    try
    {
    
    Thread.sleep(vitesse);
    }
    catch(Exception e12){}
    }
     }
    
     }