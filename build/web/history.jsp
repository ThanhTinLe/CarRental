<%-- 
    Document   : history
    Created on : Mar 6, 2021, 10:46:35 PM
    Author     : tuanv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>History Page</title>
    </head>
    <body>
        <h1>
            Car Rental
            <a href="home.jsp">Home</a>
            <c:if test="${sessionScope.LOGIN_USER.role eq('New')}">
                <a href="verify.jsp">Verify</a>
            </c:if>
            <c:if test="${sessionScope.LOGIN_USER.role eq('Active')}">
                <a href="cart.jsp">Cart</a>
            </c:if>
            <c:if test="${sessionScope.LOGIN_USER.role eq('Active')}">
                <a href="HistoryController">History</a>
            </c:if>
            <c:if test="${not empty sessionScope.LOGIN_USER}">
                <a href="LogoutController">Logout</a>
            </c:if>
            <c:if test="${empty sessionScope.LOGIN_USER}">
                <a href="login.jsp">Login</a>
            </c:if>
        </h1>
        <form action="HistoryController" method="POST">
            <input type="radio" name="rdhis" value="sName" <c:if test="${sessionScope.SH eq('sName')}"> checked="true"  </c:if>><input type="text" name="txtSearch" value="${param.txtSearch}" placeholder="Car Name"></br>
            <input type="radio" name="rdhis" value="sdate" <c:if test="${sessionScope.SH eq('sdate')}"> checked="true"  </c:if>><input type="date" name="txtdate" value="${sessionScope.DATE}"></br>
            <input type="submit" value="Search">
        </form>
                <font>${requestScope.DELEOR}</font>
            <c:if test="${sessionScope.HISTORY!=null}">
                <c:if test="${not empty sessionScope.HISTORY}">
                    <c:forEach var="order" items="${sessionScope.HISTORY}">
                        <form action="ViewDetailController" method="POST">
                        DATE: ${order.orderDate}  Price: ${order.totalPrice}
                        <input type="hidden" name="txtorderID" value="${order.orderID}">
                        <input type="submit" value="View Detai">
                        </br>
                        </form> 
                    </c:forEach>
                </c:if>
                <c:if test="${empty sessionScope.HISTORY}">
                    Not Result
                </c:if>
            </c:if>
            <c:if test="${sessionScope.HISTORY==null}">
                Not Result
            </c:if>
    </body>
</html>
