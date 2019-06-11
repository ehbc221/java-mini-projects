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
public class Tri implements CommandListener {
    public Tri(Display d){
        
        confirm = new Command("confirmer", Command.OK, 1);
        backCommand = new Command("revenir", Command.BACK, 1);
        sw = new Command("choix_nb", Command.BACK, 1);
        tf1 = new TextField(new String(),new String(),20,0) ;
        display = d;
        frame = new Form("kadiro tri");
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
                      tri(tin);
                      min = ((Double)tin.firstElement()).getvalue() ;    
                      max = ((Double)tin.lastElement()).getvalue() ;
                      for(int i=0;i<tin.size();i++){
                          act = ((Double)tin.elementAt(i)).getvalue();
                         frame2.append("val-"+i+"="+act+"\n");
                      }
                 }
                
        }catch(Exception e){frame2.append("erreurs ");}}
       if(c ==confirm){
             try{
                 try{
                 att = Integer.parseInt(tf1.getString());}
                 catch(Exception ee){frame.append("erreur parametre");return;
                 }
                 clcCommand1 = new Command("Calcule", Command.OK, 2);
                 frame2 = new Form("kadiro tri");
                 frame2.addCommand(clcCommand1);
                 frame2.addCommand(sw);
                 frame2.append("Par OUALI Abdelkader\n") ;
                 frame2.append("les valeurs des valeurs\n");
                 if(tf!=null) tf = null ;
                 tf = new Vector() ;
                 for(int i=0;i<att;i++){
                     tf.addElement(new TextField(new String(),new String(),10,0));
                     frame2.append("val"+i+" ");
                     frame2.append((TextField)tf.elementAt(i));
                 }
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
     public void tri(Vector vec){
         int j,i;
         double cle ;
         for(j=1;j<vec.size();j++)
         {
            cle =((Double)vec.elementAt(j)).getvalue() ;
            i = j-1 ;
            while (i>=0 &&  ((Double)vec.elementAt(i)).getvalue() >cle) {
               vec.setElementAt((Double)vec.elementAt(i),i+1);
               i=i-1;
            }
            vec.setElementAt(new Double(cle),i+1);
         }
     }
    public void show(){
        display.setCurrent(frame) ;
    }
    private Display display;
    private Form frame;
    private Form frame2;
    private Command clcCommand1;
    private Command confirm;
    private Command sw;
    static protected Command backCommand;
   
    private Vector tf,tin ;
    private TextField tf1 ;
    double min,max,act,res ;
    int att ;
}
