<%--
  Created by IntelliJ IDEA.
  User: kamil
  Date: 21.11.2026
  Time: 12:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мой профиль</title>
</head>
<body>
    <div>
        <a href="<c:url value='/'/>"><button type="button">Поиск игроков</button></a>
        <a href="<c:url value='/calendar'/>"><button type="button">Перейти к календарю</button></a>
        <form method="post" action="<c:url value='/logout'/>" style="display:inline;">
            <button type="submit">Выйти</button>
        </form>
    </div>

    <hr>

    <c:if test="${not empty error}">
        <p><strong><c:out value="${error}"/></strong></p>
    </c:if>

    <c:if test="${not empty steamProfile}">
        <h2>Мой профиль Steam</h2>
        <c:if test="${not empty steamProfile.avatarFullUrl}">
            <img src="<c:out value='${steamProfile.avatarFullUrl}'/>" alt="Avatar">
        </c:if>
        <ul>
            <li><strong>Никнейм:</strong> <c:out value="${steamProfile.personaName}"/></li>
            <li><strong>SteamID64:</strong> <c:out value="${steamProfile.steamId}"/></li>
            <li><strong>Статус:</strong> <c:out value="${steamProfile.statusText}"/></li>
            <li><strong>Профиль Steam:</strong> <a href="<c:out value='${steamProfile.profileUrl}'/>" target="_blank">Открыть</a></li>
        </ul>
    </c:if>
</body>
</html>
