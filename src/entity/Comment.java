package entity;

public class Comment {

    private int userId;
    private int postId;
    private String comment;


    private static int index = 1;

    private int id;


    public Comment(int userId, int postId, String comment) {
        this.id=index++;
        this.userId = userId;
        this.postId = postId;
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return comment;
    }
}
