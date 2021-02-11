package ru.test;

import java.util.function.Consumer;

/**
 * Отложенный потребитель
 * Подразумевает обработку определенного значения (типа T) через установленное количество времени (delay).
 * Сама функция потребления также может изменяться.
 */
public interface DeferredExecutor<T> extends Consumer<T> {
    /**
     * Настройка "функция потребления" - можно менять без пересоздания объекта
     * @param consumer - функциональный интерфейс Consumer, чаще лямбда
     */
    void setConsumer(Consumer<T> consumer);

    /**
     * Настройка "задержка" - можно менять без пересоздания объекта
     * @param delay - значение в мс
     */
    void setDelay(long delay);
}