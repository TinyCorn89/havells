package com.havells.core.model.signup;

import com.adobe.granite.crypto.CryptoException;
import com.adobe.granite.crypto.CryptoSupport;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.core.utils.PageInfoUtils;
import com.day.cq.wcm.foundation.forms.FormsHelper;
import com.havells.commons.sling.Resources;
import com.havells.services.SendEmailService;
import org.apache.jackrabbit.util.Base64;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class UserProfileModel {

    private final String EMAILID = "email";
    private final String PWD = "password";
    private final String LAST_NAME = "familyName";
    private final String FIRST_NAME = "givenName";
    private final String CONTACT_NO = "mobile";
    private final String DATEOFBIRTH = "dateOfBirth";
    private final String BILLING_ADDRESS = "billingAddress";
    private final String SHIPPING_ADDRESS = "shippingAddress";
    private final String PWD_CONFIRM = "confirm" + PWD;
    private final String CREATE = "cq:create";
    private String pwd = null;
    private final String PF_REP = "rep:";
    private final String PF_CQ = "cq:";
    private final String EMAIL = PF_REP + "e-mail";
    private String pwdConfirm = null;
    private String auth = null;
    private Logger LOG = LoggerFactory.getLogger(UserProfileModel.class);
    private final String EMAIL_TEMPLATE = "/etc/notification/email/html/havells/registration.txt";

    private final CryptoSupport cryptoSupport;
    private final SendEmailService sendEmailService;
    private final SlingHttpServletRequest slingHttpServletRequest;
    private final SlingHttpServletResponse slingHttpServletResponse;

    public UserProfileModel(CryptoSupport cryptoSupport, SendEmailService sendEmailService, SlingHttpServletRequest slingHttpServletRequest,
                            SlingHttpServletResponse slingHttpServletResponse){
        this.cryptoSupport = cryptoSupport;
        this.sendEmailService = sendEmailService;
        this.slingHttpServletRequest = slingHttpServletRequest;
        this.slingHttpServletResponse = slingHttpServletResponse;
    }

    public void setProfileData(HttpServletRequest request, HttpServletResponse response,Resource resource, String errorPage,ResourceResolver resourceResolver) throws IOException{


        final Session session = resourceResolver.adaptTo(Session.class);
        try {
            ValueMap props = resource.getValueMap();
            boolean isCreate = userCreation(request, props, session);
            if(isCreate) {
                String thankYouPage = props.get("thankyouPage", "");
                if (Resources.isInternalURL(thankYouPage, resource.getResourceResolver())) {
                    response.sendRedirect(slingHttpServletRequest.getResourceResolver().map(request, thankYouPage + ".html"));
                } else {
                    FormsHelper.redirectToReferrer(slingHttpServletRequest, slingHttpServletResponse, new HashMap<String, String[]>());
                }
            } else{
                response.sendRedirect(slingHttpServletRequest.getResourceResolver().map(request, errorPage));
            }
            session.save();
        } catch (MessagingException exc) {
            response.sendRedirect(slingHttpServletRequest.getResourceResolver().map(request, errorPage));
        } catch (Exception ex) {
            response.sendRedirect(slingHttpServletRequest.getResourceResolver().map(request, errorPage));
        }
    }

    public boolean userCreation(HttpServletRequest request, ValueMap props, Session session) throws Exception {
        String error = null;
        String email = request.getParameter(EMAILID);
        String path = createTempUserPath(email);
        Calendar calendar = Calendar.getInstance();
        if (request.getParameter(EMAILID) != null)
            auth = slingHttpServletRequest.getRequestParameter(EMAILID).getString();

        if (request.getParameter(PWD) != null)
            pwd = slingHttpServletRequest.getRequestParameter(PWD).getString();

        if (request.getParameter(PWD_CONFIRM) != null)
            pwdConfirm = slingHttpServletRequest.getRequestParameter(PWD_CONFIRM).getString();

        /*sets these property so that the nodes created can be reverse replicated.*/
        createTempUserAccount(request, calendar, session, path, pwd, pwdConfirm);

        final String createPara = request.getParameter(CREATE) == null ? null : slingHttpServletRequest.getRequestParameter(CREATE).getString();
        final boolean hasAuth = auth != null && auth.length() > 0;
        final boolean hasPwd = pwd != null && pwd.length() > 0;
        final boolean isCreate = createPara != null && Boolean.valueOf(createPara);
        boolean login = hasAuth && hasPwd;
        boolean create = hasAuth && ((pwdConfirm != null && pwdConfirm.length() > 0) || (isCreate && hasPwd));
        final boolean isCorrectPassword = pwd != null && pwd.equals(pwdConfirm);
        if (!(login || create)) {
            error = "Request incomplete... no user-id or no password";
        } else if (create) {
            if (!hasPwd) {
                pwd = pwdConfirm;
            }
        }
        if (!isCorrectPassword) {
            error = "Password Does not Match";
            create = false;
        }
        if (error == null) {
            // send mail if user had entered his email and password correctly
            Date tokenExpiryTime = getExpiryTime(calendar);
            String token = Base64.encode(email + "$" + tokenExpiryTime);
            String clickLink = props.get("click-link", "") + "?c=" + token + "&l=" +
                    Base64.encode(path);
            sendRegisteredEmail(email, props, session, token, clickLink);
        }
        return create;
    }

    /**
     *
     * @param email
     * @param props
     * @param session
     * @param token
     * @param clickLink
     * @throws Exception
     */
    private void sendRegisteredEmail(String email, ValueMap props, Session session,
                                     String token, String clickLink) throws Exception{
        String subject = props.get("subject", "Registration Successful");
        HashMap<String, String> propertyMap = new HashMap<String, String>();
        propertyMap.put("user", auth);
        propertyMap.put("email", email);
        propertyMap.put("click-link", clickLink);
        propertyMap.put("token", token);
        sendEmailService.sendEmail(email, session, subject, propertyMap, props.get("emailTemplatePath", EMAIL_TEMPLATE));
    }

    /**
     *
     * @param cal
     * @return
     */
    private Date getExpiryTime(Calendar cal){
        cal.add(Calendar.DAY_OF_YEAR, 1);
        return  cal.getTime();
    }

    /**
     *
     * @param email
     * @return
     */
    private String createTempUserPath(String email) {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return "/var/user/" + formatter.format(new Date()) + "/" + email.substring(0, 1).intern() + "/" + email;
    }

    public synchronized void createTempUserAccount(HttpServletRequest request, Calendar calendar, Session session, String path,
                                                   String pwd, String pwdConfirm) throws RepositoryException, CryptoException {
        Node node = JcrUtil.createPath(path, "nt:unstructured", "nt:unstructured", session, true);
        node.setProperty("email", request.getParameter(EMAILID));
        node.setProperty("creationTime", calendar);
        node.setProperty("password", cryptoSupport.protect(pwd));
        node.setProperty("passwordConfirm", cryptoSupport.protect(pwdConfirm));
        node.setProperty("givenName", request.getParameter(FIRST_NAME));
        node.setProperty("familyName", request.getParameter(LAST_NAME));
        node.setProperty("mobile", request.getParameter(CONTACT_NO));
        node.setProperty("dateOfBirth", request.getParameter(DATEOFBIRTH));
        node.setProperty("shippingAddress", request.getParameter(SHIPPING_ADDRESS));
        node.setProperty("billingAddress", request.getParameter(BILLING_ADDRESS));
    }

}