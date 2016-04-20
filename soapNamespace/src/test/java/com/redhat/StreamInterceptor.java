package com.redhat;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Viral Gohel
 */
public class StreamInterceptor extends AbstractPhaseInterceptor<SoapMessage>{
    public StreamInterceptor() {
        super(Phase.PREPARE_SEND);
    }

    public void handleMessage(SoapMessage message) {
        Map hmap = new HashMap();
        hmap.put("bla","http://redhat.com/");
        message.setContextualProperty("soap.env.ns.map", hmap);
        message.setContextualProperty("disable.outputstream.optimization", true);
    }
}
