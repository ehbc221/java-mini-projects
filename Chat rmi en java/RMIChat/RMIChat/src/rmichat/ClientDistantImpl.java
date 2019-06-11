/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmichat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author bach
 */
public class ClientDistantImpl extends UnicastRemoteObject implements ClientDistant{

    public ClientDistantImpl()throws RemoteException{
    }

    @Override
    public void Message(String s,String Usr) throws RemoteException
    {
        Client.list2.add(Usr+" Say: "+s);
    }

    public void RemoveUser(String s){
        Client.list1.add(s);
    }

    public void AddUser(String s){
     Client.list1.add(s);
    }
}