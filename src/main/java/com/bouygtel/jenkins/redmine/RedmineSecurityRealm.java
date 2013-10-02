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

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.security.AbstractPasswordBasedSecurityRealm;
import hudson.security.GroupDetails;
import hudson.security.SecurityRealm;

import java.util.HashSet;
import java.util.Set;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Group;
import com.taskadapter.redmineapi.bean.User;

/**
 * Security realm over redmine rest api
 * 
 * @author Cyrille Nofficial
 */
public class RedmineSecurityRealm extends AbstractPasswordBasedSecurityRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedmineSecurityRealm.class);

    private final String url;
    private final String key;

    /**
     * Constructeur
     * 
     * @param url redmine url
     * @param key redmine api key
     */
    @DataBoundConstructor
    public RedmineSecurityRealm(String url, String key) {
        this.url = url;
        this.key = key;
    }

    /**
     * {@inheritDoc}
     * 
     * @see hudson.security.AbstractPasswordBasedSecurityRealm#authenticate(java.lang.String, java.lang.String)
     */
    @Override
    protected UserDetails authenticate(String username, String password) throws AuthenticationException {
        try {
            RedmineManager currentManager = getUserRedmineManager(username, password);
            return getUserDetails(currentManager.getCurrentUser());
        } catch (RedmineException e) {
            LOGGER.error("login failed for user {}: {}", username, e.getMessage(), e);
            throw new AuthenticationServiceException("login failed for user " + username, e);
        }
    }

    /**
     * Return {@link RedmineManager} for user authentication
     * 
     * @param username redmine login
     * @param password redmine password
     * 
     * @return {@link RedmineManager} instance
     */
    RedmineManager getUserRedmineManager(String username, String password) {
        return new RedmineManager(url, username, password);
    }

    private UserDetails getUserDetails(User user) {
        return new RedmineUserDetails(user.getLogin(), user.getPassword(), true, true, true, true, getGroups(user));
    }

    private GrantedAuthority[] getGroups(User user) {
        Set<GrantedAuthority> groups = new HashSet<GrantedAuthority>();
        for (Group group : user.getGroups()) {
            groups.add(new RedmineGroupDetails(group.getName()));
        }
        groups.add(SecurityRealm.AUTHENTICATED_AUTHORITY);
        return groups.toArray(new GrantedAuthority[groups.size()]);
    }

    /**
     * Return {@link RedmineManager} instance
     * 
     * @return {@link RedmineManager} instance
     */
    public RedmineManager getManager() {
        return new RedmineManager(url, key);
    }

    /**
     * Redmine plugin descriptor
     * 
     * @author Cyrille Nofficial
     */
    @Extension
    public static final class DescriptorImpl extends Descriptor<SecurityRealm> {
        /**
         * {@inheritDoc}
         * 
         * @see hudson.model.Descriptor#getDisplayName()
         */
        @Override
        public String getDisplayName() {
            return "Redmine";
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see hudson.security.AbstractPasswordBasedSecurityRealm#loadGroupByGroupname(java.lang.String)
     */
    @Override
    public GroupDetails loadGroupByGroupname(String groupname) {
        LOGGER.info("Search group {}", groupname);
        return new RedmineGroupDetails(groupname);
    }

    /**
     * {@inheritDoc}
     * 
     * @see hudson.security.AbstractPasswordBasedSecurityRealm#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            RedmineManager redmineManager = getManager();
            return getUserDetails(redmineManager.getUserByLogin(username));
        } catch (RedmineException e) {
            LOGGER.info("User {} not found", username);
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }
}
