package com.maintenance.common;

import com.maintenance.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class AbstractBaseService<T extends BaseEntity, ID, R extends JpaRepository<T, ID>> 
        implements BaseService<T, ID> {

    protected final R repository;

    protected AbstractBaseService(R repository) {
        this.repository = repository;
    }

    @Override
    public T findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entity not found with id: " + id));
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public T create(T entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public T update(T entity) {
        if (!repository.existsById((ID) entity.getId())) {
            throw new NotFoundException("Entity not found with id: " + entity.getId());
        }
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Entity not found with id: " + id);
        }
        repository.deleteById(id);
    }
} 