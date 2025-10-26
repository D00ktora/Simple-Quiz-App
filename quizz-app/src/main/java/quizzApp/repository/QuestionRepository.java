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

	public Question addQuestion(Question question) {

		for (Question questionFromRepo : questions) {
			if (questionFromRepo.getId().equals(question.getId())) {
				questions.remove(questionFromRepo);
				questions.add(question);
				return question;
			} else if (questionFromRepo.getDescription().equals(question.getDescription())) {
				question.setId(questionFromRepo.getId());
				questions.remove(questionFromRepo);
				questions.add(question);
			}
		}

		if (question.getId() == null) {
			question.setId(questionIdCounter);
			questions.add(question);
			questionIdCounter++;
		}
		return question;
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
