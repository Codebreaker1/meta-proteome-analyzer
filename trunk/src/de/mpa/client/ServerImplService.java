
package de.mpa.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "ServerImplService", targetNamespace = "http://webservice.mpa.de/", wsdlLocation = "http://metaprot:8080/WS/Server?wsdl")
public class ServerImplService
    extends Service
{

    private final static URL SERVERIMPLSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(de.mpa.client.ServerImplService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = de.mpa.client.ServerImplService.class.getResource(".");
            url = new URL(baseUrl, "http://metaprot:8080/WS/Server?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://metaprot:8080/WS/Server?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        SERVERIMPLSERVICE_WSDL_LOCATION = url;
    }

    public ServerImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ServerImplService() {
        super(SERVERIMPLSERVICE_WSDL_LOCATION, new QName("http://webservice.mpa.de/", "ServerImplService"));
    }

    /**
     * 
     * @return
     *     returns Server
     */
    @WebEndpoint(name = "ServerImplPort")
    public Server getServerImplPort() {
        return super.getPort(new QName("http://webservice.mpa.de/", "ServerImplPort"), Server.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Server
     */
    @WebEndpoint(name = "ServerImplPort")
    public Server getServerImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservice.mpa.de/", "ServerImplPort"), Server.class, features);
    }

}
