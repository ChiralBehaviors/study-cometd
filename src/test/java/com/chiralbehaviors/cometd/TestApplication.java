/**
 * Copyright (c) 2015 Chiral Behaviors, LLC, all rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chiralbehaviors.cometd;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.HashMap;
import java.util.Map;

import org.cometd.annotation.AnnotationCometDServlet;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author hparry
 *
 */
public class TestApplication extends Application<TestConfiguration> {

    /* (non-Javadoc)
     * @see io.dropwizard.Application#initialize(io.dropwizard.setup.Bootstrap)
     */
    @Override
    public void initialize(Bootstrap<TestConfiguration> bootstrap) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see io.dropwizard.Application#run(io.dropwizard.Configuration, io.dropwizard.setup.Environment)
     */
    @Override
    public void run(TestConfiguration configuration, Environment environment)
                                                                             throws Exception {

        environment.jersey().register(new TestResource());
        ServletHolder holder = environment.getApplicationContext().addServlet(AnnotationCometDServlet.class.getCanonicalName(),
                                                                              "/cometd/*");
        Map<String, String> map = new HashMap<>();
        // map.put("transport", "org.cometd.websocket.server.WebSocketTransport");
        map.put("transport", LongPollingTransport.class.getCanonicalName());
        map.put("services", TestService.class.getCanonicalName());
        holder.setInitParameters(map);
        holder.doStart();

    }
}
