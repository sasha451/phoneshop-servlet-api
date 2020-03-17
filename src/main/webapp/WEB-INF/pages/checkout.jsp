<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <c:if test="${not empty order.cartItems}">
    <div>${param.message}</div>
    <form method="post" action="order">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="price">
                    Price
                </td>
                <td>
                    Quantity
                </td>
            </tr>
            </thead>
            <c:forEach var="cartItem" items="${order.cartItems}" varStatus="status">

                <tr>
                    <td>
                        <img class="product-tile"
                             src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${cartItem.product.imageUrl}">
                    </td>
                    <td>
                        <a href="products/${cartItem.product.id}">${cartItem.product.description}</a>
                    </td>
                    <td class="price">
                        <a href="products/priceHistory/${cartItem.product.id}">
                            <fmt:formatNumber value="${cartItem.product.price}" type="currency"
                                              currencySymbol="${cartItem.product.currency.symbol}"/>
                        </a>
                    </td>
                    <td>
                            ${cartItem.quantity}
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
            </tr>
            <tr>
                <td>
                    Subtotal price:
                </td>
                <td>
                        ${order.totalPrice}
                </td>
            </tr>
            <tr>
                <td>
                    Delivery costs:
                </td>
                <td>
                        ${order.deliveryCosts}
                </td>
            </tr>
            <tr>
                <td>
                    Total price:
                </td>
                <td>
                        ${order.totalPrice + order.deliveryCosts}
                </td>
            </tr>
        </table>
        <br>
        <p>
            First name: <input name="firstName" value="${not empty param.firstName ? param.firstName : ""}">
            <c:if test="${not empty errors['firstName']}">
                  <div class="error">${errors['firstName']}</div>
            </c:if>
        </p>
        <p>
            Last name: <input name="lastName" value="${not empty param.lastName ? param.lastName : ""}">
            <c:if test="${not empty errors['lastName']}">
                <div class="error">${errors['lastName']}</div>
            </c:if>
        </p>
        <p>
            Phone number: <input name="phoneNumber" value="${not empty param.phoneNumber ? param.phoneNumber : ""}">
            <c:if test="${not empty errors['phoneNumber']}">
                <div class="error">${errors['phoneNumber']}</div>
            </c:if>
        </p>
        <p>
            Delivery date: <input name="deliveryDate" value="${not empty param.deliveryDate ? param.deliveryDate : ""}">
            <c:if test="${not empty errors['deliveryDate']}">
                <div class="error">${errors['deliveryDate']}</div>
            </c:if>
        </p>
        <p>
            Delivery address: <input name="deliveryAddress" value="${not empty param.deliveryAddress ? param.deliveryAddress : ""}">
            <c:if test="${not empty errors['deliveryAddress']}">
                <div class="error">${errors['deliveryAddress']}</div>
            </c:if>
        </p>
        <p>
            Payment method:
            <select name="paymentMethod">
                <option name="cash">CASH</option>>
                <option name="creditCard">CREDIT_CARD</option>>
            </select>
            <c:if test="${not empty errors['paymentMethod']}">
                <div class="error">${errors['paymentMethod']}</div>
            </c:if>
        </p>
        <button formaction="checkout">Place order</button>
        <c:if test="${not empty errors['error']}">
            <div class="error">${errors['error']}</div>
        </c:if>
    </form>
    </c:if>
    <c:if test="${empty order.cartItems}">
            <h3>Cart is empty</h3>
    </c:if>
</tags:master>
