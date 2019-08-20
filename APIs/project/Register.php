<?php
    $con = mysqli_connect("localhost", "root", "", "project");
    
    $user_id = $_POST["user_id"];
    $full_name = $_POST["full_name"];
    $contact_no = $_POST["contact_no"];
    $password = $_POST["password"];
	$user_type = $_POST["user_type"];
	$responsible = $_POST["responsible"];
	

    $statement = mysqli_prepare($con, "INSERT INTO register (user_id, full_name, contact_no, password, user_type, responsible) VALUES (?, ?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "ssssss", $user_id, $full_name, $contact_no, $password,  $user_type, $responsible);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>
