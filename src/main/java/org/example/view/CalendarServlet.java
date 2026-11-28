package org.example.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.domain.CalendarService;
import org.example.domain.models.CalendarCodes;
import org.example.domain.models.TimeSlotDto;

import java.io.IOException;
import java.util.List;

public class CalendarServlet extends HttpServlet {

    private CalendarService calendarService;

    @Override
    public void init() {
        this.calendarService = (CalendarService) getServletContext().getAttribute("calendarService");
    }

    @Override


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = (String) req.getSession().getAttribute("userId");

        List<TimeSlotDto> mySlots = calendarService.getUserSlots(userId);
        List<TimeSlotDto> friendSlots = calendarService.getFriendsSlots(userId);

        req.setAttribute("mySlots", mySlots);
        req.setAttribute("friendSlots", friendSlots);

        // Если после POST-редиректа передан код ошибки — переводим его в сообщение
        String errorCode = req.getParameter("error");
        if (errorCode != null) {
            req.setAttribute("errorMessage", mapErrorCode(errorCode));
        }

        req.getRequestDispatcher("/WEB-INF/views/calendar.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = (String) req.getSession().getAttribute("userId");
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            handleDelete(req, resp, userId);
        } else {
            handleAdd(req, resp, userId);
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp, String userId) throws IOException {
        String startStr = req.getParameter("start");
        String endStr = req.getParameter("end");
        String zoneId = req.getParameter("zoneId");

        CalendarCodes result = calendarService.addSlot(userId, startStr, endStr, zoneId);
        if (result == CalendarCodes.SUCCESS) {
            resp.sendRedirect(req.getContextPath() + "/calendar");
        } else {
            resp.sendRedirect(req.getContextPath() + "/calendar?error=" + result.name());
        }
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp, String userId) throws IOException {
        String slotIdRaw = req.getParameter("slotId");
        try {
            int slotId = Integer.parseInt(slotIdRaw);
            CalendarCodes result = calendarService.deleteSlot(slotId, userId);
            if (result == CalendarCodes.SUCCESS) {
                resp.sendRedirect(req.getContextPath() + "/calendar");
            } else {
                resp.sendRedirect(req.getContextPath() + "/calendar?error=" + result.name());
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/calendar?error=INVALID_ID");
        }
    }

    private String mapErrorCode(String code) {
        return switch (code) {
            case "INVALID_TIME_RANGE" -> "Время окончания должно быть позже начала и не в прошлом.";
            case "PARSE_ERROR" -> "Некорректный формат даты или времени.";
            case "SLOT_NOT_FOUND" -> "Слот не найден или уже удален.";
            default -> "Произошла ошибка при сохранении расписания.";
        };
    }
}
