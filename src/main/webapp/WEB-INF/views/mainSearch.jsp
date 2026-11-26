<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Steam Profile Lookup</title>
</head>
<body>

     <div>
            <c:choose>
                <c:when test="${not empty sessionScope.userId}">
                    <a href="<c:url value='/account'/>">
                        <button type="button">Мой профиль</button>
                    </a>
                    <form method="post" action="<c:url value='/logout'/>" style="display:inline;">
                        <button type="submit">Выйти</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value='/login'/>">
                        <button type="button">Login</button>
                    </a>
                </c:otherwise>
            </c:choose>
        </div>

    <hr>

    <c:if test="${not empty error}">
        <p><strong><c:out value="${error}"/></strong></p>
    </c:if>

    <form method="get" action="<c:url value='/'/>">
        <label for="steamId">Ссылка на аккаунт, SteamID или ник:</label><br>
        <input type="text"
               id="steamId"
               name="steamId"
               value="<c:out value='${param.steamId}'/>"
               required>
        <button type="submit">Найти</button>
    </form>

    <c:if test="${not empty steamProfile}">
        <hr>
        <h2>Информация о профиле</h2>

        <c:if test="${not empty steamProfile.avatarFullUrl}">
            <img src="<c:out value='${steamProfile.avatarFullUrl}'/>" alt="Avatar">
        </c:if>

        <ul>
            <li><strong>Никнейм:</strong> <c:out value="${steamProfile.personaName}"/></li>
            <li><strong>SteamID64:</strong> <c:out value="${steamProfile.steamId}"/></li>
            <li><strong>Профиль:</strong> <a href="<c:out value='${steamProfile.profileUrl}'/>" target="_blank"><c:out value="${steamProfile.profileUrl}"/></a></li>

            <c:if test="${not empty steamProfile.realName}">
                <li><strong>Настоящее имя:</strong> <c:out value="${steamProfile.realName}"/></li>
            </c:if>

            <c:if test="${not empty steamProfile.countryCode}">
                <li><strong>Страна:</strong> <c:out value="${steamProfile.countryCode}"/></li>
            </c:if>

            <li><strong>Видимость профиля:</strong> ${steamProfile.communityVisibilityState == 3 ? 'Публичный' : 'Приватный'}</li>
            <li>
                <strong>Количество друзей:</strong>
                <c:choose>
                    <c:when test="${steamProfile.friendIds == null}">Скрыто настройками приватности</c:when>
                    <c:otherwise>${steamProfile.friendIds.size()}</c:otherwise>
                </c:choose>
            </li>
        </ul>

        <c:if test="${not empty steamProfile.friendIds}">
            <h3>Список SteamID друзей:</h3>
            <ul>
                <c:forEach var="friendId" items="${steamProfile.friendIds}">
                    <li><c:out value="${friendId}"/></li>
                </c:forEach>
            </ul>
        </c:if>
    </c:if>

</body>
</html>
