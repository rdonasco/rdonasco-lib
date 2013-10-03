<%-- 
    Document   : logonForm
    Created on : Jun 15, 2013, 4:03:23 PM
    Author     : Roy F. Donasco
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Logon Form</title>
    </head>
    <body>
        <h1>Logon Form</h1>
		<form action="<c:url value="logon"/>" method="POST">
			Login Id:<input type="text" name="userID" value="" /><br/>
			Password:<input type="password" name="password" value="" />
			<input type="submit" value="Submit" />
		</form>		
    </body>
</html>
