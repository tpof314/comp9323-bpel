package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Model.
 * A data structure for storing a project.
 * @author Haojie Huang, Peizhi Shao
 */
public class Project{
    //Keep the state of the project.
    //State 0 means not saved.
    //State 1 means has been saved.
    //State 2 means has been compiled.
    private int state;
    private String projId;
    private String projName;
    private List<Directory> dirs;
    private List<File> files;
    private String path;
    //The uri of the project runtime.
    private String uri;
    private String compileResult;
    private String executeResult;
    
    public Project(String projId, String projName) {
        this.state = 0;
        this.projId = projId;
        this.projName = projName;
        this.dirs = new ArrayList<Directory>();
        this.files = new ArrayList<File>();
        this.path = "";
        this.uri = "";
        this.compileResult = "";
        this.executeResult = "";
    } 
    
    public Project(String projId, String projName, List<Directory> dirs, List<File> files, String path) {
        this.state = 0;
        this.projId = projId;
        this.projName = projName;
        this.dirs = dirs;
        this.files = files;
        this.path = path;
        this.uri = null;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public List<Directory> getDirs() {
        return dirs;
    }

    public void setDirs(List<Directory> dirs) {
        this.dirs = dirs;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCompileResult() {
        return compileResult;
    }

    public void setCompileResult(String compileResult) {
        this.compileResult = compileResult;
    }

    public String getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }

    @Override
    public String toString() {
        return "Project{" + "state=" + state + ", projId=" + projId + ", projName=" + projName + ", dirs=" + dirs + ", files=" + files + ", path=" + path + ", uri=" + uri + '}';
    }
    
}
