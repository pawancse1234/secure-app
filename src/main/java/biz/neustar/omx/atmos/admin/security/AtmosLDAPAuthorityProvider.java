package biz.neustar.omx.atmos.admin.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;

/**
 * The Class AtmosLDAPAuthorityProvider.
 */
public class AtmosLDAPAuthorityProvider extends DefaultLdapAuthoritiesPopulator {

	/**
	 * Instantiates a new atmos LDAP authority provider.
	 *
	 * @param contextSource
	 *            the context source
	 * @param groupSearchBase
	 *            the group search base
	 */
	public AtmosLDAPAuthorityProvider(ContextSource contextSource, String groupSearchBase) {
		super(contextSource, groupSearchBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.ldap.userdetails.
	 * DefaultLdapAuthoritiesPopulator#getGroupMembershipRoles(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Set<GrantedAuthority> getGroupMembershipRoles(String userDn, String username) {
		DirContextAdapter obj = (DirContextAdapter) getLdapTemplate().lookup(userDn);
		String[] attrs = obj.getStringAttributes("neuRole");
		Set<GrantedAuthority> roles = new HashSet<>();
		for (String attr : attrs) {
			roles.add(new SimpleGrantedAuthority(attr));
		}
		return roles;
	}

}
