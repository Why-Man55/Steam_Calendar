<%--
  Created by IntelliJ IDEA.
  User: kamil
  Date: 19.11.2026
  Time: 12:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход — Steam Calendar</title>
</head>
<body>
    <div>
        <a href="<c:url value='/'/>"><button type="button">На главную</button></a>
    </div>

    <hr>

    <h2>Вход в аккаунт</h2>

    <c:if test="${param.registered == 'true'}">
        <p style="color: green;"><strong>Регистрация прошла успешно! Теперь вы можете войти.</strong></p>
    </c:if>

    <c:if test="${not empty loginError}">
        <p style="color: red;"><strong><c:out value="${loginError}"/></strong></p>
    </c:if>

    <form method="post" action="<c:url value='/login'/>">
        <div style="margin-bottom: 10px;">
            <label>SteamID64:<br>
                <input type="text" name="userId" placeholder="7656119..." required>
            </label>
        </div>
        <div style="margin-bottom: 10px;">
            <label>Пароль:<br>
                <input type="password" name="password" required>
            </label>
        </div>
        <button type="submit">Войти</button>
    </form>

    <p>Ещё нет аккаунта? <a href="<c:url value='/register'/>">Зарегистрироваться</a></p>
</body>
</html>
