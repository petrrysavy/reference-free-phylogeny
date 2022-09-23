/*
 * Copyright (c) 2013 Petr Ryšavý <rysavpe1@fel.cvut.cz>
 *
 * This file is part of MLCMP.
 *
 * MLCMP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MLCMP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MLCMP.  If not, see <http ://www.gnu.org/licenses/>.
 */
package rysavpe1.reads.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable set that stores two values. This wrapper can be used for example for
 * indexing the hash maps, as return value or similar purposes.
 *
 * @param <E> Type of values stored in this set.
 * @author Petr Ryšavý
 */
public class PairSet<E> implements Set<E>, Cloneable
{
	/**
	 * First stored value.
	 */
	private E value1;
	/**
	 * Second stored value.
	 */
	private E value2;

	/**
	 * Creates new pair set.
	 *
	 * @param value1 First value. May be {@code null}.
	 * @param value2 Second value. May be {@code null}.
	 */
	public PairSet(E value1, E value2)
	{
		this.value1 = value1;
		this.value2 = value2;
	}

	@Override
	public int size()
	{
		return 2;
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	@Override
	public boolean contains(Object o)
	{
		return Objects.equals(o, value1) || Objects.equals(o, value2);
	}

	@Override
	public Iterator<E> iterator()
	{
		return new ValueIterator();
	}

	@Override
	public Object[] toArray()
	{
		return new Object[]
		{
			value1, value2
		};
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a)
	{
		if (a.length > 1)
			a[1] = (T) value2;
		else if (a.length == 1)
			a[0] = (T) value1;
		return a;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws UnsupportedOperationException This method is not supported.
	 */
	@Override
	public boolean add(E e)
	{
		throw new UnsupportedOperationException("Cannot add a value to pair.");
	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws UnsupportedOperationException This method is not supported.
	 */
	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException("Cannot remove a value to pair.");
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		for (Object o : c)
			if (!(Objects.equals(o, value1) || Objects.equals(o, value2)))
				return false;
		return true;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws UnsupportedOperationException This method is not supported.
	 */
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		throw new UnsupportedOperationException("Cannot add a value to pair.");
	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws UnsupportedOperationException This method is not supported.
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("Cannot remove a value to pair.");
	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws UnsupportedOperationException This method is not supported.
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("Cannot remove a value to pair.");
	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws UnsupportedOperationException This method is not supported.
	 */
	@Override
	public void clear()
	{
		throw new UnsupportedOperationException("Cannot remove a value to pair.");
	}

	/**
	 * Returns one of the stored values. The value is the other that is returned by
	 * {@code getValue2} method.
	 *
	 * @return One of the stored values.
	 */
	public E getValue1()
	{
		return value1;
	}

	/**
	 * Returns one of the stored values. The value is the other that is returned by
	 * {@code getValue1} method.
	 *
	 * @return One of the stored values.
	 */
	public E getValue2()
	{
		return value2;
	}

	/**
	 * Returns the other value than is the parameter.
	 *
	 * @param e A value.
	 * @return The other value.
	 * @throws IllegalArgumentException When the element {@code e} is not in this
	 *                                  set.
	 */
	public E getOtherValue(E e)
	{
		if (e.equals(value1))
			return value2;
		else if (e.equals(value2))
			return value1;
		else
			throw new IllegalArgumentException("The value " + e + " not found.");
	}

	@Override
	public int hashCode()
	{
		final int h1 = Objects.hashCode(this.value1);
		final int h2 = Objects.hashCode(this.value2);
		if (h1 >= h2)
			return 23 * h1 + h2;
		else
			return 23 * h2 + h1;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		final PairSet<E> other = (PairSet<E>) obj;
		if (Objects.equals(this.value1, other.value1) && Objects.equals(this.value2, other.value2))
			return true;
		if (Objects.equals(this.value1, other.value2) && Objects.equals(this.value2, other.value1))
			return true;
		return false;
	}

	@Override
	public String toString()
	{
		return "{" + value1 + ", " + value2 + '}';
	}

	/**
	 * Iterator of the values in the set.
	 */
	private class ValueIterator implements Iterator<E>
	{
		/**
		 * How much was already returned.
		 */
		private int returned = 0;

		@Override
		public boolean hasNext()
		{
			return returned < 2;
		}

		@Override
		public E next()
		{
			returned++;
			switch (returned)
			{
				case 1:
					return value1;
				case 2:
					return value2;
				default:
					throw new NoSuchElementException("There are no values left in the set.");
			}
		}

		/**
		 * This method is not supported.
		 *
		 * @throws UnsupportedOperationException Is always thrown when calling this
		 *                                       method.
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("This method is not supported.");
		}
	}
}
