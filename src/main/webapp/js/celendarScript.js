document.addEventListener("DOMContentLoaded", () => {
    const zoneInput = document.getElementById("zoneIdInput");
    if (zoneInput) {
        zoneInput.value = Intl.DateTimeFormat().resolvedOptions().timeZone;
    }

    const slotElements = document.querySelectorAll(".slot-item");
    const slots = [];

    slotElements.forEach(el => {
        const start = new Date(el.dataset.start);
        const end = new Date(el.dataset.end);
        if (isNaN(start) || isNaN(end)) return;

        // Красиво форматируем текст в списке "Мои активные слоты"
        const textSpan = el.querySelector(".slot-formatted-text");
        if (textSpan) {
            textSpan.textContent = `${formatDateTime(start)} — ${formatTime(end)}`;
        }

        slots.push({
            owner: el.dataset.owner,
            label: el.dataset.label,
            start,
            end
        });
    });

    renderTimeline(slots);
});

function renderTimeline(slots) {
    const root = document.getElementById("timeline-root");
    if (!root) return;

    if (slots.length === 0) {
        root.innerHTML = "<p>Нет активных слотов для отображения на графике.</p>";
        return;
    }

    const daysMap = new Map();

    slots.forEach(slot => {
        let cursor = new Date(slot.start);
        while (cursor < slot.end) {
            const dayKey = cursor.toLocaleDateString("ru-RU", {
                day: "2-digit", month: "2-digit", year: "numeric", weekday: "short"
            });

            const startOfDay = new Date(cursor.getFullYear(), cursor.getMonth(), cursor.getDate(), 0, 0, 0);
            const endOfDay = new Date(cursor.getFullYear(), cursor.getMonth(), cursor.getDate() + 1, 0, 0, 0);

            const segmentStart = cursor > startOfDay ? cursor : startOfDay;
            const segmentEnd = slot.end < endOfDay ? slot.end : endOfDay;

            if (!daysMap.has(dayKey)) {
                daysMap.set(dayKey, { dateObj: startOfDay, segments: [] });
            }

            daysMap.get(dayKey).segments.push({
                owner: slot.owner,
                label: slot.label,
                start: segmentStart,
                end: segmentEnd,
                dayStart: startOfDay
            });

            cursor = endOfDay;
        }
    });

    const sortedDays = Array.from(daysMap.entries()).sort((a, b) => a[1].dateObj - b[1].dateObj);

    root.innerHTML = "";
    sortedDays.forEach(([dayTitle, dayData]) => {
        const dayBlock = document.createElement("div");
        dayBlock.className = "timeline-day";

        const header = document.createElement("strong");
        header.textContent = dayTitle;
        dayBlock.appendChild(header);

        // Шкала часов 00:00 - 24:00
        const scale = document.createElement("div");
        scale.className = "hours-scale";
        scale.innerHTML = "<span>00:00</span><span>06:00</span><span>12:00</span><span>18:00</span><span>24:00</span>";
        dayBlock.appendChild(scale);

        const tracksMap = new Map();
        dayData.segments.forEach(seg => {
            const key = `${seg.owner}_${seg.label}`;
            if (!tracksMap.has(key)) tracksMap.set(key, []);
            tracksMap.get(key).push(seg);
        });

        tracksMap.forEach(playerSegments => {
            const track = document.createElement("div");
            track.className = "timeline-track";

            playerSegments.forEach(seg => {
                const minutesInDay = 24 * 60;
                const startMins = (seg.start - seg.dayStart) / 60000;
                const durationMins = (seg.end - seg.start) / 60000;

                const leftPercent = (startMins / minutesInDay) * 100;
                const widthPercent = (durationMins / minutesInDay) * 100;

                const bar = document.createElement("div");
                bar.className = `timeline-bar ${seg.owner === "me" ? "bar-me" : "bar-friend"}`;
                bar.style.left = `${leftPercent}%`;
                bar.style.width = `${Math.max(widthPercent, 2)}%`;

                const timeRangeText = `${formatTime(seg.start)}–${formatTime(seg.end)}`;
                bar.textContent = `${seg.label} (${timeRangeText})`;
                bar.title = `${seg.label}: ${timeRangeText}`;

                track.appendChild(bar);
            });

            dayBlock.appendChild(track);
        });

        root.appendChild(dayBlock);
    });
}

function formatDateTime(date) {
    return date.toLocaleString("ru-RU", {
        day: "2-digit", month: "2-digit", hour: "2-digit", minute: "2-digit"
    });
}

function formatTime(date) {
    return date.toLocaleTimeString("ru-RU", {
        hour: "2-digit", minute: "2-digit"
    });
}
