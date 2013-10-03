<%-- 
    Document   : registrationConfirmationForm
    Created on : Jun 15, 2013, 3:39:54 PM
    Author     : Roy F. Donasco
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="newUserProfile" scope="request" class="com.rdonasco.security.vo.UserSecurityProfileVO"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration Confirmation</title>
    </head>
    <body>
        <h1>Registration Confirmation</h1>
		<form action="<c:url value="/confirmUserRegistration"/>" method="POST">
			Logon ID: <input type="text" name="logonId" value="${newUserProfile.logonId}" readonly="readonly" />
			Token: <input type="text" name="registrationToken" value="${newUserProfile.registrationToken}"/>
			<input type="submit" value="Submit" />
		</form>
    </body>
</html>
