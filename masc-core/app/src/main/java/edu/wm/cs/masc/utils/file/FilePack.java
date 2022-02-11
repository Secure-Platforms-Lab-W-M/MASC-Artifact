package edu.wm.cs.masc.utils.file;

public class FilePack {
    private String filename;
    private String path;
    private String content;

    public FilePack(String filename, String path, String content) {
        this.filename = filename;
        this.path = path;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
