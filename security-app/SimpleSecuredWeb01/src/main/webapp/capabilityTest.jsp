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
        <title>Capability Test</title>
    </head>
    <body>
		<a href="<c:url value="/"/>">Home</a>
		<form action="<c:url value="/capabilityTest"/>" method="POST">
			Action:<input type="text" name="action" value="<c:out value="${action}"/>" /><br/>
			Resource:<input type="text" name="resource" value="<c:out value="${resource}"/>" />
			<input type="submit" value="Submit" />
		</form>		
		<br/>
		<c:out value="${message}"/>
    </body>
</html>
