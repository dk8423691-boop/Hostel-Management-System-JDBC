# Hostel Management System using Java JDBC and MySQL

## Project Overview
The Hostel Management System is a Java-based application developed using JDBC and MySQL. It helps manage student records, hostel details, room allocation, and hostel fee information.

## Technologies Used
- Java
- JDBC
- MySQL
- MySQL Connector/J
- Eclipse IDE

## Features
1. Insert student records into the database.
2. Display students based on hostel name and fee.
3. Update student fee using student ID.
4. Search for a student and update their room number.
5. Calculate the total hostel fee.
6. Delete a student record when the fee is greater than zero.

## Database Details
Database name: `hostel_db`

Table name: `HostelStudent`

Columns:
- `student_id`
- `student_name`
- `roll_number`
- `hostel_name`
- `room_number`
- `fee`

## Project Setup

### 1. Clone or download this repository
Download the project from GitHub and import it into Eclipse.

### 2. Create the database
Open MySQL Workbench and execute the SQL script available at:

`database/hostel_db.sql`

### 3. Configure database credentials
Open `hostelmanagementsystem.java` and configure your MySQL username and password. Do not publish real passwords in the repository.

### 4. Add MySQL Connector
The MySQL Connector/J JAR is included in the `lib` folder. Make sure it is added to the Java Build Path.

### 5. Run the application
Open `hostelmanagementsystem.java` in Eclipse and select:

Run As → Java Application

## Author
BTech Student

## Project Type
Java Database Connectivity (JDBC) Mini Project