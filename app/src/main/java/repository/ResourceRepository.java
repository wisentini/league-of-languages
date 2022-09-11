package repository;

import javafx.concurrent.Task;
import entity.Resource;
import util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceRepository extends Repository<Resource> {
    public Task<List<Resource>> getResourcesTask() {
        return new Task<>() {
            @Override
            protected List<Resource> call() throws Exception {
                String action = "getResources";
                String encodedAction = StringUtil.encode(action);
                String url = String.format("%saction=%s", BASE_URL, encodedAction);

                return getAll(url, Resource[].class);
            }
        };
    }

    public Task<List<Resource>> getResourcesByLanguageIdTask(String languageId) {
        return new Task<>() {
            @Override
            protected List<Resource> call() throws Exception {
                String action = "getResourcesByLanguage";
                String encodedAction = StringUtil.encode(action);
                String encodedLanguageId = StringUtil.encode(languageId);
                String url = String.format("%saction=%s&languageId=%s", BASE_URL, encodedAction, encodedLanguageId);

                return getAll(url, Resource[].class);
            }
        };
    }

    public Task<List<Resource>> getResourcesByCategoryTask(String category) {
        return new Task<>() {
            @Override
            protected List<Resource> call() throws Exception {
                String action = "getResourcesByCategory";
                String encodedAction = StringUtil.encode(action);
                String encodedCategory = StringUtil.encode(category);
                String url = String.format("%saction=%s&category=%s", BASE_URL, encodedAction, encodedCategory);

                return getAll(url, Resource[].class);
            }
        };
    }

    public Task<RepositoryResponse> postResourceTask(Resource resource) {
        return new Task<>() {
            @Override
            protected RepositoryResponse call() throws Exception {
                String action = "postResource";
                String resourceUrl = resource.getUrl();
                String languageId = resource.getLanguageId();
                String tags = resource.getTags();
                String category = resource.getCategory();
                String comment = resource.getComment();
                String userId = resource.getUserId();

                String encodedAction = StringUtil.encode(action);
                String encodedResourceUrl = StringUtil.encode(resourceUrl);
                String encodedLanguageId = StringUtil.encode(languageId);
                String encodedTags = StringUtil.encode(tags);
                String encodedCategory = StringUtil.encode(category);
                String encodedComment = StringUtil.encode(comment);
                String encodedUserId = StringUtil.encode(userId);

                String url = String.format("%saction=%s&url=%s&languageId=%s&tags=%s&category=%s&comment=%s&userId=%s", BASE_URL, encodedAction, encodedResourceUrl, encodedLanguageId, encodedTags, encodedCategory, encodedComment, encodedUserId);

                return post(url);
            }
        };
    }

    public static List<Resource> filterResourcesByLanguageId(List<Resource> resources, String languageId) {
        return resources
            .stream()
            .filter(resource -> resource
                .getLanguageId()
                .equals(languageId)
            )
            .collect(Collectors.toList());
    }

    public static List<Resource> filterResourcesByCategory(List<Resource> resources, String category) {
        return resources
            .stream()
            .filter(resource -> resource
                .getCategory()
                .equals(category)
            )
            .collect(Collectors.toList());
    }

    public static List<Resource> filterResourcesByUserId(List<Resource> resources, String userId) {
        return resources
            .stream()
            .filter(resource -> resource
                .getUserId()
                .equals(userId)
            )
            .collect(Collectors.toList());
    }

    public static List<Resource> filterResourcesByTags(List<Resource> resources, String tags) {
        return resources
            .stream()
            .filter(resource -> Arrays
                .stream(tags.split(";"))
                .anyMatch(resource.getTags()::contains)
            )
            .collect(Collectors.toList());
    }
}
