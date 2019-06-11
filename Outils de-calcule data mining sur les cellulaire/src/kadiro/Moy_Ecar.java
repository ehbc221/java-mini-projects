/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kadiro;

/**
 *
 * @author ABDELKADER
 */
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import java.util.Vector ;
public class Moy_Ecar implements CommandListener {
  
    public Moy_Ecar(Display d){
        
        confirm = new Command("confirmer", Command.OK, 1);
        backCommand = new Command("revenir", Command.BACK, 1);
        sw = new Command("choix_nb", Command.BACK, 1);
        tf1 = new TextField(new String(),new String(),20,0) ;
        display = d;
        frame = new Form("kadiro moy_ecart");
        frame.addCommand(confirm);
        frame.addCommand(backCommand);
        frame.append("Par OUALI Abdelkader\n") ;
        frame.append("Le nombre des valeurs\n");
        frame.append(tf1);
        frame.setCommandListener(this);
    }
    public void commandAction(Command c, Displayable d) {
        if(c == clcCommand1 ){
                  try{ TextField temp ;
                         if(!tf.isEmpty()){
                            if(tin!=null) tin = null ;
                            tin = new Vector() ;  
                      for(int i=0;i<tf.size();i++){
                      temp =  (TextField) tf.elementAt(i) ;
                      try{
                      tin.addElement(new Double(Double.parsedouble(temp.getString())));
                      }catch(Exception ee){
                          System.out.println("erreurs" +ee);
                          show();frame.append("erreurs parametre"); return;
                      }
                          }
                      try{
                      double act = moyenne(tin);
                      res.setString(act+"");
                      }catch(Exception e){res.setString("erreur");}
                 }
                
        }catch(Exception e){frame2.append("erreurs ");}}
        if(c == clcCommand2 ){
                  try{ TextField temp ;
                         if(!tf.isEmpty()){
                            if(tin!=null) tin = null ;
                            tin = new Vector() ;  
                      for(int i=0;i<tf.size();i++){
                      temp =  (TextField) tf.elementAt(i) ;
                      try{
                      tin.addElement(new Double(Double.parsedouble(temp.getString())));
                      }catch(Exception ee){
                          System.out.println("erreurs" +ee);
                          show();frame.append("erreurs parametre"); return;
                      }
                          }
                      try{
                      double act = ecart(tin);
                      res.setString(act+"");
                      }catch(Exception e){res.setString("erreur");}
                 }
                
        }catch(Exception e){frame2.append("erreurs ");}}
       if(c ==confirm){
             try{
                 try{
                 nombre = Integer.parseInt(tf1.getString());}
                 catch(Exception ee){frame.append("erreur parametre");return;
                 }
                 clcCommand1 = new Command("moyenne", Command.OK, 1);
                 clcCommand2 = new Command("ecart", Command.OK, 2);
                 res = new TextField(new String(),new String(),20,0) ;
                 frame2 = new Form("kadiro");
                 frame2.addCommand(clcCommand1);
                 frame2.addCommand(clcCommand2);
                 frame2.addCommand(sw);
                 frame2.append("Par OUALI Abdelkader\n") ;
                 frame2.append("les valeurs des valeurs\n");
                 if(tf!=null) tf = null ;
                 tf = new Vector() ;
                 for(int i=0;i<nombre;i++){
                     tf.addElement(new TextField(new String(),new String(),10,0));
                     frame2.append("val"+i+" ");
                     frame2.append((TextField)tf.elementAt(i));
                 }
                 frame2.append("la résultat est \n");
                 frame2.append(res);
                 frame2.setCommandListener(this);
                 display.setCurrent(frame2);
             }
             catch(Exception e){
                 System.out.println("erreurs "+e) ;
             }
         }
         if(c ==backCommand){
             try{
                 display.setCurrent(Midlet.getframe());
             }
             catch(Exception e){
                 System.out.println("erreurs "+e) ;
             }
         }
         if(c ==sw){
             try{
                 show();
                 frame2=null ;
             }
             catch(Exception e){
                 System.out.println("erreurs "+e) ;
             }
         }
    }
    private double moyenne(Vector vec) {
        int j;
         double sum=0 ;
        for(j=0;j<vec.size();j++)
         {  
            sum +=((Double)vec.elementAt(j)).getvalue() ;
         }
        return sum/nombre ;
    }
    private double ecart(Vector vec) {
        double res=0,moy,sum=0,temp ;
        int j ;
        moy=moyenne(vec);
         for(j=0;j<vec.size();j++)
         {  
            temp =((Double)vec.elementAt(j)).getvalue() ;
            temp=(temp-moy)*(temp-moy) ;
           sum+=temp; 
         }
         System.out.println("sum="+sum+" / nombre"+nombre);
         res=Math.sqrt(sum/(nombre-1));
        return res ;
    }
    
   public void show(){
        display.setCurrent(frame) ;
    }
    private Display display;
    private Form frame;
    private Form frame2;
    private Command clcCommand1,clcCommand2;
    private Command confirm;
    private Command sw;
    static protected Command backCommand;

    private Vector tf,tin ;
    private TextField tf1,res ; 
    private int nombre ;
}
