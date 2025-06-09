# API Specification Documentation

## Base URL
```
https://api.maintenance-system.com/v1
```

## Authentication
All endpoints except `/auth/*` require JWT authentication.
Include the JWT token in the Authorization header:
```
Authorization: Bearer <jwt_token>
```

## Common Headers
```
Content-Type: application/json
Accept: application/json
```

## Common Error Responses

### 400 Bad Request
```json
{
    "error": "BAD_REQUEST",
    "message": "Invalid request parameters",
    "details": ["Field 'email' is required"]
}
```

### 401 Unauthorized
```json
{
    "error": "UNAUTHORIZED",
    "message": "Invalid or expired token"
}
```

### 403 Forbidden
```json
{
    "error": "FORBIDDEN",
    "message": "Insufficient permissions to access this resource"
}
```

### 404 Not Found
```json
{
    "error": "NOT_FOUND",
    "message": "Resource not found"
}
```

### 500 Internal Server Error
```json
{
    "error": "INTERNAL_SERVER_ERROR",
    "message": "An unexpected error occurred"
}
```

## API Endpoints

### Authentication Module

#### POST /auth/login
- **Description**: Authenticate user and get JWT token
- **Authentication**: None
- **Request Body**:
```json
{
    "email": "string",
    "password": "string"
}
```
- **Success Response**: 200 OK
```json
{
    "token": "string",
    "user": {
        "id": "number",
        "name": "string",
        "email": "string",
        "role": "string",
        "department": "string"
    }
}
```

#### POST /auth/refresh
- **Description**: Refresh JWT token
- **Authentication**: JWT Required
- **Success Response**: 200 OK
```json
{
    "token": "string"
}
```

### Users Module

#### GET /users
- **Description**: Get list of users
- **Authentication**: Admin Only
- **Query Parameters**:
  - `page`: number (default: 0)
  - `size`: number (default: 20)
  - `role`: string (optional)
  - `department`: string (optional)
- **Success Response**: 200 OK
```json
{
    "content": [
        {
            "id": "number",
            "name": "string",
            "email": "string",
            "role": "string",
            "department": "string",
            "isActive": "boolean"
        }
    ],
    "totalElements": "number",
    "totalPages": "number",
    "currentPage": "number"
}
```

#### GET /users/{id}
- **Description**: Get user details
- **Authentication**: JWT Required
- **Path Parameters**:
  - `id`: number (User ID)
- **Success Response**: 200 OK
```json
{
    "id": "number",
    "name": "string",
    "email": "string",
    "role": "string",
    "department": "string",
    "phone": "string",
    "isActive": "boolean",
    "createdAt": "string",
    "updatedAt": "string"
}
```

#### PUT /users/{id}
- **Description**: Update user details
- **Authentication**: Admin Only
- **Path Parameters**:
  - `id`: number (User ID)
- **Request Body**:
```json
{
    "name": "string",
    "email": "string",
    "role": "string",
    "department": "string",
    "phone": "string",
    "isActive": "boolean"
}
```
- **Success Response**: 200 OK
```json
{
    "id": "number",
    "name": "string",
    "email": "string",
    "role": "string",
    "department": "string",
    "phone": "string",
    "isActive": "boolean",
    "updatedAt": "string"
}
```

### Maintenance Requests Module

#### POST /maintenance-requests
- **Description**: Create new maintenance request
- **Authentication**: JWT Required
- **Request Body**:
```json
{
    "title": "string",
    "description": "string",
    "category": "string",
    "priority": "string",
    "location": "string"
}
```
- **Success Response**: 201 Created
```json
{
    "id": "number",
    "title": "string",
    "description": "string",
    "category": "string",
    "priority": "string",
    "status": "string",
    "location": "string",
    "createdBy": "number",
    "createdAt": "string"
}
```

#### GET /maintenance-requests
- **Description**: Get list of maintenance requests
- **Authentication**: JWT Required
- **Query Parameters**:
  - `page`: number (default: 0)
  - `size`: number (default: 20)
  - `status`: string (optional)
  - `priority`: string (optional)
  - `category`: string (optional)
  - `createdBy`: number (optional)
  - `assignedTo`: number (optional)
  - `sortBy`: string (optional, default: "createdAt")
  - `sortDirection`: string (optional, default: "DESC")
- **Success Response**: 200 OK
```json
{
    "content": [
        {
            "id": "number",
            "title": "string",
            "description": "string",
            "category": "string",
            "priority": "string",
            "status": "string",
            "location": "string",
            "createdBy": "number",
            "assignedTo": "number",
            "createdAt": "string",
            "updatedAt": "string",
            "resolvedAt": "string"
        }
    ],
    "totalElements": "number",
    "totalPages": "number",
    "currentPage": "number"
}
```

