package id.ac.umm.strukturdata;
import java.util.NoSuchElementException;

public class Stack<T> {
    private T[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public Stack() {
        data = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public Stack(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(
                    "Kapasitas harus lebih dari 0"
            );
        }
        data = (T[]) new Object[initialCapacity];
        size = 0;
    }

    public void push(T element) {
        if (size == data.length) {
            resize(data.length * 2);
        }

        data[size] = element;
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Stack kosong"
            );
        }

        T removedElement = data[size - 1];

        data[size - 1] = null;
        size--;

        return removedElement;
    }

    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Stack kosong"
            );
        }
        return data[size - 1];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int capacity() {
        return data.length;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        T[] newData = (T[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    public boolean validateInvariant() {
        return data != null
                && data.length > 0
                && size >= 0
                && size <= data.length;
    }
}
