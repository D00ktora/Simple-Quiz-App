package quizzApp.repository;

import lombok.Data;
import org.springframework.stereotype.Service;
import quizzApp.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class QuestionRepository {
	private List<Question> questions = new ArrayList<>();
	private Long questionIdCounter = 1L;

	public Question addQuestion(Question question, Long quizId) {

		for (Question questionFromRepo : questions) {
			if (questionFromRepo.getId().equals(question.getId())) {
				return updateQuestion(question, questionFromRepo);
			} else if (questionFromRepo.getDescription().equals(question.getDescription())) {
				return updateQuestion(question, questionFromRepo);
			}
		}

		if (question.getId() == null) {
			return createQuestion(question, quizId);
		}
		return questions.stream().filter(question1 -> question1.getId().equals(questionIdCounter - 1)).findFirst().get();
	}

	private Question createQuestion(Question question, Long quizId) {
		Question newQuestion = new Question();
		newQuestion.setId(questionIdCounter);
		questionIdCounter++;
		newQuestion.setDescription(question.getDescription());
		newQuestion.setQuizId(quizId);
		newQuestion.setAnswers(question.getAnswers());
		questions.add(newQuestion);
		return newQuestion;
	}

	private Question updateQuestion(Question question, Question questionFromRepo) {
		Question updatedQuestion = new Question();
		updatedQuestion.setId(questionFromRepo.getId());
		updatedQuestion.setQuizId(questionFromRepo.getQuizId());
		updatedQuestion.setDescription(questionFromRepo.getDescription());
		updatedQuestion.setAnswers(question.getAnswers());
		questions.remove(questionFromRepo);
		questions.add(updatedQuestion);
		return updatedQuestion;
	}

	public Optional<Question> findQuestionById(Long id) {
		for (Question question : questions) {
			if (question.getId().equals(id)) {
				return Optional.of(question);
			}
		}
		return Optional.empty();
	}

	public List<Question> findAllQuestionsByQuizId(Long quizId) {
		return questions.stream().filter(question -> question.getQuizId().equals(quizId)).toList();
	}

	public Optional<Question> findQuestionByDescription(String description) {
		for (Question question : questions) {
			if (question.getDescription().equals(description)) {
				return Optional.of(question);
			}
		}
		return Optional.empty();
	}

	public void deleteQuestionById(Long id) {
		questions.removeIf(question -> question.getId().equals(id));
	}
}
