package mg.recipe.study;

import org.springframework.data.jpa.repository.JpaRepository;

// JPA repository를 상속받는 인터페이스
public interface UserRepository extends JpaRepository<StudyUser, Long> {}