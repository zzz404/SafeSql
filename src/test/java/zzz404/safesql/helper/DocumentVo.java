package zzz404.safesql.helper;

public class DocumentVo {
    private Integer ownerId;
    private String docTitle;
    private Category category; 

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String title2) {
        this.docTitle = title2;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}
