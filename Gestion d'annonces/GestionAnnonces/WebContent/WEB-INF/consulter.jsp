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
				<a href="webannonces?page=/WEB-INF/inscription.jsp"><img src="images/m2.jpg" width="87" height="41" alt="M2"></a>
				<a href="webannonces?page=/WEB-INF/consulter.jsp"><img src="images/m3v.jpg" width="83" height="41" alt="M3"></a>
				<a href="webannonces?page=/WEB-INF/publier.jsp"><img src="images/m4.jpg" width="72" height="41" alt="M4"></a>
				<a href="webannonces?page=/WEB-INF/admin.jsp"><img src="images/m5.jpg" width="84" height="41" alt="M5"></a>
			</div>
		</div>
		
		<div id="headline">
		
			<div id="photo">
				
				<div>
					<h1 align=center> <%=Constante.TitreBody %></h1>				
				</div>
				<%		
					if (IdCat == 0){
						out.println(Constante.message_autorisation);
					}else{
				%>
				 Les catégorie des annonces à consulter:
        		<H4>
        			**			
					<a href="webannonces?page=/WEB-INF/typeannonceaconsulter/immobilier.jsp&num=0&nbr=4" title="">Immobilier</a>
					**
					<a href="webannonces?page=/WEB-INF/typeannonceaconsulter/vehicule.jsp" title="">Véhicule</a>
					**
					<a href="webannonces?page=/WEB-INF/typeannonceaconsulter/service.jsp" title="">Service</a>
					**
					<a href="webannonces?page=/WEB-INF/typeannonceaconsulter/emploi.jsp title="">Emploi</a>
					**
					<a href="webannonces?page=/WEB-INF/typeannonceaconsulter/autres.jsp" title="">Autres</a>
					**			
				</H4>	
				<%		
					}
				%>
			</div>
			
			
			<div id="search-news">
				
				<h2 class="downcast">Authentification</h2>

				<div id="news-box">
					<div id="search-news">
					<form action ="webannonces?page=/WEB-INF/consulter.jsp&action1=Authentifier" method="post">
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
							<a href="webannonces?page=/WEB-INF/consulter.jsp&action1=logout">Deconnexion</a>
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
