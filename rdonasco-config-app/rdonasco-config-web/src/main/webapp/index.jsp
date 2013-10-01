<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
	<head>
		<title>Embedding in IFrame</title>
	</head>
	<body style="background: #d0ffd0;">
		<h1>This is a HTML page</h1>
		<p>Below is the configuration panel embedded inside a table</p>
		<table align="center" border="3">
			<tr>
				<th>Configuration Table</th>
			</tr>
			<tr valign="top">
				<td>
                    <iframe src="<c:url value="/config"/>" height="600"
                            width="800" frameborder="0"></iframe>
				</td> 
			</tr>
		</table>
	</body>
</html>
