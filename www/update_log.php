<?php

//this code will update the history log every time a ticket is checked in or un-checked in

$response = array();

if(isset ($_POST['checkedIn']) && isset($_POST['OrderNumber']) && isset($_POST['FullName']) && isset($_POST['EventName'])
&& isset($_POST['quantity']) && isset($_POST['Transaction']) && isset($_POST['type']) && isset($_POST['timestamp']) 
&& isset($_POST['notes']) && isset($_POST['user'])){
	$checkedIn = $_POST['checkedIn'];
	$orderNumber = $_POST['OrderNumber'];
	$fullName = $_POST['FullName'];
	$EventName = $_POST['EventName'];
	$quantity = $_POST['quantity'];
	$transaction = $_POST['Transaction'];
	$type = $_POST['type'];
	$timestamp = $_POST['timestamp'];
	$notes = $_POST['notes'];
	$user = $_POST['user'];
	
	//conect to db
	$con = mysqli_connect("localhost","root","","wordpress");
	
	#Check connection
		if(mysqli_connect_errno()){
			echo 'Database connection error: ' . mysqli_connect_error();
			exit();
		}
		
		$result = mysqli_query($con, "INSERT INTO history_log (checkedIn, orderNumber, fullName, EventName, quantity, transaction,
			type, timestamp, notes, user) VALUES ('$checkedIn', '$orderNumber', '$fullName', '$EventName',
			'$quantity', '$transaction', '$type', '$timestamp', '$notes', '$user')");
			
		if($result){
		$response["message"] = "History log updated";
		echo json_encode($response);
		}
		else{
		$response["message"] = "Something went wrong";
		echo json_encode($response);
		}

}
else{
//required field is missing
	$response["message"] = "Required field(s) is missing";
	echo json_encode($response);
	
}



?>