<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="Modeles.*"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript">AC_FL_RunContent = 0;</script>
<script src="AC_RunActiveContent.js" language="javascript"></script>
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
%>

<body>
	<div id="wrapper">
		<div id="header">
			<h1><img src="images/logo.jpg"></h1>
			<div id="nav">
				<a href="webannonces"><img src="images/m1.jpg" width="66" height="41" alt="M1"></a>
				<a href="webannonces?page=/WEB-INF/inscription.jsp"><img src="images/m2v.jpg" width="87" height="41" alt="M2"></a>
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
				
				<form action ="webannonces?page=/WEB-INF/inscription.jsp&action1=AjouteUser" method="post">
				<%
					String ajouter_ok=(String)request.getAttribute("ajouter_ok");
					if(ajouter_ok==null){
				%>
				<table>
					<tr>
						<td>Vous devez faire l'inscription sur le site WebAnnonces pour consulter et publier les annonces que vous voulez: </td>
					</tr>
				</table>
				<p>
				<table>
					<tr>
						<%
						if (request.getParameter("txtNom")=="" || request.getParameter("txtPrenom")=="" || request.getParameter("txtNtel")=="" || request.getParameter("txtEmail")=="" || request.getParameter("txtPseudo")=="" || request.getParameter("txtPasse")==""){
							
						out.println("<B>"+Constante.message_champ_vide+"</B>");
						}
						String existe =(String)request.getAttribute("user_existe");
						if (existe != null){
							
							out.println(existe);
						}
						%>
					</tr>
					
					<tr>
						<td>Nom:</td>
						<td><input name="txtNom" value="<%if(request.getParameter("txtNom")!=null) out.println(request.getParameter("txtNom")); %>" type="text" size="30">*</td>
					</tr>
					<tr>	
						<td>Prénom:</td>
						<td><input name="txtPrenom" value="<%if(request.getParameter("txtPrenom")!=null) out.println(request.getParameter("txtPrenom")); %>" type="text" size="30">*</td>
					</tr>
					<tr>					
						<td>Sexe:</td>
						<td><input name="txtSexe" value="Homme" type="radio" checked>Homme</td>
					</tr>
					<tr>
						<td></td>
						<td><input name="txtSexe" value="Femme" type="radio" >Femme</td>
					</tr>
					<tr>
						<td>N° Tel:</td>
						<td><input name="txtNtel" value="<%if(request.getParameter("txtNtel")!=null) out.println(request.getParameter("txtNtel")); %>" type="text" size="30">*</td>
					<tr>
					</tr>	
						<td>Email:</td>
						<td><input name="txtEmail" value="<%if(request.getParameter("txtEmail")!=null) out.println(request.getParameter("txtEmail")); %>" type="text" size="30">*</td>
					</tr>
					<tr>
						<td>Adresse:</td>
						<td><input name="txtAdresse" value="<%if(request.getParameter("txtAdresse")!=null) out.println(request.getParameter("txtAdresse")); %>" type="text" size="30"></td>
					</tr>
					<tr>	
						<td>Ville:</td>
						<td><input name="txtVille" value="<%if(request.getParameter("txtVille")!=null) out.println(request.getParameter("txtVille")); %>" type="text" size="13"></td>
					</tr>
					<tr>
						<td>Region:</td>
						<td><input name="txtRegion" value="<%if(request.getParameter("txtRegion")!=null) out.println(request.getParameter("txtRegion")); %>" type="text" size="13"></td>
					</tr>
					<tr>	
						<td>Pays:</td>
						<td><input name="txtPays" value="<%if(request.getParameter("txtPays")!=null) out.println(request.getParameter("txtPays")); %>" type="text" size="13"></td>
					</tr>
					<tr>	
						<td>Pseudo:</td>
						<td><input name="txtPseudo" value="<%if(request.getParameter("txtPseudo")!=null) out.println(request.getParameter("txtPseudo")); %>" type="text" size="20">*</td>
					</tr>
					<tr>	
						<td>Mot de passe:</td>
						<td><input name="txtPasse" value="" type="password" maxlength="8" size="20">*</td>
					</tr>
				</table>
				<table>
					<tr>
						<td><input type="submit" value="Ajouter l'utilisateur"></td>
					</tr>
				</table>
				<%}else{
					out.println(ajouter_ok);
				}%>
				</form>
				</p>
			</div>
			
			
			<div id="search-news">
				
				<h2 class="downcast">Authentification</h2>

				<div id="news-box">
					<div id="search-news">
					<form action ="webannonces?page=/WEB-INF/inscription.jsp&action1=Authentifier" method="post">
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
							<a href="webannonces?page=/WEB-INF/inscription.jsp&action1=logout">Deconnexion</a>
							</table>
							<h2 class="downcast">Profil</h2>
						<table>
							<a href="webannonces?page=/WEB-INF/monprofil.jsp">Consulter Mon Profil</a>
						<%
							}
						%>
						</table>
					</form>
									<script language="javascript">
	if (AC_FL_RunContent == 0) {
		alert("Cette page nécessite le fichier AC_RunActiveContent.js.");
	} else {
		AC_FL_RunContent(
			'codebase', 'http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0',
			'width', '200',
			'height', '200',
			'src', 'anim1',
			'quality', 'high',
			'pluginspage', 'http://www.macromedia.com/go/getflashplayer',
			'align', 'middle',
			'play', 'true',
			'loop', 'true',
			'scale', 'showall',
			'wmode', 'window',
			'devicefont', 'false',
			'id', 'anim1',
			'bgcolor', '#ffffff',
			'name', 'anim1',
			'menu', 'true',
			'allowFullScreen', 'false',
			'allowScriptAccess','sameDomain',
			'movie', 'anim1',
			'salign', ''
			); //end AC code
	}
</script>
<noscript>
	<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0" width="200" height="200" id="anim1" align="middle">
	<param name="allowScriptAccess" value="sameDomain" />
	<param name="allowFullScreen" value="false" />
	<param name="movie" value="/anim1.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#ffffff" />	<embed src="anim1.swf" quality="high" bgcolor="#ffffff" width="200" height="200" name="anim1" align="middle" allowScriptAccess="sameDomain" allowFullScreen="false" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
	</object>
</noscript>
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
