package com.redhat.test;

import com.redhat.client.Client;

import org.apache.aries.blueprint.compendium.cm.CmNamespaceHandler;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.ObjectHelper;
import org.junit.Test;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

public class TestBlueprint extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/camelContext.xml,OSGI-INF/blueprint/sts.xml";
    }
    
    /* (non-Javadoc
     * @see org.apache.camel.test.blueprint.CamelBlueprintTestSupport#expectBlueprintContainerReloadOnConfigAdminUpdate()
     */
    @Override
    protected boolean expectBlueprintContainerReloadOnConfigAdminUpdate() {
    	 boolean expectedReload = false;
    		Iterator<Object> it = ObjectHelper.createIterator(getBlueprintDescriptor());
    	    while (it.hasNext()) {
    	        String s = (String) it.next();
    		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		    dbf.setNamespaceAware(true);
    		    try {
    		        Set<String> cmNamesaces = new HashSet<String>(Arrays.asList(
    		                CmNamespaceHandler.BLUEPRINT_CM_NAMESPACE_1_1,
    		                CmNamespaceHandler.BLUEPRINT_CM_NAMESPACE_1_2,
    		                CmNamespaceHandler.BLUEPRINT_CM_NAMESPACE_1_3
    		        ));
    		        
    		       /* DocumentBuilder db = dbf.newDocumentBuilder();
    		        Document doc = db.parse(ResourceHelper.resolveResourceAsInputStream(RESOLVER, s));
    		        NodeList nl = doc.getDocumentElement().getChildNodes();
    		        for (int i = 0; i < nl.getLength(); i++) {
    		            Node node = nl.item(i);
    		            if (node instanceof Element) {
    		                Element pp = (Element) node;
    		                if (cmNamesaces.contains(pp.getNamespaceURI())) {
    		                    String us = pp.getAttribute("update-strategy");
    		                    if (us != null && us.equals("reload")) {
    		                        expectedReload = true;
    		                        break;
    		                    }
    		                }
    		            }
    		        }*/
    		        
    		        if (expectedReload) {
    		        	break;
    		        }
    		    } catch (Exception e) {
    		        throw new RuntimeException(e.getMessage(), e);
    		    }
    		}
    	        
    	    return expectedReload;
    }

    @Test
    public void testSts() throws Exception {
        log.info("********** START TEST **********");
       
        String userdir = System.getProperty("user.dir");
        String[] args = new String[]{userdir + File.separator + "src/main/resources/wsdl/hello_world.wsdl", userdir + File.separator + "src/main/resources/META-INF/spring/client.xml"};
        Client.main(args, log);
    }
}