/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmichat;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author bach
 */
public interface ServeurChat extends Remote{
    void Connect(ClientDistant s,String User) throws RemoteException;
    void Desconnect(ClientDistant s,String User) throws RemoteException;
    void Getmessage(String s,String User) throws RemoteException;
}
