/*
 * @(#)AbstractJWTToken.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;

/**
 * Abstract JWT (Json Web Token) authentication
 *
 * @param <T> the authentication principal type
 * @param <A> the authentication granted authority type
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@SuppressWarnings("unchecked")
public abstract class AbstractJWTToken<T extends UserDetails, A extends GrantedAuthority>
		extends AbstractDto implements Authentication {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	/** The principal class */
	@Getter
	private Class<T> principalClass;
	/** The principal class */
	@Getter
	private Class<A> grantedAuthorityClass;
	/** {@link JWT} */
	@Getter
	private JWT jwt;
	/** The authentication principal */
	private final T principal;
	/** {@link GrantedAuthority} authorities collection */
	@Getter
	private final Collection<A> authorities;
	/** Specify this token has been authenticated or not */
	@Getter
	@Setter
	private boolean authenticated = false;
	/** {@link JWTClaimsSet} */
	@Getter
	private JWTClaimsSet claims;

	/**
	 * Initialize a new instance of {@link AbstractJWTToken}
	 *
	 * @param jwt token
	 * @param claim claim to parse authorities
	 * @param principalClass the authentication principal class
	 * @param grantedAuthorityClass the authentication granted authority class
	 */
	protected AbstractJWTToken(JWT jwt, String claim, Class<T> principalClass, Class<A> grantedAuthorityClass) {
		Assert.notNull(jwt, "jwt");
		Assert.hasText(claim, "claim");
		Assert.notNull(principalClass, "principalClass");
		Assert.notNull(grantedAuthorityClass, "grantedAuthorityClass");
		this.jwt = jwt;
		this.principalClass = principalClass;
		this.grantedAuthorityClass = grantedAuthorityClass;

		// parse token information
		try {
			this.claims = jwt.getJWTClaimsSet();
			// parse principal
			this.principal = BeanUtils.safeType(
					this.parsePrincipal(this.claims),
					this.principalClass);
			// parse claim (as granted authority roles)
			if (this.getPrincipal() != null
					&& CollectionUtils.isEmpty(this.getPrincipal().getAuthorities())) {
		        Object grantedAuthorityItems = jwt.getJWTClaimsSet().getClaim(claim);
		        if (CollectionUtils.isCollectionOf(grantedAuthorityItems, this.grantedAuthorityClass)) {
			        this.authorities = Collections.unmodifiableCollection(
			        		(Collection<A>) grantedAuthorityItems);
		        } else {
		            // FIXME Maybe empty roles because System Administrator has no authorities
		        	// throw new IllegalArgumentException("Invalid claim [" + claim + "]!!!");
		            this.authorities = Collections.unmodifiableCollection(
	                        Collections.emptyList());
		        }

		        // from principal
			} else if (this.getPrincipal() != null
					&& CollectionUtils.isCollectionOf(
							this.getPrincipal().getAuthorities(),
							this.getGrantedAuthorityClass())) {
				this.authorities = Collections.unmodifiableCollection(
						(Collection<A>) this.getPrincipal().getAuthorities());

				// empty authorities collection
			} else {
				this.authorities = Collections.unmodifiableCollection(
						Collections.emptyList());
			}
		} catch (Exception e) {
			super.logger.error(e.getMessage(), e);
			throw new ApplicationRuntimeException(e);
		}
        this.authenticated = false;
	}

	/**
	 * Parse the principal authentication object from the specified JWT principal (such as user name)
	 *
	 * @param claims to parse
	 *
	 * @return the principal authentication object
	 */
	protected abstract T parsePrincipal(JWTClaimsSet claims);

	/* (Non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	@Override
	public final String getName() {
		return (this.getPrincipal() != null
				? this.getPrincipal().getUsername() : null);
	}

	/* (Non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getCredentials()
	 */
	@Override
	public final Object getCredentials() {
		return (this.getPrincipal() != null
				? this.getPrincipal().getCredentials() : null);
	}

	/* (Non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getDetails()
	 */
	@Override
	public final Object getDetails() {
		return (this.getClaims() != null
				? this.getClaims().toJSONObject() : null);
	}

	/* (Non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getPrincipal()
	 */
	@Override
	public final T getPrincipal() {
		return this.principal;
	}
}
