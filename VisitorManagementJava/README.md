# Visitor Management System (Java Swing + JDBC + MySQL)

A simple desktop visitor-management app for society guards.
Built with Java Swing UI and MySQL via JDBC.

## Features
- Guard login (stored in `guards` table)
- Add a visitor (check-in)
- View all visitors in a table
- Checkout a selected visitor
- Live stats (Total / Checked-In / Checked-Out)

## Project Structure
```
VisitorManagementJava/
├── database_setup.sql      # MySQL schema + default guard
├── README.md
└── src/
    ├── DBConnection.java   # JDBC connection helper
    ├── Visitor.java        # Model class
    ├── VisitorDAO.java     # All DB operations
    ├── LoginFrame.java     # Login window (entry point)
    └── DashboardFrame.java # Main dashboard window
```

## Prerequisites
- Java JDK 8 or higher
- MySQL Server running locally
- MySQL Connector/J (`mysql-connector-j-8.x.x.jar`)
  Download: https://dev.mysql.com/downloads/connector/j/

## Setup

### 1. Create the database
```bash
mysql -u root -p < database_setup.sql
```

### 2. Configure DB credentials
Edit `src/DBConnection.java` and update:
```java
private static final String USER = "root";
private static final String PASSWORD = "root";   // your MySQL password
```

### 3. Compile
From the project root:
```bash
javac -cp "lib/mysql-connector-j-8.0.33.jar" -d out src/*.java
```
(Place the connector jar inside a `lib/` folder, or adjust the path.)

### 4. Run
```bash
java -cp "out:lib/mysql-connector-j-8.0.33.jar" LoginFrame
```
On Windows use `;` instead of `:`:
```bash
java -cp "out;lib/mysql-connector-j-8.0.33.jar" LoginFrame
```

## Default Login
- **Username:** `guard`
- **Password:** `guard123`

## Notes
- This is a basic learning project — passwords are stored in plain text.
  For production, hash passwords (e.g. BCrypt) and use prepared statements (already done).
- The schema can be extended with extra fields (vehicle no., ID proof, photo, etc.).
