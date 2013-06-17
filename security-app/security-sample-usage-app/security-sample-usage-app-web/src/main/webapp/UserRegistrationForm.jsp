<%-- 
    Document   : UserRegistrationForm
    Created on : Jun 15, 2013, 3:12:25 PM
    Author     : Roy F. Donasco
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Registration Form</title>
    </head>
    <body>

        <h1>User Registration Form</h1>
		<form action="<c:url value="/register"/>" method="POST">
			Login Id:<input type="text" name="loginId" value="" /><br/>
			Password:<input type="password" name="password" value="" />
			<input type="submit" value="Submit" />
		</form>
    </body>
</html>
