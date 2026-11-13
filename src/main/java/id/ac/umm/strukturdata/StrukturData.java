
package id.ac.umm.strukturdata;

public interface StrukturData<T> {
    void add(T element);
    void insert(int index, T element);
    T get(int index);
    T set(int index, T element);
    T remove(int index);
    int search(T element);
    int size();
    boolean isEmpty();
}
