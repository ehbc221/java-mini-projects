package Rsx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Modele.Agence;
import Modele.Client;
import Modele.Compte;
import Modele.Dagence;
import Modele.Dclient;
import Modele.Dcompte;
import Modele.Doperation;
import Modele.Operation;


public class Serveur extends Thread {

	private int port;
	private OutputStream os;
	public Serveur(int pPort){
		this.port = pPort;
	}

	public void run(){

		//dès le lancement du serveur je lance l'écoute des requêtes client
		try {
			ServerSocket ssServer = new ServerSocket(port); //port d'écoute du listener
			while(true){			  //je boucle en attendant une connexion cliente
				Socket sSocket = null;
				sSocket = ssServer.accept();
				System.out.println("connexion acceptée!");				
				try{
					new ServerThread(sSocket).start();}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}				
				//ois.close();
				//oos.close();
				//sSocket.close();
				//ssServer.close();
			}	
		}catch(Exception ex){
			//			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

	}


	public static void doServer(){

		Serveur s = new Serveur(2000);
		s.start();
		System.out.println("Démarré ^_^");
	}

	public static void main(String[] args){

		doServer();
	}

	private static synchronized boolean ajoutAgence(Agence agence){

		boolean res = false;
		Dagence dAgence = new Dagence();
		res = dAgence.addAgence(agence);
		return res;
	}

	private static ArrayList<Agence> afficheAgence(){

		ArrayList<Agence> lListeAgences = new ArrayList<Agence>();
		Dagence dAgence = new Dagence();
		lListeAgences = dAgence.getAgencesList();

		return lListeAgences;
	}
	
	private static synchronized boolean ajoutClient(Client client){

		boolean res;
		Dclient dClient = new Dclient();
		res = dClient.AddClient(client);
		
		return res;
	}

	private static ArrayList<Client> afficheClient(){
		ArrayList<Client> lListeClients = new ArrayList<Client>();
		Dclient dClient = new Dclient();
		lListeClients = dClient.getClientList();

		return lListeClients;
	}
	
	private static ArrayList<Client> afficheClientsAgence(String nomAgence){

		ArrayList<Client> lListeClients = new ArrayList<Client>();
		Dclient dClient = new Dclient();
		lListeClients = dClient.getClientList(nomAgence);

		return lListeClients;
	}
	
	private static synchronized boolean ajoutCompte(Compte compte, Operation operation){

		boolean res;
		Dcompte dcompte = new Dcompte();
		res = dcompte.AddCompte(compte);
		if(res){
			Doperation dOperation = new Doperation();
			res = dOperation.addOperation(operation);
		}
		return res;
	}
	
	private static ArrayList<Compte> afficheComptesClient(int numClient){
		
		ArrayList<Compte> lListeComptes = new ArrayList<Compte>();
		Dcompte dcompte = new Dcompte();
		lListeComptes = dcompte.getCompteList(numClient);
		
		return lListeComptes;
	}
	
	private static ArrayList<Compte> afficheComptes(){
		
		ArrayList<Compte> lListeComptes = new ArrayList<Compte>();
		Dcompte dcompte = new Dcompte();
		lListeComptes = dcompte.getCompteList();
		
		return lListeComptes;
	}
	
	private static synchronized boolean ajoutOperation(Operation operation){
		
		boolean res = false;
		Compte compte = new Compte();
		Dcompte dCompte = new Dcompte();
		compte = dCompte.FindCompte(operation.getNumCompteOp());
		
		if(((operation.getSensOp()).equals("CR") && (compte.getSoldeCompte() + operation.getMontantOp()) < 0)){
			Doperation dOperation = new Doperation();
			res = dOperation.addOperation(operation);
			if(res){
				compte.setSensCompte("DB");
				compte.setSoldeCompte(compte.getSoldeCompte() + operation.getMontantOp());
				res = dCompte.updateCompte(compte);
			}
		}
		if(((operation.getSensOp()).equals("CR") && (operation.getMontantOp() + compte.getSoldeCompte()) >= 0)){
			Doperation dOperation = new Doperation();
			res = dOperation.addOperation(operation);
			if(res){
				compte.setSensCompte("CR");
				compte.setSoldeCompte(compte.getSoldeCompte() + operation.getMontantOp());
				res = dCompte.updateCompte(compte);
			}
		}
		if(((operation.getSensOp()).equals("DB") && compte.getSoldeCompte() - compte.getSoldeCompte() < 0)){
			Doperation dOperation = new Doperation();
			res = dOperation.addOperation(operation);
			if(res){
				compte.setSensCompte("DB");
				compte.setSoldeCompte(compte.getSoldeCompte() - operation.getMontantOp());
				res = dCompte.updateCompte(compte);
			}
		}
		if(((operation.getSensOp()).equals("DB") && compte.getSoldeCompte() - compte.getSoldeCompte() >= 0)){
			Doperation dOperation = new Doperation();
			res = dOperation.addOperation(operation);
			if(res){
				compte.setSensCompte("DB");
				compte.setSoldeCompte(compte.getSoldeCompte() - operation.getMontantOp());
				res = dCompte.updateCompte(compte);
			}
		}
		return res;
	}
	
