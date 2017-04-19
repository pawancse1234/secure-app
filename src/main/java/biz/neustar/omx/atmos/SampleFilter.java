package biz.neustar.omx.atmos;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class SampleFilter extends Filter<ILoggingEvent> {

	@Override
	public FilterReply decide(ILoggingEvent event) {
		if (event.getLoggerName().contains("AbstractInactivityMonitor")
				|| event.getLoggerName().contains("ActiveMQMessageConsumer")
				|| event.getLoggerName().contains("QuartzSchedulerThread")
				|| event.getLoggerName().contains("RedisConnectionUtils")
				|| event.getLoggerName().contains("DefaultJmsMessageListenerContainer")
				|| event.getLoggerName().contains("RedisSessionExpirationPolicy")) {
			return FilterReply.DENY;
		} else {
			return FilterReply.NEUTRAL;
		}
	}
}