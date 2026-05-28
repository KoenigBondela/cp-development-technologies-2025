(function () {
    function parseTime(value) {
        if (!value) return null;
        const parts = value.split(':');
        if (parts.length < 2) return null;
        const h = parseInt(parts[0], 10);
        const m = parseInt(parts[1], 10);
        if (isNaN(h) || isNaN(m)) return null;
        return h * 60 + m;
    }

    function formatHours(minutes) {
        const hours = (minutes / 60).toFixed(2);
        return hours.replace('.', ',') + ' ч';
    }

    function calcFlightMinutes(takeoff, landing) {
        const t1 = parseTime(takeoff);
        const t2 = parseTime(landing);
        if (t1 === null || t2 === null) return null;
        if (t2 >= t1) return t2 - t1;
        return (24 * 60 - t1) + t2;
    }

    function updateHoursPreview() {
        const takeoff = document.getElementById('takeoffTime');
        const landing = document.getElementById('landingTime');
        const preview = document.getElementById('hoursPreview');
        if (!takeoff || !landing || !preview) return;
        const minutes = calcFlightMinutes(takeoff.value, landing.value);
        if (minutes === null || minutes <= 0) {
            preview.textContent = '—';
            preview.classList.add('text-muted');
            return;
        }
        preview.textContent = formatHours(minutes);
        preview.classList.remove('text-muted');
    }

    document.addEventListener('DOMContentLoaded', function () {
        const takeoff = document.getElementById('takeoffTime');
        const landing = document.getElementById('landingTime');
        if (takeoff) takeoff.addEventListener('change', updateHoursPreview);
        if (takeoff) takeoff.addEventListener('input', updateHoursPreview);
        if (landing) landing.addEventListener('change', updateHoursPreview);
        if (landing) landing.addEventListener('input', updateHoursPreview);
        updateHoursPreview();
    });
})();
