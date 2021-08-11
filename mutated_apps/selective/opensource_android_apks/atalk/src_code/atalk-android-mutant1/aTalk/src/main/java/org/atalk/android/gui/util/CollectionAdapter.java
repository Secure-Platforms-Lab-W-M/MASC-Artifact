/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.android.gui.util;

import android.app.Activity;
import android.view.*;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import java.util.*;

/**
 * Convenience class wrapping set of elements into {@link Adapter}
 *
 * @param <T> class of the elements contained in this adapter
 * @author Pawel Domas
 * @author Eng Chong Meng
 */
public abstract class CollectionAdapter<T> extends BaseAdapter
{
    /**
     * List of elements handled by this adapter
     */
    private List<T> items;

    /**
     * The parent {@link Activity}
     */
    private final Activity parentActivity;

    /**
     * Creates a new instance of {@link CollectionAdapter}
     *
     * @param parent the parent {@link Activity}
     */
    public CollectionAdapter(Activity parent)
    {
        this.parentActivity = parent;
    }

    /**
     * Creates new instance of {@link CollectionAdapter}
     *
     * @param parent the parent {@link Activity}
     * @param items iterator of {@link T} items
     */
    public CollectionAdapter(Activity parent, Iterator<T> items)
    {
        this.parentActivity = parent;
        setIterator(items);
    }

    /**
     * The method that accepts {@link Iterator} as a source set of objects
     *
     * @param iterator source of {@link T} instances that will be contained in this {@link CollectionAdapter}
     */
    protected void setIterator(Iterator<T> iterator)
    {
        items = new ArrayList<T>();
        while (iterator.hasNext())
            items.add(iterator.next());
    }

    /**
     * Accepts {@link List} as a source set of {@link T}
     *
     * @param collection the {@link List} that will be included in this {@link CollectionAdapter}
     */
    protected void setList(List<T> collection)
    {
        items = new ArrayList<T>();
        items.addAll(collection);
    }

    /**
     * Returns total count of items contained in this adapter
     *
     * @return the count of {@link T} stored in this {@link CollectionAdapter}
     */
    public int getCount()
    {
        return items.size();
    }

    public Object getItem(int i)
    {
        return items.get(i);
    }

    public long getItemId(int i)
    {
        return i;
    }

    /**
     * Convenience method for retrieving {@link T} instances
     *
     * @param i the index of {@link T} that will be retrieved
     * @return the {@link T} object located at <tt>i</tt> position
     */
    protected T getObject(int i)
    {
        return (T) items.get(i);
    }

    /**
     * Adds <tt>object</tt> to the adapter
     *
     * @param object instance of {@link T} that will be added to this adapter
     */
    public void add(T object)
    {
        if (!items.contains(object)) {
            items.add(object);
            doRefreshList();
        }
    }

    /**
     * Insert given object at specified position without notifying about adapter data change.
     *
     * @param pos the position at which given object will be inserted.
     * @param object the object to insert into adapter's list.
     */
    protected void insert(int pos, T object)
    {
        items.add(pos, object);
    }

    /**
     * Removes the <tt>object</tt> from this adapter
     *
     * @param object instance of {@link T} that will be removed from the adapter
     */
    public void remove(final T object)
    {
        // Remove item on UI thread to make sure it's not being painted at the same time
        parentActivity.runOnUiThread(() -> {
            if (items.remove(object)) {
                doRefreshList();
            }
        });
    }

    /**
     * Runs list change notification on the UI thread
     */
    protected void doRefreshList()
    {
        parentActivity.runOnUiThread(this::notifyDataSetChanged);
    }

    /**
     * {@inheritDoc}
     */
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        return getView(false, items.get(i), viewGroup, parentActivity.getLayoutInflater());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getView(true, items.get(position), parent, parentActivity.getLayoutInflater());
    }

    /**
     * Convenience method for creating new {@link View}s for each adapter's object
     *
     * @param isDropDown <tt>true</tt> if the <tt>View</tt> should be created for drop down spinner item
     * @param item the item for which a new View shall be created
     * @param parent {@link ViewGroup} parent View
     * @param inflater the {@link LayoutInflater} for creating new Views
     * @return a {@link View} for given <tt>item</tt>
     */
    protected abstract View getView(boolean isDropDown, T item, ViewGroup parent, LayoutInflater inflater);

    /**
     * The parent {@link android.app.Activity}
     */
    protected Activity getParentActivity()
    {
        return parentActivity;
    }
}
