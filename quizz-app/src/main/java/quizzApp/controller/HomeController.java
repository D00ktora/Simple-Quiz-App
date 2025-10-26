package quizzApp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import quizzApp.model.Quiz;
import quizzApp.service.QuizAppService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final QuizAppService quizAppService;

	@ModelAttribute("listOfQuizzes")
	public List<Quiz> listOfQuizzes() {
		return quizAppService.getAllQuiz();
	}

	@GetMapping("/")
	public String homePage() {
		return "index";
	}
}
