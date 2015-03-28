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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.cometd.annotation.Service;
import org.cometd.annotation.Session;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ConfigurableServerChannel;
import org.cometd.bayeux.server.LocalSession;
import org.cometd.bayeux.server.ServerChannel;
import org.cometd.bayeux.server.ServerMessage;

/**
 * @author hparry
 *
 */
@Service
public class TestService {
    @Inject
    private BayeuxServer  bayeuxServer;
    @Session
    private LocalSession  sender;

    private final String  _channelName;

    private ServerChannel _channel = null;

    public TestService() {
        _channelName = "/myChannel";
    }

    @PostConstruct
    private void initChannel() {
        bayeuxServer.createChannelIfAbsent(_channelName,
                                           new ConfigurableServerChannel.Initializer() {
                                               @Override
                                               public void configureChannel(ConfigurableServerChannel channel) {
                                                   // ...
                                               }
                                           });
        _channel = bayeuxServer.getChannel(_channelName);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                Map<String, String> data = new HashMap<>();
                data.put("channelId", _channelName);
                data.put("message", "Hello Cleveland!");
                ServerMessage.Mutable message = bayeuxServer.newMessage();
                message.setChannel(_channelName);
                message.setData("Hello Cleveland");
                message.setLazy(true);
                
                _channel.publish(sender, message);

            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

}
