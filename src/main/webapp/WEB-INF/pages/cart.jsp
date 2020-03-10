<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <div>${param.message}</div>
    <form method="post" action="cart">
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
                <td>
                    Action
                </td>
            </tr>
            </thead>
            <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="status">

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
                        <input name="quantity" value="${not empty paramValues.quantity[status.index] ? paramValues.quantity[status.index] : cartItem.quantity}" class="price">
                        <input type="hidden" name="productId" value="${cartItem.product.id}">
                        <br>
                        <div class="error">${errors[cartItem.product.id]}</div>
                    </td>
                    <td>
                        <button formaction="cart/deleteCartItem/${cartItem.product.id}">Delete</button>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <button>Update</button>
    </form>
</tags:master>
