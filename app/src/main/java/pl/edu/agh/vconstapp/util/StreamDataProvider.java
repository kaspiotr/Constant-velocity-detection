package pl.edu.agh.vconstapp.util;

public interface StreamDataProvider<E> {
    E get(int idx);
    int size();
    int lastSeen();
}