<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <p>
        Product Details
    </p>
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
        </tr>
        </thead>
        <tr>
            <td>
                <img class="product-tile"
                     src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${product.imageUrl}">
            </td>
            <td>
                <a href="products/${product.id}">${product.description}</a>
            </td>
            <td class="price">
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
        </tr>
    </table>
    <form method="post" action="${product.id}">
        <p>
            <input name="quantity" value="1" class="price">
            <button>Add to cart</button>
            <c:if test="${not empty error}">
        <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty param.success}">
            <p>Successfully added to cart</p>
        </c:if>
        </p>
    </form>
    <tags:recentlyViewedTag recentProducts="${recentProducts}"/>
</tags:master>
