package com.ireflect.controller;

import com.ireflect.document.SessionDocument;
import com.ireflect.domain.SessionStatus;
import com.ireflect.repository.SessionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CalendarController {

    private final SessionRepository sessionRepository;

    public CalendarController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/calendar/month")
    public ResponseEntity<?> getMonth(Authentication auth,
                                       @RequestParam int year,
                                       @RequestParam int month) {
        String userId = (String) auth.getPrincipal();
        YearMonth ym = YearMonth.of(year, month);

        // Get all sessions for this user in this month
        List<SessionDocument> allSessions = sessionRepository.findByUserIdOrderByCreatedAtDesc(userId);

        // Filter to sessions in the requested month
        List<SessionDocument> monthSessions = allSessions.stream()
            .filter(s -> s.getSessionDate() != null &&
                         s.getSessionDate().getYear() == year &&
                         s.getSessionDate().getMonthValue() == month &&
                         s.getStatus() == SessionStatus.COMPLETED)
            .toList();

        // Group by date
        Map<LocalDate, Long> dateCounts = monthSessions.stream()
            .collect(Collectors.groupingBy(SessionDocument::getSessionDate, Collectors.counting()));

        List<Map<String, Object>> days = new ArrayList<>();
        for (Map.Entry<LocalDate, Long> entry : dateCounts.entrySet()) {
            Map<String, Object> day = new LinkedHashMap<>();
            day.put("date", entry.getKey().toString());
            day.put("hasSession", true);
            day.put("sessionCount", entry.getValue());
            day.put("isAccessible", true);  // TODO: entitlement check in Phase 4
            day.put("isLocked", false);
            days.add(day);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("year", year);
        result.put("month", month);
        result.put("plan", "FREE"); // TODO: read from user
        result.put("days", days);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/calendar/day")
    public ResponseEntity<?> getDay(Authentication auth,
                                     @RequestParam String date) {
        String userId = (String) auth.getPrincipal();
        LocalDate day = LocalDate.parse(date);

        List<SessionDocument> daySessions = sessionRepository.findByUserIdAndSessionDate(userId, day);

        List<Map<String, Object>> entries = daySessions.stream()
            .filter(s -> s.getStatus() == SessionStatus.COMPLETED)
            .map(s -> {
                Map<String, Object> entry = new LinkedHashMap<>();
                entry.put("sessionId", s.getId());
                entry.put("category", s.getCategoryName());
                entry.put("summaryText", s.getSummaryText());
                entry.put("finalEmotion", s.getFinalEmotion() != null ? s.getFinalEmotion().name() : null);
                entry.put("themes", List.of()); // TODO: extract themes
                return entry;
            })
            .collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("date", date);
        result.put("isAccessible", true);
        result.put("entries", entries);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/streak")
    public ResponseEntity<?> getStreak(Authentication auth) {
        String userId = (String) auth.getPrincipal();

        List<SessionDocument> allSessions = sessionRepository.findByUserIdOrderByCreatedAtDesc(userId);

        // Only count completed sessions
        List<LocalDate> sessionDates = allSessions.stream()
            .filter(s -> s.getStatus() == SessionStatus.COMPLETED && s.getSessionDate() != null)
            .map(SessionDocument::getSessionDate)
            .distinct()
            .sorted(Comparator.reverseOrder())
            .toList();

        int currentStreak = 0;
        int longestStreak = 0;
        LocalDate today = LocalDate.now();

        // Calculate current streak
        LocalDate checkDate = today;
        for (LocalDate d : sessionDates) {
            if (d.equals(checkDate) || d.equals(checkDate.minusDays(1))) {
                currentStreak++;
                checkDate = d.minusDays(1);
            } else {
                break;
            }
        }

        // Calculate longest streak (simplified)
        if (!sessionDates.isEmpty()) {
            int tempStreak = 1;
            longestStreak = 1;
            for (int i = 1; i < sessionDates.size(); i++) {
                if (sessionDates.get(i).equals(sessionDates.get(i - 1).minusDays(1))) {
                    tempStreak++;
                    longestStreak = Math.max(longestStreak, tempStreak);
                } else {
                    tempStreak = 1;
                }
            }
        }

        // This week and month counts
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        long thisWeek = sessionDates.stream().filter(d -> !d.isBefore(weekStart)).count();
        LocalDate monthStart = today.withDayOfMonth(1);
        long thisMonth = sessionDates.stream().filter(d -> !d.isBefore(monthStart)).count();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("currentStreak", currentStreak);
        result.put("longestStreak", longestStreak);
        result.put("reflectionsThisWeek", thisWeek);
        result.put("reflectionsThisMonth", thisMonth);

        return ResponseEntity.ok(result);
    }
}
