package ch.uzh.ifi.hase.soprafs24.startup;

import ch.uzh.ifi.hase.soprafs24.controller.EbayController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupSearchItem implements ApplicationRunner {

    @Autowired
    private EbayController ebayController;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ebayController.searchItems("(laptop, shoes, gift, bicycle, cookware)", null); // Call searchItems without parameters
    }
}