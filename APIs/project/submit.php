<?php
    $con = mysqli_connect("localhost", "root", "", "project");
    
    $user_id = $_POST["user_id"];
    $responsible = $_POST["responsible"];
    $description = $_POST["description"];

    $statement = mysqli_prepare($con, "INSERT INTO report (user_id, responsible, description) VALUES (?, ?, ?)");
    mysqli_stmt_bind_param($statement, "sss", $user_id, $responsible, $description);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>
