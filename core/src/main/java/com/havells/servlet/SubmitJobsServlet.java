package com.havells.servlet;

import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.mailer.MailingException;
import com.havells.services.GenericService;
import com.havells.services.SendEmailService;
import org.apache.commons.mail.EmailException;
import org.apache.felix.scr.annotations.*;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Manisha Bano
 * Date: 29/7/15
 * Time: 1:34 PM
 */
@Component(immediate = true, metatype = true, label = "JobsApplicationSubmission")
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = "POST", propertyPrivate = true),
        @Property(name = "sling.servlet.paths", value = "/bin/servlets/submitJob", propertyPrivate = true)
})
public class SubmitJobsServlet extends SlingAllMethodsServlet {

    @Property(unbounded = PropertyUnbounded.DEFAULT, label = "templatepath", description = "Path of a template")
    private static String template_path = "template.path";
    @Property(unbounded = PropertyUnbounded.DEFAULT, label = "mailSubject", description = "Mail subject of department")
    private static String mail_subject = "mail.subject";

    protected static final Logger LOG = LoggerFactory.getLogger(SubmitJobsServlet.class);

    private String templatePath;
    private String subject;
    private Map<String, String> userDetailsMap;

    private static final String USER_ALREADY_EXIST = "101";

    @Activate
    private void activate(final ComponentContext componentContext) {
        final Dictionary<?, ?> props = componentContext.getProperties();
        this.templatePath = (String) props.get(template_path);
        this.subject = (String) props.get(mail_subject);
    }

    @Reference
    GenericService genericService;
    @Reference
    SendEmailService sendEmailService;

