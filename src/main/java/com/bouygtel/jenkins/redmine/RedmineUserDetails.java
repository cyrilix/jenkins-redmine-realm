//
// Copyright (C) 2013 Bouygues Telecom (cynoffic@bouyguestelecom.fr)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//         http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.bouygtel.jenkins.redmine;

import java.util.Arrays;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;

/**
 * Redmine user
 * 
 * @author Cyrille Nofficial
 */
public class RedmineUserDetails implements UserDetails {

    private static final long serialVersionUID = 7923631043192740335L;

    private GrantedAuthority[] authorities;
    private String password;
    private String username;
    private boolean accountNotExpired;
    private boolean accountNotLocked;
    private boolean credentialsNotExpired;
    private boolean enabled;

    /**
     * 
     * @param username
     * @param password
     * @param enabled
     * @param accountNonExpired
     * @param credentialsNonExpired
     * @param accountNonLocked
     * @param authorities
     */
    public RedmineUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
        boolean credentialsNonExpired, boolean accountNonLocked, GrantedAuthority[] authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNotExpired = accountNonExpired;
        this.credentialsNotExpired = credentialsNonExpired;
        this.accountNotLocked = accountNonLocked;
        this.authorities = authorities;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.acegisecurity.userdetails.UserDetails#getAuthorities()
     */
    @Override
    public GrantedAuthority[] getAuthorities() {
        return authorities;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.acegisecurity.userdetails.UserDetails#getPassword()
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.acegisecurity.userdetails.UserDetails#getUsername()
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.acegisecurity.userdetails.UserDetails#isAccountNonExpired()
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNotExpired;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNotLocked;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.acegisecurity.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNotExpired;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.acegisecurity.userdetails.UserDetails#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    // CHECKSTYLE:OFF CyclomaticComplexity - Méthode générée
    public int hashCode() {
        // CHECKSTYLE:ON CyclomaticComplexity
        final int prime = 31;
        int result = 1;
        result = prime * result + (accountNotExpired ? 1231 : 1237);
        result = prime * result + (accountNotLocked ? 1231 : 1237);
        result = prime * result + Arrays.hashCode(authorities);
        result = prime * result + (credentialsNotExpired ? 1231 : 1237);
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    // CHECKSTYLE:OFF CyclomaticComplexity - Méthode générée
    @Override
    public boolean equals(Object obj) {
        // CHECKSTYLE:ON CyclomaticComplexity
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RedmineUserDetails other = (RedmineUserDetails) obj;
        if (accountNotExpired != other.accountNotExpired) return false;
        if (accountNotLocked != other.accountNotLocked) return false;
        if (!Arrays.equals(authorities, other.authorities)) return false;
        if (credentialsNotExpired != other.credentialsNotExpired) return false;
        if (enabled != other.enabled) return false;
        if (password == null) {
            if (other.password != null) return false;
        } else if (!password.equals(other.password)) return false;
        if (username == null) {
            if (other.username != null) return false;
        } else if (!username.equals(other.username)) return false;
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RedmineUserDetails [authorities=");
        builder.append(Arrays.toString(authorities));
        builder.append(", password=");
        builder.append(password);
        builder.append(", username=");
        builder.append(username);
        builder.append(", accountNotExpired=");
        builder.append(accountNotExpired);
        builder.append(", accountNotLocked=");
        builder.append(accountNotLocked);
        builder.append(", credentialsNotExpired=");
        builder.append(credentialsNotExpired);
        builder.append(", enabled=");
        builder.append(enabled);
        builder.append("]");
        return builder.toString();
    }

}
