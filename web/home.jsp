<%-- 
    Document   : home
    Created on : Mar 2, 2021, 9:48:40 AM
    Author     : tuanv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
    </head>
    <body>
        <h1>
            Car Rental
            <a href="SearchController">Home</a>
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
        <form action="SearchController" method="POST">
            <font style="color: red">${requestScope.searchE}</font></br>
            <input type="radio" name="rdSearch" value="sName"<c:if test="${sessionScope.METHODSEARCH eq('sName')}"> checked="true"</c:if> > 
            <input type="text" name="txtNameCar" value="${sessionScope.SNAME}" placeholder="Car Name">
            </br>
            <input type="radio" name="rdSearch" value="sAnother" <c:if test="${sessionScope.METHODSEARCH eq('sAnother')}"> checked="true"</c:if>>
                <select name="selCategory">
                    <option value="-1" <c:if test="${sessionScope.SSELECT eq('-1')}"> selected="selected" </c:if> >All category</option>
                <c:forEach var="category" items="${sessionScope.LIST_CATE}">
                    <option value="${category.cateID}" <c:if test="${sessionScope.SSELECT eq(category.cateID)}"> selected="selected" </c:if>>${category.cateName}</option>
                </c:forEach>
            </select></br>
            Rental date: <input type="date" name="txtRentalDate" value="${sessionScope.RENTALDATE}"><font style="color: red">${requestScope.rentalE}</font>
            Return date: <input type="date" name="txtReturnDate" value="${sessionScope.RETURNDATE}"><font style="color: red">${requestScope.returnE}</font>
            Amount: <input type="number" name="txtAmount" value="${sessionScope.AMOUNT}" min="1" max="100"><font style="color: red">${requestScope.amount}</font>
            <input type="submit" value="Search">
        </form>
        <font style="color: green">${requestScope.ADDS}</font>
        <font style="color: green">${requestScope.CHECKOUT}</font>
        <font style="color: red">${requestScope.ADDE}</font>
        <c:if test="${sessionScope.LIST_CAR!=null}">
            <c:if test="${not empty sessionScope.LIST_CAR}">
                <table border="1" style="width: 1500px">
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>Car Name</th>
                            <th>Category</th>
                            <th>Description</th>
                            <th>Year</th>
                            <th>Quantity</th>
                            <th>Color</th>
                            <th>Price</th>
                            <c:if test="${sessionScope.LOGIN_USER.role eq('Active')}">
                                <th>Add</th>
                            </c:if>

                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="car" items="${sessionScope.LIST_CAR}">
                            <tr style="text-align: center">
                                <td>
                                    <img src="${car.image}" alt="${car.image}" width="360" height="200"/>
                                </td>
                                <td>${car.carName}</td>
                                <td>${car.category}</td>
                                <td>${car.description}</td>
                                <td>${car.year}</td>
                                <td>${car.quantity}</td>
                                <td>${car.color}</td>
                                <td>${car.price}</td>
                                <c:if test="${sessionScope.LOGIN_USER.role eq('Active')}">
                                    <td><a href="AddController?carID=${car.carID}">Add to cart</a></td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty sessionScope.LIST_CAR}">
            <font style="color: red">Not Result</font>
        </c:if>
        </c:if>
        <c:if test="${sessionScope.LIST_CAR==null}">
            <font style="color: red">Not Result</font>
        </c:if>
        </br>
        <c:if test="${sessionScope.NUMPAGE>1}">
            <c:forEach var="i" begin="1" end="${sessionScope.NUMPAGE}">
                <a href="SearchController?index=${i}">${i}</a>
            </c:forEach>
        </c:if>
    </body>
</html>
