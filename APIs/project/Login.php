<?php
    $con = mysqli_connect("localhost", "root", "", "project");
    
    $user_id = $_POST["user_id"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM register WHERE user_id = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $user_id, $password);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $unique_id, $user_id, $full_name, $contact_no, $password, $user_type, $responsible);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["unique_id"] = $unique_id;
        $response["user_id"] = $user_id;
		$response["full_name"] = $full_name;
		$response["contact_no"] = $contact_no;
		$response["password"] = $password;
        $response["user_type"] = $user_type;
		$response["responsible"] = $responsible;
    }
    
    echo json_encode($response);
?>
