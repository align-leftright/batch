package org.weaver.alr.batch.model;

import java.util.List;

public class TaskVO {
	
	private String name;
	
	private InputVO input;
	
	private List<PipelineVO> pipeline;
	
	private OutputVO output;

	public InputVO getInput() {
		return input;
	}

	public List<PipelineVO> getPipeline() {
		return pipeline;
	}

	public OutputVO getOutput() {
		return output;
	}

	public void setInput(InputVO input) {
		this.input = input;
	}

	public void setPipeline(List<PipelineVO> pipeline) {
		this.pipeline = pipeline;
	}

	public void setOutput(OutputVO output) {
		this.output = output;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	

}
