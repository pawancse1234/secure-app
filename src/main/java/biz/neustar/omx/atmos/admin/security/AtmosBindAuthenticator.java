package biz.neustar.omx.atmos.admin.security;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.BindAuthenticator;

public class AtmosBindAuthenticator extends BindAuthenticator {

	public AtmosBindAuthenticator(BaseLdapPathContextSource contextSource) {
		super(contextSource);
	}

	@Override
	public DirContextOperations authenticate(Authentication authentication) {
		DirContextOperations dirContext = null;
		try {
			dirContext = super.authenticate(authentication);
		} catch (BadCredentialsException e) {
			throw new AccessDeniedException("Authentication Failed!!");
		}
		return dirContext;
	}

}
