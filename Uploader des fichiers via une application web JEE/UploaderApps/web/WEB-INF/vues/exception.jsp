<%-- 
   Document   : exception
    Created on : 23 juil. 2012, 19:53:05
    Author     : bertrand
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
	<head>
            <title>Exception page</title>
            <meta name="author" content="Etolo Seme Donald, techno-pédagogue, titulaire d'une licence en informatique et d'un DIPES 1" />
            <link rel="icon" type="image/x-icon" href="/system/ressource?type=image&ref=favicon.ico" />
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	</head>
	<body>
            <div style="border: 2px black double;width:400px;background-color: red;margin: 0 auto;">
                <h2>Error has occured, contact the webmaster</h2>
            </div>  
            <div style="border: 2px black double;width:400px;background-color: lightblue;margin: 0 auto;height: 400px;margin-top: auto;">
				Exception following has occured  :
				<c:out value="${exception.message}"/>
            </div>
	</body>
</html>

