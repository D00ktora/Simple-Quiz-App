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

		for (Category categoryFromRepo : categories) {
			if (categoryFromRepo.getId().equals(category.getId())) {
				return getUpdatedCategory(category, categoryFromRepo);
			} else if (categoryFromRepo.getName().equals(category.getName())) {
				return getUpdatedCategory(category, categoryFromRepo);
			}
		}

		if (category.getId() == null) {
			return createCategory(category);
		}
		return categories.stream().filter(categoryFromRepo -> categoryFromRepo.getId().equals(idCounter - 1)).findFirst().get();
	}

	private Category createCategory(Category category) {
		Category newCategory = new Category();
		newCategory.setId(idCounter++);
		newCategory.setName(category.getName());
		newCategory.setQuestionIds(category.getQuestionIds());
		categories.add(newCategory);
		return newCategory;
	}

	private Category getUpdatedCategory(Category category, Category categoryFromRepo) {
		category.setId(categoryFromRepo.getId());
		categories.remove(categoryFromRepo);
		categories.add(category);
		return categories.stream().filter(updatedCategory -> updatedCategory.getId().equals(category.getId())).findFirst().get();
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
