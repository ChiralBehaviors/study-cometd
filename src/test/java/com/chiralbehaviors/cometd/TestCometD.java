package com.chiralbehaviors.cometd;
import java.util.HashMap;
import java.util.Map;

import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.HttpClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.http.HttpClientTransportOverHTTP;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import org.eclipse.jetty.util.thread.Scheduler;
import org.junit.Test;

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

/**
 * @author hparry
 *
 */
public class TestCometD {

    @Test
    public void testCometD() throws Exception {
        TestApplication app = new TestApplication();
        app.run(new String[] {"server", "src/test/resources/server.yml"});
        System.out.println("Success!");
        
        String url = "http://localhost:8080/cometd";
        Map<String, Object> options = new HashMap<>();
        HttpClientTransportOverHTTP clientTransport = new HttpClientTransportOverHTTP();
        clientTransport.setHttpClient(null);
        HttpClient httpClient = new HttpClient(clientTransport, null);
        Scheduler scheduler = new ScheduledExecutorScheduler();
        httpClient.setScheduler(scheduler);
        ClientTransport transport = new LongPollingTransport(url, options, httpClient);
        BayeuxClient client = new BayeuxClient(url, transport);
        client.handshake();
    }
}
