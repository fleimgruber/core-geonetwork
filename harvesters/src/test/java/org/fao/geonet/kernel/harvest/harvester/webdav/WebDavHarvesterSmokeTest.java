package org.fao.geonet.kernel.harvest.harvester.webdav;

import java.util.concurrent.atomic.AtomicBoolean;

import org.fao.geonet.Logger;
import org.fao.geonet.kernel.DataManager;
import org.fao.geonet.kernel.HarvestValidationEnum;
import org.fao.geonet.kernel.harvest.AbstractHarvesterServiceIntegrationTest;
import org.fao.geonet.kernel.harvest.harvester.HarvestResult;
import org.fao.geonet.utils.Log;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jeeves.server.context.ServiceContext;

public class WebDavHarvesterSmokeTest extends AbstractHarvesterServiceIntegrationTest {
	Logger log = Log.createLogger("test");
	@Autowired DataManager dataManager;

	
    @Test
    public void testSmoke() throws Exception {
    	ServiceContext context = createServiceContext();
    	var params = new WebDavParams(dataManager);
    	params.setUuid("1234");
    	params.url = "https://our.tld/webdav/test/";
    	params.subtype = "webdav";
    	
    	params.setUseAccount(true);    	
    	params.setUsername("user");
    	params.setPassword("pass");
    	
    	params.setValidate(HarvestValidationEnum.NOVALIDATION);    	
    	params.setOwnerId("1");
    	
    	Harvester h = new Harvester(new AtomicBoolean(), log, context, params);
        HarvestResult result = h.harvest(log);
        System.out.println(result.toString());
    }
}
