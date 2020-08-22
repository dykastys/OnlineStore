<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>main page</title>
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

    <nav>
        <a href="${pageContext.request.contextPath}/all_products?page=1">
            show all products
        </a>
    </nav>
</body>
</html>
