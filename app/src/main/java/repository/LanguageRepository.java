package repository;

import javafx.concurrent.Task;
import entity.Language;
import util.StringUtil;

import java.util.List;

public class LanguageRepository extends Repository<Language> {
    public Task<List<Language>> getLanguagesTask() {
        return new Task<>() {
            @Override
            protected List<Language> call() throws Exception {
                String action = "getLanguages";
                String encodedAction = StringUtil.encode(action);
                String url = String.format("%saction=%s", BASE_URL, encodedAction);

                return getAll(url, Language[].class);
            }
        };
    }

    public Task<RepositoryResponse> postLanguageTask(Language language) {
        return new Task<>() {
            @Override
            protected RepositoryResponse call() throws Exception {
                String action = "postLanguage";
                String languageId = language.getLanguageId();
                String firstAppeared = language.getFirstAppeared();
                String paradigm = language.getParadigm();
                String userId = language.getUserId();

                String encodedAction = StringUtil.encode(action);
                String encodedLanguageId = StringUtil.encode(languageId);
                String encodedFirstAppeared = StringUtil.encode(firstAppeared);
                String encodedParadigm = StringUtil.encode(paradigm);
                String encodedUserId = StringUtil.encode(userId);

                String url = String.format("%saction=%s&languageId=%s&firstAppeared=%s&paradigm=%s&userId=%s", BASE_URL, encodedAction, encodedLanguageId, encodedFirstAppeared, encodedParadigm, encodedUserId);

                return post(url);
            }
        };
    }
}
