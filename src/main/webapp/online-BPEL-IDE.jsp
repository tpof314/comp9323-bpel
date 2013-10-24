<%@ page import="controller.dataController.*"%>
<%@ page import="model.*" %>
<%@ page import="model.bean.*"%>
<html lang="en">
	<head>
		<meta charset="utf-8" />
  		<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  		<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
  		<script src="ace-builds/src-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>
  		<script src="javascript/interface.js"></script>
  		<script src="javascript/ajaxfileupload.js"></script>
  		<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  		<link rel="stylesheet" href="css/style.css" type="text/css" />
  		<title>Online BPEL IDE</title>
	</head>
	<body onload="ide_onload()" onunload="toolbar_reset_button()">
		<%
			UserBean userBean = (UserBean)session.getAttribute("userBean");
			String project_id = request.getParameter("project_id");
			String submission_id = request.getParameter("submission_id");
			String assignment_no = request.getParameter("assignment_no");
	        
			if (project_id != null) {
		        Project project = userBean.projectController.loadProject(userBean.getUser(), userBean.getUser().getUserProjects().get(Integer.valueOf(project_id)).getProjName(), userBean.getUser().getUserProjects().get(Integer.valueOf(project_id)).getProjId());
	        	userBean.setCurrProj(project);
			}
			else if (submission_id != null) {
				Project project = userBean.projectController.loadProject(userBean.getUser(), "assignment" + assignment_no, submission_id);
				userBean.setCurrProj(project);
			}
        	
        	int number = 0;
        	int pid = 0;
        	int quantity = 1;
        	int number_of_folders = 1;
        	int number_of_files = 0;
        	for(int i = 0; i < userBean.getCurrProj().getDirs().size(); ++i) {
        		quantity++;
        		number_of_folders++;
        		for(int j = 0; j < userBean.getCurrProj().getDirs().get(i).getFiles().size(); ++j) {
        			quantity++;
        			number_of_files++;
        		}
        	}
        	for(int i = 0; i < userBean.getCurrProj().getFiles().size(); ++i) {
        		quantity++;
        		number_of_files++;
        	}
        	
        	java.util.Locale locale = request.getLocale();
        	java.text.DateFormat dateFormat = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.LONG, java.text.DateFormat.LONG, locale);
        	
        	String time = "Compile the project on " + dateFormat.format(new java.util.Date()) + "<br/>";
        	String log = userBean.getCurrProj().getCompileResult();
        	String result = userBean.getCurrProj().getExecuteResult();
        	
        	if (!userBean.getCurrProj().getCompileResult().equals(""))
        		log = time + userBean.getCurrProj().getCompileResult();
        	
        %>
		<div id="header_information">
			<button class="unsw" title="University of New South Wales" onclick="window.location.href='http://www.unsw.edu.au'"></button>
			University of New South Wales&nbsp;&nbsp;&nbsp;&nbsp;E-Enterprise Project
		</div>
		<div id="user_information">
			Log As: <%= userBean.getUser().getUserName() %>&nbsp;&nbsp;&nbsp;&nbsp;
			Status: <% if (userBean.getUser().getUserType().equals("teacher")) { %>
						Teacher
					<% } else if (userBean.getUser().getUserType().equals("student")){%>
						Student
					<% } %>
			<button class="log-off" title="Log off" onclick="window.location.href='index.jsp'"></button>
		</div>
		<div id="list">
			<ul>
				<li><a href="#list_area">Project Explorer</a></li>
			</ul>
			<div id="list_area">
				<p id="list-p-<%= pid++ %>" onclick="list_folder_select(this.id, <%= quantity %>)" ondblclick="list_hide('list-fold-<%= number %>', '', 'list-hide-<%= number %>')">
					<img id="list-fold-<%= number %>" src="images/unfold-icon.png" onmouseover="list_onmouseover(this.id)" onmouseout="list_onmouseout(this.id)" onclick="list_hide('list-fold-<%= number %>', '', 'list-hide-<%= number %>')">
					<img src="images/project-icon.png">
					<span class="name"><%= userBean.getCurrProj().getProjName() %></span>
				</p>
				<div id="list-hide-<%= number++ %>">
					<% for(int i = 0; i < userBean.getCurrProj().getDirs().size(); ++i) { %>
						<p id="list-p-<%= pid++ %>" onclick="list_folder_select(this.id, <%= quantity %>)" ondblclick="list_hide('list-fold-<%= number %>', 'list-folder-icon-<%= number %>', 'list-hide-<%= number %>')">
							&nbsp;&nbsp;&nbsp;&nbsp;
							<img id="list-fold-<%= number %>" src="images/unfold-icon.png" onmouseover="list_onmouseover(this.id)" onmouseout="list_onmouseout(this.id)" onclick="list_hide('list-fold-<%= number %>', 'list-folder-icon-<%= number %>', 'list-hide-<%= number %>')">
							<img id="list-folder-icon-<%= number %>" src="images/folder-icon-unfolded.png">
							<span class="name"><%= userBean.getCurrProj().getDirs().get(i).getDirName() %></span>
						</p>
						<div id="list-hide-<%= number++ %>">
							<% for(int j = 0; j < userBean.getCurrProj().getDirs().get(i).getFiles().size(); ++j) { %>
								<p id="list-p-<%= pid++ %>" onclick="list_file_select(this.id, <%= quantity %>)" ondblclick="open_a_file('<%= userBean.getCurrProj().getDirs().get(i).getFiles().get(j).getFileName() %>', '<%= userBean.fileController.open(userBean.getCurrProj().getDirs().get(i).getFiles().get(j)).replaceAll("\'", "Single_Quatation_Mark").replaceAll("\"", "Quatation_Mark").replaceAll("\n", "New_Line") %>')">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<img src="images/document-icon.png">
									<span class="name"><%= userBean.getCurrProj().getDirs().get(i).getFiles().get(j).getFileName() %></span>
									<span class="is_in_project">false</span>
									<span class="directory_number"><%= i %></span>
									<span class="file_number"><%= j %></span>
								</p>
							<% } %>
						</div>
					<% } %>
					<% for(int i = 0; i < userBean.getCurrProj().getFiles().size(); ++i) { %>
						<p id="list-p-<%= pid++ %>" onclick="list_file_select(this.id, <%= quantity %>)" ondblclick="open_a_file('<%= userBean.getCurrProj().getFiles().get(i).getFileName() %>', '<%= userBean.fileController.open(userBean.getCurrProj().getFiles().get(i)).replaceAll("\'", "Single_Quatation_Mark").replaceAll("\"", "Quatation_Mark").replaceAll("\n", "New_Line") %>')">
							&nbsp;&nbsp;&nbsp;&nbsp;
							<img src="images/document-icon.png">
							<span class="name"><%= userBean.getCurrProj().getFiles().get(i).getFileName() %></span>
							<span class="is_in_project">true</span>
							<span class="directory_number">null</span>
							<span class="file_number"><%= i %></span>
						</p>
					<% } %>
				</div>
				<div class="hide-information">
					<span class="number_of_folders"><%= number_of_folders %></span>
					<span class="number_of_files"><%= number_of_files %></span>
					<span class="quantity"><%= quantity %></span>
				</div>
			</div>
		</div>
		<div id="view">
			<div id="toolbar">
				&nbsp;
				<button class="new" title="Create a new file"></button>
				<img src="images/separate-line.png">
				<button class="remove" title="Remove a file" disabled="disabled"></button>
				<img src="images/separate-line.png">
				<button class="save" title="Save" disabled="disabled"></button>
				<img src="images/separate-line.png">
				<button class="save-all" title="Save all" disabled="disabled"></button>
				<img src="images/separate-line.png">
				<button class="upload" title="Upload a file"></button>
				<img src="images/separate-line.png">
				<button class="compile" title="Compile the project"></button>
				<img src="images/separate-line.png">
				<button class="execute" title="Execute the project"></button>
				<div class="align_right">
					<button class="back" title="Exit the project" onclick="window.location.href='home.jsp'"></button>
					&nbsp;
				</div>
			</div>
			<div id="editor">
				<div id="editor_tabs">
					<ul></ul>
				</div>
			</div>
			<div id="console">
				<div id="console_tabs">
					<ul>
						<li><a href="#log">log</a></li>
						<li><a href="#result">result</a></li>
					</ul>
					<div id="log_and_result">
						<div class="load">
							<div id="log" class="ui-tabs-panel ui-widget-content ui-corner-bottom" style="display: block" aria-hidden="false" aria-expanded="true" aria-labelledby="ui-id-2" role="tabpanel">
								<div class="log_load">
									<p><%= log %></p>
								</div>
							</div>
							<div id="result" class="ui-tabs-panel ui-widget-content ui-corner-bottom" style="display: none" aria-hidden="true" aria-expanded="false" aria-labelledby="ui-id-3" role="tabpanel">
								<div class="result_load">
									<p><%= result %></p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="create_a_new_file_dialog">
			<p>File Directory&nbsp;:</p>
			<div id="create_a_new_file_dialog_list">
				<div id="create_a_new_file_dialog_folders">
					<%
						int create_a_new_file_dialog_number = 0;
						int create_a_new_file_dialog_did = 0;
					%>
					<p id="create_a_new_file_dialog_folders-<%= create_a_new_file_dialog_did %>" onclick="create_a_new_file_dialog_folders_select(this.id, 'create_a_new_file_dialog_files-did-<%= create_a_new_file_dialog_did %>', <%= number_of_folders %>)">
						<% create_a_new_file_dialog_did++; %>
						&nbsp;
						<img id="create_a_new_file_dialog_folders-fold-<%= create_a_new_file_dialog_number %>" src="images/unfold-icon.png" onmouseover="list_onmouseover(this.id)" onmouseout="list_onmouseout(this.id)">
						<img src="images/project-icon.png">
						<span><%= userBean.getCurrProj().getProjName() %></span>
					</p>
					<% create_a_new_file_dialog_number++; %>
					<% for(int i = 0; i < userBean.getCurrProj().getDirs().size(); ++i) { %>
						<p id="create_a_new_file_dialog_folders-<%= create_a_new_file_dialog_did %>" onclick="create_a_new_file_dialog_folders_select(this.id, 'create_a_new_file_dialog_files-did-<%= create_a_new_file_dialog_did %>', <%= number_of_folders %>)">
							<% create_a_new_file_dialog_did++; %>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<img id="create_a_new_file_dialog_folders-fold-<%= create_a_new_file_dialog_number %>" src="images/unfold-icon.png" onmouseover="list_onmouseover(this.id)" onmouseout="list_onmouseout(this.id)">
							<img src="images/folder-icon-unfolded.png">
							<span><%= userBean.getCurrProj().getDirs().get(i).getDirName() %></span>
						</p>
						<% create_a_new_file_dialog_number++; %>
					<% } %>
				</div>
				<div id="create_a_new_file_dialog_files">
					<%
						create_a_new_file_dialog_number = 0;
						create_a_new_file_dialog_did = 0;
					%>
					<div id="create_a_new_file_dialog_files-did-<%= create_a_new_file_dialog_did %>">
						<% for(int i = 0; i < userBean.getCurrProj().getFiles().size(); ++i) { %>
							<p id="create_a_new_file_dialog_files-<%= create_a_new_file_dialog_number++ %>" onclick="create_a_new_file_dialog_files_select('<%= userBean.getCurrProj().getFiles().get(i).getFileName() %>')" onmouseover="create_a_new_file_dialog_files_onmouseover(this.id)" onmouseout="create_a_new_file_dialog_files_onmouseout(this.id)">
								&nbsp;
								<img src="images/document-icon.png">
								<span><%= userBean.getCurrProj().getFiles().get(i).getFileName() %></span>
							</p>
						<% } %>
					</div>
					<% create_a_new_file_dialog_did++; %>
					<% for(int i = 0; i < userBean.getCurrProj().getDirs().size(); ++i) { %>
						<div id="create_a_new_file_dialog_files-did-<%= create_a_new_file_dialog_did %>">
							<% create_a_new_file_dialog_did++; %>
							<% for(int j = 0; j < userBean.getCurrProj().getDirs().get(i).getFiles().size(); ++j) { %>
								<p id="create_a_new_file_dialog_files-<%= create_a_new_file_dialog_number++ %>" onclick="create_a_new_file_dialog_files_select('<%= userBean.getCurrProj().getDirs().get(i).getFiles().get(j).getFileName() %>')" onmouseover="create_a_new_file_dialog_files_onmouseover(this.id)" onmouseout="create_a_new_file_dialog_files_onmouseout(this.id)">
									&nbsp;
									<img src="images/document-icon.png">
									<span><%= userBean.getCurrProj().getDirs().get(i).getFiles().get(j).getFileName() %></span>
								</p>
							<% } %>
						</div>
					<% } %>
				</div>
			</div>
			<br/>
			<form>
				<table>
					<tr>
						<td>File Name&nbsp;:
						</td>
						<td>
							<input class="text ui-widget-content ui-corner-all" type="text" name="new_file_name" id="new_file_name" value="" onkeydown="return create_a_new_file_dialog_on_key_down(event.keyCode);"/>
						</td>
					</tr>
					<tr>
						<td>File Type&nbsp;&nbsp;&nbsp;:
						</td>
						<td>
							<select class="text ui-widget-content ui-corner-all" name="new_file_type" id="new_file_type">
								<option value=".bpel">bpel</option>
								<option value=".soap">soap</option>
								<option value=".txt">txt</option>
								<option value=".wsdl">wsdl</option>
								<option value=".xml">xml</option>
							</select>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div id="overwrite_a_file_dialog"></div>
		<div id="remove_a_file_dialog"></div>
		<div id="save_a_file_dialog"></div>
		<div id="upload_a_file_dialog">
			<form id="upload_a_file" action="uploadFile" method="post" enctype="multipart/form-data">
				<input id="upload_a_file_input" type="file" name="file">
			</form>
		</div>
		<div id="upload_a_file_no_file_dialog">
			No file is selected to upload !
		</div>
		<div id="upload_a_file_file_exists_dialog">
			File already existed !
		</div>
		<div id="upload_a_file_error_dialog">
			We only accept files with extension "bpel", "soap", "txt", "wsdl" or "xml".
		</div>
		<div id="compile_the_project_save_dialog"></div>
		<div id="execute_the_project_save_dialog"></div>
		<div id="execute_the_project_dialog">
			<p>Please select a soap file and corresponding service</p>
			<div id="execute_the_project_dialog_list">
				<div id="execute_the_project_dialog_soap">
					<%
						int execute_the_project_dialog_number = 0;
						for (int i = 0; i < userBean.getCurrProj().getDirs().size(); ++i)
							if (userBean.getCurrProj().getDirs().get(i).getDirName().equals("input"))
								for (int j = 0; j < userBean.getCurrProj().getDirs().get(i).getFiles().size(); ++j) {
					%>
									<p id="execute_the_project_dialog_files-<%= execute_the_project_dialog_number++ %>" onclick="execute_the_project_dialog_files_select(this.id)">
										&nbsp;
										<img src="images/document-icon.png">
										<span class="name"><%= userBean.getCurrProj().getDirs().get(i).getFiles().get(j).getFileName() %></span>
									</p>
					<%
								}
					%>
				</div>
				<div id="execute_the_project_dialog_service">
				<%
					execute_the_project_dialog_number = 0;
					String url = new String();
					for (int i = 0; i < userBean.getCurrProj().getDirs().size(); ++i)
						if (userBean.getCurrProj().getDirs().get(i).getDirName().equals("wsdl"))
	        				for (int j = 0; j < userBean.getCurrProj().getDirs().get(i).getFiles().size(); ++j) {
	        					String[] split = userBean.fileController.open(userBean.getCurrProj().getDirs().get(i).getFiles().get(j)).split("<soap:address location=\"");
	        					for (int k = 1; k < split.length; ++k) {
	        						url = split[k].substring(0, split[k].indexOf("\""));
	        	%>
	        						<p id="execute_the_project_dialog_services-<%=execute_the_project_dialog_number++ %>" onclick="execute_the_project_dialog_services_select(this.id)">
	        							<img src="images/service-icon.png">
	        							<span class="name"><%= url.substring(url.lastIndexOf("/") + 1) %></span>
	        						</p>
	        	<%
	        					}
	        				}
	        	%>
	        	</div>
	        </div>
		</div>
		<form id="save_a_file" action="saveFile" method="post">
			<input id="save_a_file-is_in_project" name="is_in_project" value="">
			<input id="save_a_file-directory_number" name="directory_number" value="">
			<input id="save_a_file-file_number" name="file_number" value="">
			<input id="save_a_file-content" name="content" value="">
		</form>
	</body>
</html>