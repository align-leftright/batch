package org.weaver.alr.batch.pipeline;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class PipelineManager {

	private Map<String, LinkedList<Pipe>> pipesMap = new HashMap<String, LinkedList<Pipe>>();
	private Map<String, Object> resultMap;

	private String name;

	public PipelineManager(String name){
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addPipe(String name, Pipe pipe){
		LinkedList<Pipe> pipes = pipesMap.get(name);
		if(pipes == null){
			pipesMap.put(name, new LinkedList<Pipe>());
		}
		pipesMap.get(name).add(pipe);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> doPipeline(Object initialInput){

		resultMap = new HashMap<String, Object>();

		for(String key : pipesMap.keySet()){

			LinkedList<Pipe> pipeList = pipesMap.get(key);

			Object input = initialInput;
			Object output = null;
			Pipe handler;

			for (int i = 0; i < pipeList.size(); i++) {
				handler = pipeList.get(i);
				output = handler.process(input);
				input = output;
			}

			resultMap.put(key, output);
		}

		return resultMap;
	}


}
