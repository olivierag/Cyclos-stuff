package cyclos.backend;

import org.cyclos.server.utils.HttpServiceFactory;
import org.cyclos.server.utils.HttpServiceInvocationData;

public class Cyclos {

    private static final String ROOT_URL = "http://localhost:8080/cyclos/"; //TODO: CHANGE THIS TO YOUR SERVER URL
    private static final String PRINCIPAL = "sanderac"; //TODO: CHANGE THIS TO YOUR USERNAME
    private static final String PASSWORD = "harad23!"; //TODO: CHANGE THIS TO YOUR PASSWORD

    private static HttpServiceFactory factory;

    static{
        factory = new HttpServiceFactory();
        factory.setRootUrl(ROOT_URL);
        factory.setInvocationData(HttpServiceInvocationData.stateless(PRINCIPAL, PASSWORD));
    }

    public static HttpServiceFactory getServiceFactory() {
        return factory;
    }

    public static HttpServiceFactory getServiceFactory(HttpServiceInvocationData invData){
        HttpServiceFactory factory = new HttpServiceFactory();
        factory.setRootUrl(ROOT_URL);
        factory.setInvocationData(invData);
        return factory;
    }

}
