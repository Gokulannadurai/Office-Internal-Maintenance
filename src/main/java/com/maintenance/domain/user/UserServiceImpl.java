package com.maintenance.domain.user;

import com.maintenance.common.AbstractBaseService;
import com.maintenance.exception.BusinessException;
import com.maintenance.exception.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends AbstractBaseService<User, Long, UserRepository> implements UserService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = findById(userId);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        repository.save(user);
    }

    @Override
    @Transactional
    public void updateStatus(Long userId, UserStatus status) {
        User user = findById(userId);
        user.setStatus(status);
        repository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    @Transactional
    public User create(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new BusinessException("Username already exists", "USERNAME_EXISTS");
        }
        if (existsByEmail(user.getEmail())) {
            throw new BusinessException("Email already exists", "EMAIL_EXISTS");
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return super.create(user);
    }
} 