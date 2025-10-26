package quizzApp.model;

import lombok.Data;

import java.util.Map;

@Data
public class Question {
	private Long id;
	private String description;
	private Long categoryId;
	private Map<String, Boolean> answers;
}
