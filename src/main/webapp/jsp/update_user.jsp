<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <style>
        .container {
            display: flex;
            justify-content: flex-start;
            background-color: transparent;
            width: 450px;
            padding: 2em;
            box-sizing: content-box;
        }
        .container div {
            background-color: transparent;
        }
    </style>
<head>
    <title>${sessionScope.user.login}</title>
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

    <form action="${pageContext.request.contextPath}/user" method="post">
        <label>
            <div class="container">
                <div>
                    <b>
                        <span style="white-space: nowrap">Change login:</span>
                        <span style="white-space: nowrap">Old password:</span>
                        <span style="white-space: nowrap">New password:</span>
                        <span style="white-space: nowrap">Repeat new Password:&nbsp;&nbsp;&nbsp;</span>
                    </b>
                </div>
                <div>
                    <input type="text" name="login" value="enter new login" size="30" maxlength="30">
                    <input type="text" name="oldPassword" value="enter old password" size="30" maxlength="50">
                    <input type="text" name="newPassword1" value="enter new password" size="30" maxlength="50">
                    <input type="text" name="newPassword2" value="repeat new password" size="30" maxlength="50">
                </div>
                <br/>
                <input type="submit" value="apply">
            </div>
        </label>
    </form>

    <c:if test="${not empty requestScope.success}">
        <c:forEach var="success" items="${requestScope.success}">
            <span style="padding: 2em; color: forestgreen; font-style: italic">
                    ${success}<br/>
            </span>
        </c:forEach>
    </c:if>
    <c:if test="${not empty requestScope.error}">
        <c:forEach var="error" items="${requestScope.error}">
            <span style="padding: 2em; color: red; font-style: italic">
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
    </span>
</body>
</html>
