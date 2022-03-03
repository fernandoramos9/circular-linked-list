package main;

import main.ReverseIterator;

import java.util.Iterator;

public interface SortedList<E extends Comparable<E>> extends Iterable<E>{
	// valid position in the list
	// [0, size() - 1]
	
	public boolean add(E obj);
	public int size();
	public boolean remove(E obj);
	public boolean remove(int index);
	public int removeAll(E obj);
	public E first();
	public E last();
	public E get(int index);
	public void clear();
	public boolean contains(E e);
	public boolean isEmpty();
	public Iterator<E> iterator(int index);
	public int firstIndex(E e);
	public int lastIndex(E e);
	public ReverseIterator<E> reverseIterator();
	public ReverseIterator<E> reverseIterator(int index);
	
}
