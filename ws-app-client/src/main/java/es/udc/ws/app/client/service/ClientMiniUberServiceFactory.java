package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientMiniUberServiceFactory {

    private final static String CLASS_NAME_PARAMETER
            = "ClientMiniUberServiceFactory.className";
    private static Class<ClientMiniUberService> serviceClass = null;

    private ClientMiniUberServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientMiniUberService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager
                        .getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientMiniUberService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }

    public static ClientMiniUberService getService() {

        try {
            return (ClientMiniUberService) getServiceClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}