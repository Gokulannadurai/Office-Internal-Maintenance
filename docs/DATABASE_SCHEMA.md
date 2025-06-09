# Database Schema Documentation

## Overview
This document outlines the database schema for the Office Internal Maintenance Management System. The schema is designed to support all core functionalities while maintaining data integrity and optimizing query performance.

## Table Definitions

### 1. Users
Stores information about all system users (employees, support staff, and admins).

| Column Name | Data Type | Constraints | Description |
|-------------|-----------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for the user |
| name | VARCHAR(100) | NOT NULL | User's full name |
| email | VARCHAR(255) | NOT NULL, UNIQUE | User's email address |
| password_hash | VARCHAR(255) | NOT NULL | Hashed password |
| role | ENUM('EMPLOYEE', 'SUPPORT_STAFF', 'ADMIN') | NOT NULL | User's role in the system |
| department | VARCHAR(100) | NOT NULL | User's department |
| phone | VARCHAR(20) | | Contact phone number |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Record update timestamp |
| is_active | BOOLEAN | NOT NULL, DEFAULT TRUE | User account status |

**Indexes:**
- PRIMARY KEY (id)
- UNIQUE INDEX (email)
- INDEX (role)
- INDEX (department)

### 2. MaintenanceRequests
Stores all maintenance requests submitted by employees.

| Column Name | Data Type | Constraints | Description |
|-------------|-----------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for the request |
| title | VARCHAR(200) | NOT NULL | Request title |
| description | TEXT | NOT NULL | Detailed description of the issue |
| category | ENUM('AC', 'PLUMBING', 'ELECTRICAL', 'HVAC', 'GENERAL') | NOT NULL | Request category |
| priority | ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') | NOT NULL | Request priority level |
| status | ENUM('PENDING', 'ACKNOWLEDGED', 'IN_PROGRESS', 'RESOLVED', 'CLOSED') | NOT NULL, DEFAULT 'PENDING' | Current status of the request |
| location | VARCHAR(255) | NOT NULL | Location of the issue |
| created_by | BIGINT | NOT NULL, FOREIGN KEY | ID of the user who created the request |
| assigned_to | BIGINT | FOREIGN KEY | ID of the support staff assigned to the request |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Request creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Request update timestamp |
| resolved_at | TIMESTAMP | | Timestamp when the request was resolved |

**Indexes:**
- PRIMARY KEY (id)
- INDEX (created_by)
- INDEX (assigned_to)
- INDEX (status)
- INDEX (priority)
- INDEX (category)
- INDEX (created_at)

### 3. Comments
Stores comments/updates on maintenance requests.

| Column Name | Data Type | Constraints | Description |
|-------------|-----------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for the comment |
| request_id | BIGINT | NOT NULL, FOREIGN KEY | ID of the associated maintenance request |
| user_id | BIGINT | NOT NULL, FOREIGN KEY | ID of the user who made the comment |
| content | TEXT | NOT NULL | Comment content |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Comment creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Comment update timestamp |

**Indexes:**
- PRIMARY KEY (id)
- INDEX (request_id)
- INDEX (user_id)
- INDEX (created_at)

### 4. Attachments
Stores files attached to maintenance requests.

| Column Name | Data Type | Constraints | Description |
|-------------|-----------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for the attachment |
| request_id | BIGINT | NOT NULL, FOREIGN KEY | ID of the associated maintenance request |
| file_name | VARCHAR(255) | NOT NULL | Original file name |
| file_path | VARCHAR(255) | NOT NULL | Path to stored file |
| file_type | VARCHAR(100) | NOT NULL | MIME type of the file |
| file_size | BIGINT | NOT NULL | File size in bytes |
| uploaded_by | BIGINT | NOT NULL, FOREIGN KEY | ID of the user who uploaded the file |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Upload timestamp |

**Indexes:**
- PRIMARY KEY (id)
- INDEX (request_id)
- INDEX (uploaded_by)
- INDEX (created_at)

## Relationships

1. **Users to MaintenanceRequests**
   - One-to-Many: A user can create multiple maintenance requests
   - One-to-Many: A support staff can be assigned to multiple maintenance requests

2. **MaintenanceRequests to Comments**
   - One-to-Many: A maintenance request can have multiple comments

3. **MaintenanceRequests to Attachments**
   - One-to-Many: A maintenance request can have multiple attachments

## SQL DDL Statements

```sql
-- Create Users table
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('EMPLOYEE', 'SUPPORT_STAFF', 'ADMIN') NOT NULL,
    department VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX idx_role (role),
    INDEX idx_department (department)
);

-- Create MaintenanceRequests table
CREATE TABLE maintenance_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    category ENUM('AC', 'PLUMBING', 'ELECTRICAL', 'HVAC', 'GENERAL') NOT NULL,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') NOT NULL,
    status ENUM('PENDING', 'ACKNOWLEDGED', 'IN_PROGRESS', 'RESOLVED', 'CLOSED') NOT NULL DEFAULT 'PENDING',
    location VARCHAR(255) NOT NULL,
    created_by BIGINT NOT NULL,
    assigned_to BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (assigned_to) REFERENCES users(id),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_category (category),
    INDEX idx_created_at (created_at)
);

-- Create Comments table
CREATE TABLE comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    request_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES maintenance_requests(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_request_id (request_id),
    INDEX idx_created_at (created_at)
);

-- Create Attachments table
CREATE TABLE attachments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    request_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    uploaded_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES maintenance_requests(id),
    FOREIGN KEY (uploaded_by) REFERENCES users(id),
    INDEX idx_request_id (request_id),
    INDEX idx_created_at (created_at)
);
```

## Additional Considerations

1. **Indexing Strategy**
   - Primary keys are automatically indexed
   - Foreign keys are indexed for better join performance
   - Frequently queried fields (status, priority, category) are indexed
   - Timestamp fields are indexed for efficient date-based queries

2. **Data Integrity**
   - Foreign key constraints ensure referential integrity
   - NOT NULL constraints on required fields
   - ENUM types for constrained choice fields
   - Default values for status and timestamps

3. **Performance Optimization**
   - Appropriate data types for each column
   - Indexed fields for common query patterns
   - Timestamp fields for tracking record changes
   - Efficient storage of file metadata

4. **Security Considerations**
   - Password hashing for user authentication
   - File path storage for attachments
   - Role-based access control through user roles 