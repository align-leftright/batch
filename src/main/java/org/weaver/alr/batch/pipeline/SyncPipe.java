package org.weaver.alr.batch.pipeline;

import java.io.IOException;

public abstract class SyncPipe<I, O> implements Pipe<I, O> {

	private boolean isOpen = true;

	public O process(I input){
		if (isOpen) {
			return use(input);
		}
		else {
			throw new IllegalStateException();
		}
	}

	protected abstract O use(I input);

	public void close() throws IOException {
		this.isOpen = false;
	}
	
	

}