/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.runtime;

import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;
import controller.dataController.ProjectController;
import controller.tools.ResultReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import model.Project;
import model.User;
import org.xml.sax.SAXException;

public class RuntimeController {

    private Client client;
    private WebResource baseService;
    private WebResource service;
    private ClientResponse response;
    private ProjectController projectController;
    private ResultReader resultReader;

    public RuntimeController() throws GeneralSecurityException, IOException, URISyntaxException, ParserConfigurationException {
        ClientConfig config = new DefaultClientConfig();
        this.client = Client.create(config);
        this.baseService = client.resource(getBaseURI());
        this.projectController = new ProjectController();
        this.resultReader = new ResultReader();
    }

    public boolean compile(User user, Project project, boolean isProjectModified) {

        if (!isProjectModified) {
            /*
             this.service = client.resource(project.getUri());
             this.response = this.service.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
             if (response.getStatus() == 500) {
             return "Compile Failed by @GET.<br/>";
             } else {
             String resultXml = response.getEntity(String.class);
                
             try{
             compileResult = this.resultReader.readCompileResult(resultXml);
             }
             catch(Exception ex){
             return "Xml phrase Failed of @GET result.<br/>";
             }
                
             return compileResult;
             }
             * */
            return true;
        } else {
            String compileResult = "";

            if (!project.getUri().equals("")) {
                this.service = this.client.resource(project.getUri());
                this.response = service.accept(MediaType.APPLICATION_XML).delete(ClientResponse.class);
                if (response.getStatus() == 500) {
                    project.setCompileResult("<span style=\"color: red\">Compile Failed.</span><br/>");
                    return false;
                }
            }

            this.projectController.saveToServer(project);
            Form form = new Form();
            form.add("user", user.getUserName());
            form.add("token", project.getProjId());
            form.add("project", project.getProjName());

            response = this.baseService.type(MediaType.APPLICATION_FORM_URLENCODED)
                    .post(ClientResponse.class, form);
            if (response.getStatus() == 500) {
                project.setCompileResult("<span style=\"color: red\">Compile Failed.</span><br/>");
                return false;
            } else {
                String resultXml = "";
                resultXml = response.getEntity(String.class);
                String uri = "";

                try {
                    compileResult = this.resultReader.readCompileResult(resultXml);
                    uri = this.resultReader.readUri(resultXml);
                } catch (Exception ex) {
                    project.setCompileResult("<span style=\"color: red\">Compile Failed.</span><br/>");
                    return false;
                }
                if (compileResult.equals("")) {
                    project.setCompileResult("<span style=\"color: red\">Compile Failed.</span><br/>");
                    return false;
                }

                project.setState(2);
                project.setUri(uri);

                String totalResult = compileResult + "<span style=\"color: green\">Compile Successfully.</span><br/>";
                project.setCompileResult(totalResult);

                return true;
            }
        }
    }

    public boolean execute(User user, Project project, String function, String inputParam, boolean isProjectModified) {
        String executeResult = "";

        if (isProjectModified) {
            boolean compileSuccess = this.compile(user, project, isProjectModified);
            if (!compileSuccess) {
                return false;
            }
            /*
             if(project.getState() != 2)
             return "Compile Failed.<br/>";
             else
             executeResult += compileResult;
             */
        }

        Form form = new Form();
        form.add("function", function);
        form.add("inputParams", inputParam);
        this.service = this.client.resource(project.getUri());
        this.response = service.type(MediaType.APPLICATION_FORM_URLENCODED)
                .put(ClientResponse.class, form);

        if (this.response.getStatus() == 500) {
            project.setExecuteResult("<span style=\"color: red\">Execute Failed.</span><br/>");
            return false;
        } else {
            String resultXml = "";
            resultXml = response.getEntity(String.class);
            try {
                executeResult = this.resultReader.readExecuteResult(resultXml);
            } catch (Exception ex) {
                project.setExecuteResult("<span style=\"color: red\">Execute Failed.</span><br/>");
                return false;
            }
            project.setExecuteResult(executeResult);
            return true;
        }
    }

    private static String getBaseURI() {
        return "http://weill.cse.unsw.edu.au:10001/comp9323-runtime/bpel/runtimes/";
    }
}
