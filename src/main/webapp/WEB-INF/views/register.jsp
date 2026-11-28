<%--
  Created by IntelliJ IDEA.
  User: kamil
  Date: 28.11.2026
  Time: 7:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация — Steam Calendar</title>
</head>
<body>
    <div>
        <a href="<c:url value='/'/>"><button type="button">На главную</button></a>
    </div>

    <hr>

    <h2>Регистрация нового аккаунта</h2>

    <c:if test="${not empty registerStatus}">
        <p style="color: red;"><strong>
            <c:choose>
                <c:when test="${registerStatus == 'WRONG_ID'}">
                    Некорректный SteamID64
                </c:when>
                <c:when test="${registerStatus == 'STEAM_NOT_FOUND'}">
                    Профиль не найден.
                </c:when>
                <c:when test="${registerStatus == 'WEAK_PASSWORD'}">
                    Пароль введите минимум 8 символов.
                </c:when>
                <c:when test="${registerStatus == 'EXISTS'}">
                    Аккаунт уже зарегистрирован.
                </c:when>
                <c:otherwise>
                    Ошибка сервера при сохранении аккаунта.
                </c:otherwise>
            </c:choose>
        </strong></p>
    </c:if>

    <form method="post" action="<c:url value='/register'/>">
        <div>
            <label>SteamID64:<br>
                <input type="text" name="userId" pattern="\d{17}" title="Введите 17 цифр вашего SteamID64" placeholder="7656119..." required>
            </label>
        </div>
        <div>
            <label>Имя пользователя (опционально):<br>
                <input type="text" name="username" maxlength="50">
            </label>
        </div>
        <div>
            <label>Пароль (минимум 8 символов)<br>
                <input type="password" name="password" minlength="8" required>
            </label>
        </div>
        <button type="submit">Зарегистрироваться</button>
    </form>

    <p>Уже есть аккаунт? <a href="<c:url value='/login'/>">Войти</a></p>
</body>
</html>
