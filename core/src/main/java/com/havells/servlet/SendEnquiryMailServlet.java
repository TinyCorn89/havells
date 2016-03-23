package com.havells.servlet;

import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.mailer.MailingException;
import com.havells.services.GenericService;
import com.havells.services.SendEmailService;
import org.apache.commons.mail.EmailException;
import org.apache.felix.scr.annotations.*;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.UUID;


@Component(label = "QuickEnquirySubmission", metatype = true, immediate = true, description = "this is for quick enquiry")
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = "POST", propertyPrivate = true),
        @Property(name = "sling.servlet.paths", value = "/bin/servlets/quickEnquiry", propertyPrivate = true)
})

public class SendEnquiryMailServlet extends SlingAllMethodsServlet {

    protected static final Logger LOG = LoggerFactory.getLogger(SendEnquiryMailServlet.class);

    @Property(unbounded = PropertyUnbounded.DEFAULT, label = "templatepathAdmin", description = "Path of a template for sending mail to admin")
    private static String template_path_admin = "template.pathAdmin";
    @Property(unbounded = PropertyUnbounded.DEFAULT, label = "templatepathUser", description = "Path of a template for sending mail to user")
    private static String template_path_user = "template.pathUser";
    @Property(unbounded = PropertyUnbounded.DEFAULT, label = "mailSubject", description = "Mail subject")
    private static String mail_subject = "mail.subject";
    @Property(unbounded = PropertyUnbounded.DEFAULT, label = "userDataPath", description = "the data of the user is stored at this path")
    private static String user_detail_path = "userDetail.path";
    @Property(unbounded = PropertyUnbounded.DEFAULT, label = "AdminEmailId", description = "email id of the admin of havells")
    private static String admin_email = "adminEmail_id";

    @Reference
    GenericService genericService;
    @Reference
    SendEmailService sendEmailService;

    private String templatePathAdmin;
    private String templatePathUser;
    private String subject;
    private String adminEmail;
    private String userDetailPath;
    private HashMap<String, String> userData;

    @Activate
    @Modified
    private void activate(final ComponentContext componentContext) {
        final Dictionary<?, ?> props = componentContext.getProperties();
        this.templatePathAdmin = (String) props.get(template_path_admin);
        this.templatePathUser = (String) props.get(template_path_user);
        this.userDetailPath = (String) props.get(user_detail_path);
        this.adminEmail = (String) props.get(admin_email);
        this.subject = (String) props.get(mail_subject);
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws
            IOException {
        populateUserData(request);
        String userEmail = request.getParameter("emailId");
        ResourceResolver resourceResolver = genericService.getAdminResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        Resource resource = resourceResolver.getResource(userDetailPath);
        String userUniqueId = userData.get("userName") + "-" + UUID.randomUUID().toString();

        if (resource != null) {
            Node enquiry = resource.adaptTo(Node.class);
            try {
                if (resource.getChild(userUniqueId) == null)
                {
                    Node node = saveUserData(session, userUniqueId, enquiry);
                       sendEmailService.sendEmail(adminEmail, session, subject, userData, templatePathAdmin);
                       sendEmailService.sendEmail(userEmail, session, subject, userData, templatePathUser);
                    response.getWriter().write("success=1");
                } else {
                    response.getWriter().write("error=101");
                }
            } catch (RepositoryException repositoryException) {
                LOG.error("Repository Exception Occurred. Error Message is ", repositoryException.getMessage());
                response.getWriter().write("error=500");
            } catch (IllegalStateException illegalStateException) {
                LOG.error("Illegal State Exception Occurred. Error Message is ", illegalStateException.getMessage());
                response.getWriter().write("error=500");
            } catch (MessagingException messagingException) {
                LOG.error("Messaging Exception Occurred. Error Message is ", messagingException.getMessage());
                response.getWriter().write("success=1");
            } catch (EmailException emailException) {
                LOG.error("Email Exception Occurred. Error Message is ", emailException.getMessage());
                response.getWriter().write("success=1");
            } catch (MailingException mailingException) {
                LOG.error("Mailing Exception Occurred. Error Message is ", mailingException.getMessage());
                response.getWriter().write("success=1");
            } catch (FileNotFoundException fileNotFoundException) {
                LOG.error("File Not Found Exception Occurred. Error Message is ", fileNotFoundException.getMessage());
                response.getWriter().write("success=1");
            } finally {
                if (session.isLive()) {
                    session.logout();
                }
            }
        }
    }
    private void populateUserData(SlingHttpServletRequest request) {
        userData = new HashMap<String, String>();
        userData.put("userName", request.getParameter("name"));
        userData.put("userEmailId", request.getParameter("emailId"));
        userData.put("mobile", request.getParameter("phoneNumber"));
        userData.put("location", request.getParameter("location"));
        userData.put("description", request.getParameter("description"));
    }
    private Node saveUserData(Session session, String userUniqueId, Node enquiry) throws RepositoryException, IOException {
        Node node = JcrUtil.createUniqueNode(enquiry, userUniqueId, JcrConstants.NT_UNSTRUCTURED, session);
        node.setProperty("userName", userData.get("userName"));
        node.setProperty("userEmail", userData.get("userEmailId"));
        node.setProperty("mobile", userData.get("mobile"));
        node.setProperty("location", userData.get("location"));
        node.setProperty("description", userData.get("description"));
        node.setProperty(JcrConstants.JCR_CREATED, Calendar.getInstance());
        session.save();
        return node;
    }
}
