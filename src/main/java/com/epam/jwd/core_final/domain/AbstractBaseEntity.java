package com.epam.jwd.core_final.domain;

/**
 * Expected fields:
 * <p>
 * id {@link Long} - entity id
 * name {@link String} - entity name
 */
public abstract class AbstractBaseEntity implements BaseEntity {

    private final Long id;
    private final String name;

    public AbstractBaseEntity(String name, Long count) {
        this.name = name;
        this.id = count;
    }

    @Override
    public Long getId() {
        return id;

    }

    @Override
    public String getName() {

        return name;
    }

    public abstract void reduceCount();

    @Override
    public String toString() {
        return "id=" + id +
                ", name=" + name;
    }
}