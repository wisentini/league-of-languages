package repository;

import javafx.concurrent.Task;
import entity.Resource;
import util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceRepository extends Repository<Resource> {
    public Task<List<Resource>> getResourcesTask() {
        Task<List<Resource>> task = new Task<>() {
            @Override
            protected List<Resource> call() throws Exception {
                String action = "getResources";
                String encodedAction = StringUtil.encode(action);
                String url = String.format("%saction=%s", BASE_URL, encodedAction);

                List<Resource> resources = getAll(url, Resource[].class);
                return resources;
            }
        };

        return task;
    }

    public Task<List<Resource>> getResourcesByLanguageIdTask(String languageId) {
        Task<List<Resource>> task = new Task<>() {
            @Override
            protected List<Resource> call() throws Exception {
                String action = "getResourcesByLanguage";
                String encodedAction = StringUtil.encode(action);
                String encodedLanguageId = StringUtil.encode(languageId);
                String url = String.format("%saction=%s&languageId=%s", BASE_URL, encodedAction, encodedLanguageId);

                List<Resource> resources = getAll(url, Resource[].class);
                return resources;
            }
        };

        return task;
    }

    public Task<List<Resource>> getResourcesByCategoryTask(String category) {
        Task<List<Resource>> task = new Task<>() {
            @Override
            protected List<Resource> call() throws Exception {
                String action = "getResourcesByCategory";
                String encodedAction = StringUtil.encode(action);
                String encodedCategory = StringUtil.encode(category);
                String url = String.format("%saction=%s&category=%s", BASE_URL, encodedAction, encodedCategory);

                List<Resource> resources = getAll(url, Resource[].class);
                return resources;
            }
        };

        return task;
    }

    public Task<RepositoryResponse> postResourceTask(Resource resource) {
        Task<RepositoryResponse> task = new Task<>() {
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
                RepositoryResponse repositoryResponse = post(url);

                return repositoryResponse;
            }
        };

        return task;
    }

    public static List<Resource> filterResourcesByLanguageId(List<Resource> resources, String languageId) {
        List<Resource> filteredResources = resources
                .stream()
                .filter(
                        resource -> resource
                                .getLanguageId()
                                .equals(languageId)
                )
                .collect(
                        Collectors.toList()
                );

        return filteredResources;
    }

    public static List<Resource> filterResourcesByCategory(List<Resource> resources, String category) {
        List<Resource> filteredResources = resources
                .stream()
                .filter(
                        resource -> resource
                                .getCategory()
                                .equals(category)
                )
                .collect(
                        Collectors.toList()
                );

        return filteredResources;
    }

    public static List<Resource> filterResourcesByUserId(List<Resource> resources, String userId) {
        List<Resource> filteredResources = resources
                .stream()
                .filter(
                        resource -> resource
                                .getUserId()
                                .equals(userId)
                )
                .collect(
                        Collectors.toList()
                );

        return filteredResources;
    }

    public static List<Resource> filterResourcesByTags(List<Resource> resources, String tags) {
        List<Resource> filteredResources = resources
                .stream()
                .filter(
                        resource -> Arrays
                                .stream(
                                        tags
                                                .split(";")
                                )
                                .anyMatch(
                                        resource
                                                .getTags()::contains
                                )
                )
                .collect(
                        Collectors.toList()
                );

        return filteredResources;
    }
}
