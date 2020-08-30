<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <style>
        .container {
            display: flex;
            justify-content: space-between;
            background-color: transparent;
            padding: 2em;
            box-sizing: border-box;
        }
        .container div {
            background-color: transparent;
        }
    </style>
<head>
    <title>main page</title>
</head>
<body>
    <br/>
    <header>
        <div class="container">
            <div>
                <h1>
                    <span style="font-style: italic; color: blue">
                        Online Store
                    </span>
                </h1>
            </div>
            <div>
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <span style="color: coral">
                            <h3>Unnamed user</h3>
                        </span>
                        <a href="${pageContext.request.contextPath}/login">enter the site</a>
                    </c:when>
                    <c:otherwise>
                        <h3>
                            <a href="${pageContext.request.contextPath}/user?name=${sessionScope.user.login}">${sessionScope.user.login}</a>
                        </h3>
                        <form action="${pageContext.request.contextPath}/logout">
                            <label>
                                <input type="submit" value="logout">
                            </label>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
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
