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
  		<title>Home</title>
	</head>
	<body onunload="project_reset_action()">
		<%
	        UserBean userBean = (UserBean) session.getAttribute("userBean");
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
		<div id="project_list">
			<ul>
				<li><a href="#project_list_area">My Project</a></li>
				<li><a href="#assignment_list_area">Assignment</a></li>
			</ul>
			<div id="project_list_area">
				<div class="align_right">
					<button class="new" title="Create a new project">New Project</button>
				</div>
				<br/>
				<div id="project_list_area_projects">
				<% for (int i = 0; i < userBean.getUser().getUserProjects().size(); ++i) { %>
					<div id="pid-<%= i %>" class="project" onmouseover="project_onmouseover(this.id)" onmouseout="project_onmouseout(this.id)" ondblclick="window.location.href='online-BPEL-IDE.jsp?project_id=<%= i %>'">
						<div class="project_information">
							<p class="project_name">
								<img src="images/project-icon.png">&nbsp;
								<span class="name"><%= userBean.getUser().getUserProjects().get(i).getProjName() %></span>
							</p>
							<p class="last_modified">Double click to open the project - Private</p>
						</div>
						<div class="project_action">
							<p class="action">
								<select class="text ui-widget-content ui-corner-all">
									<option>Actions</option>
									<option onclick="remove_a_project(<%= i %>)">Remove</option>
								</select>
							</p>
						</div>
					</div>
					<br/>
				<% } %>
				</div>
			</div>
			<div id="assignment_list_area">
				<div class="align_right">
					<% if (userBean.getUser().getUserType().equals("teacher")) { %>
					<button class="new" title="Create a new assignment">New Assignment</button>
					<% } %>
				</div>
				<br/>
				<div id="assignment_list_area_project">
					<div id="assignment_list_area_projects">
						<% for (int i = 0; i < userBean.getAssignments().size(); ++i) { %>
						<h3 id="aid-<%= i %>" style="font: bold 15px/24px Arial, Helvetica, sans-serif">
							<% if (userBean.getUser().getUserType().equals("teacher")) { %>
							<div style="float: left">
								<img src="images/assignment-icon.png">&nbsp;
								<span class="name">Assignment <%= userBean.getAssignments().get(i).getAssNo() %>: </span>
								<span class="name"><%= userBean.getAssignments().get(i).getAssName() %></span>
							</div>
							<div class="align_right">
								<button class="remove" title="Remove this assignment" style="background: url('images/remove-icon.png') center no-repeat; height: 20px; width: 20px; border: none" onclick="remove_an_assignment(<%= i %>)"></button>
							</div>
							<% } else { %>
							<div>
								<img src="images/assignment-icon.png">&nbsp;
								<span class="name">Assignment <%= userBean.getAssignments().get(i).getAssNo() %>: </span>
								<span class="name"><%= userBean.getAssignments().get(i).getAssName() %></span>
							</div>
							<% } %>
						</h3>
						<div id="aid_div-<%= i %>">
							<table class="assignment_information" style="font: 15px/24px Arial, Helvetica, sans-serif">
								<tr>
									<td width="100px">Name:</td>
									<td><%= userBean.getAssignments().get(i).getAssName() %></td>
								</tr>
								<tr>
									<td>Deadline:</td>
									<td colspan="2"><%= userBean.getAssignments().get(i).getDeadline() %></td>
								</tr>
								<tr>
									<td>Specification:</td>
									<td><a href="<%= userBean.getAssignments().get(i).getSpecification() %>" target="_blank">Details</a></td>
								</tr>
								<tr>
									<td>Submission:</td>
									<% if (userBean.getAssignments().get(i).getSubmitRecord().size() == 0) { %>
									<td style="color: red">No submission record&nbsp;&nbsp;&nbsp;&nbsp;</td>
									<% } %>
									<% if (userBean.getUser().getUserType().equals("student")) { %>
									<td>
										<button class="submit" title="Submit a project" style="background: url('images/submit-icon.png') center no-repeat; height: 20px; width: 20px" onclick="submit_a_project(<%= i %>)"></button>
									</td>
									<% } %>
								</tr>
							</table>
							<% if (userBean.getAssignments().get(i).getSubmitRecord().size() > 0) { %>
							<table class="submission" style="font: 15px/24px Arial, Helvetica, sans-serif" border="1">
								<tr>
									<td width="100px">&nbsp;Student</td>
									<% if (userBean.getUser().getUserType().equals("teacher")) { %>
									<td width="100px">&nbsp;Project</td>
									<% } %>
									<td width="100px">&nbsp;Mark</td>
									<td>&nbsp;Last Submission</td>
								</tr>
								<% if (userBean.getUser().getUserType().equals("teacher")) { %>
									<% for (int j = 0; j < userBean.getAssignments().get(i).getSubmitRecord().size(); ++j) { %>
									<tr>
										<td>&nbsp;<%= userBean.getAssignments().get(i).getSubmitRecord().get(j).getStuName() %></td>
										<td>&nbsp;<a href="online-BPEL-IDE.jsp?submission_id=<%= userBean.getAssignments().get(i).getSubmitRecord().get(j).getProjID() %>&project_name=<%= userBean.getAssignments().get(i).getSubmitRecord().get(j).getSubmitName() %>" target="_blank">Details</a></td>
										<td>
										<% if (userBean.getAssignments().get(i).getSubmitRecord().get(j).getMark() < 0) { %>
											<input id="mark-<%= i %>-<%= j %>" class="mark" style="width: 67px" type="text" value="-" onFocus="if(value=defaultValue){value='';this.style.color='#000'}" onBlur="if(!value){value=defaultValue;this.style.color='#000'}" onKeyUp="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onKeyDown="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}">
										<% } else { %>
											<input id="mark-<%= i %>-<%= j %>" class="mark" style="width: 67px" type="text" value="<%= (int) userBean.getAssignments().get(i).getSubmitRecord().get(j).getMark() %>" onFocus="if(value=defaultValue){value='';this.style.color='#000'}" onBlur="if(!value){value=defaultValue;this.style.color='#000'}" onKeyUp="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onKeyDown="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}">
										<% } %>
											<span><button title="Update mark" style="background: url('images/mark-icon.png') center no-repeat; height: 25px; width: 25px" onclick="update_mark(<%= i %>, <%= j %>)"></button></span>
										</td>
										<td>&nbsp;<%= userBean.getAssignments().get(i).getSubmitRecord().get(j).getSubmitTime()%>&nbsp;</td>
									</tr>
									<% } %>
								<% } else { %>
									<% for (int j = 0; j < userBean.getAssignments().get(i).getSubmitRecord().size(); ++j) { %>
									<tr>
										<td>&nbsp;<%= userBean.getAssignments().get(i).getSubmitRecord().get(j).getStuName() %></td>
										<td>
										<% if (userBean.getAssignments().get(i).getSubmitRecord().get(j).getMark() < 0) { %>
											&nbsp;-
										<% } else { %>
											&nbsp;<%= (int) userBean.getAssignments().get(i).getSubmitRecord().get(j).getMark() %>
										<% } %>
										</td>
										<td>&nbsp;<%= userBean.getAssignments().get(i).getSubmitRecord().get(j).getSubmitTime()%>&nbsp;</td>
									</tr>
									<% } %>
								<% } %>
							</table>
							<% } %>
						</div>
						<% } %>
					</div>
				</div>
			</div>
		</div>
		<div id="create_a_new_project_dialog">
			<br/>
			<form>
				<table>
					<tr>
						<td>Project Name&nbsp;:</td>
						<td>
							<input id="new_project_name" style="width: 370px" class="text ui-widget-content ui-corner-all" type="text" name="new_project_name" value="" onkeyup="value=value.replace(/[\W]/g,'')" onkeydown="return create_a_new_file_dialog_on_key_down(event.keyCode);"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div id="create_a_new_project_no_name_dialog">
			Please enter a project name !
		</div>
		<div id="create_a_new_project_existed_dialog">
			Project already existed !
		</div>
		<div id="remove_a_project_dialog"></div>
		<div id="remove_an_assignment_dialog"></div>
		<div id="create_a_new_assignment_dialog">
			<br/>
			<form>
				<table>
					<tr>
						<td>Assignment Name&nbsp;:</td>
						<td>
							<input id="new_assignment_name" type="text" style="width: 335px" class="text ui-widget-content ui-corner-all" name="new_assignment_name" onkeyup="value=value.replace(/[\W]/g,'')" onkeydown="return create_a_new_file_dialog_on_key_down(event.keyCode);"/>
						</td>
					</tr>
					<tr>
						<td>Deadline&nbsp;:</td>
						<td>
							<input id="new_assignment_deadline" type="text" style="width: 335px" name="new_assignment_deadline"/>
						</td>
					</tr>
					<tr>
						<td>Specification&nbsp;:</td>
						<td>
							<input id="new_assignment_specification" type="text" title="Please enter a specification url address http://www.bpel.com.au/example.pdf" style="width: 335px" name="new_assignment_specification" onkeyup="value=value.replace(' ','')"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div id="create_a_new_assignment_no_name_dialog">
			Please enter an assignment name !
		</div>
		<div id="create_a_new_assignment_no_deadline_dialog">
			Please enter an assignment deadline !
		</div>
		<div id="create_a_new_assignment_no_specification_dialog">
			Please upload an assignment specification !
		</div>
		<div id="create_a_new_assignment_existed_dialog">
			Assignment already existed !
		</div>
		<div id="submit_a_project_dialog">
			<p>Please select a project to upload</p>
			<div id="submit_a_project_dialog_list">
				<% for (int i = 0; i < userBean.getUser().getUserProjects().size(); ++i) { %>
					<p id="sid-<%= i %>" onclick="submit_a_project_dialog_list_select(this.id)">
						<img src="images/project-icon.png">&nbsp;
						<span class="name"><%= userBean.getUser().getUserProjects().get(i).getProjName() %></span>
						<span class="number" style="display: none"><%= i %></span>
					</p>
				<% } %>
	        </div>
		</div>
		<div id="submit_a_project_no_project_dialog">
			Please select a project to submit !
		</div>
	</body>
</html>