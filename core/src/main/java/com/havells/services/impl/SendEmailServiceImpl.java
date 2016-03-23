package com.havells.services.impl;


import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MailingException;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.havells.services.SendEmailService;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Binary;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component(metatype = true)
@Service
@org.apache.felix.scr.annotations.Properties({
        @Property(name = "service.vendor", value = "Havells India"),
        @Property(name = "service.description", value = "Sending an Email to the users.")
})
public class SendEmailServiceImpl implements SendEmailService {

    private MessageGateway<HtmlEmail> messageGateway;
    @Reference
    private MessageGatewayService messageGatewayService;

    protected static final Logger LOG = LoggerFactory.getLogger(SendEmailServiceImpl.class);

    /**
     * This method will send a simple mail using the mail template.
     *
     * @param emailid
     * @param session
     * @param subject
     * @param parameters
     * @param templatePath
     * @throws MessagingException
     * @throws EmailException
     * @throws IOException
     */
    @Override
    public void sendEmail(String emailid, Session session, String subject, Map<String, String> parameters, String templatePath) throws MessagingException, EmailException, IOException {

        if (templatePath != null) {
            final MailTemplate mailTemplate = MailTemplate.create(templatePath, session);
            final HtmlEmail email = mailTemplate.getEmail(StrLookup.mapLookup(new HashMap<String, String>(parameters)), HtmlEmail.class);
            email.setSubject(subject);
            email.addTo(emailid);
            messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
            messageGateway.send(email);
            LOG.info("Email Sent Successfully.");
        } else {
            LOG.error("template path is missing.");
           throw new FileNotFoundException();
        }
    }

    /**
     * This method will send a mail with an attachment attached with it.
     *
     * @param emailid      : mailid to which mail is being sent
     * @param session      : session to create mail
     * @param subject      : subject of mail
     * @param parameters   : hashmap used in template to map the values
     * @param templatePath : path where mail template is stored
     * @throws MessagingException
     * @throws MailingException
     * @throws EmailException
     * @throws IOException
     */
    @Override
    public void sendEmailWithAttachment(String emailid, Session session, String subject, Map<String, String> parameters,
                                        String templatePath, String type, Binary imageBinary) throws MessagingException, MailingException, EmailException, IOException {
        ByteArrayDataSource fileDS = null;
        String defaultUserResumeName = "Resume";
        MailTemplate mailTemplate;
        if (templatePath != null && (mailTemplate = MailTemplate.create(templatePath, session)) != null) {
            try {
                InputStream imageStream = imageBinary.getStream();
                fileDS = new ByteArrayDataSource(imageStream, type);
            } catch (RepositoryException repositoryException) {
                LOG.error("RepositoryException " + repositoryException);
            }
            final HtmlEmail email = mailTemplate.getEmail(StrLookup.mapLookup(new HashMap<String, String>(parameters)), HtmlEmail.class);
            email.setSubject(subject);
            email.addTo(emailid);
            email.attach(fileDS, "application/pdf".equalsIgnoreCase(type) ? defaultUserResumeName + ".pdf" : defaultUserResumeName + ".docx", "This is your attached resume.");

            messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
            messageGateway.send(email);
            LOG.info("Email Sent Successfully.");
        } else {
            LOG.error("template path is missing.");
           throw new FileNotFoundException();
        }
    }

    @Override
    public String getUserName(String email, Resource resource) {
        UserManager manager = resource.getResourceResolver().adaptTo(UserManager.class);
        String user = "";
        try {
            Authorizable auth = manager.getAuthorizable(email);
            if (auth != null && auth.hasProperty("profile/username")) {
                user = auth.getProperty("profile/username")[0].getString();
            }
        } catch (RepositoryException exc) {
            LOG.error("Repository not found" + exc);
        }
        return user;
    }
}
