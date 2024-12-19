
package org.navitrace.api.security;

import java.security.Principal;
import java.util.Date;

public class UserPrincipal implements Principal {

    private final long userId;
    private final Date expiration;

    public UserPrincipal(long userId, Date expiration) {
        this.userId = userId;
        this.expiration = expiration;
    }

    public Long getUserId() {
        return userId;
    }

    public Date getExpiration() {
        return expiration;
    }

    @Override
    public String getName() {
        return null;
    }

}
