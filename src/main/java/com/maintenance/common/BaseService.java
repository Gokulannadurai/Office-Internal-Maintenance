package com.maintenance.common;

import java.util.List;

public interface BaseService<T extends BaseEntity, ID> {
    T findById(ID id);
    List<T> findAll();
    T create(T entity);
    T update(T entity);
    void delete(ID id);
} 