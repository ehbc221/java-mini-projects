/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kadiro;

/**
 *
 * @author ABDELKADER
 */
public class Double {
    double b ;
    public Double(double a){
        b=a ;
    }
    public static String gauche(String az){
    		String temp="" ;
    		for(int i=0;i<az.length();i++){
    			if(az.charAt(i)=='.'&&i==0){
    				return temp="0" ;
    			}
    			else if(az.charAt(i)=='.'&&i!=0){
    				return temp ;
    			}
    			else if(az.charAt(i)!='.'&&i!=az.length()-1)
    				       temp+=az.charAt(i) ;
    			else if(az.charAt(i)!='.'&&i==az.length()-1)
    				      return temp+=az.charAt(i) ;
    			else return temp="az";
    		}
    		return temp="az";
    	}
    	public static String droit(String az){
    		String temp="" ;
    		int i =0 ;
    		for(int j=0;j<az.length();j++){
    			if(az.charAt(j)=='.') {i=j+1 ;j+=az.length();}
    			else if(az.charAt(j)!='.'&&(j==az.length()-1)){return temp="0";}
    		}
    		for(;i<az.length();i++){
    			temp += az.charAt(i);
    		}
    		return temp ;
    	}
    	public static double parsedouble(String az){
    		double result,temp ;
    		String d=droit(az);
    		String g=gauche(az);
    		int id = Integer.parseInt(d);
    		int ig = Integer.parseInt(g);
    		temp = id ;
    		for(int i=0;i<d.length();i++){
    			temp=temp/10 ;
    		}
    		result=ig+temp ;
    		return result ;
    	}
     public void setvalue(double a) {
         b=a;
     }
     public double getvalue(){
         return b ;
     }
}
