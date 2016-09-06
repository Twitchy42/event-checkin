<?php 

    #Ensure that the client has provided a value for "Transaction" 
    if (isset($_POST["Transaction"]) && $_POST["Transaction"] != ""){ 
         
        #Setup variables 
        $transaction = $_POST["Transaction"]; 
         
        #Connect to Database 
        $con = mysqli_connect("localhost","root","", "wordpress"); 
         
        #Check connection 
        if (mysqli_connect_errno()) { 
            echo 'Database connection error: ' . mysqli_connect_error(); 
            exit(); 
        } 

        #Escape special characters to avoid SQL injection attacks 
        $transaction = mysqli_real_escape_string($con, $transaction); 
         
		#We'll want to change some things here - check first if our new table "tickets" exists
		#If it does not, create the new table using select query.
		#We'll concatenate first and last names in the new table into one, and add a new column "checkedIn"
		#Our android app will compare the quantity field with the checkedIn field to perform operations
		
		$val = mysqli_query($con,'select 1 from tickets');

		if($val == FALSE)
		{
			//does not exist!
			$blah = mysqli_query($con, "CREATE TABLE tickets (checkedIn INT not null) as 
			(SELECT purchase as orderNumber, CONCAT(firstname, ' ', lastname) as 
			fullName, name as EventName, quantity, txnid as transaction 
			FROM wp_shopp_purchase INNER JOIN wp_shopp_purchased 
			ON wp_shopp_purchase.id= wp_shopp_purchased.purchase");
		}
		else
		{
			//Table already exists. Do nothing.
		}

		
        #Query the database to get the user details. 
        $ticketdetails = mysqli_query($con, "SELECT checkedIn, orderNumber, fullName, eventName, quantity, transaction FROM tickets WHERE transaction=$transaction"); 

        #If no data was returned, check for any SQL errors 
        if (!$ticketdetails) { 
            echo 'Could not run query: ' . mysqli_error($con); 
            exit; 
        } 
		
	#if(mysql_num_rows($ticketdetails) > 0){
		
		$response["tickets"]=array();
		
		while($row = mysqli_fetch_array($ticketdetails)){
		#Get the first row of the results 
        
        #Build the result array (Assign keys to the values) 
        $result_data = array( 
            'CheckedIn' => $row[0], 
            'OrderNumber' => $row[1], 
            'FullName' => $row[2], 
            'EventName' => $row[3],
			'Quantity' => $row[4],
			'Transaction' => $row[5]
            ); 
		array_push($response["tickets"], $result_data);
        #Output the JSON data 
         
		
		}
		 echo json_encode($response);
    }
	
	#Ensure that the client has provided a value for "FullName" 
    else if (isset($_POST["FullName"]) && $_POST["FullName"] != ""){ 
         
        #Setup variables 
        $fullname = $_POST["FullName"]; 
         
        #Connect to Database 
        $con = mysqli_connect("localhost","root","", "wordpress"); 
         
        #Check connection 
        if (mysqli_connect_errno()) { 
            echo 'Database connection error: ' . mysqli_connect_error(); 
            exit(); 
        } 

        #Escape special characters to avoid SQL injection attacks 
        $fullname = mysqli_real_escape_string($con, $fullname); 
         
		#We'll want to change some things here - check first if our new table "tickets" exists
		#If it does not, create the new table using select query.
		#We'll concatenate first and last names in the new table into one, and add a new column "checkedIn"
		#Our android app will compare the quantity field with the checkedIn field to perform operations
		
		$val = mysqli_query($con,'select 1 from tickets');

		if($val == FALSE)
		{
			//does not exist!
			$blah = mysqli_query($con, "CREATE TABLE tickets (checkedIn INT not null) as 
			(SELECT purchase as orderNumber, CONCAT(firstname, ' ', lastname) as 
			fullName, name as EventName, quantity, txnid as transaction 
			FROM wp_shopp_purchase INNER JOIN wp_shopp_purchased 
			ON wp_shopp_purchase.id= wp_shopp_purchased.purchase");
		}
		else
		{
			//Table already exists. Do nothing.
		}

		
        #Query the database to get the user details. 
        $ticketdetails = mysqli_query($con, "SELECT checkedIn, orderNumber, fullName, eventName, quantity, transaction FROM tickets WHERE fullName LIKE '%$fullname%'"); 

        #If no data was returned, check for any SQL errors 
        if (!$ticketdetails) { 
            echo 'Could not run query: ' . mysqli_error($con); 
            exit; 
        } 
		
	#if(mysql_num_rows($ticketdetails) > 0){
		
		$response["tickets"]=array();
		
		while($row = mysqli_fetch_array($ticketdetails)){
		#Get the first row of the results 
        
        #Build the result array (Assign keys to the values) 
        $result_data = array( 
            'CheckedIn' => $row[0], 
            'OrderNumber' => $row[1], 
            'FullName' => $row[2], 
            'EventName' => $row[3],
			'Quantity' => $row[4],
			'Transaction' => $row[5]
            ); 
		array_push($response["tickets"], $result_data);
        #Output the JSON data 
         
		
		}
		 echo json_encode($response);
    }
	
	else{ 
        echo "Could not complete query. Missing parameter";  
    }

	
	
?>