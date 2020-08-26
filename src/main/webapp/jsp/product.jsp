<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>one_product</title>
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
        <span style="color: darkslategray">
            <h2>${requestScope.prod.name}</h2>
        </span>
    </div>
    <br/>
    <div>
        <b>
            Maker:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ${requestScope.prod.maker} <br/>
            Price:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ${requestScope.prod.price / 100.0} <br/>
            Quantity:&nbsp;&nbsp; ${requestScope.prod.quantity} <br/>
        </b>
    </div>
    <br/>
    <footer>
        <a href="${pageContext.request.contextPath}/all_products?page=1">back</a> <br/>
        <a href="${pageContext.request.contextPath}/">main page</a>
    </footer>
</body>
</html>
