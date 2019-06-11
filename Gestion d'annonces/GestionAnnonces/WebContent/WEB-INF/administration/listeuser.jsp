<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="Modeles.*"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<%@page import="org.apache.jasper.tagplugins.jstl.core.Redirect"%><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
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
	
	Integer num=0;
	Integer nbr=8;
	
	if (request.getParameter("num")!=null || (String)request.getParameter("nbr")!=null){
	 	num= Integer.parseInt((String)request.getParameter("num"));
	 	nbr = Integer.parseInt((String)request.getParameter("nbr"));
	}
	Integer num1 = 0;
	Integer num2 = 0;
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
				<a href="webannonces?page=/WEB-INF/admin.jsp"><img src="images/m5v.jpg" width="84" height="41" alt="M5"></a>
			</div>
		</div>
		
		<div id="headline">
		
			<div id="photo">
				
				<div>
					<h1 align=center> <%=Constante.TitreBody %></h1>				
				</div>
				<%		
					if (IdCat != 1){
						response.sendRedirect("/GestionAnnonces/webannonces");
					}else{
				%>
				<p/>
					**			
					<a href="webannonces?page=/WEB-INF/administration/listeuser.jsp" title="">Liste des utilisateurs</a>
					**
					<a href="webannonces?page=/WEB-INF/administration/listeannonce.jsp" title="">Liste des annonces</a>
					**
				<table width=100% border=1>
				<H4>
					<tr bgcolor=#FFF000>
					<B>
						<th>Nom</th>
						<th>Prénom</th>
						<th>Sexe</th>
						<th>N° de Tél</th>
						<th>Adresse</th>
						<th>Ville</th>
						<th>Region</th>
						<th>Pays</th>
						<th>Pseudo</th>
						<th>Mot de passe</th>
					</B>
					</tr>
				</H4>
				<%  AfficheUser liste =new AfficheUser(num,nbr);
        			while(liste.rs.next()){
        				Integer IdUtilisateur = liste.rs.getInt(1);
        				String nom = (String)liste.rs.getString(3);
						String prenom = (String)liste.rs.getString(4);
						String numtel = (String)liste.rs.getString(5);
						String adresse = (String)liste.rs.getString(6);
						String email = (String)liste.rs.getString(7);
						String ville = (String)liste.rs.getString(8);
						String pays = (String)liste.rs.getString(10);
						String region = (String)liste.rs.getString(9);
						String sexe = (String)liste.rs.getString(11);
						String pseudo = (String)liste.rs.getString(12);
						String password = (String)liste.rs.getString(13);
						
				%>
					<tr align="center" valign="middle">
					<font color=#FFFFFF >
						<td bgcolor=#CCCCFF><%=nom %></td>
						<td bgcolor=#CC9966><%=prenom %></td>
						<td bgcolor=#CCCCFF><%=sexe %></td>
						<td bgcolor=#CC9966><%=numtel %></td>
						<td bgcolor=#CCCCFF><%=adresse %></td>
						<td bgcolor=#CC9966><%=ville %></td>
						<td bgcolor=#CCCCFF><%=region %></td>
						<td bgcolor=#CC9966><%=pays %></td>
						<td bgcolor=#CCCCFF><%=pseudo %></td>
						<td bgcolor=#CC9966><%=password %></td>
						<td bgcolor=#ffffff><a href="webannonces?page=/WEB-INF/administration/listeuser.jsp&action1=deleteuser&IdUtilisateur=<%=IdUtilisateur %>"><img src ="images/supprimer.png"></img></a></td>
					</font>
					</tr>
				<%} %>
				<%
					CountUser Count =new CountUser();
					Integer nombre = Count.nombre;
					
					num1=num+nbr;
					if(num1>nombre-1){
						num1=num;
					}
					
					num2 = num-nbr;
					if (num2<0){
						num2=0;
					}
				%>
				<tr><td colspan="11" align="center">
					<a href="webannonces?page=/WEB-INF/administration/listeuser.jsp&num=<%=num2%>&nbr=<%=nbr %>">
					Précedent
					</a>
					--
					<a href="webannonces?page=/WEB-INF/administration/listeuser.jsp&num=<%=num1 %>&nbr=<%=nbr %>">
					Next
					</a>
				</td></tr>
			</table>
				<%		
					}
				%>
			</div>
			
			
			<div id="search-news">
				
				<h2 class="downcast">Authentification</h2>

				<div id="news-box">
					<div id="search-news">
					<form action ="webannonces?page=/WEB-INF/admin.jsp&action1=Authentifier" method="post">
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
							<a href="webannonces?action1=logout">Deconnexion</a>
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
