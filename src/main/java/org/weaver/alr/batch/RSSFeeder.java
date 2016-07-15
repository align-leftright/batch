package org.weaver.alr.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.stereotype.Component;
import org.weaver.alr.batch.common.Constants;
import org.weaver.alr.batch.common.util.DateUtIl;
import org.weaver.alr.batch.metadata.News;
import org.weaver.alr.batch.model.MySyndEntry;
import org.weaver.alr.batch.model.PipeVO;
import org.weaver.alr.batch.model.PipelineVO;
import org.weaver.alr.batch.output.Output;
import org.weaver.alr.batch.pipeline.PipelineManager;
import org.weaver.alr.batch.pipeline.SyncPipe;
import org.weaver.alr.batch.pipeline.impl.HtmlParserPipe;
import org.weaver.alr.batch.pipeline.impl.HtmlSearchPipe;
import org.weaver.alr.batch.pipeline.impl.SyndToElementPipe;
import org.weaver.alr.batch.pipeline.impl.YoutubeMediaParsePipe;

import com.rometools.rome.feed.synd.SyndEntry;

@Component
@Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RSSFeeder extends Thread{

	private static final Logger logger = LoggerFactory.getLogger(RSSFeeder.class);

	private String channelName;
	private List<PipelineVO> pipelineList;
	private Output output;
	private PollableChannel channel;
	private PropertiesPersistingMetadataStore metadataStore;


	private boolean isInitialized = false;


	public void initialize(String channelName, PollableChannel channel, PropertiesPersistingMetadataStore metadataStore, List<PipelineVO> pipelineList, Output output){
		this.channelName = channelName;
		this.pipelineList = pipelineList;
		this.output = output;
		this.channel = channel;
		this.metadataStore = metadataStore;
		isInitialized = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		if(!isInitialized){
			return;
		}

		try {
			PipelineManager pipelineManager = buildPipelineManager(channelName, pipelineList);

			Message<SyndEntry> message;
			while((message = (Message<SyndEntry>) channel.receive(10000)) != null) {
				SyndEntry entry = (SyndEntry)message.getPayload();
				
				
				MySyndEntry myEntry = processPipe(entry, pipelineManager);
				
				logger.info("----------------------------------------------------------");
				logger.info(myEntry.toString());
				
				processOutput(myEntry, output, channelName);
				metadataStore.flush();
			}  

		}catch(Exception e ){
			logger.error(e.toString());
		}
	}


	private PipelineManager buildPipelineManager(String channelName, List<PipelineVO> pipelineList){

		if(pipelineList == null){
			return null;
		}

		PipelineManager pipelineManager = new PipelineManager(channelName);
		for(PipelineVO pipelineVO : pipelineList){

			String pipelineName = pipelineVO.getName();
			for(PipeVO pipe : pipelineVO.getPipes()){
				String pipeName = pipe.getPipe();
				SyncPipe<?, ?> syncpipe;
				
				if("SyndToElementPipe".equals(pipeName)){
					syncpipe = new SyndToElementPipe();
				}else if("HtmlParserPipe".equals(pipeName)){
					syncpipe = new HtmlParserPipe(pipe.getType(), pipe.getSortType());
				}else if("HtmlSearchPipe".equals(pipeName)){
					syncpipe = new HtmlSearchPipe(pipe.getType(), pipe.getKey());
				}else if("YoutubeMediaPipe".equals(pipeName)){
					syncpipe = new YoutubeMediaParsePipe();
				}else{
					break;
				}
				pipelineManager.addPipe(pipelineName, syncpipe);
			}
		}

		return pipelineManager;
	}

	private MySyndEntry processPipe(SyndEntry entry, PipelineManager pipelineManager){

		if(pipelineManager == null || entry == null){
			return null;
		}
		
		MySyndEntry myEntry = new MySyndEntry(entry);

		Map<String, Object> result = pipelineManager.doPipeline(entry);
		if(result != null){
			String shortText = (String) result.get(Constants.Pipeline.SHORT_TEXT);
			String imageUrl  = (String) result.get(Constants.Pipeline.IMAGE_URL);
			String contentUrl= (String) result.get(Constants.Pipeline.CONTENT_URL);
			myEntry.setShortBody(shortText);
			myEntry.setImageUrl(imageUrl);
			myEntry.setContentUrl(contentUrl);
		}

		return myEntry;
	}

	private void processOutput(MySyndEntry myEntry, Output output, String channelName){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String docId = generateDocId(myEntry.getSyndEntry().getPublishedDate());
		map.put(Constants.Output.KEY_DOC_ID, docId);

		News metadata = new News();
		metadata.setChannel(channelName);
		metadata.setDescription(myEntry.getShortBody());
		metadata.setId(docId);
		metadata.setImageUrl(myEntry.getImageUrl());
		
		if(myEntry.getContentUrl()!=null){
			metadata.setLinkUrl(myEntry.getContentUrl());
		}else{
			metadata.setLinkUrl(myEntry.getSyndEntry().getLink());
		}
		
		
		metadata.setTitle(myEntry.getSyndEntry().getTitle());
		metadata.setPublishedDate(myEntry.getSyndEntry().getPublishedDate());
		map.put(Constants.Output.KEY_DOC, metadata);
	
		if(output != null){
			output.send(map);
		}
	}

	
	private String generateDocId(Date date){
		StringBuilder sb = new StringBuilder();
		sb.append(DateUtIl.getDate(date));
		return sb.toString();
	}





}








