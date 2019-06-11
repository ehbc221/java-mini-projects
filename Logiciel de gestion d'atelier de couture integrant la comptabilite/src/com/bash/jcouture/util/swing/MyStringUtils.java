/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.util.swing;

import java.util.StringTokenizer;

/**
 *
 * @author bashizip
 */
public class MyStringUtils {


    public static  String returnJustID(String in,String token) {
        StringTokenizer tok = new StringTokenizer(in,token);
        return tok.nextToken();
    }
}
