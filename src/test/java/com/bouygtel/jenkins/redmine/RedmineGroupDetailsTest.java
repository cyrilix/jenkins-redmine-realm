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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class {@link RedmineGroupDetails}
 * 
 * @author Cyrille Nofficial
 */
public class RedmineGroupDetailsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedmineGroupDetailsTest.class);

    /**
     * Default Constructor
     */
    public RedmineGroupDetailsTest() {}

    /**
     * Test de la méthode {@link RedmineGroupDetails#getName()}
     */
    @Test
    public void testGetName() {
        LOGGER.info("------ testGetName -------");

        try {
            RedmineGroupDetails instance = new RedmineGroupDetails("nameGroup");
            assertThat(instance.getName(), is("nameGroup"));

        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            fail("Erreur imprévue: " + e.getMessage());
        }
    }

    /**
     * Test de la méthode {@link RedmineGroupDetails#getDisplayName()}
     */
    @Test
    public void testGetDisplayName() {
        LOGGER.info("------ testGetDisplayName -------");

        try {
            RedmineGroupDetails instance = new RedmineGroupDetails("nameGroup");
            assertThat(instance.getDisplayName(), is("nameGroup"));

        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            fail("Erreur imprévue: " + e.getMessage());
        }
    }

    /**
     * Test de la méthode {@link RedmineGroupDetails#getAuthority()}
     */
    @Test
    public void testGetAuthority() {
        LOGGER.info("------ testGetAuthority -------");

        try {
            RedmineGroupDetails instance = new RedmineGroupDetails("nameGroup");
            assertThat(instance.getAuthority(), is("nameGroup"));

        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            fail("Erreur imprévue: " + e.getMessage());
        }
    }
}
