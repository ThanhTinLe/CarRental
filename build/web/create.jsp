<%-- 
    Document   : create
    Created on : Mar 1, 2021, 4:25:04 PM
    Author     : tuanv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Account Page</title>
    </head>
    <body>
        <h1>Car rental
            <a href="home.jsp">Home</a>
            <a href="login.jsp">Login</a>
        </h1>
        <form action="CreateController" method="POST">
            UserID: <input type="text" name="txtUserID" value="${param.txtUserID}">${requestScope.CREATEERROR.userE}</br>
            Name: <input type="text" name="txtName" value="${param.txtName}">${requestScope.CREATEERROR.nameE}</br>
            Password: <input type="password" name="txtPassword">${requestScope.CREATEERROR.passwordE}</br>
            Confirm: <input type="password" name="txtConfirm">${requestScope.CREATEERROR.confrimE}</br>
            Phone: <input type="number" name="txtPhone" min="0" length="10" value="${param.txtPhone}">${requestScope.CREATEERROR.phoneE}</br>
            Address: <input type="text" name="txtAddress" value="${param.txtAddress}">${requestScope.CREATEERROR.addressE}</br>
            <input type="submit" name="btnAction" value="Create Account">
        </form>
    </body>
</html>
