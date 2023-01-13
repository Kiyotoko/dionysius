package org.dionysius.grpc;

public interface Creator<T> {
    public T create();
}
