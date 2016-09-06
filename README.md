# event-checkin
android app for event checkin

This is an android app I was working on in the summer of 2014. The idea here was to have an app for ticket handlers to use for checking customers into an event. It performs a few basic functions:

  0. Asks for login credentials. Note: What I have coded here is a great example of how not to write a login function.
  1. Searches for the ticket by inputting the code number. The intention here was to add in the ability to scan a barcode.
  2. Returns the customer info for the ticket
  3. Checks in the customer - if a customer buys 3 tickets, you can check their ticket in 3 times.
  4. Keeps a log of check-in transactions
  
The app works by making REST calls to php files that makes the SQL queries to read from or update the database.

This project was eventually abandoned when I stopped going to the events that this was to be used for... but it was a great project for learning about writing android apps and working with databases.

