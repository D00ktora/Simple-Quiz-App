package quizzApp.repository;

import org.springframework.stereotype.Service;
import lombok.Data;
import quizzApp.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class CategoryRepository {
	private List<Category> categories = new ArrayList<>();
	private Long idCounter = 1L;

	public Category addCategory(Category category) {
		if (category.getId() == null) {
			category.setId(idCounter);
			categories.add(category);
			idCounter++;
			return category;
		}
		//todo: Check -> this null is not ok, on late state I will see what can be return instead of null
		return null;
	}

	public Optional<Category> findCategoryById(Long id) {
		for (Category category : categories) {
			if (category.getId().equals(id)) {
				return Optional.of(category);
			}
		}
		return Optional.empty();
	}

	public List<Category> findAllCategories() {
		return categories;
	}

	public Optional<Category> findCategoryByName(String name) {
		for (Category category : categories) {
			if (category.getName().equals(name)) {
				return Optional.of(category);
			}
		}
		return Optional.empty();
	}

	public void DeleteCategoryById(Long id) {
		categories.removeIf(category -> category.getId().equals(id));
	}
}