    /**
     * This method will be called when the user apply for some job. It will take all the values
     * and save the user details to the node with the attached resume which is mainly docx and
     * pdf file.
     * This method will also send a mail which has all the details of the job and the user who
     * is applying for it.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws
            IOException {

        populateUserDetailMap(request);

        String email = request.getParameter("departmentMailId");
        String thankYouPageUrl = request.getParameter("thankYouPageUrl");
        String thisPage = request.getParameter("thisPage");
        String postedJobsPath = request.getParameter("postedJobs");
        RequestParameter attach = request.getRequestParameter("file");
        ResourceResolver resourceResolver = genericService.getAdminResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);

        Resource resource = ResourceUtil.getOrCreateResource(resourceResolver, postedJobsPath, JcrResourceConstants.NT_SLING_FOLDER, JcrResourceConstants.NT_SLING_FOLDER, true);
        String userUniqueId = userDetailsMap.get("jobSeeker") + "-" + userDetailsMap.get("mobile");

        if (resource != null) {
            Node postedJobs = resource.adaptTo(Node.class);
            try {
                if (resource.getChild(userUniqueId) == null) {
                    Node node = saveUserRecord(attach, session, userUniqueId, postedJobs);
                    if (node != null && node.hasNode(attach.getFileName())) {
                        Node contentNode = node.getNode(attach.getFileName()).getNode(JcrConstants.JCR_CONTENT);
                        sendEmailService.sendEmailWithAttachment(email, session, subject, userDetailsMap, templatePath,
                                contentNode.getProperty(JcrConstants.JCR_MIMETYPE).getString(),
                                contentNode.getProperty(JcrConstants.JCR_DATA).getBinary());
                    } else {
                        sendEmailService.sendEmail(email, session, subject, userDetailsMap, templatePath);
                    }
                    response.sendRedirect(thankYouPageUrl + ".html");
                } else {
                    response.sendRedirect(thisPage + ".html?error=" + USER_ALREADY_EXIST);
                }
            }catch (NullPointerException nullPointerException) {
                LOG.error("NullPointer Exception Occurred. Error Message is ", nullPointerException.getMessage());
                response.sendRedirect(thisPage + ".html?error=500");
            } catch (RepositoryException repositoryException) {
                LOG.error("Repository Exception Occurred. Error Message is ", repositoryException.getMessage());
                response.sendRedirect(thisPage + ".html?error=500");
            } catch (IllegalStateException illegalStateException) {
                LOG.error("Illegal State Exception Occurred. Error Message is ", illegalStateException.getMessage());
                response.sendRedirect(thisPage + ".html?error=500");
            } catch (MessagingException messagingException) {
                LOG.error("Messaging Exception Occurred. Error Message is ", messagingException.getMessage());
                response.sendRedirect(thankYouPageUrl + ".html");
            } catch (EmailException emailException) {
                LOG.error("Email Exception Occurred. Error Message is ", emailException.getMessage());
                response.sendRedirect(thankYouPageUrl + ".html");
            } catch (MailingException mailingException) {
                LOG.error("Mailing Exception Occurred. Error Message is ", mailingException.getMessage());
                response.sendRedirect(thankYouPageUrl + ".html");
            } catch (FileNotFoundException fileNotFoundException) {
                LOG.error("File Not Found Exception Occurred. Error Message is ", fileNotFoundException.getMessage());
                response.sendRedirect(thankYouPageUrl + ".html");
            } finally {
                if (session.isLive()) {
                    session.logout();
                }
            }
        } else {
            response.sendRedirect(thisPage + ".html");
        }
    }

    /**
     * To populate Map with JobSeeker details.
     *
     * @param request
     */
    private void populateUserDetailMap(SlingHttpServletRequest request) {
        userDetailsMap = new HashMap<String, String>();
        userDetailsMap.put("jobSeeker", request.getParameter("userName"));
        userDetailsMap.put("JobsToBeApplied", request.getParameter("JobsToBeApplied"));
        userDetailsMap.put("mobile", request.getParameter("mobile"));
        userDetailsMap.put("location", request.getParameter("location"));
        String year = "0";
        String months = "0";
        StringBuilder builder = new StringBuilder();
        if (!("year".equalsIgnoreCase(request.getParameter("year")))) {
            year = request.getParameter("year");
        }
        if (!("months".equalsIgnoreCase(request.getParameter("month")))) {
            months = request.getParameter("month");
        }
        builder.append(year).append(" Years ").append(months).append(" Months ");
        userDetailsMap.put("experience", builder.toString());
        userDetailsMap.put("currentCompany", request.getParameter("currentCompany"));
        userDetailsMap.put("currentDesignation", request.getParameter("currentDesignation"));
    }

    /**
     * This method creates and returns the node of Job applicant information.
     *
     * @param attach        : attached resume as RequsetParameter
     * @param session       : get admin session to create node
     * @param userUniqueId  : node name should be unique
     * @param postedJobs    : relative node under which all the jobs are stored
     * @return node which has the details of the jobSeeker.
     * @throws RepositoryException
     */
    private Node saveUserRecord(RequestParameter attach, Session session, String userUniqueId, Node postedJobs) throws RepositoryException, IOException {
        InputStream ip = attach.getInputStream();
        Node node = JcrUtil.createUniqueNode(postedJobs, userUniqueId, JcrConstants.NT_UNSTRUCTURED, session);
        node.setProperty("userName", userDetailsMap.get("jobSeeker"));
        node.setProperty("JobsToBeApplied", userDetailsMap.get("JobsToBeApplied"));
        node.setProperty("mobile", userDetailsMap.get("mobile"));
        node.setProperty("location", userDetailsMap.get("location"));
        node.setProperty("experience", userDetailsMap.get("experience"));
        node.setProperty("currentCompany", userDetailsMap.get("currentCompany"));
        node.setProperty("currentDesignation", userDetailsMap.get("currentDesignation"));
        if (!attach.getFileName().equals("")) {
            JcrUtils.putFile(node, attach.getFileName(), getServletContext().getMimeType(attach.getFileName()), ip);
        }
        session.save();
        return node;
    }
}
