<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>--%>
<tags:master pageTitle="Advanced search">
    <form>
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
        </p>
        <p>
            Max price <input name="maxPrice" value="${not empty param.maxPrice ? param.maxPrice : ""}">
        </p>
        <button formaction="">Search</button>
    </form>
</tags:master>
