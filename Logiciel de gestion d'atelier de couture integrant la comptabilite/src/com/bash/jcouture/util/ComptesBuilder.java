/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.util;

/**
 *
 * @author bashizip
 */
public class ComptesBuilder {

    public static  String[] comptes={"Capital",
    "Achat des accésoires",
    "Couturier",
    "Brodeur",
    "Recettes associées aux commandes",
    "Recette associées aux prêt-à-porter"};
    
    public  static  String[] codes={"10","39","4221","4223","570","571"};

    public String  getCodeForCompte(int index){
        return codes[index];
    }


    public static void main(String[] args) {

    }
}
