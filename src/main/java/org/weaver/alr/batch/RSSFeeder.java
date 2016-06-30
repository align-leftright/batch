package org.weaver.alr.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.stereotype.Component;
import org.weaver.alr.batch.DynamicChannelResolver.ChannelManagement;
import org.weaver.alr.batch.common.Constants;
import org.weaver.alr.batch.model.MySyndEntry;
import org.weaver.alr.batch.model.PipeVO;
import org.weaver.alr.batch.model.PipelineVO;
import org.weaver.alr.batch.news.Metadata;
import org.weaver.alr.batch.output.Output;
import org.weaver.alr.batch.output.impl.ElasticSearchOutput;
import org.weaver.alr.batch.pipeline.PipelineManager;
import org.weaver.alr.batch.pipeline.SyncPipe;
import org.weaver.alr.batch.pipeline.impl.HtmlParserPipe;
import org.weaver.alr.batch.pipeline.impl.HtmlSearchPipe;
import org.weaver.alr.batch.util.DateUtIl;
import org.weaver.alr.batch.util.JsoupUtil;
import org.weaver.alr.batch.util.StringUtil;

import com.rometools.rome.feed.synd.SyndEntry;

@Component
public class RSSFeeder {


	private static final Logger logger = LoggerFactory.getLogger(RSSFeeder.class);

	@Autowired
	private DynamicChannelResolver dynamicChannelResolver;

	@SuppressWarnings("unchecked")
	public void run(String channelName, String url, List<PipelineVO> pipelineList, Output output){

		try {
			ChannelManagement channelManagement = dynamicChannelResolver.resolve(url);
			PollableChannel feedChannel    = (PollableChannel) channelManagement.channel;
			channelManagement.start();

			Message<SyndEntry> message;
			while( (message = (Message<SyndEntry>)feedChannel.receive()) != null){

				logger.debug("----------------------------------------------------------");
				logger.debug(url);

				SyndEntry entry = (SyndEntry)message.getPayload();
				MySyndEntry myEntry = new MySyndEntry(entry);

				PipelineManager pipelineManager = buildPipelineManager(channelName, pipelineList);
				processPipe(myEntry, pipelineManager);
				logger.debug("\n"+myEntry.toString());

				processOutput(myEntry, output, channelName);
				
				logger.debug("----------------------------------------------------------");
			}

		} catch(Exception e ){
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

		Elements elements = JsoupUtil.getAllElements(myEntry.getSyndEntry().getUri());
		logger.debug("pipelineManager name : "+pipelineManager.getName());
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
		
		if(output instanceof ElasticSearchOutput){
			String docId = generateDocId(myEntry.getSyndEntry().getPublishedDate());

			map.put(ElasticSearchOutput.KEY_DOC_ID, docId);
			map.put(ElasticSearchOutput.KEY_INDEX, Constants.ES_INDEX);
			map.put(ElasticSearchOutput.KEY_DOC_TYPE, Constants.ES_TYPE_NEWS);
			
			Metadata metadata = new Metadata();
			metadata.setChannel(channelName);
			metadata.setDescription(myEntry.getSyndEntry().getDescription().toString());
			metadata.setId(docId);
			metadata.setImageUrl(myEntry.getImageUri());
			metadata.setLinkUrl(myEntry.getSyndEntry().getLink());
			metadata.setTitle(myEntry.getSyndEntry().getTitle());
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








