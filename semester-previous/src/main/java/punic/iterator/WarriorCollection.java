package punic.iterator;

import punic.core.Warrior;

/**
 * Коллекция воинов, реализующая паттерн Iterator.
 * Предоставляет интерфейс для создания итератора без раскрытия
 * внутренней структуры хранения данных.
 */
public class WarriorCollection {
    private Warrior[] warriors;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Создает новую коллекцию воинов с начальной емкостью.
     */
    public WarriorCollection() {
        this.warriors = new Warrior[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Создает новую коллекцию воинов с указанной начальной емкостью.
     * @param initialCapacity начальная емкость коллекции
     */
    public WarriorCollection(int initialCapacity) {
        this.warriors = new Warrior[initialCapacity];
        this.size = 0;
    }

    /**
     * Добавляет воина в коллекцию.
     * @param warrior воин для добавления
     */
    public void addWarrior(Warrior warrior) {
        if (warrior == null) {
            throw new IllegalArgumentException("Воин не может быть null");
        }
        if (size >= warriors.length) {
            resize();
        }
        warriors[size++] = warrior;
    }

    /**
     * Увеличивает размер массива при необходимости.
     */
    private void resize() {
        Warrior[] newWarriors = new Warrior[warriors.length * 2];
        System.arraycopy(warriors, 0, newWarriors, 0, size);
        warriors = newWarriors;
    }

    /**
     * Возвращает воина по индексу.
     * @param index индекс воина
     * @return воин по указанному индексу
     */
    public Warrior getWarrior(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс выходит за границы коллекции");
        }
        return warriors[index];
    }

    /**
     * Возвращает количество воинов в коллекции.
     * @return размер коллекции
     */
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуста ли коллекция.
     * @return true, если коллекция пуста, иначе false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Создает и возвращает итератор для обхода коллекции.
     * Это ключевой метод паттерна Iterator - клиент получает итератор
     * без знания о внутренней структуре коллекции.
     * @return итератор для обхода коллекции
     */
    public WarriorIterator createIterator() {
        return new WarriorIteratorImpl();
    }

    /**
     * Внутренняя реализация итератора.
     * Инкапсулирует логику обхода коллекции.
     */
    private class WarriorIteratorImpl implements WarriorIterator {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public Warrior next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("Нет больше элементов в коллекции");
            }
            return warriors[currentIndex++];
        }

        @Override
        public void reset() {
            currentIndex = 0;
        }
    }
}

