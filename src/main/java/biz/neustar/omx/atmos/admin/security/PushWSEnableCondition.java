package biz.neustar.omx.atmos.admin.security;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class PushWSEnableCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String enable = context.getEnvironment().getProperty("push.ws.server.host.enable");
		return "true".equalsIgnoreCase(enable);
	}

}