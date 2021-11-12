# Employee Reimbursement System (ERS)

## Project Description

The Expense Reimbursement System (ERS) will manage the process of reimbursing employees for expenses incurred while on company time. All employees in the company can login and submit requests for reimbursement and view their past tickets and pending requests. Finance managers can log in and view all reimbursement requests and past history for all employees in the company. Finance managers are authorized to approve and deny requests for expense reimbursement.

## Technologies Used

* Java
* Hibernate
* Javalin
* HTML/JavaScript/CSS
* Logback
* JUnit
* AWS RDS
* AWS EC2
* DBeaver

## Features

List of features ready and TODOs for future development
* Multi-tier account logins
* View/Create reimbursement tickets as an Employee user
* View all and approve/deny tickets as a Finance Manager user

To-do list:
* Uploading image files to the database that can be retrieved and displayed on client view
* Static file hosting on AWS S3 bucket
* Account registration so account population does not have to occur in DBeaver

## Getting Started
   

* Clone project onto local machine using git clone https://github.com/210927-JavaFS/project1-tubigervie.git
* Configure connection in the hibernate.cfg.xml file within src to match your local RDS
* Run Driver.java class as Java application
* Navigate to the url: http:localhost:8081/login.html


## Usage
* Create users and employees to use in your RDS using DBeaver
* Login at the specified URL above to access employee and manager features
* Use application to create/review reimbursement requests
