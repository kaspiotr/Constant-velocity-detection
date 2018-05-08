package pl.edu.agh.vconstapp.util;

public interface MultiStreamDataProvider<E> {
    Iterable<String> getStreamTags();
    boolean containsStreamTag(String tag);
    StreamDataProvider<E> getStream(String tag);
}
