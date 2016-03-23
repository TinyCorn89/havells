package com.havells.services;

import org.apache.commons.mail.EmailException;
import org.apache.sling.api.resource.Resource;

import javax.jcr.Binary;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface SendEmailService {
    public void sendEmail(String email, Session session,String subject, Map<String, String> parameters, String templatePath) throws EmailException, MessagingException, IOException;
    public void sendEmailWithAttachment(String email, Session session,String subject, Map<String, String> parameters, String templatePath,String type, Binary binary) throws EmailException, MessagingException, IOException;
    public String getUserName(String email, Resource resource) throws RepositoryException;

}
