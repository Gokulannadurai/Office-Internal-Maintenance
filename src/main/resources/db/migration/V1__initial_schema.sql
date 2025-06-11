-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Assets table
CREATE TABLE assets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    location VARCHAR(100),
    purchase_date DATE,
    warranty_end_date DATE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Maintenance Requests table
CREATE TABLE maintenance_requests (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    requester_id BIGINT REFERENCES users(id),
    assigned_to_id BIGINT REFERENCES users(id),
    asset_id BIGINT REFERENCES assets(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Maintenance Schedule table
CREATE TABLE maintenance_schedule (
    id BIGSERIAL PRIMARY KEY,
    asset_id BIGINT REFERENCES assets(id),
    maintenance_type VARCHAR(50) NOT NULL,
    scheduled_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    assigned_to_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Notifications table
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    type VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Maintenance History table
CREATE TABLE maintenance_history (
    id BIGSERIAL PRIMARY KEY,
    request_id BIGINT REFERENCES maintenance_requests(id),
    asset_id BIGINT REFERENCES assets(id),
    performed_by_id BIGINT REFERENCES users(id),
    description TEXT,
    completion_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL
); 