package quizzApp.model.DTO;

import lombok.Data;

@Data
public class FormSingleQuestionDTO {
	private String description;
	private String quizName;
	private String categoryName;
	private String firstAnswer;
	private String secondAnswer;
	private String thirdAnswer;
	private String forthAnswer;
	private String correctAnswer;
}
