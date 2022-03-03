package main;

import main.ReverseIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedCircularDoublyLinkedList<E extends Comparable<E>> implements SortedList<E> {

    private class SortedCircularDoublyLinkedListIterator<E> implements Iterator<E>{
        private int currentPosition;
        private Node<E> nextNode;

        public SortedCircularDoublyLinkedListIterator(int index){
            //If the index is 0 starts to iterate from the beginning, otherwise from given position
            this.currentPosition = index;
            if(index != 0){
                int counter = 0;
                this.nextNode = (Node<E>) header.getNext();
                while(counter < index){
                    this.nextNode = nextNode.getNext();
                    counter++;
                }
            }else{
                this.nextNode = (Node<E>) header.getNext();
            }
        }

        @Override
        public boolean hasNext() {
            return this.currentPosition < size();
        }

        @Override
        public E next() {
            if (this.hasNext()){
                E result = this.nextNode.getElement();
                this.nextNode = this.nextNode.getNext();
                this.currentPosition++;
                return result;
            }
            else {
                throw new NoSuchElementException();
            }
        }
    }

    private class SortedCircularDoublyLinkedListReverseIterator<E> implements ReverseIterator<E> {
        int currentPosition;
        Node<E> prevNode;

        public SortedCircularDoublyLinkedListReverseIterator(int index){
            this.currentPosition = index;

            //If the index is 0 iterates from the end, else from the given position

            if(index != size()-1){
                int counter = size()-1;
                this.prevNode = (Node<E>) header.getPrev();
                while(counter > index){
                    this.prevNode = this.prevNode.getPrev();
                    counter--;
                }
            }else{
                this.prevNode = (Node<E>) header.getPrev();
            }
        }

        @Override
        public boolean hasPrevious() {
            return this.currentPosition >= 0;
        }

        @Override
        public E previous() {
            if(this.hasPrevious()){
                E result = this.prevNode.getElement();
                this.prevNode = this.prevNode.getPrev();
                this.currentPosition--;
                return result;
            }else{
                throw new NoSuchElementException();
            }
        }
    }

    private static class Node<E> {
        private E element;
        private Node<E> next;
        private Node<E> prev;

        public Node(E element, Node<E> next, Node<E> prev) {
            this.element = element;
            this.next = next;
            this.prev = prev;

        }

        public Node(){
            this.element = null;
            this.next = null;
            this.prev = null;

        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }
        public Node<E> getPrev() {
            return prev;
        }

        public void setPrev(Node<E> prev) {
            this.prev = prev;
        }
        public E getElement() {
            return element;
        }

        public void setElement(E element) {
            this.element = element;
        }
    }

    private Node<E> header;
    private int currentSize;

    public SortedCircularDoublyLinkedList(){
        this.currentSize = 0;
        this.header = new Node<>();
        this.header.setNext(this.header);
        this.header.setPrev(this.header);
    }

    @Override
    public boolean add(E obj) {
        Node<E> newNode = new Node<>();
        newNode.setElement(obj);

        //Adds new element when list is empty, points next and prev to header, header points to the new Node
        if(this.isEmpty()){
            this.header.setNext(newNode);
            this.header.setPrev(newNode);

            newNode.setNext(this.header);
            newNode.setPrev(this.header);

        }else{
            //Adds element if there is already at least one element in the list
            Iterator<E> tempIterator = this.iterator();
            E tempObj = null;
            Node<E> tempNode = this.header;

            while(tempIterator.hasNext()){
                tempNode = tempNode.getNext();
                tempObj = tempIterator.next();

                //If the value is less or equal than the element in the Node adds it before it.
                if(obj.compareTo(tempObj) < 0 || obj.compareTo(tempObj) == 0){

                    newNode.setPrev(tempNode.getPrev());
                    tempNode.getPrev().setNext(newNode);
                    newNode.setNext(tempNode);
                    tempNode.setPrev(newNode);
                    this.currentSize++;

                    return true;
                }

            }
            //Adds element at the end
            newNode.setNext(this.header);
            newNode.setPrev(tempNode);

            tempNode.setNext(newNode);
            this.header.setPrev(newNode);

        }
        this.currentSize++;
        return true;
    }

    @Override
    public int size() {
        return this.currentSize;
    }

    //Get first index of obj then remove with index
    @Override
    public boolean remove(E obj) {

        return this.remove(this.firstIndex(obj));
    }

    @Override
    public boolean remove(int index) {
        if(index > this.size() || index < 0){
            //Exception for invalid index

            throw new IndexOutOfBoundsException();
        }else{
            int counter = 0;
            Node<E> tempNode = this.header.getNext();

            while(tempNode.getElement() != null){
                if(counter == index){
                    /*Switches pointer so that the previous element points toward the next element after the removed one, and the next element points towards the element
                    before the one that was removed.*/

                    tempNode.getNext().setPrev(tempNode.getPrev());
                    tempNode.getPrev().setNext(tempNode.getNext());

                    //Sets all to null so there is no memory leak
                    tempNode.setNext(null);
                    tempNode.setPrev(null);
                    tempNode.setElement(null);

                    this.currentSize--;
                    return true;
                }
                counter++;
                tempNode = tempNode.getNext();
            }
        }
        //Returns false if the object is not removed

        return false;
    }

    //Removes the first index of the object until there is none left
    @Override
    public int removeAll(E obj) {
        int counter = 0;
        while(this.contains(obj)){
            this.remove(this.firstIndex(obj));
            counter++;
        }
        return counter;
    }

    //Returns the next element after the header
    @Override
    public E first() {
        if(this.isEmpty()){
            return null;
        }else{
            return this.header.getNext().getElement();
        }
    }

    //Return the previous element to the header
    @Override
    public E last() {
        if(this.isEmpty()){
            return null;
        }else{
            return this.header.getPrev().getElement();
        }

    }

    @Override
    public E get(int index) {
        if(index > this.size() || index < 0){
            throw new IndexOutOfBoundsException();
        }else{
            Iterator<E> tempIterator = this.iterator();
            E temp = null;
            int counter = 0;
            //Iterates through the List while counting
            while(tempIterator.hasNext()){
                temp = tempIterator.next();
                if(counter == index){
                    return temp;
                }
                counter++;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        while(!this.isEmpty()){
            this.remove(0);
        }
    }

    @Override
    public boolean contains(E e) {
        return this.firstIndex(e) > -1;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public Iterator<E> iterator(int index) {
        if(index > this.size() || index < 0){
            throw new IndexOutOfBoundsException();
        }else{
            return new SortedCircularDoublyLinkedListIterator<E>(index);
        }
    }

    //Iterates through the list until it finds the first instance of the object
    @Override
    public int firstIndex(E e) {
        int counter = 0;
        Iterator<E> tempIterator = iterator();
        E tempObj = null;

        while(tempIterator.hasNext()){
            tempObj = tempIterator.next();
            if(tempObj.equals(e)){
                return counter;
            }
            counter++;
        }
        return -1;
    }

    //Iterates backwards through the list until it finds the first instance of the object
    @Override
    public int lastIndex(E e) {
        int counter = this.size()-1;
        ReverseIterator<E> tempIterator = reverseIterator();
        E tempObj = null;
        while(tempIterator.hasPrevious()){
            tempObj = tempIterator.previous();
            if(tempObj.equals(e)){
                return counter;
            }
            counter--;
        }
        return -1;
    }

    @Override
    public ReverseIterator<E> reverseIterator() {
        return new SortedCircularDoublyLinkedListReverseIterator<>(size()-1);
    }

    @Override
    public ReverseIterator<E> reverseIterator(int index) {
        if(index > this.size() || index < 0){
            throw new IndexOutOfBoundsException();
        }else{
            return new SortedCircularDoublyLinkedListReverseIterator<>(index);
        }

    }

    @Override
    public Iterator<E> iterator() {
        return new SortedCircularDoublyLinkedListIterator<E>(0);
    }
}
