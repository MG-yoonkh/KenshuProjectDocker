package mg.recipe;

//import mg.recipe.sbb.answer.Answer;
//import mg.recipe.sbb.answer.AnswerRepository;
//import mg.recipe.sbb.question.Question;
//import mg.recipe.sbb.question.QuestionRepository;
import mg.recipe.ingredient.Ingredient;
import mg.recipe.ingredient.IngredientRepository;
import mg.recipe.instruction.InstructionRepository;
import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeRepository;
//import mg.recipe.user.UserInfo;
//import mg.recipe.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RecipeApplicationTests {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private InstructionRepository instructionRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

//    @Test
//    void testJpa(){
//        Optional<Recipe> or = this.recipeRepository.findById(1);
//        assertTrue(or.isPresent());
//        Recipe r1 = or.get();
//
//        Instruction i = new Instruction();
//        i.setStepNum(1);
//        i.setDescription("감자씻기");
//        this.instructionRepository.save(i);
//    }

//    @Test
//    @Transactional
//    void testJpa(){
//        Optional<Recipe> oi = this.recipeRepository.findById(1);
//        assertTrue(oi.isPresent());
//        Recipe r1 = oi.get();
//
//        List<Ingredient> ingredientList = r1.getIngredientList();
//        assertEquals(1, ingredientList.size());
//        assertEquals("감자", ingredientList.get(0).getName());
//    }
//    @Autowired
//    private UserRepository userRepository;

    @Test
    void testJpa(){
        Recipe r1 = new Recipe();

        r1.setRecipeName("감자탕");
        r1.setCreateDate(LocalDateTime.now());
        r1.setCooktime(10);
        r1.setThumbnail("assets/img/food-pizza_640.jpg");
        r1.setCategory("한국");
        this.recipeRepository.save(r1);
    }
//    @Test
//    void testJpa(){
//        List<Recipe> all = this.recipeRepository.findAll();
//        assertEquals(1,all.size());
//        Recipe r1 = all.get(0);
//        assertEquals("감자탕", r1.getRecipeName());
//    }
//    @Test
//    void testJpa(){
//        Optional<Recipe> or = this.recipeRepository.findById(1);
//        if(or.isPresent()){
//            Recipe r1 = or.get();
//            assertEquals("감자탕", r1.getRecipeName());
//        }
//    }

//    @Test
//    void testJpa(){
//        Recipe r1 = this.recipeRepository.findByRecipeName("감자탕");
//        assertEquals(1, r1.getId());
//    }

//	@Test
//	void create() { // 컬럼생성
//		Question q1 = new Question();
//		q1.setSubject("recipe가 무엇인가요?");
//		q1.setContent("recipe에 대해 알고싶습니다.");
//		q1.setCreateDate(LocalDateTime.now());
//		this.questionRepository.save(q1); // 첫번째 질문 저장
//
//		Question q2 = new Question();
//		q2.setSubject("스프링부트 모델 질문입니다.");
//		q2.setContent("id는 자동으로 생성되나요?");
//		q2.setCreateDate(LocalDateTime.now());
//		this.questionRepository.save(q2); // 두번째 질문 저장
//	}


//    @Test
//    void testJpa(){
//    List<Recipe> rList = this.recipeRepository.findByRecipeNameLike("감%");
//    Recipe r1 = rList.get(0);
//    assertEquals("감자탕", r1.getRecipeName());
//    }

//    @Test
//    void testJpa(){
//        Optional<Recipe> oq = this.recipeRepository.findById(3);
//        assertTrue(oq.isPresent());
//        Recipe r1 = oq.get();
//        r1.setRecipeName("된장찌개");
//        this.recipeRepository.save(r1);
//    }

//    @Test
//    void testJpa(){
//        assertEquals(1, this.recipeRepository.count());
//        Optional<Recipe> oq = this.recipeRepository.findById(1);
//        assertTrue(oq.isPresent());
//        Recipe r1 = oq.get();
//        this.recipeRepository.delete(r1);
//        assertEquals(0, this.recipeRepository.count());
//    }


//	@Test
//	void select(){ // 전체데이터 조회
//		List<Question> all = this.questionRepository.findAll();
//		assertEquals(2, all.size());
//
//		Question q = all.get(0);
//		assertEquals("recipe가 무엇인가요?", q.getSubject());
//	}

//	@Test
//	void findById(){ // ID로 찾기
//		Optional<Question> oq = this.questionRepository.findById(1);
//		if (oq.isPresent()){
//			Question q = oq.get();
//			assertEquals("recipe가 무엇인가요", q.getSubject());
//		}
//	}



//	@Test
//	void findBySubject(){ //제목으로 찾기
//		Question q = this.questionRepository.findBySubject("recipe가 무엇인가요?");
//		assertEquals(7, q.getId());
//	}

//	@Test
//	void findBySubjectAndContent(){
//		Question q = this.questionRepository.findBySubjectAndContent("recipe가 무엇인가요?", "recipe에 대해 알고싶습니다.");
//		assertEquals(7, q.getId());
//	}

//	@Test
//	void findBySubjectLike(){
//		List<Question> qList = this.questionRepository.findBySubjectLike("recipe%");
//		Question q = qList.get(0);
//		assertEquals("recipe가 무엇인가요?", q.getSubject());
//	}

//	@Test
//	void modify(){
//		Optional<Question> oq = this.questionRepository.findById(8);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//		q.setSubject("수정된 제목");
//		this.questionRepository.save(q);
//	}


//	@Test
//	void delete(){
//		assertEquals(2,this.questionRepository.count());
//		Optional<Question> oq = this.questionRepository.findById(1);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//		this.questionRepository.delete(q);
//		assertEquals(1, this.questionRepository.count());
//	}


//	@Test
//	void createAnswer(){ // 답변 생성하고 저장
//		Optional<Question> oq = this.questionRepository.findById(2);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//
//		Answer a = new Answer();
//		a.setContent("네 자동으로 생성됩니다.");
//		a.setQuestion(q); // 어떤 질문의 답변인지 알기 위해서 Question 객체가 필요하다.
//		a.setCreateDate(LocalDateTime.now());
//		this.answerRepository.save(a);
//
//	}

//    @Transactional
//    @Test
//    void readAnswer(){
//        Optional<Question> oq = this.questionRepository.findById(2);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//
//        List<Answer> answerList = q.getAnswerList();
//
//        assertEquals(1, answerList.size());
//        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
//    }

}