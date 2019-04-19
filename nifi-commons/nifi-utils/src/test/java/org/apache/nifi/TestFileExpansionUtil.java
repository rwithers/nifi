/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi;

import org.apache.nifi.util.FileExpansionUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestFileExpansionUtil {
    private static final Logger logger = LoggerFactory.getLogger(TestFileExpansionUtil.class);
    private String originalHome = "";

    @Rule public ExpectedException exception = ExpectedException.none();

    @Before
    public void beforeEach() {
        originalHome = System.getProperty("user.home");
    }

    @After
    public void afterEach() {
        System.setProperty("user.home", originalHome);
    }

   @Test
   public void  testExpandPath() {
       System.setProperty("user.home", "/Users/testuser");

       logger.debug("User Home: " + System.getProperty("user.home"));

       // act
       String result = FileExpansionUtil.expandPath("~/somedirectory");

       // assert
       assertEquals("/Users/testuser/somedirectory", result);
   }

   @Test
    public void testExpandPathShouldReturnNullWhenNullIsInput() {
       System.setProperty("user.home", "/Users/testuser");
       logger.debug("User Home: " + System.getProperty("user.home"));

       // act
       String result = FileExpansionUtil.expandPath(null);

       // assert
       assertNull(result);
   }

   @Test(expected = RuntimeException.class)
    public void testExceptionIsThrownWhenUserHomeIsEmpty() {
       System.setProperty("user.home", "");

       // act
       FileExpansionUtil.expandPath("~/somedirectory");
   }

   @Test
    public void testExceptionIsThrownWhenUserHomeIsNull() {
       String expectedErrorMessage = "Nifi assumes user.home is set to your home directory.  Nifi detected user.home is " +
               "either null or empty and this means your environment can't determine a value for this information.  " +
               "You can get around this by specifying a -Duser.home=<your home directory> when running nifi.";

       exception.expect(RuntimeException.class);
       exception.expectMessage(equalTo(expectedErrorMessage));

       System.getProperties().remove("user.home");

       FileExpansionUtil.expandPath("~/somedirectory");
   }

}
