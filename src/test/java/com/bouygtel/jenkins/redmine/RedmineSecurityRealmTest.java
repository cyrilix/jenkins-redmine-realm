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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import hudson.security.GroupDetails;
import hudson.security.SecurityRealm;

import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bouygtel.jenkins.redmine.RedmineSecurityRealm.DescriptorImpl;
import com.taskadapter.redmineapi.NotFoundException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Group;

/**
 * Test class {@link RedmineSecurityRealm}
 * 
 * @author Cyrille Nofficial
 */
public class RedmineSecurityRealmTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedmineSecurityRealmTest.class);

    /**
     * Default Constructor
     */
    public RedmineSecurityRealmTest() {}

    /**
     * Test de la méthode {@link RedmineSecurityRealm#authenticate(String, String)}
     */
    @Test
    public void testAuthenticate() {
        LOGGER.info("------ testAuthenticate -------");

        try {
            IMocksControl control = createStrictControl();

            final RedmineManager redmineManager = control.createMock(RedmineManager.class);

            RedmineSecurityRealm instance = new RedmineSecurityRealm(
                "url", "key") {
                /**
                 * {@inheritDoc}
                 * 
                 * @see com.bouygtel.jenkins.redmine.RedmineSecurityRealm#getUserRedmineManager(java.lang.String,
                 *      java.lang.String)
                 */
                @Override
                RedmineManager getUserRedmineManager(String username, String password) {
                    assertThat(username, is("username"));
                    assertThat(password, is("password"));
                    return redmineManager;
                }
            };

            com.taskadapter.redmineapi.bean.User userRedmine = new com.taskadapter.redmineapi.bean.User();
            userRedmine.setMail("userId@email.test");
            userRedmine.setLogin("username");
            userRedmine.setPassword("password");
            userRedmine.setFullName("User fullName");
            Group groupTest = new Group();
            groupTest.setId(42);
            groupTest.setName("group-test");
            userRedmine.getGroups().add(groupTest);

            RedmineUserDetails expectedUserDetails =
                new RedmineUserDetails("username", "password", true, true, true, true, new GrantedAuthority[]{
                        new RedmineGroupDetails("group-test"), SecurityRealm.AUTHENTICATED_AUTHORITY});

            control.reset();
            expect(redmineManager.getCurrentUser()).andReturn(userRedmine);
            control.replay();

            UserDetails userDetails = instance.authenticate("username", "password");

            assertThat(userDetails, is((UserDetails) expectedUserDetails));
            control.verify();

            // Invalid user
            control.reset();
            expect(redmineManager.getCurrentUser()).andThrow(new NotFoundException("User not found"));
            control.replay();

            try {
                instance.authenticate("username", "password");
                fail("An exception would be throwed");
            } catch (AuthenticationServiceException e) {
                LOGGER.info("Exception throw for test");
            }
            control.verify();
        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            Assert.fail("Erreur imprévue: " + e.getMessage());
        }
    }

    /**
     * Test de la méthode {@link RedmineSecurityRealm#loadGroupByGroupname}
     */
    @Test
    public void testLoadGroupByGroupname() {
        LOGGER.info("------ testLoadGroupByGroupname -------");

        try {
            IMocksControl control = createStrictControl();

            final RedmineManager redmineManager = control.createMock(RedmineManager.class);

            RedmineSecurityRealm instance = new RedmineSecurityRealm(
                "url", "key") {
                /**
                 * {@inheritDoc}
                 * 
                 * @see com.bouygtel.jenkins.redmine.RedmineSecurityRealm#getUserRedmineManager(java.lang.String,
                 *      java.lang.String)
                 */
                @Override
                RedmineManager getUserRedmineManager(String username, String password) {
                    assertThat(username, is("username"));
                    assertThat(password, is("password"));
                    return redmineManager;
                }
            };

            control.reset();
            control.replay();

            GroupDetails result = instance.loadGroupByGroupname("groupname");

            assertThat(result, is((GroupDetails) new RedmineGroupDetails("groupname")));
            control.verify();
        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            Assert.fail("Erreur imprévue: " + e.getMessage());
        }
    }

    /**
     * Test de la méthode {@link RedmineSecurityRealm#loadUserByUsername(String)}
     */
    @Test
    public void testLoadUserByUsername() {
        LOGGER.info("------ testLoadUserByUsername -------");

        try {
            IMocksControl control = createStrictControl();

            final RedmineManager redmineManager = control.createMock(RedmineManager.class);

            RedmineSecurityRealm instance = new RedmineSecurityRealm(
                "url", "key") {
                /**
                 * {@inheritDoc}
                 * 
                 * @see com.bouygtel.jenkins.redmine.RedmineSecurityRealm#getUserRedmineManager(java.lang.String,
                 *      java.lang.String)
                 */
                @Override
                public RedmineManager getManager() {
                    return redmineManager;
                }
            };

            com.taskadapter.redmineapi.bean.User userRedmine = new com.taskadapter.redmineapi.bean.User();
            userRedmine.setMail("userId@email.test");
            userRedmine.setLogin("username");
            userRedmine.setPassword("password");
            userRedmine.setFullName("User fullName");
            Group groupTest = new Group();
            groupTest.setId(42);
            groupTest.setName("group-test");
            userRedmine.getGroups().add(groupTest);

            RedmineUserDetails expectedUserDetails =
                new RedmineUserDetails("username", "password", true, true, true, true, new GrantedAuthority[]{
                        new RedmineGroupDetails("group-test"), SecurityRealm.AUTHENTICATED_AUTHORITY});

            control.reset();
            expect(redmineManager.getUserByLogin("username")).andReturn(userRedmine);
            control.replay();

            UserDetails result = instance.loadUserByUsername("username");

            assertThat(result, is((UserDetails) expectedUserDetails));
            control.verify();

            // User not found
            control.reset();
            expect(redmineManager.getUserByLogin("username")).andThrow(new NotFoundException("User not found"));
            control.replay();

            try {
                instance.loadUserByUsername("username");
                fail("An exception would be throwed");
            } catch (UsernameNotFoundException e) {
                LOGGER.info("Exception throwed for test");
            }
            control.verify();
        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            fail("Erreur imprévue: " + e.getMessage());
        }
    }

    /**
     * Test de la méthode {@link DescriptorImpl#getDisplayName()}
     */
    @Test
    public void testGetDisplayName() {
        LOGGER.info("------ testGetDisplayName -------");

        try {
            DescriptorImpl instance = new DescriptorImpl();

            assertThat(instance.getDisplayName(), is("Redmine"));
        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            Assert.fail("Erreur imprévue: " + e.getMessage());
        }
    }
}
