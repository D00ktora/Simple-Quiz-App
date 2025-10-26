package quizzApp.model.DTO;

import lombok.Data;
import quizzApp.model.Question;

import java.util.List;

@Data
public class QuizDTO {
	private Long id;
	private String name;
	private Long categoryId;
	private String categoryName;
	private List<Question> questions;
}
