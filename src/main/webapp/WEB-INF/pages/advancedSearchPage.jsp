<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced search">
    <form method="get">
        <h2>Advanced search</h2>

        <p>
            Description <input name="description" value="${not empty param.description ? param.description : ""}">
            <span>
                    <select name="advancedSearchOptions">
                        <option name="allWords">all words</option>>
                        <option name="anyWord">any word</option>>
                     </select>
             </span>
        </p>
        <p>
            Min price <input name="minPrice" value="${not empty param.minPrice ? param.minPrice : ""}">
            <c:if test="${not empty errors['minPrice']}">
                <span class="error">${errors['minPrice']}</span>
            </c:if>
        </p>
        <p>
            Max price <input name="maxPrice" value="${not empty param.maxPrice ? param.maxPrice : ""}">
            <c:if test="${not empty errors['maxPrice']}">
                <span class="error">${errors['maxPrice']}</span>
            </c:if>
        </p>
        <button formmethod="get">Search</button>
    </form>
    <c:if test="${not empty products}">
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
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>
                        <img class="product-tile"
                             src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${product.imageUrl}">
                    </td>
                    <td>
                        <a href="products/${product.id}">${product.description}</a>
                    </td>
                    <td class="price">
                        <a href="products/priceHistory/${product.id}">
                            <fmt:formatNumber value="${product.price}" type="currency"
                                              currencySymbol="${product.currency.symbol}"/>
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</tags:master>
