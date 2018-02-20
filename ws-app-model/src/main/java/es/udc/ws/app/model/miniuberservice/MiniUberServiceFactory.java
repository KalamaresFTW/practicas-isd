package es.udc.ws.app.model.miniuberservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class MiniUberServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "MiniUberServiceFactory.className";
    private static MiniUberService service = null;

    private MiniUberServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static MiniUberService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (MiniUberService) serviceClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static MiniUberService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}