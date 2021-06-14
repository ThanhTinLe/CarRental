<%-- 
    Document   : verify
    Created on : Mar 2, 2021, 9:49:46 AM
    Author     : tuanv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Verify Page</title>
    </head>
    <body>
        <h1>Car Rental
            <a href="home.jsp">Home</a>
            <c:if test="${sessionScope.LOGIN_USER.role eq('New')}">
                <a href="verify.jsp">Verify</a>
            </c:if>
            <a href="LogoutController">Logout</a
        </h1>
        <form action="SendEmailController" method="POST">
            Email: <input type="email" name="txtemail" value="${sessionScope.LOGIN_USER.email}">${requestScope.SENDEMAIL}</br>
            <input type="submit" value="Send verify">
        </form>
        <form action="VerifyController" method="POST">
            Code: <input type="text" name="txtcode" >${requestScope.VERIFY}</br>
            <input type="submit" value="Verify">
        </form>
    </body>
</html>
