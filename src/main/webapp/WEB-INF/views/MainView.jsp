<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Авторизация</title>
</head>
<body>

    <h2>Вход в систему</h2>
    <c:if test="${not empty authStatus}">
        <c:choose>
            <c:when test="${authStatus == 0}">
                <p style="color: green;">${message}</p>
            </c:when>
            <c:otherwise>
                <p style="color: red;">Ошибка (код ${authStatus}): ${message}</p>
            </c:otherwise>
        </c:choose>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/test">
        <div>
            <label for="username">Логин:</label><br>
            <input type="text" id="username" name="username" required>
        </div>
        <br>
        <div>
            <label for="password">Пароль:</label><br>
            <input type="password" id="password" name="password" required>
        </div>
        <br>
        <button type="submit">Войти</button>
    </form>

</body>
</html>
