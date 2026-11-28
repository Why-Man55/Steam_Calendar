<%--
  Created by IntelliJ IDEA.
  User: kamil
  Date: 27.11.2026
  Time: 2:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Календарь игровых сессий</title>
</head>
<body>

<div>
    <a href="<c:url value='/account'/>"><button type="button">Мой профиль</button></a>
    <a href="<c:url value='/'/>"><button type="button">Поиск игроков</button></a>
</div>

<hr>

<h2>Добавить свободное время</h2>

<c:if test="${not empty errorMessage}">
    <p><strong><c:out value="${errorMessage}"/></strong></p>
</c:if>

<form method="post" action="<c:url value='/calendar'/>">
    <input type="hidden" name="action" value="add">

    <label>Начало:
        <input type="datetime-local" name="start" required>
    </label>
    <label>Конец:
        <input type="datetime-local" name="end" required>
    </label>
    <button type="submit">Добавить окно</button>
</form>

<hr>

<h3>Мои активные слоты</h3>
<c:choose>
    <c:when test="${empty mySlots}">
        <p>У вас пока нет запланированных окон.</p>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach var="slot" items="${mySlots}">
                <li>
                    <c:out value="${slot.start}"/> — <c:out value="${slot.end}"/>
                    <form method="post" action="<c:url value='/calendar'/>" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="slotId" value="${slot.id}">
                        <button type="submit">Удалить</button>
                    </form>
                </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<hr>

<h3>Свободное время друзей</h3>
<c:choose>
    <c:when test="${empty friendSlots}">
        <p>У ваших друзей пока нет запланированных окон (или вы еще не добавили друзей в профиле).</p>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach var="fSlot" items="${friendSlots}">
                <li>
                    <strong><c:out value="${fSlot.accountId}"/>:</strong>
                    <c:out value="${fSlot.start}"/> — <c:out value="${fSlot.end}"/>
                </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

</body>
</html>