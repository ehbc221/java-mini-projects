package Annonces;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import Modeles.*;


//import Modeles.*;
/**
 * Servlet implementation class webannonces
 */
public class webannonces extends HttpServlet {
	//private static final long serialVersionUID = 1L;
	static Connection Con ;
	static Statement St ;
	public String page=null;
	
	public String UserName = null;
	public String Password = null;
	
	public HttpSession session;
	public String UserN = null;
	public String PassW = null;
	
	public Integer IdCat;
	public Integer IdUser;
	
	public Constante Constante = new Constante();
	
  
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		//getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
		ConnexionDB conn = new ConnexionDB();
		IdCat = 0;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		session = request.getSession(true);
		UserName = request.getParameter("txtUserName");
		Password = request.getParameter("txtPassword");
		
		IdCat = (Integer)session.getAttribute("IdCat");
		
		if (IdCat==null){
			session.setAttribute("IdCat", 0);
		}
		
		try {
			doAuthentification(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("erreur d'authentification");
		}
		
		String action = request.getParameter("action1");
		if (action!=null){
			if (action.equalsIgnoreCase("AjouteUser")){
				try {
					doInscription(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("erreur d'action ajouter user");
				}
			}
			
			if (action.equalsIgnoreCase("AjouteImmobilier")){
				try {
					doAjouteImmobilier(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("erreur d'action ajouter immobilier");
				}
			}
			
			if (action.equalsIgnoreCase("EditUser")){
				try {
					doEditUser(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("erreur d'action edit user");
				}
			}
			
			if (action.equalsIgnoreCase("EditImmobilier")){
				try {
					doEditImmobilier(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("erreur d'action edit immobilier");
				}
			}
			
			if (action.equalsIgnoreCase("AjouteVehicule")){
				try {
					doAjouteVehicule(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("erreur d'action ajouter vehicule");
				}
			}
			
			if (action.equalsIgnoreCase("Authentifier")){
				try {
					doAuthentification(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Erreur au niveau de l'action authentifier");
				}			
			}
			
			if(action.equalsIgnoreCase("logout")){
				session = request.getSession(true);
				
			//on vide les variables des session
				session.setAttribute("UserName", null);
				session.setAttribute("Password", null);
				session.setAttribute("IdCat", 0);
				session.setAttribute("IdUser", 0);
				session.setAttribute("message", null);
				
				
			}
			
			if(action.equalsIgnoreCase("deleteuser")){
				try{
					doDeleteUser(request,response);
				}catch(Exception E){
					System.out.println("Erreur dans l'action DeleteUser");
				}
			}
			
			if(action.equalsIgnoreCase("deletetoutimmobilier")){
				try{
					doDeleteToutImmobilier(request,response);
				}catch(Exception E){
					System.out.println("Erreur dans l'action DeleteToutAnnonce");
				}
			}
		}		
		
		//Faire la redirection selon la demande
		page = request.getParameter("page");	
		if (page!=null){	
			getServletContext().getRequestDispatcher(page).forward(request, response);
		}else {
			getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
		}
		
		
	}
	
	public void doInscription(HttpServletRequest request, HttpServletResponse response) throws SQLException{
		//reception des parametres de l'inscription
		String nom = (String)request.getParameter("txtNom");
		String prenom = (String)request.getParameter("txtPrenom");
		String sexe = (String)request.getParameter("txtSexe");
		String numtel = (String)request.getParameter("txtNtel");
		String email = (String)request.getParameter("txtEmail");
		String adresse = (String)request.getParameter("txtAdresse");
		String ville = (String)request.getParameter("txtVille");
		String region = (String)request.getParameter("txtRegion");
		String pays = (String)request.getParameter("txtPays");
		
		String Pseudo=(String)request.getParameter("txtPseudo");
		String Passe=(String)request.getParameter("txtPasse");
		Integer IdCat =2;
		
		Authentification Existe_User =new Authentification();
		Existe_User.Existe_User(Pseudo);
		String Pseudo1 = null;
		while(Existe_User.rs.next()){
			Pseudo1 = Existe_User.rs.getString(1);
		}
		if(Pseudo.equalsIgnoreCase(Pseudo1)){
			request.setAttribute("user_existe","Ce pseudo est déja utilisé");
			
		}else{
			if (nom=="" || prenom=="" || numtel=="" || email=="" || Pseudo=="" || Passe==""){
				
			}else{
				inscription user = new inscription(nom,prenom,numtel,adresse,email,ville,pays,region,sexe,IdCat,Pseudo,Passe);
				request.setAttribute("ajouter_ok",user.ajouter_ok);
			}
		}
		
	}

	public void doEditUser(HttpServletRequest request, HttpServletResponse response) throws SQLException{
		
		
		String nom = (String)request.getParameter("txtNom");
		String prenom = (String)request.getParameter("txtPrenom");
		String numtel = (String)request.getParameter("txtNtel");
		String email = (String)request.getParameter("txtEmail");
		String adresse = (String)request.getParameter("txtAdresse");
		String ville = (String)request.getParameter("txtVille");
		String region = (String)request.getParameter("txtRegion");
		String pays = (String)request.getParameter("txtPays");
		
		String Passe=(String)request.getParameter("txtPasse");
		
		
		Integer IdUtilisateur = Integer.parseInt(request.getParameter("id"));
		
		if (nom=="" || prenom=="" || numtel=="" || email=="" || Passe==""){
			request.setAttribute("edit_ok","un ou plusieur champ necessaire sont vide");	
		}else{
			EditUser user = new EditUser(IdUtilisateur, nom, prenom, numtel, adresse, email, ville, region, pays,Passe);
			request.setAttribute("edit_ok",user.edit_ok);
		}
		
		
	}
	
	public void doEditImmobilier(HttpServletRequest request, HttpServletResponse response) throws SQLException{
		String titre = (String)request.getParameter("txtTitre");
		String type = (String)request.getParameter("txtTypeIm");
		String texte = (String)request.getParameter("txtAnnonce");
		String adresse = (String)request.getParameter("txtAdresseI");
		String ville = (String)request.getParameter("txtVilleI");
		String region = (String)request.getParameter("txtRegionI");
		String pays = (String)request.getParameter("txtPaysI");
		String date = (String)request.getParameter("txtDate");
		
		Integer IdAnnonce = Integer.parseInt((String)request.getParameter("IdAnnonce"));
		Integer IdAI = Integer.parseInt((String)request.getParameter("IdAI"));
		
		if (titre=="" || type=="" || adresse=="" || ville=="" || date==""){
			request.setAttribute("edit_ok","un ou plusieur champ necessaire sont vide");
		}else{
			EditImmobilier Immobilier= new EditImmobilier(IdAnnonce,IdAI,type,texte,adresse,ville,region,pays,date,titre);
			request.setAttribute("edit_ok",Immobilier.Edit_immobilier_ok);
		}
		
	}
	
	public void doAjouteImmobilier(HttpServletRequest request, HttpServletResponse response) throws SQLException{
		
		String titre = (String)request.getParameter("txtTitre");
		String type = (String)request.getParameter("txtTypeIm");
		String texte = (String)request.getParameter("txtAnnonce");
		String adresse = (String)request.getParameter("txtAdresseI");
		String ville = (String)request.getParameter("txtVilleI");
		String region = (String)request.getParameter("txtRegionI");
		String pays = (String)request.getParameter("txtPaysI");
		String date = (String)request.getParameter("txtDate");
		IdUser = (Integer) session.getAttribute("IdUser");
		
		if (titre=="" || type=="" || texte=="" || adresse=="" || ville=="" || region=="" || pays=="" || date==""){
			
		}else{
			if (IdUser!=0){
				try{
					AjouterImmobilier Immobilier = new AjouterImmobilier(type,texte,adresse,ville,region,pays,IdUser,date,titre);
					request.setAttribute("immobilier_ok",Immobilier.immobilier_ok);
				}catch(Exception e){
					request.setAttribute("immobilier_ok","problème au niveau de la requete d'ajout");
				}
			}
		}
	}
	
	public void doAjouteVehicule(HttpServletRequest request, HttpServletResponse response) throws SQLException{
		
		String titre = (String)request.getParameter("txtTitreV");
		String marque = (String)request.getParameter("txtMarque");
		String modele = (String)request.getParameter("txtModele");
		String texte = (String)request.getParameter("txtAnnonceV");
		String type = (String)request.getParameter("txtTypeV");
		String date = (String)request.getParameter("txtDateV");
		
		IdUser = (Integer) session.getAttribute("IdUser");
		if (titre=="" || type=="" || date=="" || marque=="" || modele=="" || texte=="" ){
			
		}else{
			if (IdUser!=0){
				AjouterVehicule Vehicule = new AjouterVehicule(marque,modele, type, texte, IdUser, date, titre);
				request.setAttribute("vehicule_ok",Vehicule.vehicule_ok);
			}
		}
	}

	public void doAuthentification(HttpServletRequest request, HttpServletResponse response){
		try {
			if(UserName=="" || Password=="" || UserName==null || Password==null){
				session.setAttribute("message", null);
			}else{
				Authentification id = new Authentification(UserName, Password);
				while(id.rs.next()){
					UserN = id.rs.getString(1);
					PassW = id.rs.getString(2);
					IdCat = id.rs.getInt(3);
					IdUser = id.rs.getInt(4);
				}
				
				if(UserName.equalsIgnoreCase(UserN) && Password.equalsIgnoreCase(PassW)){
							
					session.setAttribute("UserName", UserN);
					session.setAttribute("Password", PassW);
					session.setAttribute("IdCat", IdCat);
					session.setAttribute("IdUser", IdUser);
					session.setAttribute("message", null);
					
				}else{
					session.setAttribute("UserName", null);
					session.setAttribute("Password", null);
					session.setAttribute("IdCat", 0);
					session.setAttribute("IdUser", 0);
					session.setAttribute("message", Constante.message_authentification);
					
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("Problème au nsiveau de la requete d'authentification");
		}
		
	}
	
	protected void doDeleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		// TODO Auto-generated method stub
		Integer IdUtilisateur = 0;
		IdUtilisateur = Integer.parseInt(request.getParameter("IdUtilisateur"));
		
		DeleteUser User = new DeleteUser(IdUtilisateur);
		
	}
	
	protected void doDeleteToutImmobilier(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		// TODO Auto-generated method stub
		Integer IdAnnonce = 0;
		Integer IdAI=0;
		IdAnnonce = Integer.parseInt(request.getParameter("IdAnnonce"));
		IdAI = Integer.parseInt(request.getParameter("IdAI"));
		
		DeleteToutImmobilier Immobilier = new DeleteToutImmobilier(IdAnnonce, IdAI);
		
		//DeleteUser D = new DeleteUser(IdUser);
		
	}
	
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
