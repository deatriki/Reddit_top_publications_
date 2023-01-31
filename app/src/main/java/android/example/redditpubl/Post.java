package android.example.redditpubl;

public class Post {
    private String title;
    private String author;
    private String data_update;
    private String postURL;
    private String thubnailURL;

    public Post(String title, String author, String data_update, String postURL, String thubnailURL) {
        this.title = title;
        this.author = author;
        this.data_update = data_update;
        this.postURL = postURL;
        this.thubnailURL = thubnailURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getData_update() {
        return data_update;
    }

    public void setData_update(String data_update) {
        this.data_update = data_update;
    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getThubnailURL() {
        return thubnailURL;
    }

    public void setThubnailURL(String thubnailURL) {
        this.thubnailURL = thubnailURL;
    }
}
