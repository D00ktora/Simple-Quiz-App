package quizzApp.repository;

import lombok.Data;
import org.springframework.stereotype.Service;
import quizzApp.model.Question;

import java.util.List;
import java.util.Optional;

@Data
@Service
public class QuestionRepository {
	private List<Question> questions;
	private Long questionIdCounter = 1L;

	public Question addQuestion(Question question) {
		if (question.getId() == null) {
			question.setId(questionIdCounter);
			questions.add(question);
			questionIdCounter++;
		}
		//todo: Check -> this null is not ok, on late state I will see what can be return instead of null
		return null;
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
