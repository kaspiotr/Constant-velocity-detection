package pl.edu.agh.vconstapp;

public interface StreamDataProvider<E> {
    E get(int idx);
    int size();
    int lastSeen();
}