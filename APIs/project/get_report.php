<?php 

	//database constants
	define('DB_HOST', 'localhost');
	define('DB_USER', 'root');
	define('DB_PASS', '');
	define('DB_NAME', 'project');
	
	//connecting to database and getting the connection object
	$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
	
	//Checking if any error occured while connecting
	if (mysqli_connect_errno()) {
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
		die();
	}
	
	//creating a query
	$stmt = $conn->prepare("SELECT report_id, user_id, responsible, submission, description FROM report;");
	
	//executing the query 
	$stmt->execute();
	
	//binding results to the query 
	$stmt->bind_result($report_id, $user_id, $responsible, $submission, $description);
	
	$products = array(); 
	
	//traversing through all the result 
	while($stmt->fetch()){
		$temp = array();
		$temp['report_id'] = $report_id; 
		$temp['user_id'] = $user_id; 
		$temp['responsible'] = $responsible; 
		$temp['submission'] = $submission; 
		$temp['description'] = $description; 
		array_push($products, $temp);
	}
	
	//displaying the result in json format 
	echo json_encode($products);