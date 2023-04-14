package mg.recipe.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Integer> {
    Optional<SiteUser> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<SiteUser> findByEmail(String email);

    @Query("SELECT EXTRACT(YEAR FROM su.createDate) as year, EXTRACT(MONTH FROM su.createDate) as month, COUNT(su) as count " +
            "FROM SiteUser su " +
            "WHERE su.createDate >= :startMonth AND su.createDate < :endMonth " +
            "GROUP BY year, month " +
            "ORDER BY year, month")
    List<Object[]> countMonthlyRegistrations(@Param("startMonth") LocalDateTime startMonth, @Param("endMonth") LocalDateTime endMonth);
}
