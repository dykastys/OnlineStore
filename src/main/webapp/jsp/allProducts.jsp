<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>all_products</title>
</head>
<body>
    <br/>
    <header>
        <h1>
            <span style="font-style: italic; color: blue">
                <pre>           Online Store</pre>
            </span>
        </h1>
    </header>

    <hr>
    <br/>

    <div>
        <h3>
        <span style="color: cadetblue">
            All products:
        </span>
        </h3>
    </div>

    <br/>

    <c:choose>
        <c:when test="${empty requestScope.products}">
            <span style="color: red">
                no available products
            </span>
        </c:when>
        <c:otherwise>
            <c:forEach var="product" items="${requestScope.products}">
                <a href="${pageContext.request.contextPath}/product?id=${product.id}">${product.name} - ${product.maker}</a>
                <br/>
            </c:forEach>

            <br/>

            <c:forEach var="page" items="${requestScope.pages}">
                <a href="${pageContext.request.contextPath}/all_products?page=${page}">${page} </a>
            </c:forEach>
        </c:otherwise>
    </c:choose>

    <br/>
    <br/>
    <br/>

    <footer>
        <a href="${pageContext.request.contextPath}/">main page</a>
    </footer>
</body>
</html>
