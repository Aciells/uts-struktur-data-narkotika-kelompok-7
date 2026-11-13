package id.ac.umm.strukturdata;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

class StackTest {
    @Test
    void testPush() {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        assertEquals(2, stack.size());
        assertEquals(20, stack.peek());
    }

    @Test
    void testPop() {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        Integer removed = stack.pop();
        assertEquals(30, removed);
        assertEquals(2, stack.size());
        assertEquals(20, stack.peek());
    }

    @Test
    void testPeek() {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        assertEquals(20, stack.peek());
        assertEquals(2, stack.size());
    }

    @Test
    void testLifoOrder() {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        assertEquals(30, stack.pop());
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());
    }

    @Test
    void testSize() {
        Stack<Integer> stack = new Stack<>();

        stack.push(10);
        stack.push(20);

        assertEquals(2, stack.size());
        stack.pop();
        assertEquals(1, stack.size());
    }

    @Test
    void testIsEmpty() {
        Stack<Integer> stack = new Stack<>();
        assertTrue(stack.isEmpty());
        stack.push(10);
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test
    void testPopFromEmptyStack() {
        Stack<Integer> stack = new Stack<>();
        assertThrows(
                NoSuchElementException.class,
                () -> stack.pop()
        );
    }

    @Test
    void testPeekFromEmptyStack() {
        Stack<Integer> stack = new Stack<>();
        assertThrows(
                NoSuchElementException.class,
                () -> stack.peek()
        );
    }

    @Test
    void testResize() {
        Stack<Integer> stack = new Stack<>(2);
        stack.push(10);
        stack.push(20);
        assertEquals(2, stack.capacity());
        stack.push(30);
        assertEquals(4, stack.capacity());
        assertEquals(3, stack.size());
        assertEquals(30, stack.peek());
    }

    @Test
    void testValidateInvariant() {
        Stack<Integer> stack = new Stack<>();
        assertTrue(stack.validateInvariant());
        stack.push(10);
        stack.push(20);
        assertTrue(stack.validateInvariant());
        stack.pop();
        assertTrue(stack.validateInvariant());
    }

    @Test
    void testInvalidInitialCapacity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Stack<>(0)
        );
    }
}
