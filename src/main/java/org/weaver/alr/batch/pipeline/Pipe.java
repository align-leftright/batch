package org.weaver.alr.batch.pipeline;


public interface Pipe<I, O> extends java.io.Closeable  {
  
	O process(I input);
	
}