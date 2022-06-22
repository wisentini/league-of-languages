package entity;

public class Resource extends Entity {
    private String url;
    private String languageId;
    private String tags;
    private String category;
    private String comment;
    private String userId;

    public Resource(String url, String languageId, String tags, String category, String comment, String userId) {
        this.url = url;
        this.languageId = languageId;
        this.tags = tags;
        this.category = category;
        this.comment = comment;
        this.userId = userId;
    }

    public String getUrl() {
        return this.url;
    }

    public String getLanguageId() {
        return this.languageId;
    }

    public String getTags() {
        return this.tags;
    }

    public String getCategory() {
        return this.category;
    }

    public String getComment() {
        return this.comment;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
