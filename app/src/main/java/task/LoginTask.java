package task;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javafx.concurrent.Task;
import repository.RepositoryResponse;

public class LoginTask extends Task<RepositoryResponse> {
    private static final String GITHUB_USERS_API_BASE_URL = "https://api.github.com/users/";

    private static final List<String> ALLOWED_USERS = List.of(
        "andreainfufsm", "afrofuturist", "crazynds", "davidlopes22",
        "deivisfelipe", "edusmrs", "josi-aggens", "leobenatti11",
        "marcosnoble", "miguelwisneski", "phcosta0", "piekala",
        "pivetta21", "virginiacolares", "wisentini"
    );

    private String userId;

    public LoginTask(String userId) {
        this.userId = userId.toLowerCase();
    }

    @Override
    protected RepositoryResponse call() throws Exception {
        RepositoryResponse response = basicUserIdValidation(userId);
        boolean userIdIsValid = response.getSuccess();

        if (!userIdIsValid) {
            return response;
        }

        if (!ALLOWED_USERS.contains(userId)) {
            String message = String.format("\"%s\" doesn't belong to \"elc117\".", userId);
            return new RepositoryResponse(false, message);
        }

        String endpoint = String.format("%s%s", GITHUB_USERS_API_BASE_URL, userId);
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        int responseCode = connection.getResponseCode();

        connection.disconnect();

        boolean success = false;
        String message = "";

        if (responseCode != 200) {
            message = String.format("Couldn't found \"%s\" on GitHub.", userId);
        } else {
            success = true;
        }

        return new RepositoryResponse(success, message);
    }

    private static RepositoryResponse basicUserIdValidation(String userId) {
        boolean success = false;
        String message = "";

        if (userId.length() < 1) {
            message = "userId must have at least 1 character";
        } else if (userId.length() > 39) {
            message = "userId cannot exceed 39 characters";
        } else if (userId.startsWith("-")) {
            message = "userId cannot start with a hyphen";
        } else if (userId.endsWith("-")) {
            message = "userId cannot end with a hyphen";
        } else if (userId.contains("--")) {
            message = "userId cannot contain multiple consecutive hyphens";
        } else if (!isAlphanumeric(userId)) {
            message = "userId cannot contain special characters";
        } else {
            success = true;
        }

        RepositoryResponse repositoryResponse = new RepositoryResponse(success, message);
        return repositoryResponse;
    }

    private static boolean isAlphanumeric(String string) {
        int stringLength = string.length();

        for (int index = 0; index < stringLength; index++) {
            char character = string.charAt(index);

            if ((!Character.isLetterOrDigit(character) || Character.isSpaceChar(character)) && character != '-') {
                return false;
            }
        }

        return true;
    }
}
