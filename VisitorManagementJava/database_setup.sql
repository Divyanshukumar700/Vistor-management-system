-- ============================================
-- Visitor Management System - Database Setup
-- ============================================
-- Run this script in MySQL before starting the app.
-- Example: mysql -u root -p < database_setup.sql

CREATE DATABASE IF NOT EXISTS visitor_db;
USE visitor_db;

-- Visitors table
CREATE TABLE IF NOT EXISTS visitors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    whom_to_visit VARCHAR(100) NOT NULL,
    purpose VARCHAR(200),
    check_in_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    check_out_time DATETIME DEFAULT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'checked-in'
);

-- Guards table (for login)
CREATE TABLE IF NOT EXISTS guards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL
);

-- Default guard account
INSERT IGNORE INTO guards (username, password, name)
VALUES ('guard', 'guard123', 'Security Guard');
