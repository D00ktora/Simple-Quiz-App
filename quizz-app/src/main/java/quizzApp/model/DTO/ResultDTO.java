package quizzApp.model.DTO;

import lombok.Data;

import java.util.Map;

@Data
public class ResultDTO {
	private Long quizId;
	private String quizName;
	private String result;
	private String numberOfCorrectAnswers;
	private Map<String, String> wrongAnswers;
}
