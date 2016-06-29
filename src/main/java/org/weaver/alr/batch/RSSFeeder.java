package org.weaver.alr.batch;

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
import org.weaver.alr.batch.model.MySyndEntry;
import org.weaver.alr.batch.model.PipeVO;
import org.weaver.alr.batch.model.PipelineVO;
import org.weaver.alr.batch.pipeline.PipelineManager;
import org.weaver.alr.batch.pipeline.SyncPipe;
import org.weaver.alr.batch.pipeline.impl.HtmlParserPipe;
import org.weaver.alr.batch.pipeline.impl.HtmlSearchPipe;
import org.weaver.alr.batch.util.JsoupUtil;
import org.weaver.alr.batch.util.StringUtil;

import com.rometools.rome.feed.synd.SyndEntry;

@Component
public class RSSFeeder {
	
	
	private static final Logger logger = LoggerFactory.getLogger(RSSFeeder.class);

	@Autowired
	private DynamicChannelResolver dynamicChannelResolver;

	@SuppressWarnings("unchecked")
	public void run(String name, String url, List<PipelineVO> pipelineList){

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
				
				
				PipelineManager pipelineManager = buildPipelineManager(name, pipelineList);
				processPipe(myEntry, pipelineManager);
				
				logger.debug(myEntry.toString());
				logger.debug("----------------------------------------------------------");
			}

		} catch(Exception e ){
			logger.error(e.toString());
		}
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

	
	
	private PipelineManager buildPipelineManager(String name, List<PipelineVO> pipelineList){
		
		if(pipelineList == null){
			return null;
		}
		
		PipelineManager pipelineManager = new PipelineManager(name);
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

}








