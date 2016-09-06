<?php

//this code will update checkedIn field in db for each product

$response = array();

//check for required fields
if(isset ($_POST['Transaction']) && isset($_POST['EventName']) && isset($_POST['quantity']) && isset($_POST['checkedIn'])){
	$transaction = $_POST['Transaction'];
	$eventName = $_POST['EventName'];
	$quantity = $_POST['quantity'];
	$checkedIn = $_POST['checkedIn'];
	
	//connect to db
	$con = mysqli_connect("localhost","root","", "wordpress");
	
	#Check connection 
        if (mysqli_connect_errno()) { 
            echo 'Database connection error: ' . mysqli_connect_error(); 
            exit(); 
        } 
		
		$result = mysqli_query($con, "UPDATE tickets SET checkedIn = '$checkedIn' 
		WHERE transaction = '$transaction' AND EventName = '$eventName' AND quantity = '$quantity'");
		
		if($result){
		$response["message"] = "Successfully Checked In";
		echo json_encode($response);
		}
		else{}
	}
else{
	//required field is missing
	$response["message"] = "Required field(s) is missing";
	
	echo json_encode($response);
}

?>
