package quizzApp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import quizzApp.model.Category;
import quizzApp.model.DTO.QuizDTO;
import quizzApp.model.DTO.ResultDTO;
import quizzApp.model.Question;
import quizzApp.model.Quiz;
import quizzApp.repository.CategoryRepository;
import quizzApp.repository.QuestionRepository;
import quizzApp.repository.QuizRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizAppService {

	private final QuizRepository quizRepository;
	private final CategoryRepository categoryRepository;
	private final QuestionRepository questionRepository;

	public Quiz createQuiz(Quiz quiz, Category category, List<Question> questions) {
		Quiz createdQuiz = quizRepository.addQuiz(quiz, category);
		categoryRepository.addCategory(category);
		for (Question question : questions) {
			questionRepository.addQuestion(question, createdQuiz.getId());
		}
		List<Question> allQuestionsByQuizId = questionRepository.findAllQuestionsByQuizId(quiz.getId());
		createdQuiz.setQuestions(allQuestionsByQuizId);
		categoryRepository.addCategory(category);
		return quizRepository.addQuiz(createdQuiz, category);
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

	//todo this method is vert bad written, its need to be changed if time left.
	private List<Quiz> createQuizzesFromJson(MultipartFile json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<QuizDTO> quizzesFromJson = mapper.readValue(json.getInputStream(), new TypeReference<List<QuizDTO>>() {
		});
		for (QuizDTO quiz : quizzesFromJson) {
			Optional<Category> categoryById = categoryRepository.findCategoryById(quiz.getCategoryId());
			if (categoryById.isEmpty()) {
				Category category = new Category();
				category.setId(quiz.getCategoryId());
				category.setName(quiz.getCategoryName());
				categoryRepository.addCategory(category);
			}
			Category category = categoryRepository.findCategoryByName(quiz.getCategoryName()).orElse(null);
			Quiz newQuizFromDTO = new Quiz();
			newQuizFromDTO.setId(quiz.getId());
			newQuizFromDTO.setCategoryId(category.getId());
			newQuizFromDTO.setNumberOfQuestions((long) quiz.getQuestions().size());
			newQuizFromDTO.setQuestions(quiz.getQuestions());
			newQuizFromDTO.setName(quiz.getName());
			createQuiz(newQuizFromDTO, category, quiz.getQuestions());
		}
		return quizRepository.getQuizzes();
	}

	public ResultDTO evaluateSubmit(Map<String, String> answers) {
		ResultDTO resultDTO = new ResultDTO();
		Map<String, String> wrongAnswers = new HashMap<>();
		int counter = 0;
		for (Map.Entry<String, String> answer : answers.entrySet()) {
			Optional<Question> question = questionRepository.findQuestionById(Long.valueOf(answer.getKey()));
			Optional<Quiz> quiz = quizRepository.findQuizById(question.get().getQuizId());
			resultDTO.setQuizName(quiz.get().getName());
			resultDTO.setQuizId(quiz.get().getId());
			Boolean result = question.get().getAnswers().get(answer.getValue());
			if (result) {
				counter++;
			} else {
				for (Map.Entry<String, Boolean> answersFromQuestion : question.get().getAnswers().entrySet()) {
					if (answersFromQuestion.getValue().equals(true)) {
						String description = question.get().getDescription();
						String correctAnswer = answersFromQuestion.getKey();
						wrongAnswers.put(description, correctAnswer);
						break;
					}
				}
			}
		}
		resultDTO.setWrongAnswers(wrongAnswers);
		resultDTO.setNumberOfCorrectAnswers(String.valueOf(counter));
		if (counter == 5) {
			resultDTO.setResult("Success");
		} else {
			resultDTO.setResult("Failed: You have " + counter + " correct answers");
		}
		return resultDTO;
	}
}
