package org.dionysius.grpc;

import java.util.Collection;

public interface Observable {
	void changed();

	Collection<PushHelper> getPushHelpers();
}
