var is_project_modified = true;
var tab_number = 0;
var	tab_counter = 1;
$(function() {
	$(document).tooltip();
	$("#user_information button").button();
	$("#toolbar button").button();
	$("#project_list").tabs();
	$("#assignment_list_area_projects").accordion({
		heightStyle: "content"
	});
	$("#assignment_list_area_projects button").button();
	$("#new_assignment_deadline").datepicker();
	$("#list").tabs();
	$("#console_tabs").tabs();
	var	tabs = $("#editor_tabs").tabs();
	var create_a_new_file_dialog = $("#create_a_new_file_dialog").dialog({
		autoOpen: false,
		height: 496,
		width: 600,
		modal: true,
		resizable: false,
		title: "Create a new file",
		closeText: "Close",
		buttons: {
			Create : function() {
				if (file_exists($(this).data("number_of_files"))) {
					var new_file_name = $("#new_file_name");
					var new_file_type = $("#new_file_type");
					var label = new_file_name.val() || "untitled" + tab_counter;
					label += new_file_type.val();
					$("#overwrite_a_file_dialog").text("\"" + label + "\" already existed. Do you want to overwrite it?");
					$("#overwrite_a_file_dialog").data("file_name", label);
					$("#overwrite_a_file_dialog").dialog("open");
				}
				else {
					is_project_modified = true;
					create_a_new_file();
					$(this).dialog("close");
					$("#list_area").load("loading.jsp #list_area");
					$("#list_area").load("createFile?file_name=" + $(this).data("file_name") + " #list_area", function() {
						$("#create_a_new_file_dialog").load("online-BPEL-IDE.jsp #create_a_new_file_dialog");
						$("#execute_the_project_dialog").load("online-BPEL-IDE.jsp #execute_the_project_dialog");
					});
				}
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			reset();
			$("#toolbar button.new").tooltip().tooltip("close");
		}
	});
	$("#overwrite_a_file_dialog").dialog({
		autoOpen: false,
		height: 200,
		width: 520,
		modal: true,
		resizable: false,
		title: "Warning",
		closeText: "Close",
		buttons: {
			Overwrite : function() {
				var file_name = $(this).data("file_name");
				$("#editor_tabs ul li").each(function() {
					if ($(this).find("a").text() == file_name || ($(this).find("a").text()[0] == '*' && $(this).find("a").text().substring(1) == file_name)) {
						var panelId = $(this).remove().attr("aria-controls");
						$("#" + panelId).remove();
						tab_number--;
						tabs.tabs("refresh");
						close_a_tab();
					}
				});
				create_a_new_file();
				$(this).dialog("close");
				$("#create_a_new_file_dialog").dialog("close");
				var save_id = $("#editor_tabs").tabs("option", "active") + 1;
				$("#toolbar button.save").prop('disabled', false).button("refresh");
				$("#editor_tabs ul li:nth-child(" + save_id + ") a").text('*' + $("#editor_tabs ul li:nth-child(" + save_id + ") a").text());
				$("#toolbar button.save-all").prop('disabled', false).button("refresh");
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			reset();
		}
	});
	$("#remove_a_file_dialog").dialog({
		autoOpen: false,
		height: 200,
		width: 520,
		modal: true,
		resizable: false,
		title: "Remove a file",
		closeText: "Close",
		buttons: {
			Remove : function() {
				is_project_modified = true;
				var file_name = $(this).data("file_name");
				$("#editor_tabs ul li").each(function() {
					if ($(this).find("a").text() == file_name || ($(this).find("a").text()[0] == '*' && $(this).find("a").text().substring(1) == file_name)) {
						var panelId = $(this).remove().attr("aria-controls");
						$("#" + panelId).remove();
						tab_number--;
						tabs.tabs("refresh");
						close_a_tab();
					}
				});
				remove_a_file($(this).data("quantity"));
				$(this).dialog("close");
				$("#list_area").load("loading.jsp #list_area");
				$("#list_area").load("removeFile?is_in_project=" + $(this).data("is_in_project") + "&directory_number=" + $(this).data("directory_number") + "&file_number=" + $(this).data("file_number") + " #list_area", function() {
					$("#create_a_new_file_dialog").load("online-BPEL-IDE.jsp #create_a_new_file_dialog");
					$("#execute_the_project_dialog").load("online-BPEL-IDE.jsp #execute_the_project_dialog");
				});
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			$("#toolbar button.remove").tooltip().tooltip("close");
		}
	});
	$("#save_a_file_dialog").dialog({
		autoOpen: false,
		height: 200,
		width: 520,
		modal: true,
		resizable: false,
		title: "Save Resource",
		closeText: "Close",
		buttons: {
			Yes : function() {
				save_a_file($(this).data("li_id").attr("id"));
				var panelId = $(this).data("li_id").remove().attr("aria-controls");
				$("#" + panelId).remove();
				tab_number--;
				tabs.tabs("refresh");
				close_a_tab();
				$(this).dialog("close");
			},
			No : function() {
				var panelId = $(this).data("li_id").remove().attr("aria-controls");
				$("#" + panelId).remove();
				tab_number--;
				tabs.tabs("refresh");
				close_a_tab();
				$(this).dialog("close");
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		}
	});
	$("#upload_a_file_dialog").dialog({
		autoOpen: false,
		height: 200,
		width: 520,
		modal: true,
		resizable: false,
		title: "Upload a file",
		closeText: "Close",
		buttons: {
			Upload : function() {
				var file_exists = false;
				var extension = document.getElementById("upload_a_file_input").value;
				$("#list_area p").each(function() {
					if ($(this).find("span.name").text() == extension)
						file_exists = true;
				});
				if (file_exists)
					$("#upload_a_file_file_exists_dialog").dialog("open");
				else {
					extension = extension.substring(extension.lastIndexOf(".") + 1);
					if (extension == "bpel" || extension == "soap" || extension == "txt" || extension == "wsdl" || extension == "xml") {
						$("#list_area").load("loading.jsp #list_area");
						upload_a_file();
						$(this).dialog("close");
						sleep(3000);
						$("#list_area").load("online-BPEL-IDE.jsp #list_area");
						$("#create_a_new_file_dialog").load("online-BPEL-IDE.jsp #create_a_new_file_dialog");
						$("#execute_the_project_dialog").load("online-BPEL-IDE.jsp #execute_the_project_dialog");
					}
					else if (extension == "")
						$("#upload_a_file_no_file_dialog").dialog("open");
					else
						$("#upload_a_file_error_dialog").dialog("open");
				}
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			$("#upload_a_file")[0].reset();
			$("#toolbar button.upload").tooltip().tooltip("close");
		}
	});
	$("#upload_a_file_no_file_dialog").dialog({
		autoOpen: false,
		height: 150,
		width: 300,
		modal: true,
		resizable: false,
		title: "Error",
		closeText: "Close",
		buttons: {
			OK : function() {
				$(this).dialog("close");
			}
		}
	});
	$("#upload_a_file_file_exists_dialog").dialog({
		autoOpen: false,
		height: 150,
		width: 300,
		modal: true,
		resizable: false,
		title: "Error",
		closeText: "Close",
		buttons: {
			OK : function() {
				$(this).dialog("close");
			}
		}
	});
	$("#upload_a_file_error_dialog").dialog({
		autoOpen: false,
		height: 150,
		width: 600,
		modal: true,
		resizable: false,
		title: "Error",
		closeText: "Close",
		buttons: {
			OK : function() {
				$(this).dialog("close");
			}
		}
	});
	$("#compile_the_project_save_dialog").dialog({
		autoOpen: false,
		height: 200,
		width: 520,
		modal: true,
		resizable: false,
		title: "Save Resource",
		closeText: "Close",
		buttons: {
			"Save and compile" : function() {
				$(this).dialog("close");
				$("#toolbar button.save-all").tooltip().tooltip("close");
				$("#toolbar button.save").prop('disabled', true).button("refresh");
				$("#toolbar button.save-all").prop('disabled', true).button("refresh");
				for (var i = 0; i < tab_counter; i++) {
					if ($("#li-" + i + " a").text()[0] == '*') {
						save_a_file("li-" + i);
						$("#li-" + i + " a").text($("#li-" + i + " a").text().substring(1));
					}
				}
				$("#log p").text("Please wait ...");
				$("#log_and_result").load("compileProject?is_project_modified=" + is_project_modified + " #log_and_result div.load");
				is_project_modified = false;
				$("#console_tabs").tabs("option", "active", 0);
				return false;
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			$("#toolbar button.compile").tooltip().tooltip("close");
		}
	});
	$("#execute_the_project_save_dialog").dialog({
		autoOpen: false,
		height: 200,
		width: 520,
		modal: true,
		resizable: false,
		title: "Save Resource",
		closeText: "Close",
		buttons: {
			"Save and execute" : function() {
				$(this).dialog("close");
				$("#toolbar button.save-all").tooltip().tooltip("close");
				$("#toolbar button.save").prop('disabled', true).button("refresh");
				$("#toolbar button.save-all").prop('disabled', true).button("refresh");
				for (var i = 0; i < tab_counter; i++) {
					if ($("#li-" + i + " a").text()[0] == '*') {
						save_a_file("li-" + i);
						$("#li-" + i + " a").text($("#li-" + i + " a").text().substring(1));
					}
				}
				execute_the_project_dialog_reset();
				$("#execute_the_project_dialog").dialog("open");
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			$("#toolbar button.execute").tooltip().tooltip("close");
		}
	});
	$("#execute_the_project_dialog").dialog({
		autoOpen: false,
		height: 350,
		width: 600,
		modal: true,
		resizable: false,
		title: "Execute the project",
		closeText: "Close",
		buttons: {
			Execute : function() {
				var file_name = $("#execute_the_project_dialog_files-0").find("span.name").text();
				var service = $("#execute_the_project_dialog_services-0").find("span.name").text();
				$("#execute_the_project_dialog_soap p").each(function() {
					if ($(this).attr("style") == "background: lightblue")
						file_name = $(this).find("span.name").text();
				});
				$("#execute_the_project_dialog_service p").each(function() {
					if ($(this).attr("style") == "background: lightblue")
						service = $(this).find("span.name").text();
				});
				$(this).dialog("close");
				$("#log p").text("Please wait ...");
				$("#result p").text("Please wait ...");
				$("#log_and_result").load("executeProject?file_name=" + file_name + "&service=" + service + "&is_project_modified=" + is_project_modified + " #log_and_result div.load");
				is_project_modified = false;
				$("#console_tabs").tabs("option", "active", 0);
				return false;
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			$("#toolbar button.execute").tooltip().tooltip("close");
		}
	});
	var create_a_new_project_dialog = $("#create_a_new_project_dialog").dialog({
		autoOpen: false,
		height: 210,
		width: 520,
		modal: true,
		resizable: false,
		title: "Create a new project",
		closeText: "Close",
		buttons: {
			Create : function() {
				if ($("#new_project_name").val() == "")
					$("#create_a_new_project_no_name_dialog").dialog("open");
				else {
					var project_exists = false;
					$("#project_list_area_projects div").each(function() {
						if ($(this).find("div.project_information").find("p.project_name").find("span.name").text() == $("#new_project_name").val())
							project_exists = true;
					});
					if (project_exists)
						$("#create_a_new_project_existed_dialog").dialog("open");
					else {
						$("#project_list_area_projects").load("loading.jsp #project_list_area_projects");
						$("#project_list_area_projects").load("createProject?project_name=" + $("#new_project_name").val() + " #project_list_area_projects");
						$("#submit_a_project_dialog").load("home.jsp #submit_a_project_dialog");
						$(this).dialog("close");
					}
				}
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			project_reset();
			$("#project_list_area div.align_right button.new").tooltip().tooltip("close");
		}
	});
	$("#create_a_new_project_no_name_dialog").dialog({
		autoOpen: false,
		height: 150,
		width: 300,
		modal: true,
		resizable: false,
		title: "Error",
		closeText: "Close",
		buttons: {
			OK : function() {
				$(this).dialog("close");
			}
		}
	});
	$("#create_a_new_project_existed_dialog").dialog({
		autoOpen: false,
		height: 150,
		width: 300,
		modal: true,
		resizable: false,
		title: "Error",
		closeText: "Close",
		buttons: {
			OK : function() {
				$(this).dialog("close");
			}
		}
	});
	$("#remove_a_project_dialog").dialog({
		autoOpen: false,
		height: 200,
		width: 480,
		modal: true,
		resizable: false,
		title: "Remove a project",
		closeText: "Close",
		buttons: {
			Remove : function() {
				$(this).dialog("close");
				$("#project_list_area_projects").load("loading.jsp #project_list_area_projects");
				$("#project_list_area_projects").load("removeProject?id=" + $(this).data("id") + " #project_list_area_projects");
				$("#submit_a_project_dialog").load("home.jsp #submit_a_project_dialog");
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		}
	});
	var create_a_new_assignment_dialog = $("#create_a_new_assignment_dialog").dialog({
		autoOpen: false,
		height: 260,
		width: 520,
		modal: true,
		resizable: false,
		title: "Create a new assignment",
		closeText: "Close",
		buttons: {
			Create : function() {
				if ($("#new_assignment_name").val() == "")
					$("#create_a_new_assignment_no_name_dialog").dialog("open");
				else {
					var assignment_exists = false;
					$("#assignment_list_area_projects h3").each(function() {
						if ($(this).find("div").find("span.name").text() == $("#new_assignment_name").val())
							assignment_exists = true;
					});
					if (assignment_exists)
						$("#create_a_new_assignment_existed_dialog").dialog("open");
					else if ($("#new_assignment_deadline").val() == "")
						$("#create_a_new_assignment_no_deadline_dialog").dialog("open");
					else if ($("#new_assignment_specification").val() == "")
						$("#create_a_new_assignment_no_specification_dialog").dialog("open");
					else {
						$("#assignment_list_area_projects").load("loading.jsp #project_list_area_projects");
						$(this).dialog("close");
						$("#assignment_list_area_project").load("createAssignment?new_assignment_name=" + $("#new_assignment_name").val() + "&new_assignment_deadline=" + $("#new_assignment_deadline").val() + "&new_assignment_specification=" + $("#new_assignment_specification").val() + " #assignment_list_area_projects", function() {
							$("#assignment_list_area_projects").accordion({
								heightStyle: "content"
							});
							$("#assignment_list_area_projects button").button();
						});
					}
				}
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			assignment_reset();
			$("#assignment_list_area div.align_right button.new").tooltip().tooltip("close");
		}
	});
	$("#create_a_new_assignment_no_name_dialog").dialog({
		autoOpen: false,
		height: 150,
		width: 300,
		modal: true,
		resizable: false,
		title: "Error",
		closeText: "Close",
		buttons: {
			OK : function() {
				$(this).dialog("close");
			}
		}
	});
	$("#create_a_new_assignment_no_deadline_dialog").dialog({
		autoOpen: false,
		height: 150,
		width: 350,
		modal: true,
		resizable: false,
		title: "Error",
		closeText: "Close",
		buttons: {
			OK : function() {
				$(this).dialog("close");
			}
		}
	});
	$("#create_a_new_assignment_no_specification_dialog").dialog({
		autoOpen: false,
		height: 150,
		width: 400,
		modal: true,
		resizable: false,
		title: "Error",
		closeText: "Close",
		buttons: {
			OK : function() {
				$(this).dialog("close");
			}
		}
	});
	$("#create_a_new_assignment_existed_dialog").dialog({
		autoOpen: false,
		height: 150,
		width: 300,
		modal: true,
		resizable: false,
		title: "Error",
		closeText: "Close",
		buttons: {
			OK : function() {
				$(this).dialog("close");
			}
		}
	});
	$("#remove_an_assignment_dialog").dialog({
		autoOpen: false,
		height: 200,
		width: 520,
		modal: true,
		resizable: false,
		title: "Remove a assignment",
		closeText: "Close",
		buttons: {
			Remove : function() {
				$(this).dialog("close");
				$("#assignment_list_area_projects").load("loading.jsp #project_list_area_projects");
				$("#assignment_list_area_project").load("removeAssignment?assignment_id=" + $(this).data("assignment_id") + " #assignment_list_area_projects", function() {
					$("#assignment_list_area_projects").accordion({
						heightStyle: "content"
					});
					$("#assignment_list_area_projects button").button();
				});
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			$("#assignment_list_area_projects button.remove").tooltip().tooltip("close");
		}
	});
	$("#submit_a_project_dialog").dialog({
		autoOpen: false,
		height: 350,
		width: 500,
		modal: true,
		resizable: false,
		title: "Submit a project",
		closeText: "Close",
		buttons: {
			Submit : function() {
				var project_id = "null";
				$("#submit_a_project_dialog_list p").each(function() {
					if ($(this).attr("style") == "background: lightblue")
						project_id = $(this).find("span.number").text();
				});
				if (project_id == "null")
					$("#submit_a_project_no_project_dialog").dialog("open");
				else {
					$("#aid_div-" + $(this).data("assignment_id")).load("loading.jsp #mark");
					$("#aid_div-" + $(this).data("assignment_id")).load("submitProject?assignment_id=" + $(this).data("assignment_id") + "&project_id=" + project_id + " #aid_div-" + $(this).data("assignment_id"), function() {
						$("#assignment_list_area_projects button").button();
					});
					$(this).dialog("close");
				}
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			$("#submit_a_project_dialog_list p").each(function() {
				$(this).attr("style", "background: white");
			});
			$("#assignment_list_area_projects button.submit").tooltip().tooltip("close");
		}
	});
	$("#submit_a_project_no_project_dialog").dialog({
		autoOpen: false,
		height: 150,
		width: 300,
		modal: true,
		resizable: false,
		title: "Error",
		closeText: "Close",
		buttons: {
			OK : function() {
				$(this).dialog("close");
			}
		}
	});
	var form = create_a_new_file_dialog.find("form").submit(function(event) {
		create_a_new_file_dialog.dialog("close");
		event.preventDefault();
	});
	var project_form = create_a_new_project_dialog.find("form").submit(function(event) {
		create_a_new_project_dialog.dialog("close");
		event.preventDefault();
	});
	var assignment_form = create_a_new_assignment_dialog.find("form").submit(function(event) {
		create_a_new_assignment_dialog.dialog("close");
		event.preventDefault();
	});
	function reset() {
		form[0].reset();
	}
	function project_reset() {
		project_form[0].reset();
	}
	function assignment_reset() {
		assignment_form[0].reset();
	}
	$("#toolbar button.new").click(function() {
		create_a_new_file_dialog_reset();
		$("#create_a_new_file_dialog").data("number_of_files", $("#list_area div.hide-information span.number_of_files").text()).dialog("open");
	});
	$("#toolbar button.remove").click(function() {
		var quantity = $("#list_area div.hide-information span.quantity").text();
		for (var i = 0; i < quantity; ++i)
			if (document.getElementById("list-p-" + i.toString()).style.background.contains("lightblue")) {
				$("#remove_a_file_dialog").text("Do you really want to remove \"" + $("#list-p-" + i.toString() + " span.name").text() + "\" ?");
				$("#remove_a_file_dialog").data("file_name", $("#list-p-" + i.toString() + " span.name").text());
				$("#remove_a_file_dialog").data("is_in_project", $("#list-p-" + i.toString() + " span.is_in_project").text());
				$("#remove_a_file_dialog").data("directory_number", $("#list-p-" + i.toString() + " span.directory_number").text());
				$("#remove_a_file_dialog").data("file_number", $("#list-p-" + i.toString() + " span.file_number").text());
				$("#remove_a_file_dialog").data("quantity", quantity).dialog("open");
				break;
			}
	});
	$("#toolbar button.save").click(function() {
		var save_id = $("#editor_tabs").tabs("option", "active") + 1;
		var save_all_flag = false;
		save_a_file($("#editor_tabs ul li:nth-child(" + save_id + ")").attr("id"));
		$("#toolbar button.save").tooltip().tooltip("close");
		$("#toolbar button.save").prop('disabled', true).button("refresh");
		$("#editor_tabs ul li:nth-child(" + save_id + ") a").text($("#editor_tabs ul li:nth-child(" + save_id + ") a").text().substring(1));
		for (var i = 0; i < tab_counter; i++)
			if ($("#li-" + i + " a").text()[0] == '*')
				save_all_flag = true;
		if (!save_all_flag)
			$("#toolbar button.save-all").prop('disabled', true).button("refresh");
	});
	$("#toolbar button.save-all").click(function() {
		$("#toolbar button.save-all").tooltip().tooltip("close");
		$("#toolbar button.save").prop('disabled', true).button("refresh");
		$("#toolbar button.save-all").prop('disabled', true).button("refresh");
		for (var i = 0; i < tab_counter; i++) {
			if ($("#li-" + i + " a").text()[0] == '*') {
				save_a_file("li-" + i);
				$("#li-" + i + " a").text($("#li-" + i + " a").text().substring(1));
			}
		}
	});
	$("#toolbar button.upload").click(function() {
		$("#upload_a_file_dialog").dialog("open");
	});
	$("#toolbar button.compile").click(function() {
		if ($("#toolbar button.save-all").attr("aria-disabled") == "true") {
			$("#log p").text("Please wait ...");
			$("#log_and_result").load("compileProject?is_project_modified=" + is_project_modified + " #log_and_result div.load");
			is_project_modified = false;
			$("#console_tabs").tabs("option", "active", 0);
			return false;
		}
		else {
			$("#compile_the_project_save_dialog").text("Some files have been modified. Save all changes and compile?");
			$("#compile_the_project_save_dialog").dialog("open");
		}
	});
	$("#toolbar button.execute").click(function() {
		if ($("#toolbar button.save-all").attr("aria-disabled") == "true") {
			execute_the_project_dialog_reset();
			$("#execute_the_project_dialog").dialog("open");
		}
		else {
			$("#execute_the_project_save_dialog").text("Some files have been modified. Save all changes and execute?");
			$("#execute_the_project_save_dialog").dialog("open");
		}
	});
	tabs.delegate("span.ui-icon-close", "click", function() {
		if ($(this).closest("li").find("a").text()[0] == '*') {
			$("#save_a_file_dialog").text("\"" + $(this).closest("li").find("a").text().substring(1) + "\" has been modified. Save changes?");
			$("#save_a_file_dialog").data("li_id", $(this).closest("li")).dialog("open");
		}
		else {
			var panelId = $(this).closest("li").remove().attr("aria-controls");
			$("#" + panelId).remove();
			tab_number--;
			tabs.tabs("refresh");
			close_a_tab();
		}
	});
	$("#project_list_area div.align_right button.new").click(function() {
		project_reset();
		$("#create_a_new_project_dialog").dialog("open");
	});
	$("#assignment_list_area div.align_right button.new").click(function() {
		assignment_reset();
		$("#create_a_new_assignment_dialog").dialog("open");
	});
});
function toolbar_reset_button() {
	$("#toolbar button.remove").prop('disabled', true).button("refresh");
	$("#toolbar button.save").prop('disabled', true).button("refresh");
	$("#toolbar button.save-all").prop('disabled', true).button("refresh");
}
function project_reset_action() {
	$("#project_list_area div.project").each(function() {
		$(this).find("div.project_action").find("p.action").find("select").prop('selectedIndex',0);
	});
}
function select_a_tab(id) {
	if ($("#" + id + " a").text()[0] == '*')
		$("#toolbar button.save").prop('disabled', false).button("refresh");
	else
		$("#toolbar button.save").prop('disabled', true).button("refresh");
}
function close_a_tab() {
	var next_id = $("#editor_tabs").tabs("option", "active") + 1;
	var save_all_flag = false;
	if ($("#editor_tabs ul li:nth-child(" + next_id + ") a").text()[0] == '*')
		$("#toolbar button.save").prop('disabled', false).button("refresh");
	else
		$("#toolbar button.save").prop('disabled', true).button("refresh");
	for (var i = 0; i < tab_counter; i++)
		if ($("#li-" + i + " a").text()[0] == '*')
			save_all_flag = true;
	if (!save_all_flag)
		$("#toolbar button.save-all").prop('disabled', true).button("refresh");
}
function create_a_new_file() {
	var new_file_name = $("#new_file_name");
	var new_file_type = $("#new_file_type");
	var label = new_file_name.val() || "untitled" + tab_counter;
	label += new_file_type.val();
	var id = "tabs-" + tab_counter;
	var	aceEditorId = "aceEditor-" + tab_counter;
	var li_id = "li-" + tab_counter;
	var	tabTemplate = "<li id = '" + li_id + "'><a href='#{href}' onclick='select_a_tab(\"" + li_id + "\")'>#{label}</a><span class='ui-icon ui-icon-close' role='presentation' title='Close'></span></li>";
	var	li = $(tabTemplate.replace(/#\{href\}/g, "#" + id).replace(/#\{label\}/g, label));
	var	tabContentHtml = "&lt;!-- This is an empty file, please add some contents here. --&gt;\n";
	var	tabs = $("#editor_tabs").tabs();
	tabs.find(".ui-tabs-nav").append(li);
	tabs.append("<div id='" + id + "'>" + "<div id='" + aceEditorId + "'>" + tabContentHtml + "</div>" + "</div>");
	tabs.tabs("refresh");
	var editor = ace.edit(aceEditorId);
	editor.setTheme("ace/theme/eclipse");
	editor.getSession().setMode("ace/mode/xml");
	editor.on("change", function() {
		$('#' + li_id + " a").text('*' + label);
		$("#toolbar button.save").prop('disabled', false).button("refresh");
		$("#toolbar button.save-all").prop('disabled', false).button("refresh");
	});
	$("#" + aceEditorId).css({
		position: "absolute",
		top: "45px",
		right: "4px",
		bottom: "4px",
		left: "0"
	});
	$("#editor_tabs").tabs("option", "active", tab_number);
	$("#toolbar button.save").prop('disabled', true).button("refresh");
	tab_number++;
	tab_counter++;
	$("#create_a_new_file_dialog").data("file_name", label);
}
function file_exists(number_of_files) {
	var new_file_name = $("#new_file_name");
	var new_file_type = $("#new_file_type");
	var label = new_file_name.val() || "untitled" + tab_counter;
	label += new_file_type.val();
	for (var i = 0; i < number_of_files; i++) {
		if ($("#create_a_new_file_dialog_files-" + i.toString() + " span").text() == label)
			return true;
	}
	return false;
}
function open_a_file(file_name, content) {
	var tab_exists = false;
	var number = 0;
	$("#editor_tabs ul li").each(function() {
		if ($(this).find("a").text() == file_name) {
			$("#editor_tabs").tabs("option", "active", number);
			tab_exists = true;
		}
		number++;
	});
	if (!tab_exists) {
		var label = file_name;
		var id = "tabs-" + tab_counter;
		var	aceEditorId = "aceEditor-" + tab_counter;
		var li_id = "li-" + tab_counter;
		var	tabTemplate = "<li id = '" + li_id + "'><a href='#{href}' onclick='select_a_tab(\"" + li_id + "\")'>#{label}</a><span class='ui-icon ui-icon-close' role='presentation' title='Close'></span></li>";
		var	li = $(tabTemplate.replace(/#\{href\}/g, "#" + id).replace(/#\{label\}/g, label));
		var	tabs = $("#editor_tabs").tabs();
		tabs.find(".ui-tabs-nav").append(li);
		tabs.append("<div id='" + id + "'>" + "<div id='" + aceEditorId + "'></div>" + "</div>");
		tabs.tabs("refresh");
		var editor = ace.edit(aceEditorId);
		editor.insert(content.split("Single_Quatation_Mark").join("\'").split("Quatation_Mark").join("\"").split("New_Line").join("\n"));
		editor.setTheme("ace/theme/eclipse");
		editor.getSession().setMode("ace/mode/xml");
		editor.on("change", function() {
			$('#' + li_id + " a").text('*' + label);
			$("#toolbar button.save").prop('disabled', false).button("refresh");
			$("#toolbar button.save-all").prop('disabled', false).button("refresh");
		});
		$("#" + aceEditorId).css({
			position: "absolute",
			top: "45px",
			right: "4px",
			bottom: "4px",
			left: "0"
		});
		$("#editor_tabs").tabs("option", "active", tab_number);
		$("#toolbar button.save").prop('disabled', true).button("refresh");
		tab_number++;
		tab_counter++;
	}
}
function remove_a_file(quantity) {
	for (var i = 0; i < quantity; ++i)
		if (document.getElementById("list-p-" + i.toString()).style.background.contains("lightblue")) {
			document.getElementById("list-p-" + i.toString()).style.background = "white";
			break;
		}
	$("#toolbar button.remove").tooltip().tooltip("close");
	$("#toolbar button.remove").prop('disabled', true).button("refresh");
}
function save_a_file(li_id) {
	$("#list_area").load("loading.jsp #list_area");
	is_project_modified = true;
	var quantity = $("#list_area div.hide-information span.quantity").text();
	for (var i = 0; i < quantity; ++i) {
		if ($("#" + li_id + " a").text().substring(1) == $("#list-p-" + i.toString() + " span.name").text()) {
			var is_in_project = $("#list-p-" + i.toString() + " span.is_in_project").text();
			var directory_number = $("#list-p-" + i.toString() + " span.directory_number").text();
			var file_number = $("#list-p-" + i.toString() + " span.file_number").text();
			var aceEditorId = li_id.replace("li", "aceEditor");
			var editor = ace.edit(aceEditorId);
			var content = editor.getSession().getValue();
			$("#save_a_file-is_in_project").attr("value", is_in_project);
			$("#save_a_file-directory_number").attr("value", directory_number);
			$("#save_a_file-file_number").attr("value", file_number);
			$("#save_a_file-content").attr("value", content.split("\n").join("New_Line").split("\\").join("\\\\"));
			$("#save_a_file").on("submit", function(event) {
				$.ajax({
		            type: "post",
		            url: "saveFile",
		            data: $("#save_a_file").serialize()
		        });
				event.preventDefault();
			});
			$("#save_a_file").submit();
			$("#list_area").load("online-BPEL-IDE.jsp #list_area");
			$("#execute_the_project_dialog").load("online-BPEL-IDE.jsp #execute_the_project_dialog");
			break;
		}
	}
}
function upload_a_file() {
	$.ajaxFileUpload({
		url: "uploadFile",
		secureuri: false,
		fileElementId: "upload_a_file_input",
		dataType: "json",
		success: function (data) {
			$("#list_area").load("online-BPEL-IDE.jsp #list_area");
			$("#create_a_new_file_dialog").load("online-BPEL-IDE.jsp #create_a_new_file_dialog");
			$("#execute_the_project_dialog").load("online-BPEL-IDE.jsp #execute_the_project_dialog");
		}
	});
	return false;
}
function remove_a_project(id) {
	project_reset_action();
	$("#remove_a_project_dialog").text("Do you really want to remove \"" + $("#pid-" + id + " span.name").text() + "\" ?");
	$("#remove_a_project_dialog").data("id", id);
	$("#remove_a_project_dialog").dialog("open");
}
function create_an_assignment() {
	$.ajaxFileUpload({
		url: "createAssignment?new_assignment_name=" + $("#new_assignment_name").val() + "&new_assignment_deadline=" + $("#new_assignment_deadline").val(),
		secureuri: false,
		fileElementId: "new_assignment_specification",
		dataType: "json",
		success: function (data) {
			$("#assignment_list_area_project").load("home.jsp #assignment_list_area_projects", function() {
				$("#assignment_list_area_projects").accordion({
					heightStyle: "content"
				});
				$("#assignment_list_area_projects button").button();
			});
		}
	});
	return false;
}
function remove_an_assignment(assignment_id) {
	$("#remove_an_assignment_dialog").text("Do you really want to remove \"" + $("#aid-" + assignment_id + " span.name").text() + "\" ?");
	$("#remove_an_assignment_dialog").data("assignment_id", assignment_id);
	$("#remove_an_assignment_dialog").dialog("open");
}
function submit_a_project(assignment_id) {
	$("#submit_a_project_dialog").load("loading.jsp #submit_a_project_dialog", function() {
		$("#submit_a_project_dialog").load("home.jsp #submit_a_project_dialog");
	});
	$("#submit_a_project_dialog").data("assignment_id", assignment_id);
	$("#submit_a_project_dialog").dialog("open");
}
function update_mark(assignment_id, student_id) {
	var mark = $("#mark-" + assignment_id + "-" + student_id).val();
	$("#aid_div-" + assignment_id).load("loading.jsp #mark");
	$("#aid_div-" + assignment_id).load("updateMark?assignment_id=" + assignment_id + "&student_id=" + student_id + "&mark=" + mark + " #aid_div-" + assignment_id, function() {
		$("#assignment_list_area_projects button").button();
	});
}
function project_onmouseover(id) {
	var p = document.getElementById(id);
	p.style.background = "lightblue";
}
function project_onmouseout(id) {
	var p = document.getElementById(id);
	p.style.background = "white";
}
function list_folder_select(id, quantity) {
	var p = document.getElementById(id);
	p.style.background = (p.style.background.contains("lightblue")) ? "white" : "lightblue";
	$("#toolbar button.remove").prop('disabled', true).button("refresh");
	for (var i = 0; i < quantity; ++i)
		if (("list-p-" + i.toString()) != id) {
			var temp = document.getElementById("list-p-" + i.toString());
			temp.style.background = "white";
		}
}
function list_file_select(id, quantity) {
	var p = document.getElementById(id);
	p.style.background = (p.style.background.contains("lightblue")) ? "white" : "lightblue";
	if (p.style.background.contains("lightblue"))
		$("#toolbar button.remove").prop('disabled', false).button("refresh");
	else
		$("#toolbar button.remove").prop('disabled', true).button("refresh");
	for (var i = 0; i < quantity; ++i)
		if (("list-p-" + i.toString()) != id) {
			var temp = document.getElementById("list-p-" + i.toString());
			temp.style.background = "white";
		}
}
function list_hide(original_id, folder_icon_id, hide_id) {
	var original_img = document.getElementById(original_id);
	var folder_icon_img = document.getElementById(folder_icon_id);
	var hide_img = document.getElementById(hide_id);
	hide_img.style.display = (hide_img.style.display == "none") ? "block" : "none";
	if (original_img.src.contains("un")) {
		original_img.src = "images/fold-icon.png";
		folder_icon_img.src = "images/folder-icon-folded.png";
	}
	else {
		original_img.src = "images/unfold-icon.png";
		folder_icon_img.src="images/folder-icon-unfolded.png";
	}
}
function list_onmouseover(id) {
	var img = document.getElementById(id);
	if (img.src.contains("un"))
		img.src = "images/unfold-icon-onmouseover.png";
	else
		img.src = "images/fold-icon-onmouseover.png";
}
function list_onmouseout(id) {
	var img = document.getElementById(id);
	if (img.src.contains("un"))
		img.src = "images/unfold-icon.png";
	else
		img.src = "images/fold-icon.png";
}
function create_a_new_file_dialog_reset() {
	var number_of_folders = $("#list_area div.hide-information span.number_of_folders").text();
	var temp = document.getElementById("create_a_new_file_dialog_folders-0");
	temp.style.background = "lightblue";
	for (var i = 1; i < number_of_folders; ++i) {
		temp = document.getElementById("create_a_new_file_dialog_folders-" + i.toString());
		temp.style.background = "white";
	}
	temp = document.getElementById("create_a_new_file_dialog_files-did-0");
	temp.style.display = "block";
	for (var i = 1; i < number_of_folders; ++i) {
		temp = document.getElementById("create_a_new_file_dialog_files-did-" + i.toString());
		temp.style.display = "none";
	}
}
function create_a_new_file_dialog_folders_select(id, hide_id, number_of_folders) {
	var p = document.getElementById(id);
	p.style.background = "lightblue";
	for (var i = 0; i < number_of_folders; ++i)
		if (("create_a_new_file_dialog_folders-" + i.toString()) != id) {
			var temp = document.getElementById("create_a_new_file_dialog_folders-" + i.toString());
			temp.style.background = "white";
		}
	var hide = document.getElementById(hide_id);
	hide.style.display = "block";
	for (var i = 0; i < number_of_folders; ++i)
		if (("create_a_new_file_dialog_files-did-" + i.toString()) != hide_id) {
			var temp = document.getElementById("create_a_new_file_dialog_files-did-" + i.toString());
			temp.style.display = "none";
		}
}
function create_a_new_file_dialog_files_select(file_name) {
	var new_file_name = document.getElementById("new_file_name");
	var new_file_type = document.getElementById("new_file_type");
	new_file_name.value = file_name.substring(0, file_name.indexOf('.'));
	new_file_type.value = file_name.substring(file_name.indexOf('.'));
}
function create_a_new_file_dialog_files_onmouseover(id) {
	$("#" + id).attr("style", "background: lightblue");
}
function create_a_new_file_dialog_files_onmouseout(id) {
	$("#" + id).attr("style", "background: white");
}
function create_a_new_file_dialog_on_key_down(key_code) {
	if (key_code == "13")
		return false;
}
function execute_the_project_dialog_reset() {
	$("#execute_the_project_dialog_soap p").each(function() {
		$(this).attr("style", "background: white");
	});
	$("#execute_the_project_dialog_service p").each(function() {
		$(this).attr("style", "background: white");
	});
	$("#execute_the_project_dialog_files-0").attr("style", "background: lightblue");
	$("#execute_the_project_dialog_services-0").attr("style", "background: lightblue");
}
function execute_the_project_dialog_files_select(id) {
	$("#execute_the_project_dialog_soap p").each(function() {
		$(this).attr("style", "background: white");
	});
	$("#" + id).attr("style", "background: lightblue");
}
function execute_the_project_dialog_services_select(id) {
	$("#execute_the_project_dialog_service p").each(function() {
		$(this).attr("style", "background: white");
	});
	$("#" + id).attr("style", "background: lightblue");
}
function submit_a_project_dialog_list_select(id) {
	$("#submit_a_project_dialog_list p").each(function() {
		$(this).attr("style", "background: white");
	});
	$("#" + id).attr("style", "background: lightblue");
}
function ide_onload() {
	window.onbeforeunload = function() {
		$.get("saveProject");
		alert("The project has been saved automatically");
	};
}
function sleep(milliseconds) {
	var start = new Date().getTime();
	for (var i = 0; i < 1e7; i++) {
		if ((new Date().getTime() - start) > milliseconds) {
			break;
		}
	}
}