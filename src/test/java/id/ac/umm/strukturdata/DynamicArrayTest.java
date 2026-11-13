
package id.ac.umm.strukturdata;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    @Test
    void testAdd() {
        DynamicArray<Integer> array = new DynamicArray<>();

        array.add(10);
        array.add(20);

        assertEquals(2, array.size());
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
    }

    @Test
    void testInsert() {
        DynamicArray<Integer> array = new DynamicArray<>();

        array.add(10);
        array.add(30);
        array.insert(1, 20);

        assertEquals(3, array.size());
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
    }

    @Test
    void testGet() {
        DynamicArray<String> array = new DynamicArray<>();

        array.add("Java");

        assertEquals("Java", array.get(0));
    }

    @Test
    void testSet() {
        DynamicArray<Integer> array = new DynamicArray<>();

        array.add(10);

        Integer oldValue = array.set(0, 50);

        assertEquals(10, oldValue);
        assertEquals(50, array.get(0));
    }

    @Test
    void testRemove() {
        DynamicArray<Integer> array = new DynamicArray<>();

        array.add(10);
        array.add(20);
        array.add(30);

        Integer removed = array.remove(1);

        assertEquals(20, removed);
        assertEquals(2, array.size());
        assertEquals(30, array.get(1));
    }

    @Test
    void testSearch() {
        DynamicArray<Integer> array = new DynamicArray<>();

        array.add(10);
        array.add(20);
        array.add(30);

        assertEquals(1, array.search(20));
        assertEquals(-1, array.search(99));
    }

    @Test
    void testResize() {
        DynamicArray<Integer> array = new DynamicArray<>(2);

        array.add(10);
        array.add(20);

        assertEquals(2, array.capacity());

        array.add(30);

        assertEquals(4, array.capacity());
        assertEquals(3, array.size());
        assertEquals(30, array.get(2));
    }

    @Test
    void testEmptyArray() {
        DynamicArray<Integer> array = new DynamicArray<>();

        assertTrue(array.isEmpty());
        assertEquals(0, array.size());
    }

    @Test
    void testInvalidIndex() {
        DynamicArray<Integer> array = new DynamicArray<>();

        array.add(10);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> array.get(5)
        );
    }

    @Test
    void testInsertInvalidIndex() {
        DynamicArray<Integer> array = new DynamicArray<>();

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> array.insert(2, 10)
        );
    }

    @Test
    void testRemoveFromEmptyArray() {
        DynamicArray<Integer> array = new DynamicArray<>();

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> array.remove(0)
        );
    }
}
