package biz.neustar.omx.atmos.admin.security;

import static java.util.stream.Collectors.toList;
import static org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;

/**
 * SessionRegistry that retrieves session information from Spring Session,
 * rather than maintaining it by itself. This allows concurrent session
 * management with Spring Security in a clustered environment.
 * <p>
 * Note that expiring a SessionInformation when reaching the configured maximum
 * will simply delete an existing session rather than marking it as expired,
 * since Spring Session has no way to programmatically mark a session as
 * expired. This means that you cannot configure an expired URL; users will
 * simply lose their session as if they logged out.
 * <p>
 * Relies on being able to derive the same String-based representation of the
 * principal given to {@link #getAllSessions(Object, boolean)} as used by Spring
 * Session in order to look up the user's sessions.
 * <p>
 * Does not support {@link #getAllPrincipals()}, since that information is not
 * available.
 */
public class OMXSessionRepository implements SessionRegistry {

	/** The session repository. */
	private FindByIndexNameSessionRepository<? extends ExpiringSession> sessionRepository;

	/**
	 * Instantiates a new OMX session repository.
	 *
	 * @param sessionRepository the session repository
	 */
	@Autowired
	public OMXSessionRepository(FindByIndexNameSessionRepository<? extends ExpiringSession> sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.session.SessionRegistry#getAllPrincipals()
	 */
	@Override
	public List<Object> getAllPrincipals() {
		throw new UnsupportedOperationException(
				"OMXSessionRepositary does not support retrieving all principals, since Spring Session provides no way to obtain that information");
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.session.SessionRegistry#getAllSessions(java.lang.Object, boolean)
	 */
	@Override
	public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
		return sessionRepository.findByIndexNameAndIndexValue(PRINCIPAL_NAME_INDEX_NAME, name(principal)).values()
				.stream().filter(session -> includeExpiredSessions || !session.isExpired())
				.map(session -> new SpringSessionBackedSessionInfo(session, sessionRepository))
				.collect(toList());
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.session.SessionRegistry#getSessionInformation(java.lang.String)
	 */
	@Override
	public SessionInformation getSessionInformation(String sessionId) {
		ExpiringSession session = sessionRepository.getSession(sessionId);
		if (session != null) {
			return new SpringSessionBackedSessionInfo(session, sessionRepository);
		}
		return null;
	}

	/**
	 * This is a no-op, as we don't administer sessions ourselves.
	 *
	 * @param sessionId the session id
	 */
	@Override
	public void refreshLastRequest(String sessionId) {
		throw new UnsupportedOperationException(
				"OMXSessionRepositary does not support refreshing Last Request");
	}

	/**
	 * This is a no-op, as we don't administer sessions ourselves.
	 *
	 * @param sessionId the session id
	 * @param principal the principal
	 */
	@Override
	public void registerNewSession(String sessionId, Object principal) {
		throw new UnsupportedOperationException(
				"OMXSessionRepositary does not support registering New Session");
	}

	/**
	 * This is a no-op, as we don't administer sessions ourselves.
	 *
	 * @param sessionId the session id
	 */
	@Override
	public void removeSessionInformation(String sessionId) {
		throw new UnsupportedOperationException(
				"OMXSessionRepositary does not support removing Session Information");
	}

	/**
	 * Derives a String name for the given principal.
	 *
	 * @param principal the principal
	 * @return the string
	 */
	private String name(Object principal) {
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		if (principal instanceof Principal) {
			return ((Principal) principal).getName();
		}
		return principal.toString();
	}
}