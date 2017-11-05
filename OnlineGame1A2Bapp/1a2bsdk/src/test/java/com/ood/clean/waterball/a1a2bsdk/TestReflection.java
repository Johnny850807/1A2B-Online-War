package com.ood.clean.waterball.a1a2bsdk;

import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;

import org.junit.Test;

import javax.inject.Inject;

import container.base.Client;
import container.protocol.ProtocolFactory;

import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestReflection {
    private @Inject EventBus eventBus;
    private @Inject Client client;
    private @Inject ProtocolFactory protocolFactory;

    @Test
    public void addition_isCorrect() throws Exception {
        Component.inject(this);
        assertNotNull(eventBus);
        assertNotNull(client);
        assertNotNull(protocolFactory);
    }
}