#### GET /maintenance-requests/{id}
- **Description**: Get maintenance request details
- **Authentication**: JWT Required
- **Path Parameters**:
  - `id`: number (Request ID)
- **Success Response**: 200 OK
```json
{
    "id": "number",
    "title": "string",
    "description": "string",
    "category": "string",
    "priority": "string",
    "status": "string",
    "location": "string",
    "createdBy": "number",
    "assignedTo": "number",
    "createdAt": "string",
    "updatedAt": "string",
    "resolvedAt": "string",
    "comments": [
        {
            "id": "number",
            "content": "string",
            "userId": "number",
            "createdAt": "string"
        }
    ],
    "attachments": [
        {
            "id": "number",
            "fileName": "string",
            "fileType": "string",
            "fileSize": "number",
            "createdAt": "string"
        }
    ]
}
```

#### PUT /maintenance-requests/{id}
- **Description**: Update maintenance request
- **Authentication**: JWT Required (Support Staff Only)
- **Path Parameters**:
  - `id`: number (Request ID)
- **Request Body**:
```json
{
    "status": "string",
    "assignedTo": "number",
    "priority": "string"
}
```
- **Success Response**: 200 OK
```json
{
    "id": "number",
    "status": "string",
    "assignedTo": "number",
    "priority": "string",
    "updatedAt": "string"
}
```

### Comments Module

#### POST /maintenance-requests/{id}/comments
- **Description**: Add comment to maintenance request
- **Authentication**: JWT Required
- **Path Parameters**:
  - `id`: number (Request ID)
- **Request Body**:
```json
{
    "content": "string"
}
```
- **Success Response**: 201 Created
```json
{
    "id": "number",
    "content": "string",
    "userId": "number",
    "requestId": "number",
    "createdAt": "string"
}
```

#### GET /maintenance-requests/{id}/comments
- **Description**: Get comments for maintenance request
- **Authentication**: JWT Required
- **Path Parameters**:
  - `id`: number (Request ID)
- **Query Parameters**:
  - `page`: number (default: 0)
  - `size`: number (default: 20)
- **Success Response**: 200 OK
```json
{
    "content": [
        {
            "id": "number",
            "content": "string",
            "userId": "number",
            "requestId": "number",
            "createdAt": "string"
        }
    ],
    "totalElements": "number",
    "totalPages": "number",
    "currentPage": "number"
}
```

### Attachments Module

#### POST /maintenance-requests/{id}/attachments
- **Description**: Upload attachment for maintenance request
- **Authentication**: JWT Required
- **Path Parameters**:
  - `id`: number (Request ID)
- **Request Headers**:
  - `Content-Type: multipart/form-data`
- **Request Body**:
  - `file`: File (required)
- **Success Response**: 201 Created
```json
{
    "id": "number",
    "fileName": "string",
    "fileType": "string",
    "fileSize": "number",
    "requestId": "number",
    "uploadedBy": "number",
    "createdAt": "string"
}
```

#### GET /maintenance-requests/{id}/attachments
- **Description**: Get attachments for maintenance request
- **Authentication**: JWT Required
- **Path Parameters**:
  - `id`: number (Request ID)
- **Success Response**: 200 OK
```json
{
    "content": [
        {
            "id": "number",
            "fileName": "string",
            "fileType": "string",
            "fileSize": "number",
            "requestId": "number",
            "uploadedBy": "number",
            "createdAt": "string"
        }
    ],
    "totalElements": "number",
    "totalPages": "number",
    "currentPage": "number"
}
```

## Security Notes

1. **Rate Limiting**
   - Authentication endpoints: 5 requests per minute
   - Other endpoints: 60 requests per minute per user

2. **Input Validation**
   - All string inputs are sanitized
   - File uploads are validated for type and size
   - Enum values are validated against allowed values

3. **Authorization Rules**
   - Employees can only view and create their own requests
   - Support staff can view and update all requests
   - Admins have full access to all resources

4. **File Upload Security**
   - Maximum file size: 5MB
   - Allowed file types: JPG, PNG, PDF
   - Files are stored in secure location
   - File names are sanitized

5. **Password Requirements**
   - Minimum 8 characters
   - Must contain uppercase, lowercase, number, and special character
   - Passwords are hashed using BCrypt

6. **JWT Security**
   - Token expiration: 1 hour
   - Refresh token expiration: 7 days
   - Tokens are signed with RSA-256 