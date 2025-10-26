package quizzApp.model;

import lombok.Data;

import java.util.List;

@Data
public class Quiz {
	private Long id;
	private String name;
	private Long categoryId;
	private Long numberOfQuestions;
	private List<Question> questions;
}
