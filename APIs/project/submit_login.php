<?php
    $con = mysqli_connect("localhost", "root", "", "project");
    
    $user_id = $_POST["user_id"];
    $password = $_POST["password"];

    $statement = mysqli_prepare($con, "SELECT * FROM register WHERE user_id = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $user_id, $password);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = false;  

	if(mysqli_stmt_execute($statement))
		$response["success"] = true;;
	
    echo json_encode($response);
?>
