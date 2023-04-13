package mg.recipe.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

@Repository
public interface SiteVisitRepository extends JpaRepository<SiteVisit, Long> {
    @Query("SELECT EXTRACT(YEAR FROM sv.visitDateTime) as year, EXTRACT(MONTH FROM sv.visitDateTime) as month, COUNT(sv) as count " +
            "FROM SiteVisit sv " +
            "WHERE sv.visitDateTime >= :startMonth AND sv.visitDateTime < :endMonth " +
            "GROUP BY year, month " +
            "ORDER BY year, month")
    List<Object[]> countMonthlyVisitors(YearMonth startMonth, YearMonth endMonth);
}
