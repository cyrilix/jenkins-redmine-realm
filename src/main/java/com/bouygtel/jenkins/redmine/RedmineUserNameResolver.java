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
import hudson.security.SecurityRealm;
import hudson.tasks.UserNameResolver;
import jenkins.model.Jenkins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;

/**
 * Implementation of {@link UserNameResolver} using redmine api
 * 
 * @author Cyrille Nofficial
 */
@Extension
public class RedmineUserNameResolver extends UserNameResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserNameResolver.class);

    /**
     * Constructeur
     */
    public RedmineUserNameResolver() {}

    /**
     * {@inheritDoc}
     * 
     * @see hudson.tasks.UserNameResolver#findNameFor(hudson.model.User)
     */
    @Override
    public String findNameFor(hudson.model.User u) {
        LOGGER.info("Search username for {}", u);

        RedmineManager redmineManager = getRedmineManager();
        if (redmineManager == null) {
            return null;
        }

        String userId = u.getId();
        try {
            return redmineManager.getUserByLogin(userId).getFullName();
        } catch (RedmineException e) {
            LOGGER.info("User {} not found: {}", userId, e.getMessage());
            return null;
        }
    }

    /**
     * Return instance of {@link RedmineManager}
     * 
     * @return
     */
    RedmineManager getRedmineManager() {
        SecurityRealm realm = Jenkins.getInstance().getSecurityRealm();
        if (!(realm instanceof RedmineSecurityRealm)) {
            LOGGER.error("Redmine manager not found");
            return null;
        }

        return ((RedmineSecurityRealm) realm).getManager();
    }

}
