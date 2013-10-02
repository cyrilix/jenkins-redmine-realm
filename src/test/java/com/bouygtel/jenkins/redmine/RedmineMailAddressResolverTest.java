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

import static org.easymock.EasyMock.createStrictControl;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import hudson.model.User;

import org.easymock.IMocksControl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taskadapter.redmineapi.NotFoundException;
import com.taskadapter.redmineapi.RedmineManager;

/**
 * Test class {@link RedmineMailAddressResolver}
 * 
 * @author Cyrille Nofficial
 */
public class RedmineMailAddressResolverTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedmineMailAddressResolverTest.class);

    /**
     * Default Constructor
     */
    public RedmineMailAddressResolverTest() {}

    /**
     * Test de la méthode {@link RedmineMailAddressResolver#findMailAddressFor(hudson.model.User)}
     */
    @Test
    public void testFindMailAddressFor() {
        LOGGER.info("------ testFindMailAddressFor -------");

        try {
            IMocksControl control = createStrictControl();
            final RedmineManager redmineManager = control.createMock(RedmineManager.class);
            RedmineMailAddressResolver instance = new RedmineMailAddressResolver() {
                /**
                 * {@inheritDoc}
                 * 
                 * @see com.bouygtel.jenkins.redmine.RedmineMailAddressResolver#getRedmineManager()
                 */
                @Override
                RedmineManager getRedmineManager() {
                    return redmineManager;
                }
            };

            User user = control.createMock(User.class);
            com.taskadapter.redmineapi.bean.User userRedmine = new com.taskadapter.redmineapi.bean.User();
            userRedmine.setMail("userId@email.test");

            control.reset();
            expect(user.getId()).andReturn("userId");
            expect(redmineManager.getUserByLogin("userId")).andReturn(userRedmine);
            control.replay();

            String email = instance.findMailAddressFor(user);
            LOGGER.info("Email: {}", email);

            assertThat(email, is("userId@email.test"));
            control.verify();

            // User invalid
            control.reset();
            expect(user.getId()).andReturn("userId");
            expect(redmineManager.getUserByLogin("userId")).andThrow(new NotFoundException("User not found for test"));
            control.replay();

            assertThat(instance.findMailAddressFor(user), nullValue());

            control.verify();

            // None Redmine realm
            instance = new RedmineMailAddressResolver() {
                /**
                 * {@inheritDoc}
                 * 
                 * @see com.bouygtel.jenkins.redmine.RedmineMailAddressResolver#getRedmineManager()
                 */
                @Override
                RedmineManager getRedmineManager() {
                    return null;
                }
            };

            control.reset();
            control.replay();

            assertThat(instance.findMailAddressFor(user), nullValue());

            control.verify();

        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            fail("Erreur imprévue: " + e.getMessage());
        }
    }
}
