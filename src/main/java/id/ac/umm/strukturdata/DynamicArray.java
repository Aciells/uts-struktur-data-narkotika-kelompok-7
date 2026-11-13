
package id.ac.umm.strukturdata;

import java.util.Objects;

public class DynamicArray<T> implements StrukturData<T> {

    private T[] data;
    private int size;

    private static final int DEFAULT_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public DynamicArray() {
        data = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(
                    "Kapasitas harus lebih dari 0"
            );
        }

        data = (T[]) new Object[initialCapacity];
        size = 0;
    }

    @Override
    public void add(T element) {
        if (size == data.length) {
            resize(data.length * 2);
        }

        data[size] = element;
        size++;
    }

    @Override
    public void insert(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index
            );
        }

        if (size == data.length) {
            resize(data.length * 2);
        }

        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }

        data[index] = element;
        size++;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        return data[index];
    }

    @Override
    public T set(int index, T element) {
        checkIndex(index);

        T oldElement = data[index];
        data[index] = element;

        return oldElement;
    }

    @Override
    public T remove(int index) {
        checkIndex(index);

        T removedElement = data[index];

        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        data[size - 1] = null;
        size--;

        return removedElement;
    }

    @Override
    public int search(T element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i], element)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index
            );
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        T[] newData = (T[]) new Object[newCapacity];

        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }

        data = newData;
    }

    public int capacity() {
        return data.length;
    }
}
