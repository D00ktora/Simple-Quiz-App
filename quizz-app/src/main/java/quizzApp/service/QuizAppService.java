package quizzApp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import quizzApp.model.Category;
import quizzApp.model.DTO.QuizDTO;
import quizzApp.model.Question;
import quizzApp.model.Quiz;
import quizzApp.repository.CategoryRepository;
import quizzApp.repository.QuestionRepository;
import quizzApp.repository.QuizRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizAppService {

	private final QuizRepository quizRepository;
	private final CategoryRepository categoryRepository;
	private final QuestionRepository questionRepository;

	public Quiz createQuiz(Quiz quiz, Category category, List<Question> questions) {
		categoryRepository.addCategory(category);
		for (Question question : questions) {
			questionRepository.addQuestion(question);
		}
		return quizRepository.addQuiz(quiz);
	}

	public Quiz updateQuiz(Quiz quiz) {
		Quiz newQuiz = new Quiz();
		newQuiz.setId(quiz.getId());
		newQuiz.setName(quiz.getName());
		newQuiz.setCategoryId(quiz.getCategoryId());
		for (Question question : quiz.getQuestions()) {
			questionRepository.addQuestion(question);
		}
		Long size = (long) questionRepository.findAllQuestionsByQuizId(quiz.getId()).size();
		newQuiz.setNumberOfQuestions(size);
		List<Question> allQuestionsByQuizId = questionRepository.findAllQuestionsByQuizId(quiz.getId());
		newQuiz.setQuestions(allQuestionsByQuizId);
		return quizRepository.addQuiz(newQuiz);
	}

	public Quiz getQuizById(Long id) throws Exception {
		Optional<Quiz> quiz = quizRepository.findQuizById(id);
		if (quiz.isPresent()) {
			return quiz.get();
		}
		throw new Exception("Quiz with id " + id + " do not exist.");
	}

	public List<Quiz> getAllQuiz() {
		return quizRepository.findAllQuizzes();
	}

	public String deleteQuiz(Long id) {
		Optional<Quiz> quiz = quizRepository.findQuizById(id);
		if (quiz.isPresent()) {
			quizRepository.deleteQuizById(id);
			return "Quiz deleted successfully";
		}
		return "Quiz with id " + id + " do not exist.";
	}

	public List<Quiz> createQuizzesFromFile(MultipartFile file) throws Exception {
		String contentType = file.getContentType();
		String fileName = file.getOriginalFilename();

		if ((contentType.equals("text/csv") ||
			contentType.equals("application/csv") ||
			contentType.equals("application/vnd.ms-excel")) &&
			fileName.endsWith(".csv")) {
			//todo: create quizzes from CSV
		} else if (contentType.equals("application/json") && fileName.endsWith(".json")) {
			return createQuizzesFromJson(file);
		}
		throw new Exception("File format is not supported");
	}

	private List<Quiz> createQuizzesFromJson(MultipartFile json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<QuizDTO> quizzesFromJson = mapper.readValue(json.getInputStream(), new TypeReference<List<QuizDTO>>() {
		});
		for (QuizDTO quiz : quizzesFromJson) {
			Category category = new Category();
			category.setId(quiz.getCategoryId());
			category.setName(quiz.getName());
			List<Question> questions = quiz.getQuestions();
			for (Question question : questions) {
				questionRepository.addQuestion(question);
			}
			Quiz newQuiz = new Quiz();
			newQuiz.setId(quiz.getId());
			newQuiz.setName(quiz.getName());
			newQuiz.setCategoryId(category.getId());
			Quiz createdQuiz = createQuiz(newQuiz, category, questions);
			createdQuiz.setQuestions(questionRepository.findAllQuestionsByQuizId(newQuiz.getId()));
			createdQuiz.setNumberOfQuestions((long) questionRepository.findAllQuestionsByQuizId(newQuiz.getId()).size());
			quizRepository.addQuiz(createdQuiz);
		}
		return quizRepository.getQuizzes();
	}

}
