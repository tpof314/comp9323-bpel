package model;

/**
 * File Model.
 * A data structure for storing a file. 
 * @author Haojie Huang, Peizhi Shao
 */
public class File {
    private String fileName;
    private String path;

    public File(String fileName, String path) {
        this.fileName = fileName;
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "File{" + "fileName=" + fileName + ", path=" + path + '}';
    }
}
