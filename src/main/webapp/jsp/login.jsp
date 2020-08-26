<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <style>
        .container {
            display: flex;
            justify-content: flex-start;
            background-color: transparent;
            width: 150px;
            padding: 2em;
            box-sizing: border-box;
        }
        .container div {
            background-color: transparent;
        }
    </style>
<head>
    <title>login</title>
</head>
<body>
    <br/>
    <header>
        <div class="container" style="white-space: nowrap">
            <div>
                <h1>
                    <span style="font-style: italic; color: blue">
                        Online Store
                    </span>
                </h1>
            </div>
            <div></div>
        </div>
    </header>

    <hr>
    <br/>

    <span style="color: coral; padding: 2em; font-weight: bold">
        Authorization
    </span>

    <br/>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <label>
            <div class="container">
                <div>
                    <b>
                        Login:
                        Password:&nbsp;&nbsp;&nbsp;
                    </b>
                </div>
                <div>
                    <input type="text" name="login" value="enter the login" size="30" maxlength="30">
                    <input type="text" name="password" value="enter the password" size="30" maxlength="50">
                </div>
                <br/>
                <input type="submit" value="check">
            </div>
        </label>
    </form>

    <c:if test="${not empty requestScope.error}">
        <c:forEach var="error" items="${requestScope.error}">
            <span style="color: red; padding: 2em">
                    ${error}<br/>
            </span>
        </c:forEach>
    </c:if>

    <br/>
    <br/>

    <span style="padding: 2em">
        <a href="${pageContext.request.contextPath}/">
            <b>main page</b>
        </a>

        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

        <a href="${pageContext.request.contextPath}/create_account">
            <b>create a new account</b>
        </a>
    </span>
</body>
</html>
