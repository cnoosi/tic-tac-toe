package Observers;

public interface Subject
{
    void addObserver(Object o);
    void removeObserver(Object o);
    void notifyObservers(ObserverMessage message);
}
