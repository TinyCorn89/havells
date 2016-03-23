package com.havells.supportUtils.events
import com.day.cq.wcm.api.PageEvent
import com.day.cq.wcm.api.PageModification
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Deactivate
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import org.osgi.service.component.ComponentContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.osgi.service.event.Event
import org.osgi.service.event.EventHandler
import org.apache.sling.jcr.api.SlingRepository
import javax.jcr.Session

@Component(enabled = true, immediate = true, metatype = false)
@Service
@Property(name="event.topics", value=PageEvent.EVENT_TOPIC)
public class PimUpdatePageEvent implements EventHandler{

    private static final Logger LOG = LoggerFactory.getLogger(PimUpdatePageEvent.class)

    private Session adminSession

    @org.apache.felix.scr.annotations.Reference
    SlingRepository repository

    @Activate
    public void activate(ComponentContext context) {
        adminSession = repository.loginAdministrative(null)
    }

    @Deactivate
    public void deactivate(){
        if (adminSession != null){
            adminSession.logout();
        }
    }

    @Override
    public void handleEvent(Event event) {
        PageEvent pgEvent = PageEvent.fromEvent(event);
        Iterator<PageModification> modifications = pgEvent.getModifications();
        try {
            while (modifications.hasNext()) {
                PageModification pageModification = modifications.next()
                if ("PageCreated".equalsIgnoreCase(pageModification.getType().toString())
                        || "PageModified".equalsIgnoreCase(pageModification.getType().toString())) {

                    String pagePath = pageModification.getPath()
                    LOG.info("page path created {}",pagePath)
                    javax.jcr.Node node = adminSession.getNode(pagePath + "/jcr:content/par/product")
                    if (node != null && node.hasProperty("productData")) {
                        String etcPath = node.getProperty("productData").getString()
                        javax.jcr.Node temp = adminSession.getNode(etcPath)
                        if (temp != null) {
                            temp.setProperty("pagePath", pagePath)
                            adminSession.save()
                        }
                    }
                }
            }
        }catch(Exception ex){
            LOG.info("exception occurs in page update event {}" ,ex)
        }
    }
}
