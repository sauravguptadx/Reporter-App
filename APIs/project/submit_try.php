<?php

 
 $con = mysqli_connect("localhost", "root", "", "project");
 
	$user_id = $_POST["user_id"];
    $password= $_POST["password"];

 $Sql_Query = "SELECT * FROM register WHERE user_id = ? AND password = ?";
 
 $response = array();
 
 $response["success"] = false;
 if(mysqli_query($con,$Sql_Query)){
 
 echo 'Data Submit Successfully';
 $response["success"] = true;
 
 }
 else
 {
 
 echo 'Try Again';
 $response["success"] = false;
 
 }
 echo json_encode($response);
 mysqli_close($con);
?>