package model.bean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import model.User;
import model.Project;
import model.Assignment;
import controller.dataController.AssignmentController;
import controller.dataController.UserController;
import controller.dataController.ProjectController;
import controller.dataController.DirectoryController;
import controller.dataController.FileController;
import controller.runtime.RuntimeController;

public class UserBean {
    private User user;
    private Project currProj;
    private List<Assignment> assignments;
    public UserController userController;
    public ProjectController projectController;
    public DirectoryController directoryController;
    public FileController fileController;
    public RuntimeController runtimeController;
    public AssignmentController assignmentController;

    public UserBean() throws GeneralSecurityException, IOException, URISyntaxException, ParserConfigurationException {
        user = null;
        currProj = null;
        assignments = null;
        userController = new UserController();
        projectController = new ProjectController();
        directoryController = new DirectoryController();
        fileController = new FileController();
        runtimeController = new RuntimeController();
        assignmentController = new AssignmentController();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getCurrProj() {
        return currProj;
    }

    public void setCurrProj(Project currProj) {
        this.currProj = currProj;
    }

	public List<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}

}
