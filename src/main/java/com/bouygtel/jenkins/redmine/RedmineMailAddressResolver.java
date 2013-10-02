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
import hudson.tasks.MailAddressResolver;
import jenkins.model.Jenkins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;

/**
 * Implementation of {@link MailAddressResolver} to search mail adress with redmine rest api
 * 
 * @author Cyrille Nofficial
 */
@Extension
public class RedmineMailAddressResolver extends MailAddressResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailAddressResolver.class);

    /**
     * Constructeur
     */
    public RedmineMailAddressResolver() {}

    /**
     * {@inheritDoc}
     * 
     * @see hudson.tasks.MailAddressResolver#findMailAddressFor(hudson.model.User)
     */
    @Override
    public String findMailAddressFor(hudson.model.User u) {

        RedmineManager redmineManager = getRedmineManager();
        if (redmineManager == null) {
            return null;
        }

        try {
            return redmineManager.getUserByLogin(u.getId()).getMail();
        } catch (RedmineException e) {
            LOGGER.error("Email not found for {}", u);
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
