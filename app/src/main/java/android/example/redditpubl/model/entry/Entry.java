package android.example.redditpubl.model.entry;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "entry", strict = false)
public class Entry implements Serializable {

    @Element(name = "title")
    private String title;

//    @Element(name = "link")
//    private String link;

    @Element(name = "id")
    private String id;

    @Element(name = "content")
    private String content;

    @Element(required = false, name = "author")
    private String author;

    @Element(name = "updated")
    private String updated;



    public Entry() {
    }

    public Entry(String title, String link, String id, String content, String author, String category, String updated) {
        this.title = title;
//        this.link = link;
        this.id = id;
        this.content = content;
        this.author = author;
        this.updated = updated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public String getLink() {
//        return link;
//    }
//
//    public void setLink(String link) {
//        this.link = link;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }



    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "title='" + title + '\'' +
//                ", link='" + link + '\'' +
                ", id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", updated='" + updated + '\'' +
                '}';
    }
}
