<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" scope="request" class="com.es.phoneshop.model.cart.Cart"/>

<a href="${pageContext.request.contextPath}/cart">Cart: ${cart.totalPrice}</a>
