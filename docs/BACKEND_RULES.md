# Backend Implementation Rules and Standards

## General Development Rules

### Code Organization
1. Follow standard Spring Boot project structure:
   ```
   src/main/java/com/maintenance
   ├── config/         # Configuration classes
   ├── controller/     # REST controllers
   ├── dto/           # Data Transfer Objects
   ├── entity/        # JPA entities
   ├── exception/     # Custom exceptions
   ├── repository/    # JPA repositories
   ├── security/      # Security related classes
   ├── service/       # Business logic
   └── util/          # Utility classes
   ```

2. Package naming convention:
   - Use lowercase
   - Use reverse domain notation
   - Example: `com.maintenance.api`

### Code Style
1. Follow Java Code Conventions
2. Use meaningful names for classes, methods, and variables
3. Maximum line length: 120 characters
4. Use proper indentation (4 spaces)
5. Add Javadoc for public methods and classes

## Implementation Standards

### API Development
1. **Input Validation**
   - Use Jakarta Validation annotations
   - Implement custom validators when needed
   - Validate all incoming requests
   - Return detailed validation error messages

2. **Error Handling**
   - Use global exception handler
   - Follow error response format from API_SPEC.md
   - Log all errors with appropriate level
   - Never expose internal errors to clients

3. **Authentication & Authorization**
   - Implement JWT-based authentication
   - Use Spring Security for authorization
   - Follow role-based access control
   - Implement rate limiting

4. **Response Format**
   - Use consistent response structure
   - Include proper HTTP status codes
   - Add pagination for list endpoints
   - Include proper headers

### Database
1. **Entity Design**
   - Use JPA annotations properly
   - Implement proper relationships
   - Add appropriate indexes
   - Use proper data types

2. **Query Optimization**
   - Use proper indexing
   - Implement pagination
   - Use appropriate fetch types
   - Avoid N+1 problems

3. **Transaction Management**
   - Use proper transaction boundaries
   - Handle concurrent access
   - Implement proper rollback
   - Use appropriate isolation levels

### Security
1. **Password Handling**
   - Use BCrypt for password hashing
   - Implement password policies
   - Secure password reset flow
   - Never store plain text passwords

2. **JWT Implementation**
   - Use secure token generation
   - Implement proper token validation
   - Handle token expiration
   - Secure token storage

3. **File Upload Security**
   - Validate file types
   - Implement size limits
   - Scan for malware
   - Secure file storage

### Testing
1. **Unit Testing**
   - Test all service methods
   - Use proper mocking
   - Test edge cases
   - Aim for >80% coverage

2. **Integration Testing**
   - Test all API endpoints
   - Use test containers
   - Test security features
   - Test error scenarios

3. **Performance Testing**
   - Test under load
   - Monitor response times
   - Test concurrent access
   - Test database performance

## Development Workflow

### Task Execution
1. Work sequentially through tasks_backend.md
2. Mark completed tasks with:
   - Completion date
   - Any deviations from plan
   - Testing coverage achieved
   - Known issues or limitations

### Code Review Process
1. Self-review before submission
2. Peer review required
3. Address all review comments
4. Update documentation

### Documentation
1. Keep API documentation updated
2. Document all configuration changes
3. Maintain changelog
4. Update README.md

## Performance Requirements

### Response Times
1. API response time < 200ms
2. Database query time < 100ms
3. File upload time < 5s
4. Authentication time < 1s

### Resource Usage
1. Memory usage < 1GB
2. CPU usage < 50%
3. Database connections < 100
4. File storage < 10GB

## Monitoring and Logging

### Logging Standards
1. Use appropriate log levels
2. Include request IDs
3. Log all errors
4. Include relevant context

### Monitoring
1. Monitor application health
2. Track performance metrics
3. Monitor security events
4. Set up alerts

## Deployment

### Environment Setup
1. Use environment variables
2. Secure sensitive data
3. Use proper profiles
4. Document configuration

### Deployment Process
1. Automated testing
2. Security scanning
3. Performance testing
4. Zero-downtime deployment

## Maintenance

### Code Maintenance
1. Regular dependency updates
2. Security patches
3. Performance optimization
4. Code cleanup

### Database Maintenance
1. Regular backups
2. Index optimization
3. Data cleanup
4. Performance tuning 