	public ArrayList<Operation> afficheOperations(String numCompte){
		
		ArrayList<Operation> listeOperations = new ArrayList<Operation>();
		Doperation dOperation = new Doperation();
		listeOperations = dOperation.getReleveCompte(numCompte);
		return listeOperations;
	}
	
	public class ServerThread extends Thread{
		ObjectInputStream ois;
		ObjectOutputStream oos;
		Socket sk;
		String action;

		public ServerThread(Socket sk){
			this.sk=sk;
		}

		public void run(){
			try{
				do {
//				je récupère l'entité sur laquelle l'opération doit s'effectuer
					ois = new ObjectInputStream(sk.getInputStream());
					action = (String) ois.readObject();
					
					if(action.equals("nouvelleAgence")){
						boolean res = false;
						Agence agence = new Agence();
						
						ois = new ObjectInputStream(sk.getInputStream());
						agence = (Agence)ois.readObject();
						res = ajoutAgence(agence);
						
						os = sk.getOutputStream();
						oos = new ObjectOutputStream(os);
						oos.writeObject("nouvelleAgence");
						oos.flush();
						
						os = sk.getOutputStream();
						oos = new ObjectOutputStream(os);
						oos.writeObject(res);
						oos.flush();
					}
					
					if (action.equals("listeAgence")) {

						ArrayList<Agence> listeAgences = new ArrayList<Agence>();
						listeAgences = afficheAgence();

//						une fois la liste récupérée je la renvoie au client
						try {
							os = sk.getOutputStream();
							oos = new ObjectOutputStream(os);
							oos.writeObject("listeAgence");
							oos.flush();
							//Instancier une nouvelle fois oos
							oos = new ObjectOutputStream(os);
							oos.writeObject(listeAgences);
							oos.flush();
						}catch(Exception ex){
							//							System.out.println(ex.getMessage());
							ex.printStackTrace();
						}
					}

					if(action.equals("nouveauClient")){
						boolean res = false;
						Client client = new Client();
						ois = new ObjectInputStream(sk.getInputStream());
						client = (Client)ois.readObject();
						res = ajoutClient(client);
						
						os = sk.getOutputStream();
						oos = new ObjectOutputStream(os);
						oos.writeObject("nouveauClient");
						oos.flush();
						
						os = sk.getOutputStream();
						oos = new ObjectOutputStream(os);
						oos.writeObject(res);
						oos.flush();
					}
					
					if (action.equals("listeClient")){
						ArrayList<Client> listeClients = new ArrayList<Client>();
						listeClients = afficheClient();
						try {
							os = sk.getOutputStream();
							oos = new ObjectOutputStream(os);
							oos.writeObject("listeClient");
							oos.flush();
							//Instancier une nouvelle fois oos
							oos = new ObjectOutputStream(os);
							oos.writeObject(listeClients);
							oos.flush();
						}catch(Exception ex){
							//							System.out.println(ex.getMessage());
							ex.printStackTrace();
						}
					}
					
					if(action.equals("nouveauCompte")){
						boolean res;
						Compte compte = new Compte();
						ois = new ObjectInputStream(sk.getInputStream());
						compte = (Compte)ois.readObject();
						Operation operation = new Operation();
						operation.setSensOp(compte.getSensCompte());
						operation.setMontantOp(compte.getSoldeCompte());
						operation.setNumCompteOp(compte.getNumCompte());
						res = ajoutCompte(compte, operation);
						
						os = sk.getOutputStream();
						oos = new ObjectOutputStream(os);
						oos.writeObject("nouveauCompte");
						oos.flush();
						
						os = sk.getOutputStream();
						oos = new ObjectOutputStream(os);
						oos.writeObject(res);
						oos.flush();
					}
					
					if(action.equals("selectionAgence")){
						ArrayList<Agence> listeAgences = new ArrayList<Agence>();
						
						try{
							listeAgences = afficheAgence();
							os = sk.getOutputStream();
							oos = new ObjectOutputStream(os);
							oos.writeObject("selectionAgence");
							oos.flush();
							//Instancier une nouvelle fois oos
							oos = new ObjectOutputStream(os);
							oos.writeObject(listeAgences);
							oos.flush();
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
					
					if(action.equals("listeClientsAgence")){
						
						try {
							ois = new ObjectInputStream(sk.getInputStream());
							String nomAgence = (String)ois.readObject();
							ArrayList<Client> listeClientsAgence = new ArrayList<Client>();
							listeClientsAgence = afficheClientsAgence(nomAgence);
							os = sk.getOutputStream();
							oos = new ObjectOutputStream(os);
							oos.writeObject("listeClientsAgence");
							oos.flush();
							//Instancier une nouvelle fois oos
							oos = new ObjectOutputStream(os);
							oos.writeObject(listeClientsAgence);
							oos.flush();
						}catch(Exception ex){
							//							System.out.println(ex.getMessage());
							ex.printStackTrace();
						}
					}
					
					if(action.equals("rechercherClient")){
						
						try {
							ois = new ObjectInputStream(sk.getInputStream());
							int numClient = (Integer)ois.readObject();
														
							ArrayList<Compte> listeComptesClient = new ArrayList<Compte>();
							listeComptesClient = afficheComptesClient(numClient);
							
							os = sk.getOutputStream();
							oos = new ObjectOutputStream(os);
							oos.writeObject("listerComptesClient");
							oos.flush();
							//Instancier une nouvelle fois oos
							oos = new ObjectOutputStream(os);
							oos.writeObject(listeComptesClient);
							oos.flush();
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
					
					if (action.equals("listeComptes")){
						ArrayList<Compte> listeComptes= new ArrayList<Compte>();
						listeComptes = afficheComptes();
						
						try {
							os = sk.getOutputStream();
							oos = new ObjectOutputStream(os);
							oos.writeObject("listeComptes");
							oos.flush();
							//Instancier une nouvelle fois oos
							oos = new ObjectOutputStream(os);
							oos.writeObject(listeComptes);
							oos.flush();
						}catch(Exception ex){
							//							System.out.println(ex.getMessage());
							ex.printStackTrace();
						}
					}
					
					if(action.equals("ajoutOperation")){
						boolean res =false;
						Operation operation = new Operation();
						Compte compte = new Compte();
						ois = new ObjectInputStream(sk.getInputStream());
						operation = (Operation)ois.readObject();
						res = ajoutOperation(operation);
						
						os = sk.getOutputStream();
						oos = new ObjectOutputStream(os);
						oos.writeObject("ajoutOperation");
						oos.flush();
						
						os = sk.getOutputStream();
						oos = new ObjectOutputStream(os);
						oos.writeObject(res);
						oos.flush();
					}
					
					if(action.equals("rechercherCompte")){
						
						try {
							ois = new ObjectInputStream(sk.getInputStream());
							String numCompte = (String)ois.readObject();
														
							ArrayList<Operation> listeOperations = new ArrayList<Operation>();
							Compte compte = new Compte();
							Dcompte dCompte = new Dcompte();
							listeOperations = afficheOperations(numCompte);
							compte = dCompte.FindCompte(numCompte);
							os = sk.getOutputStream();
							oos = new ObjectOutputStream(os);
							oos.writeObject("releveOperations");
							oos.flush();
							//Instancier une nouvelle fois oos
							oos = new ObjectOutputStream(os);
							oos.writeObject(listeOperations);
							oos.flush();
							oos = new ObjectOutputStream(os);
							oos.writeObject(compte);
							oos.flush();
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
				while(!action.equals("quitter"));
				
				ois.close();
				oos.close();
				sk.close();
			}
			catch (IOException e) {
				//				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				//				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
