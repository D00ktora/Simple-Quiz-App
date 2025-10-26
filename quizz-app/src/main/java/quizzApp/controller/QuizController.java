package quizzApp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import quizzApp.model.DTO.QuizDTO;
import quizzApp.model.DTO.ResultDTO;
import quizzApp.model.Question;
import quizzApp.model.Quiz;
import quizzApp.service.QuizAppService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class QuizController {
//todo this methods are created only for testing, later they need to be implemented with proper Bootstrap
	private final QuizAppService quizAppService;

	@GetMapping("/create/json")
	public String createQuizFromFile() {
		return "uploadQuiz";
	}

	@PostMapping("/create/json")
	public String createQuizFromFile(@RequestParam("file") MultipartFile file) {
		try {
			quizAppService.createQuizzesFromFile(file);
		} catch (Exception e) {
			e.printStackTrace();
			return "error in reading file";
		}
		return "redirect:/";
	}

	@GetMapping("/quiz/{id}")
	public String quiz(@ModelAttribute QuizDTO quizDTO, @PathVariable Long id) throws Exception {
		Quiz quiz = quizAppService.getQuizById(id);
		if (quiz == null) {
			return "/";
		}
		quizDTO.setId(quiz.getId());
		quizDTO.setName(quiz.getName());
		quizDTO.setCategoryId(quiz.getCategoryId());
		List<Question> questions = new ArrayList<>(quiz.getQuestions());
		Collections.shuffle(questions);
		List<Question> randomQuestions = questions.stream().limit(5).toList();
		quizDTO.setQuestions(randomQuestions);
		return "quiz";
	}

	@PostMapping("/quiz/submit")
	public String submitResult(@RequestParam Map<String, String> answers, RedirectAttributes redirectAttributes) {
		ResultDTO resultDTO = quizAppService.evaluateSubmit(answers);
		redirectAttributes.addFlashAttribute("result", resultDTO);
		return "redirect:/quiz/result";
	}

	@GetMapping("quiz/result")
	public String result() {
		return "result";
	}
}
