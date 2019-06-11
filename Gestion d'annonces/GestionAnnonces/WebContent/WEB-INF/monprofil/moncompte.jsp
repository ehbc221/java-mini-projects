<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="Modeles.*"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

	<title>WebAnnonces</title>
	<link rel="stylesheet" href="style.css" type="text/css" charset="utf-8" />
</head>
<% 
	Constante Constante = new Constante();
	String UserName = (String)session.getAttribute("UserName");
	String Password = (String)session.getAttribute("Password");
	String message = (String)session.getAttribute("message");

	Integer IdCat = (Integer)session.getAttribute("IdCat");
	Integer IdUser = (Integer)session.getAttribute("IdUser");
	
	String edit = request.getParameter("edit");
	
	String Categorie;
	String Nom;
	String Prenom;
	String NumTel;
	String Adresse;
	String Email;
	String Ville;
	String Region;
	String Pays;
	String Sexe;
	String PseudoName;
	String PassWord;
	
	String action=null;
	String edit_ok=(String)request.getAttribute("edit_ok");
	
%>

<body>
	<div id="wrapper">
		<div id="header">
			<h1><img src="images/logo.jpg"></h1>
			<div id="nav">
				<a href="webannonces"><img src="images/m1.jpg" width="66" height="41" alt="M1"></a>
				<a href="webannonces?page=/WEB-INF/inscription.jsp"><img src="images/m2.jpg" width="87" height="41" alt="M2"></a>
				<a href="webannonces?page=/WEB-INF/consulter.jsp"><img src="images/m3.jpg" width="83" height="41" alt="M3"></a>
				<a href="webannonces?page=/WEB-INF/publier.jsp"><img src="images/m4.jpg" width="72" height="41" alt="M4"></a>
				<a href="webannonces?page=/WEB-INF/admin.jsp"><img src="images/m5.jpg" width="84" height="41" alt="M5"></a>
			</div>
		</div>
		
		<div id="headline">
		
			<div id="photo">
				
				<div>
					<h1 align=center> <%=Constante.TitreBody %></h1>				
				</div>
				<p/>		
				
				<% 
					if(IdUser==0){
						response.sendRedirect("/GestionAnnonces/webannonces");
					}else{
						
						ProfilUser User = new ProfilUser(IdUser);
						while(User.rs.next()){
							
							Integer CategorieID = User.rs.getInt(2);
							if (CategorieID==1){
								Categorie="Administrateur";
							}else{
								Categorie="Utilisateur";
							}
							Nom=User.rs.getString(3);
							Prenom=User.rs.getString(4);
							NumTel=User.rs.getString(5);
							Adresse=User.rs.getString(6);
							Email=User.rs.getString(7);
							Ville=User.rs.getString(8);
							Region=User.rs.getString(9);
							Pays=User.rs.getString(10);
							Sexe=User.rs.getString(11);
							PseudoName=User.rs.getString(12);
							PassWord=User.rs.getString(13);
				%>
				<h1>Le profil de l'utilisateur:<%=Nom %> <%=Prenom %></h1>
				<p/><p/>
				**			
				<a href="webannonces?page=/WEB-INF/monprofil/mesannonces.jsp" title="">Mes annonces</a>
				**
				<a href="webannonces?page=/WEB-INF/monprofil/moncompte.jsp" title="">Mon compte</a>
				**
				<br/><br/>
				<%
					if(edit==null){ 
				
						if(edit_ok!=null){
							out.println(edit_ok);
						}
				%>
				<table Border="1" width=80%>
					
						
					<tr bgcolor=#CCCCFF>
						<td>Nom:</td>
						<td><%=Nom %></td>
					</tr>
					<tr>
						<td>Prenom:</td>
						<td><%=Prenom %></td>
					</tr>
					<tr  bgcolor=#CCCCFF>
						<td>Catégorie d'utilisateur:</td>
						<td><%=Categorie%></td>
					</tr>
					<tr>
						<td>N° de Téléphone:</td>
						<td><%=NumTel %></td>
					</tr>
					<tr  bgcolor=#CCCCFF>
						<td>Email</td>
						<td><%=Email %></td>
					</tr>
					<tr>
						<td>Adresse</td>
						<td><%=Adresse %></td>
					</tr>
					<tr  bgcolor=#CCCCFF>
						<td>Ville</td>
						<td><%=Ville %></td>
					</tr>
					<tr>
						<td>Region</td>
						<td><%=Region %></td>
					</tr>
					<tr  bgcolor=#CCCCFF>
						<td>Pays</td>
						<td><%=Pays %></td>
					</tr>
					<tr>
						<td>Pseudo:</td>
						<td><%=PseudoName %></td>
					</tr>
					<tr bgcolor=#CCCCFF>
						<td>Mot de Passe:</td>
						<td><%=PassWord %></td>
					</tr>
					<tr>
						<td>Sexe:</td>
						<td><%=Sexe %></td>
					</tr>
				</table>
				<a href="webannonces?page=/WEB-INF/monprofil/moncompte.jsp?edit=yes&id=<%=IdUser %>"><img src="images/modifier.png" title="edit"/></a>
				<%}else{
				%>
				<form action="webannonces?page=/WEB-INF/monprofil/moncompte.jsp&action1=EditUser&id=<%=IdUser %>" method="post">
				
				<table Border="1" width=80%>
					
						
					<tr bgcolor=#CCCCFF>
						<td>Nom:</td>
						<td><input name="txtNom" value="<%=Nom%><%if(request.getParameter("txtNom")!=null) out.println(request.getParameter("txtNom")); %>" type="text" size="30"></td>
						</tr>
					<tr>
						<td>Prenom:</td>
						<td><input name="txtPrenom" value="<%=Prenom%><%if(request.getParameter("txtPrenom")!=null) out.println(request.getParameter("txtPrenom")); %>" type="text" size="30"></td>
					</tr>
					<tr  bgcolor=#CCCCFF>
						<td>Catégorie d'utilisateur:</td>
						<td><%=Categorie%></td>
					</tr>
					<tr>
						<td>N° de Téléphone:</td>
						<td><input name="txtNtel" value="<%=NumTel %><%if(request.getParameter("txtNtel")!=null) out.println(request.getParameter("txtNtel")); %>" type="text" size="30"></td>
					</tr>
					<tr  bgcolor=#CCCCFF>
						<td>Email</td>
						<td><input name="txtEmail" value="<%=Email %><%if(request.getParameter("txtEmail")!=null) out.println(request.getParameter("txtEmail")); %>" type="text" size="30"></td>
					</tr>
					<tr>
						<td>Adresse</td>
						<td><input name="txtAdresse" value="<%=Adresse %><%if(request.getParameter("txtAdresse")!=null) out.println(request.getParameter("txtAdresse")); %>" type="text" size="30"></td>
					</tr>
					<tr  bgcolor=#CCCCFF>
						<td>Ville</td>
						<td><input name="txtVille" value="<%=Ville %><%if(request.getParameter("txtVille")!=null) out.println(request.getParameter("txtVille")); %>" type="text" size="13"></td>
					</tr>
					<tr>
						<td>Region</td>
						<td><input name="txtRegion" value="<%=Region %><%if(request.getParameter("txtRegion")!=null) out.println(request.getParameter("txtRegion")); %>" type="text" size="13"></td>
					</tr>
					<tr  bgcolor=#CCCCFF>
						<td>Pays</td>
						<td><input name="txtPays" value="<%=Pays %><%if(request.getParameter("txtPays")!=null) out.println(request.getParameter("txtPays")); %>" type="text" size="13"></td>
					</tr>
					<tr>
						<td>Pseudo:</td>
						<td><%=PseudoName%></td>
					</tr>
					<tr bgcolor=#CCCCFF>
						<td>Mot de Passe:</td>
						<td><input name="txtPasse" value="<%=PassWord %><%if(request.getParameter("txtPasse")!=null) out.println(request.getParameter("txtPasse")); %>" type="text" maxlength="8" size="20"></td>
					</tr>	
									
				</table>
				<input type="image" name="ok" value="" src="images/valider.png" title="Valider"/>
					
				</form>
				
				<%}%>
				
				<%}
					}
				%>
				
				
			</div>
			
			
			<div id="search-news">
				
				<h2 class="downcast">Authentification</h2>

				<div id="news-box">
					<div id="search-news">
					<form action ="webannonces?page=/WEB-INF/monprofil.jsp&action1=Authentifier" method="post">
						<table>
							<%		
								if (UserName==null || Password==null){
									if (message!=null){
										out.println("<font color=#ff3300>"+message+"!!!</font>");
									}
							%>
							<tr>
								<td>Nom d'utilisateur:</td>
								<td><div><input type="text" name="txtUserName" value="" id="q" /></div></td>
							</tr>
							<tr>
								<td>Mot de pass:</td>
								<td><div><input type="password" name="txtPassword" value="" maxlength="8" id="q" /></div></td>
							</tr>
							<tr>
								<td></td>
								<td>
									<div class="more">
										<input type="image" name="ok" value="" src="images/btn_ok.gif">
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<a href="webannonces?page=/WEB-INF/inscription.jsp" title="">S'inscrire</a>
								</td>
							</tr>
							<%
								}else{
									out.println("Bienvenue <font color = #ED5929>"+UserName+"</font>");
							%>
							<br><br>
							<a href="webannonces?action1=logout">Deconnexion</a>
							<p/><p/><p/><br><br><br><br>
							</table>
							<h2 class="downcast">Profil</h2>
						<table>
							<a href="webannonces?page=/WEB-INF/monprofil.jsp">Consulter Mon Profil</a>
						<%
							}
						%>
						</table>
					</form>
					</div>
				</div>
				
			</div>
			
			
		</div>
		
		<div id="body">
			<div id="body-left">
				
			</div>
			<div id="body-right">
				
			</div>
		</div>
		<div class="clear">
			
		</div>
	</div>

	</div>
	<div id="footer">
		<p>&copy; 2010 all rights reserved.</p>
		<p>&copy; DAHMANI Tarik & BELLAARI Abdelouahid.</p>
	</div>
</body>
</html>
