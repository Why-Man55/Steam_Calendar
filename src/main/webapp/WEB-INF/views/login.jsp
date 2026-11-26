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
    <title>Вход и регистрация</title>
</head>
<body>
    <a href="<c:url value='/'/>">← На главную (поиск)</a>
    <hr>

    <h2>Вход в аккаунт</h2>
    <c:if test="${not empty loginError}">
        <p style="color: red;"><strong><c:out value="${loginError}"/></strong></p>
    </c:if>
    <form method="post" action="<c:url value='/login'/>">
        <label>SteamID64 или ссылка на профиль:</label><br>
        <input type="text" name="userId" required><br><br>

        <label>Пароль:</label><br>
        <input type="password" name="password" required><br><br>

        <button type="submit">Войти</button>
    </form>

    <hr>

    <h2>Регистрация по SteamID</h2>
    <c:if test="${not empty registerStatus}">
        <p style="color: red;"><strong>Ошибка регистрации: <c:out value="${registerStatus}"/></strong></p>
    </c:if>
    <form method="post" action="<c:url value='/register'/>">
        <label>SteamID64 или ссылка на профиль:</label><br>
        <input type="text" name="userId" required><br><br>

        <label>Отображаемое имя (внутри приложения):</label><br>
        <input type="text" name="username" required><br><br>

        <label>Пароль:</label><br>
        <input type="password" name="password" required><br><br>

        <button type="submit">Зарегистрироваться</button>
    </form>
</body>
</html>
