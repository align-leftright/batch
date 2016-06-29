package org.weaver.alr.batch.model;

import java.util.List;

public class PipelineVO {
	
	private String name;
	
	private List<PipeVO> pipes;


	public List<PipeVO> getPipes() {
		return pipes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPipes(List<PipeVO> pipes) {
		this.pipes = pipes;
	}

	
	
}
