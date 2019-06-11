<%-- 
    Document   : index
    Created on : 11 févr. 2013, 23:54:49
    Author     : Absolute
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">	
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type" />
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
    <link rel="stylesheet" href="/system/ressource?type=css&ref=style.css"  media="screen"/>
    <link rel="shortcut icon" type="image/x-icon" href="/system/ressource?type=image&ref=favicon.ico" />
    <title>Uploader Apps</title>
</head>
<body>
	<div id="main">
		<center><h1>Upload Your file</h1></center>
		<p><strong>INFORMATION : </strong> Select your file from directory and click on "uploader" to store the file in the website</p>
		<center><form method="post" enctype="multipart/form-data"  action="/system/store-picture" target="uploadFrame" id="save" >
                    <input type="file" name="images" id="images" />
                    <button type="submit" id="btn">UPLOADER</button>
                    </form>
		</center>
                
        <center><div id="response"></div>            
            <div id="uploadFrame"><iframe name="uploadFrame"></iframe></div></center>
	<center><span>Application develop by Donald</span></center>
	</div>
  <script src="/system/ressource?type=script&ref=jquery-1.3.1.min.js"></script>
  <script src="/system/ressource?type=script&ref=upload.js"></script>
</body>
</html>
