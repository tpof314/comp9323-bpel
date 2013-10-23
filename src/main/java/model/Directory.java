package model;

import java.util.ArrayList;
import java.util.List;


public class Directory {
    private String dirName;
    private List<File> files;
    private String path;

    public Directory(String dirName, String path) {
        this.dirName = dirName;
        this.files = new ArrayList<File>();
        this.path = path;
    }

    public Directory(String dirName, List<File> files, String path) {
        this.dirName = dirName;
        this.files = files;
        this.path = path;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Directory{" + "dirName=" + dirName + ", files=" + files + ", path=" + path + '}';
    }

}
