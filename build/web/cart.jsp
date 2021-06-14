<%-- 
    Document   : cart
    Created on : Mar 5, 2021, 4:44:27 PM
    Author     : tuanv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cart Page</title>
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
        <c:if test="${sessionScope.CART.hash!=null}">
            <c:if test="${not empty sessionScope.CART.hash}">
                <h1>
                <font>${requestScope.rentalE}</font>
                <font>${requestScope.returnE}</font>
                <font>${requestScope.quantityE}</font>
                </h1>
                <table border="1">
                    <thead>
                        <tr>
                            <th>Car Name</th>
                            <th>image</th>
                            <th>Category</th>
                            <th>Quantity</th>
                            <th>Rental Date</th>
                            <th>Return Date</th>
                            <th>price</th>
                            <th>Update</th>
                            <th>Remove</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="orderDetail" items="${sessionScope.CART.hash}">
                            <tr>
                            <form action="UpdateController" method="POST">
                            <td>${orderDetail.value.car.carName}</td>
                            <td>
                                <img src="${orderDetail.value.car.image}" alt="${orderDetail.value.car.image}" width="360" height="200"/>
                            </td>
                            <td>${orderDetail.value.car.category}</td>
                            <td>
                                <input type="number" name="txtQuantity" value="${orderDetail.value.quantity}" min="1" max="100">
                            </td>
                            <td>
                                ${orderDetail.value.rentalDate}
                            </td>
                            <td>
                                ${orderDetail.value.returnDate}
                            </td>
                            <td>
                                ${orderDetail.value.price}
                                <c:set var="toltal" value="${orderDetail.value.price +toltal}"></c:set>
                            </td>
                            <td>
                                <input type="hidden" name="txtCarID" value="${orderDetail.value.car.carID}">
                                <input type="hidden" name="txtDetailID" value="${orderDetail.key}">
                                <input type="submit" value="Update">
                            </td>
                            <td>
                                <a style="text-decoration: none" href="RemoveController?txtDetailID=${orderDetail.key}" onclick="return confirm('Do you want to remove?')">Remove</a>
                            </td>
                            </form>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <h1>${requestScope.UPSUCCESS}</h1>
                <h1>TOTAL PRICE: ${toltal}</h1>
                <h1>Discount PRICE: <c:if test="${sessionScope.disPrive!=null}"><font>${sessionScope.disPrive/100*toltal}</font></c:if> <c:if test="${sessionScope.disPrive==null}">0</c:if> </h1>
                <h1>TOTAL PAYMENT: <c:if test="${sessionScope.disPrive!=null}"><font>${toltal-sessionScope.disPrive/100*toltal}</font></c:if> <c:if test="${sessionScope.disPrive==null}">${toltal}</c:if></h1>
                <form action="CheckDisController" method="POST">
                    <input type="hidden" name="txtTotal" value="${toltal}">
                    Discount code: <input type="text" name="txtcode" value="${param.txtcode}">
                    <input type="submit" value="Check"></br>
                    <font >${requestScope.disE}</font>
                </form>
                <form action="CheckOutController" method="POST">
                    <input type="hidden" name="txtTotal" value="${toltal}">
                    <input type="submit" value="RENTAL"></br>
                </form>
                    <font style="color: red">${requestScope.CHECKOUTE}</font>
            </c:if>
            <c:if test="${empty sessionScope.CART.hash}">
                Not Result
            </c:if>
        </c:if>
        <c:if test="${sessionScope.CART.hash==null}">
            Not Result
        </c:if>
    </body>
</html>
