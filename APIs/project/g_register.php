<?php 

	define('DB_HOST', 'localhost');
	define('DB_USER', 'root');
	define('DB_PASS', '');
	define('DB_NAME', 'project');
	
	$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
	
	
	if (mysqli_connect_errno()) 
	{
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
		die();
	}
	
	$stmt = $conn->prepare("SELECT unique_id, user_id, full_name, contact_no, password, user_type, responsible, submission, updated_at FROM register;");
	
	$stmt->execute();
	
	//binding results to the query 
	$stmt->bind_result($unique_id, $user_id, $full_name, $contact_no, $password, $user_type, $responsible, $submission, $updated_at);
	
	$products = array(); 
	
	//traversing through all the result 
	while($stmt->fetch()){
		$temp = array();
		$temp['unique_id'] = $unique_id; 
		$temp['user_id'] = $user_id; 
		$temp['full_name'] = $full_name; 
		$temp['contact_no'] = $contact_no; 
		$temp['password'] = $password; 
		$temp['user_type'] = $user_type; 
		$temp['responsible'] = $responsible; 
		$temp['submission'] = $submission; 
		$temp['updated_at'] = $updated_at; 
		array_push($products, $temp);
	}
	
	//displaying the result in json format 
	echo json_encode($products);