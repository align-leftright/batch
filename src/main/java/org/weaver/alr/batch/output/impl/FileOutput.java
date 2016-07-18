package org.weaver.alr.batch.output.impl;


import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.weaver.alr.batch.common.util.JsonUtil;
import org.weaver.alr.batch.metadata.News;
import org.weaver.alr.batch.output.BasicOutput;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;


@Component
public class FileOutput extends BasicOutput{

	static ConcurrentHashMap<String, Logger> loggerMap  = new ConcurrentHashMap<String, Logger>();
	
	public void process(String channel, Object doc) {
		News news = (News)doc;
		Logger logger = getLogger(news.getChannel());
		logger.info("process : "+channel);
		logger.info(JsonUtil.toJson(doc));
	}

	public synchronized final Logger getLogger(String name){
		Logger logger = loggerMap.get(name);
		if(logger == null){
			logger = createLogger(name);
			loggerMap.put(name, logger);
		}
		return logger;
	}
	
	
	public synchronized final static Logger createLogger(String name){
		LoggerContext context = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
		ch.qos.logback.classic.Logger logger = context.getLogger(name);

	    // Don't inherit root appender
	    logger.setAdditive(false);

	    RollingFileAppender<ILoggingEvent> rollingFile = new RollingFileAppender<ILoggingEvent>();
	    rollingFile.setContext(context);
	    rollingFile.setName("dynamic_logger_fileAppender");

	    // Optional
	    rollingFile.setFile("./output" + File.separator + name+".log");
	    rollingFile.setAppend(true);

	    // Set up rolling policy
	    TimeBasedRollingPolicy<Object> rollingPolicy = new TimeBasedRollingPolicy<Object>();
	    rollingPolicy.setFileNamePattern("/log"
	            + File.separator + "%d{yyyy-MM,aux}"
	            + File.separator + "msg_%d{yyyy-MM-dd_HH-mm}.txt");

	    rollingPolicy.setParent(rollingFile);
	    rollingPolicy.setContext(context);
	    rollingPolicy.start();

	    // set up pattern encoder
	    PatternLayoutEncoder encoder = new PatternLayoutEncoder();
	    encoder.setContext(context);
	    encoder.setPattern("%msg%n");
	    encoder.start();

	    rollingFile.setRollingPolicy(rollingPolicy);
	    rollingFile.setEncoder(encoder);
	    rollingFile.start();

	    // Atach appender to logger
	    logger.addAppender(rollingFile);
	    
	    return logger;
	}
	

}
