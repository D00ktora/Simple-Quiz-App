package quizzApp.model.DTO;

import lombok.Data;

@Data
public class ResultDTO {
	private Long quizId;
	private String quizName;
	private String result;
	private String numberOfCorrectAnswers;
}
