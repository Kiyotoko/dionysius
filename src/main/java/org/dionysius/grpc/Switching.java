package org.dionysius.grpc;

public interface Switching<T> {
	void switched(T associated);

	public interface ExtendableSwitching extends Switching<Object> {

	}
}
