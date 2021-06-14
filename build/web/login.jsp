<%-- 
    Document   : login
    Created on : Mar 1, 2021, 4:06:14 PM
    Author     : tuanv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <script src='https://www.google.com/recaptcha/api.js'></script>
    </head>
    <body>
        <h1>
            Car Rental
            <a href="SearchController">Home</a>
        </h1>
        <form action="LoginController" method="POST">
            User ID: <input type="text" name="txtUserID" value="${param.txtUserID}">${requestScope.ERRORUSER}</br>
            Password: <input type="password" name="txtPassword">${requestScope.ERRORPASSWORD}</br>
            <input type="submit" name="btnAction" value="Login">${requestScope.ERRORLOGIN}</br>
            ${requestScope.capchaError}
            <div class="g-recaptcha"
                 data-sitekey="6LcckeAZAAAAACIid6Qt_gd8gjzIgIjExkR3Z-mk">
            </div>
        </form>
        <a href="create.jsp">Create new Account</a>

    </body>
</html>
