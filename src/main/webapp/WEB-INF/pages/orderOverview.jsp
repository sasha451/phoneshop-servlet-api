<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Overview">
        <div>${param.message}</div>
        <form method="get" action="products">
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
                First name: ${order.firstName}
            </p>
            <p>
                Last name: ${order.lastName}
            </p>
            <p>
                Delivery date: ${order.deliveryDate}
            </p>
            <p>
                Delivery address: ${order.deliveryAddress}
            </p>
            <p>
                Payment method: ${order.paymentMethod}
            </p>
            <button formaction="/phoneshop-servlet-api/products">Continue shopping</button>
        </form>
</tags:master>
