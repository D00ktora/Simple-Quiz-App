package quizzApp.repository;

import lombok.Data;
import org.springframework.stereotype.Service;
import quizzApp.model.Category;
import quizzApp.model.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class QuizRepository {
	private List<Quiz> quizzes = new ArrayList<>();
	private Long idCounter = 1L;

	public Quiz addQuiz(Quiz quiz, Category category) {

		for (Quiz quizFromRepo : quizzes) {
			if (quizFromRepo.getId().equals(quiz.getId())) {
				return updateQuiz(quiz, category, quizFromRepo);
			} else if (quizFromRepo.getName().equals(quiz.getName())) {
				return updateQuiz(quiz, category, quizFromRepo);
			}
		}

		if (quiz.getId() == null) {
			quiz.setId(idCounter);
			quizzes.add(quiz);
			idCounter++;
		}
		return quiz;
	}

	private Quiz updateQuiz(Quiz quiz, Category category, Quiz quizFromRepo) {
		Quiz updatedQuiz = new Quiz();
		updatedQuiz.setId(quizFromRepo.getId());
		updatedQuiz.setName(quizFromRepo.getName());
		updatedQuiz.setCategoryId(category.getId());
		updatedQuiz.setNumberOfQuestions(quizFromRepo.getNumberOfQuestions());
		updatedQuiz.setQuestions(quiz.getQuestions());
		quizzes.remove(quizFromRepo);
		quizzes.add(updatedQuiz);
		return updatedQuiz;
	}

	public Optional<Quiz> findQuizById(Long id) {
		for (Quiz quiz : quizzes) {
			if (quiz.getId().equals(id)) {
				return Optional.of(quiz);
			}
		}
		return Optional.empty();
	}

	public List<Quiz> findAllQuizzes() {
		return quizzes;
	}

	public Optional<Quiz> findQuizByName(String name) {
		for (Quiz quiz : quizzes) {
			if (quiz.getName().equals(name)) {
				return Optional.of(quiz);
			}
		}
		return Optional.empty();
	}

	public Optional<Quiz> findQuizByCategoryId(Long id) {
		for (Quiz quiz : quizzes) {
			if (quiz.getCategoryId().equals(id)) {
				return Optional.of(quiz);
			}
		}
		return Optional.empty();
	}

	public void deleteQuizById(Long id) {
		quizzes.removeIf(quiz -> quiz.getId().equals(id));
	}
}
