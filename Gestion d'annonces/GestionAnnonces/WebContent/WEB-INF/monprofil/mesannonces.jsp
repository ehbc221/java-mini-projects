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
	
	String editmesannonces=request.getParameter("editmesannonces");
	String edit_ok=(String)request.getAttribute("edit_ok");
	
	String type;
	String adresse ;
	String ville;
	String region;
	String pays;
	String texte;
	String date;
	String titre;
	
	Integer IdAnnonce;
	Integer IdAI;
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
				<h1>Le profil de l'utilisateur:<B><%=UserName %></B></h1>
				<p/><p/>
				<% 
					if(IdUser==0){
						response.sendRedirect("/GestionAnnonces/webannonces");
					}else{
				%>
				**			
				<a href="webannonces?page=/WEB-INF/monprofil/mesannonces.jsp" title="">Mes annonces</a>
				**
				<a href="webannonces?page=/WEB-INF/monprofil/moncompte.jsp" title="">Mon compte</a>
				**
				<H4>Le liste des annonces publier par l'utilisateur:</H4>
				<%
					if(editmesannonces==null){	
						
						if(edit_ok!=null){
							out.println(edit_ok);
						}
				%>
				<table width=100% border=1>
				
					Les annonces <B>Immobilier</B>: 
					<tr bgcolor=#FFF000 >
					<B>
						<th>Titre</th>
						<th>Date de l'annonce</th>
						<th>Type de l'immobiler</th>
						<th>Adresse</th>
						<th>Ville</th>
						<th>Region</th>
						<th>Pays</th>
						<th>Texte de l'annonce</th>
						<th colspan="3">-------</th>
											
					</B>		
					</tr>
				
				<%  AnnonceUser MesAnnonces =new AnnonceUser(IdUser);
        			while(MesAnnonces.rs_imm.next()){ 
        				
        				type = (String)MesAnnonces.rs_imm.getString(3);
						adresse = (String)MesAnnonces.rs_imm.getString(4);
						ville = (String)MesAnnonces.rs_imm.getString(5);
						region = (String)MesAnnonces.rs_imm.getString(6);
						pays = (String)MesAnnonces.rs_imm.getString(7);
						texte = (String)MesAnnonces.rs_imm.getString(8);
						date = (String)MesAnnonces.rs_imm.getString(12);
						titre = (String)MesAnnonces.rs_imm.getString(11);
						
						IdAnnonce = MesAnnonces.rs_imm.getInt(9);
						IdAI = MesAnnonces.rs_imm.getInt(1);
				%>
				
					<tr align="center" valign="middle">
						
					<font color=#ffffff>
						<td bgcolor=#446666><%=titre %></td>
						<td bgcolor=#CC9966><%=date %></td>
						<td bgcolor=#446666><%=type %></font></td>
						<td bgcolor=#CC9966><%=adresse %></td>
						<td bgcolor=#446666><%=ville %></td>
						<td bgcolor=#CC9966><%=region %></td>
						<td bgcolor=#446666><%=pays %></td>
						<td bgcolor=#CC9966><%=texte %></td>
						<td><a href="webannonces?page=/WEB-INF/monprofil/mesannonces.jsp&action1=deletetoutimmobilier&IdAnnonce=<%=IdAnnonce %>&IdAI=<%=IdAI %>"><img src ="images/supprimer.png" title="supprimer"></img></a></td>
						<td><a href="webannonces?page=/WEB-INF/monprofil/mesannonces.jsp&editmesannonces=yes&IdAnnonce=<%=IdAnnonce %>&IdAI=<%=IdAI %>"><img src ="images/edit.png" title="modifier"></img></a></td>
						<td><a href="webannonces?page=/WEB-INF/typeannonceapublier/immobilier.jsp"><img src ="images/add.png" title="Ajouter"></img></a></td>
					</font>	
					</tr>
					
				<%} %>
				</table>
				<%}else{
					Integer IdAI_param = Integer.parseInt(request.getParameter("IdAI"));
					Integer IdAnnonce_param = Integer.parseInt(request.getParameter("IdAnnonce")); 
					mannonce mannonce_immobilier = new mannonce();
					mannonce_immobilier.mannonceimmobilier(IdAnnonce_param,IdAI_param);
					
					while(mannonce_immobilier.rs.next()){
						
						type = (String)mannonce_immobilier.rs.getString(3);
						adresse = (String)mannonce_immobilier.rs.getString(4);
						ville = (String)mannonce_immobilier.rs.getString(5);
						region = (String)mannonce_immobilier.rs.getString(6);
						pays = (String)mannonce_immobilier.rs.getString(7);
						texte = (String)mannonce_immobilier.rs.getString(8);
						date = (String)mannonce_immobilier.rs.getString(12);
						titre = (String)mannonce_immobilier.rs.getString(11);
						
						IdAnnonce = mannonce_immobilier.rs.getInt(9);
						IdAI = mannonce_immobilier.rs.getInt(1);
					
				%>
				<form action ="webannonces?page=/WEB-INF/monprofil/mesannonces.jsp&action1=EditImmobilier&IdAnnonce=<%=IdAnnonce %>&IdAI=<%=IdAI %>" method="post">
				<%
					String immobilier_ok=(String)request.getAttribute("immobilier_ok");
					if(immobilier_ok==null){
				%>
				<table>
					<tr>
						<%
							if (request.getParameter("txtTitre")=="" || request.getParameter("txtTypeIm")=="" || request.getParameter("txtAnnonce")=="" || request.getParameter("txtAdresseI")=="" || request.getParameter("txtVilleI")=="" || request.getParameter("txtRegionI")=="" || request.getParameter("txtPaysI")=="" || request.getParameter("txtDate")==""){
								out.println("<B>"+Constante.message_champ_vide+"</B>");
							}
							
						%>
					</tr>
					<tr >
						<td>Titre de l'annonce:</td>
						<td><input name="txtTitre" value="<%=titre %><%if(request.getParameter("txtTitre")!=null) out.println(request.getParameter("txtTitre")); %>" type="text" size="60">*</td>
					</tr>
					<tr>
						<td>Type d'immobilier:</td>
						<td><input name="txtTypeIm" value="<%=type%><%if(request.getParameter("txtTypeIm")!=null) out.println(request.getParameter("txtTypeIm")); %>" type="text" size="20">*</td>
					</tr>
					<tr>
						<td>Texte de l'annonce:</td>
						<td><TEXTAREA rows="5" cols="40" name="txtAnnonce" ><%=texte %><%if(request.getParameter("txtAnnonce")!=null) out.println(request.getParameter("txtAnnonce")); %></TEXTAREA>*</td>
					</tr>
					<tr>
						<td>Adresse:</td>
						<td><input name="txtAdresseI" value="<%=adresse %><%if(request.getParameter("txtAdresseI")!=null) out.println(request.getParameter("txtAdresseI")); %>" type="text" size="30">*</td>
					</tr>
					<tr>
						<td>Ville:</td>
						<td><input name="txtVilleI" value="<%=ville%><%if(request.getParameter("txtVilleI")!=null) out.println(request.getParameter("txtVilleI")); %>" type="text" size="20">*</td>
					</tr>
					<tr>
						<td>Region:</td>
						<td><input name="txtRegionI" value="<%=region %><%if(request.getParameter("txtRegionI")!=null) out.println(request.getParameter("txtRegionI")); %>" type="text" size="20">*</td>
					</tr>
					<tr>
						<td>Pays:</td>
						<td><input name="txtPaysI" value="<%=pays %><%if(request.getParameter("txtPaysI")!=null) out.println(request.getParameter("txtPaysI")); %>" type="text" size="20">*</td>
					</tr>
					<tr>
						<td>Date de l'annonce:</td>
						<td><input name="txtDate" value="<%=date %><%if(request.getParameter("txtDate")!=null) out.println(request.getParameter("txtDate")); %>" type="text" size="20"> jj/mm/aaaa</td>
					</tr>
				</table>
				<table>
					<tr>
						<td><input type="image" src="images/valider.png" title="valider"></td>
					</tr>
				</table>
				<%
				}else{
					out.println(immobilier_ok);
				}%>
			</form>
				
				<%} %>
				<%
					}
				%>
				<%} %>
				
				
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
