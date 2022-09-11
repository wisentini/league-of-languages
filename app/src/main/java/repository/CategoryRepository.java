package repository;

import javafx.concurrent.Task;
import entity.Category;
import util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository extends Repository<Category> {
    public Task<List<Category>> getCategoriesTask() {
        return new Task<>() {
            @Override
            protected List<Category> call() throws Exception {
                String action = "getCategories";
                String encodedAction = StringUtil.encode(action);
                String url = String.format("%saction=%s", BASE_URL, encodedAction);

                return getAll(url, Category[].class);
            }
        };
    }

    public static List<String> getCategoryNames(List<Category> categories) {
        List<String> categoryNames = new ArrayList<>();

        for (Category category : categories) {
            String categoryName = category.getName();
            categoryNames.add(categoryName);
        }

        return categoryNames;
    }

    public static List<String> getCategoryDescriptions(List<Category> categories) {
        List<String> categoryDescriptions = new ArrayList<>();

        for (Category category : categories) {
            String categoryDescription = category.getDescription();
            categoryDescriptions.add(categoryDescription);
        }

        return categoryDescriptions;
    }
}
