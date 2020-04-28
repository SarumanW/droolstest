package lavka.drools.service;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("keys.properties")
public class ListexIntegrationService implements IntegrationService {

    @Override
    public void importProductBase() {

    }

    @Override
    public void importCategories() {

    }
}
