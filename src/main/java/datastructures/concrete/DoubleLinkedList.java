package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;
    
    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        //front and back point to the new node
        //increase size by 1
        //create new node
        //back.front is 
        if (size == 0) {
            front = new Node<T>(item);
            back = front;
        }else {
        back.next = new Node<T>(back, item, null);
        back = back.next;
        }
        size++;  
    }

    @Override
    public T remove() {
        if (size == 0) {
            throw new EmptyContainerException();
        } 
        
        else if (size == 1){
            T temp = back.data;
            front = null;
            back = null;
            size--;
            return temp;
        }
        
        else {
            T temp = back.data;
            back = back.prev;
            back.next = null;
            size--;
            return temp;
        }
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) { throw new IndexOutOfBoundsException("index does not exist");
    }else {
        Node<T> current = front;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }
}


    @Override
    public void set(int index, T item) {
        if (index >= size || index < 0) { throw new IndexOutOfBoundsException("index does not exist");
        }else {
            if (size == 1) {
                front = new Node<T>(item);
                back = front;
            }
            
            else if (index == size - 1) {
                back.prev.next = new Node<T>(back.prev, item, null);
                back = back.prev.next;
            } else if (index == 0) {
                front = new Node<T>(null, item, front.next);
                if (front.next != null) {
                    front.next.prev = front;
                }
            }
            
            else {
            
            Node<T> current = front;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            Node<T> temp = new Node<T>(current.prev, item, current.next);
            current.prev.next = temp;
            current.next.prev = temp;
            }
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException("index does not exist");
        }
        
        else if (index==size) {
            add(item);
        }
        
        //adds in the middle
        else if (index != 0) {
            if (index < size/2) {
                Node<T> current = front;
            for (int i = 0; i < index; i++) 
            {
                current = current.next;
            }
            } else {
            Node<T> current = back;
            for (int i = size-1; i > index; i--) 
            {
            current = current.prev;
            }
            
        current.prev = new Node<T>(current.prev, item, current);
        current = current.prev;
        size++;
        current.prev.next = current;        
        } 
        }
        //index = 0
        else {
            front.prev = new Node<T>(null, item, front);
            front = front.prev;
            size++;
        }
    }
    
    @Override
    public T delete(int index) {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        else {
        if (index == 0) {
            T temp = front.data;
            if (size == 1) {
                front = null;
                back = null;
            }
            else if (size > 1) {
                front = front.next;
                front.prev = null;
            }
            size--;
            return temp;
        }
        
        else if (index == size - 1) {
            T temp = back.data;
            back = back.prev;
            back.next = null;
            size--;
            return temp;
        }
        
        else {
        Node<T> current = null;
            if (index < size/2) {
                current = front;
                for (int i = 0; i < index; i++) 
                {
                    current = current.next;
                }
            } else {
                current = back;
                for (int i = size-1; i > index; i--) 
                {
                    current = current.prev;
                }
            }
        
        T temp = current.data;
        current.prev.next = current.next;
        current.next.prev = current.prev;
        size--;
        return temp;
        }

        }

    }

    @Override
    public int indexOf(T item) {
        Node<T> current = front;
        for (int i = 0; i < size; i++) {
            if (current.data == item|| current.data.equals(item)) {
                return i;
            }
            else {
                current = current.next;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        Node<T> current = front;
        for (int i = 0; i < size; i++) {
            if (current.data == other|| current.data.equals(other)) {
                return true;
            }
            else {
                current = current.next;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            else {
                T temp = current.data;
                current = current.next;
                return temp;
            }
        }
    }
}
