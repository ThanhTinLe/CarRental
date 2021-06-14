<%-- 
    Document   : detail
    Created on : Mar 7, 2021, 4:39:43 AM
    Author     : tuanv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Detail Page</title>
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
        <c:if test="${requestScope.ORDERVIEW!=null}">
            <c:if test="${not empty requestScope.ORDERVIEW}">
                DATE: ${requestScope.ORDERVIEW.orderDate}  
                <table border="1">
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>Car Name</th>
                            <th>Category</th>
                            <th>Rental Date</th>
                            <th>Return Date</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th>Rating</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="detail" items="${requestScope.ORDERVIEW.hash}">
                            <tr>
                                <td>
                                    <img src="${detail.value.car.image}" alt="${car.image}" width="360" height="200"/>
                                </td>
                                <td>${detail.value.car.carName}</td>
                                <td>${detail.value.car.category}</td>
                                <td>${detail.value.rentalDate}</td>
                                <td>${detail.value.returnDate}</td>
                                <td>${detail.value.quantity}</td>
                                <td>${detail.value.price}</td>
                                <td>
                                    <c:if test="${detail.value.checkRating}">
                                        <c:if test="${detail.value.rating==null}">
                                            <form action="RatingConmtroller" method="POST">
                                                <input type="number" name="rating" value="10" max="10" min="0">/10
                                                <input type="hidden" name="detailID" value="${detail.key}">
                                                <input type="submit" value="Feedback">
                                            </form>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${detail.value.rating!=null}">
                                        ${detail.value.rating.ratingValue}/10
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <h1>
                    TOTAL PRICE:<font>${requestScope.ORDERVIEW.totalPrice}</font></br>
                    Discount PRICE:<font>${requestScope.ORDERVIEW.totalPrice*requestScope.ORDERVIEW.discount.disValue/100}</font></br>
                    TOTAL PAYMENT:<font>${requestScope.ORDERVIEW.totalPrice*(1-requestScope.ORDERVIEW.discount.disValue/100)}</font></br>
                </h1>
                <c:if test="${requestScope.ALLOWREMOVE!=null}">
                    <a style="text-decoration: none" href="RemoveOderController?orderID=${requestScope.ORDERVIEW.orderID}" onclick="return confirm('Do you want to remove?')">Remove</a>
                </c:if>
            </c:if>
            <c:if test="${empty requestScope.ORDERVIEW}">
                not result
            </c:if>
        </c:if>
        <c:if test="${requestScope.ORDERVIEW==null}">
            not result
        </c:if>
    </body>
</html>
