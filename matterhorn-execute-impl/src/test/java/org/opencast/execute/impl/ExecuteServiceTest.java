/**
 *  Copyright 2009, 2010 The Regents of the University of California
 *  Licensed under the Educational Community License, Version 2.0
 *  (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at
 *
 *  http://www.osedu.org/licenses/ECL-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS"
 *  BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.opencast.execute.impl;

import org.opencastproject.execute.api.ExecuteException;
import org.opencastproject.execute.api.ExecuteService;
import org.opencastproject.execute.impl.ExecuteServiceImpl;
import org.opencastproject.mediapackage.MediaPackageElement;
import org.opencastproject.mediapackage.MediaPackageElementBuilderFactory;
import org.opencastproject.util.NotFoundException;
import org.opencastproject.workspace.api.Workspace;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * Test suite for the Execute Service
 */
public class ExecuteServiceTest {

  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(ExecuteServiceTest.class);

  private static ExecuteServiceImpl executor;
  private static final String TEXT = "En un lugar de la Mancha de cuyo nombre no quiero acordarme...";
  private static String PATTERN;
  private static URI baseDirURI;
  private static File baseDir;

  @BeforeClass
  public static void prepareTest() throws URISyntaxException, NotFoundException, IOException {
    // Get the base directory
    baseDirURI = ExecuteServiceTest.class.getResource("/").toURI();
    baseDir = new File(baseDirURI);

    // Create the executor service
    executor = new ExecuteServiceImpl();

    // Create a mock workspace
    Workspace workspace = EasyMock.createNiceMock(Workspace.class);
    EasyMock.expect(workspace.get(baseDirURI)).andReturn(baseDir).anyTimes();
    EasyMock.replay(workspace);
    executor.setWorkspace(workspace);
    
    // Set up the text pattern to test
    PATTERN = String.format("The specified track (%s) is in the following location: %s",
            ExecuteService.INPUT_FILE_PATTERN, ExecuteService.INPUT_FILE_PATTERN);
  }

  @Test
  // FIXME Buscar un xeito de testear que se xenera ben a liña de comandos
  public void testNoElements() throws ExecuteException, NotFoundException {
    List<String> params = new ArrayList<String>();
    params.add("echo");
    params.add(TEXT);

    try {
      executor.doProcess(params, (MediaPackageElement)null, null, null);
      Assert.fail("The input element should never be null");
    } catch (NullPointerException e) {
      // This exception is expected
    }
  }

  @Test
  // FIXME Buscar un xeito de testear que se xenera ben a liña de comandos
  public void testWithInputElement() throws ExecuteException, NotFoundException {
    List<String> params = new ArrayList<String>();
    params.add("echo");
    params.add(PATTERN);
    MediaPackageElement element = MediaPackageElementBuilderFactory.newInstance().newElementBuilder().elementFromURI(baseDirURI);

    String result = executor.doProcess(params, element, null, null);

    Assert.assertEquals(result, "");    
  }
  
}
