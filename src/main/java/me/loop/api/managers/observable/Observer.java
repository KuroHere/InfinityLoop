package me.loop.api.managers.observable;

public interface Observer<T>
{
    /**
     * Should be called by the {@link Observable} this
     * Observer is registered in. Notifies this Observer
     * about a value change.
     *
     * @param value the value.
     */
    void onChange(T value);

}
