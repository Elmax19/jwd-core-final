package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.BaseEntity;

/**
 * Should be a builder for {@link BaseEntity} fields
 */
public abstract class Criteria<T extends BaseEntity> {
    private final Long id;
    private final String name;

    Criteria(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    static public abstract class CriteriaBuilder<C extends CriteriaBuilder> {
        public abstract C id(Long id);

        public abstract C name(String name);

    }
}