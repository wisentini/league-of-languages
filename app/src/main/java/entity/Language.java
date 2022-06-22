package entity;

public class Language extends Entity {
    private String languageId;
    private String firstAppeared;
    private String paradigm;
    private String userId;

    public Language(String languageId, String firstAppeared, String paradigm, String userId) {
        this.languageId = languageId;
        this.firstAppeared = firstAppeared;
        this.paradigm = paradigm;
        this.userId = userId;
    }

    public String getLanguageId() {
        return languageId;
    }

    public String getFirstAppeared() {
        return firstAppeared;
    }

    public String getParadigm() {
        return paradigm;
    }

    public String getUserId() {
        return userId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public void setFirstAppeared(String firstAppeared) {
        this.firstAppeared = firstAppeared;
    }

    public void setParadigm(String paradigm) {
        this.paradigm = paradigm;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
