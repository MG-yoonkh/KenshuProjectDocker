package mg.recipe.admin;

import mg.recipe.admin.SiteVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Repository
public interface SiteVisitRepository extends JpaRepository<SiteVisit, Long> {
    @Query("SELECT EXTRACT(YEAR FROM sv.visitDateTime) as year, EXTRACT(MONTH FROM sv.visitDateTime) as month, COUNT(sv) as count " +
            "FROM SiteVisit sv " +
            "WHERE sv.visitDateTime >= :startMonth AND sv.visitDateTime < :endMonth " +
            "GROUP BY year, month " +
            "ORDER BY year, month")
    List<Object[]> countMonthlyVisitors(@Param("startMonth") LocalDateTime startMonth, @Param("endMonth") LocalDateTime endMonth);
}
