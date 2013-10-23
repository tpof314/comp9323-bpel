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
  		<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  		<link rel="stylesheet" href="css/style.css" type="text/css" />
  		<title>Loading</title>
	</head>
	<body>
		<div id="project_list_area_projects" style="text-align: center">
			<br/>
			<img src="images/project_loading.gif">
		</div>
		<div id="list">
			<ul>
				<li><a href="#list_area">Project Explorer</a></li>
			</ul>
			<div id="list_area" style="text-align: center">
				<br/><br/><br/><br/><br/><br/>
				<img src="images/loading.gif">
			</div>
		</div>
	</body>
</html>