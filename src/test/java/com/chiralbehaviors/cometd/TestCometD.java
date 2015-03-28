package com.chiralbehaviors.cometd;

import java.util.HashMap;
import java.util.Map;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
import org.junit.Test;
import static org.junit.Assert.*;

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

    private final ClientSessionChannel.MessageListener testListener = new TestListener();

    @Test
    public void testCometD() throws Exception {
        String channelName = "/myChannel/";
        TestApplication app = new TestApplication();
        app.run(new String[] { "server", "src/test/resources/server.yml" });
        System.out.println("Success!");

        String url = "http://localhost:8080/cometd";
        Map<String, Object> options = new HashMap<>();

        HttpClient httpClient = new HttpClient();
        httpClient.start();

        ClientTransport transport = new LongPollingTransport(url, options,
                                                             httpClient);
        BayeuxClient client = new BayeuxClient(url, transport);
        client.handshake();
        boolean handshaken = client.waitFor(1000, BayeuxClient.State.CONNECTED);
        assertTrue(handshaken);
        if (handshaken) {
            client.getChannel(channelName).subscribe(testListener);
        } 
    }

    private static class TestListener implements
            ClientSessionChannel.MessageListener {

        /* (non-Javadoc)
         * @see org.cometd.bayeux.client.ClientSessionChannel.MessageListener#onMessage(org.cometd.bayeux.client.ClientSessionChannel, org.cometd.bayeux.Message)
         */
        @Override
        public void onMessage(ClientSessionChannel channel, Message message) {
            System.out.println("Message received: " + message);

        }

    }
}
