package com.maintenance.domain.user;

import com.maintenance.common.BaseService;

public interface UserService extends BaseService<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    void changePassword(Long userId, String newPassword);
    void updateStatus(Long userId, UserStatus status);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 