package org.weaver.alr.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.select.Elements;
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
import org.weaver.alr.batch.common.util.JsoupUtil;
import org.weaver.alr.batch.common.util.StringUtil;
import org.weaver.alr.batch.metadata.News;
import org.weaver.alr.batch.model.MySyndEntry;
import org.weaver.alr.batch.model.PipeVO;
import org.weaver.alr.batch.model.PipelineVO;
import org.weaver.alr.batch.output.Output;
import org.weaver.alr.batch.output.impl.ElasticSearchOutput;
import org.weaver.alr.batch.pipeline.PipelineManager;
import org.weaver.alr.batch.pipeline.SyncPipe;
import org.weaver.alr.batch.pipeline.impl.HtmlParserPipe;
import org.weaver.alr.batch.pipeline.impl.HtmlSearchPipe;

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
				MySyndEntry myEntry = new MySyndEntry(entry);
				processPipe(myEntry, pipelineManager);
				logger.info("----------------------------------------------------------");
				logger.info(myEntry.getSyndEntry().getLink());
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
				if("HtmlParserPipe".equals(pipeName)){
					syncpipe = new HtmlParserPipe(pipe.getType(), pipe.getSortType());
				}else if("HtmlSearchPipe".equals(pipeName)){
					syncpipe = new HtmlSearchPipe(pipe.getType(), pipe.getKey());
				}else{
					break;
				}
				pipelineManager.addPipe(pipelineName, syncpipe);
			}
		}

		return pipelineManager;
	}

	private MySyndEntry processPipe(MySyndEntry myEntry, PipelineManager pipelineManager){

		if(pipelineManager == null || StringUtil.isEmpty(myEntry.getSyndEntry().getUri())){
			return myEntry;
		}

		String url = myEntry.getSyndEntry().getUri();
		Elements elements = JsoupUtil.getAllElements(url);
		Map<String, Object> result = pipelineManager.doPipeline(elements);

		if(result != null){
			String shortText = (String) result.get("shortText");
			String imageUrl  = (String) result.get("imageUrl");
			myEntry.setShortBody(shortText);
			myEntry.setImageUri(imageUrl);
		}

		return myEntry;
	}

	private void processOutput(MySyndEntry myEntry, Output output, String channelName){
		Map<String, Object> map = new HashMap<String, Object>();

		if(output == null){
			return;
		}

		if(output instanceof ElasticSearchOutput){
			String docId = generateDocId(myEntry.getSyndEntry().getPublishedDate());

			map.put(ElasticSearchOutput.KEY_DOC_ID, docId);
			map.put(ElasticSearchOutput.KEY_INDEX, Constants.ES_INDEX);
			map.put(ElasticSearchOutput.KEY_DOC_TYPE, Constants.ES_TYPE_NEWS);

			News metadata = new News();
			metadata.setChannel(channelName);
			metadata.setDescription(myEntry.getShortBody());
			metadata.setId(docId);
			metadata.setImageUrl(myEntry.getImageUri());
			metadata.setLinkUrl(myEntry.getSyndEntry().getLink());
			metadata.setTitle(myEntry.getSyndEntry().getTitle());
			metadata.setPublishedDate(myEntry.getSyndEntry().getPublishedDate());

			map.put(ElasticSearchOutput.KEY_DOC, metadata);
		}else{
			return;
		}

		output.send(map);
	}

	private String generateDocId(Date date){
		StringBuilder sb = new StringBuilder();
		sb.append(DateUtIl.getDate(date));
		return sb.toString();
	}
	
	



}








