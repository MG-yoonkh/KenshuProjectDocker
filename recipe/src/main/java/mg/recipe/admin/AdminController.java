package mg.recipe.admin;

import lombok.RequiredArgsConstructor;
import mg.recipe.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final SiteVisitRepository siteVisitRepository;

    @GetMapping("/admin/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> data = new HashMap<>();

        YearMonth endMonth = YearMonth.now();
        YearMonth startMonth = endMonth.minusMonths(11);

        Map<YearMonth, Long> monthlyRegisteredUsers = userService.getMonthlyRegistrations(startMonth, endMonth);
        data.put("monthlyRegisteredUsers", monthlyRegisteredUsers);

        Map<YearMonth, Long> monthlySiteVisits = userService.getMonthlyVisitors(startMonth,endMonth);
        data.put("monthlySiteVisits", monthlySiteVisits);

        return ResponseEntity.ok(data);
    }
}
