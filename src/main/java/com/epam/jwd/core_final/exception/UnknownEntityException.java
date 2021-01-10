package com.epam.jwd.core_final.exception;

public class UnknownEntityException extends RuntimeException {

    private final String entityName;
    private final Object[] args;

    public UnknownEntityException(String entityName) {
        super();
        this.entityName = entityName;
        this.args = null;
    }

    public UnknownEntityException(String entityName, Object[] args) {
        super();
        this.entityName = entityName;
        this.args = args;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder("Неверные данные для " + entityName + " !");
        if (args != null) {
            message.append(" Возможные аргументы:");
            for (Object arg : args) {
                message.append(' ').append(arg).append(',');
            }
            message.deleteCharAt(message.capacity() - 1);
        }
        return message.toString();
    }
}