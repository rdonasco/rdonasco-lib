<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
		<a href="<c:url value="/UserRegistrationForm.jsp"/>">Register</a><br/>
		<a href="<c:url value="/logon"/>">logon</a><br/>
		<a href="<c:url value="/greeting"/>">greeting</a><br/>
		<a href="<c:url value="/logout"/>">logout</a>
    </body>
</html>
