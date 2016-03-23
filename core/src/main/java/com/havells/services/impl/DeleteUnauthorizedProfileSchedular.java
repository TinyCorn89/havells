package com.havells.services.impl;

import com.havells.services.GenericService;
import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.Property;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.query.Query;
import javax.jcr.version.VersionException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

@Component(immediate = true, metatype = true, label = "Havells scheduler : Temporary registration node purge service", description = "Havells scheduler : This service will delete temporary registration nodes created under '/var/users' hierarchy which were created 2 days ago. This way temporary registration nodes do not increase repository size.")
@Service(value = Runnable.class)
@Property(name = "scheduler.expression", value = "0 1 0 1/1 * ? *", description = "This schedular expression is to make schedular run after two days.")

public class DeleteUnauthorizedProfileSchedular implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteUnauthorizedProfileSchedular.class);

    @Reference
    GenericService genericService;

    /**
     * This method will call automatically after a specified time. And it will delete all the nodes of /var/user...
     * which are of two days later and are not authenticated by the user.
     * <p/>
     * So that we cannot have a bunch of waste/ garbage user data.
     */
    @Override
    public void run() {

        LOG.debug("Schedular Run {}", new Date());
        ResourceResolver resourceResolver = genericService.getAdminResourceResolver();

        final Session session = resourceResolver.adaptTo(Session.class);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -2);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
        String twoDaysBeforeDateString = formatter.format(cal.getTime());

        String searchQuery = "SELECT * FROM [nt:unstructured] AS s WHERE ISDESCENDANTNODE([/var/user]) AND s.creationTime IS NOT NULL and s.creationTime<CAST('" + twoDaysBeforeDateString + "' AS DATE)";

        Iterator<Resource> result = resourceResolver.findResources(searchQuery, Query.JCR_SQL2);
        try {
            while (result.hasNext()) {
                Resource tempResource = result.next();
                Node node = tempResource.adaptTo(Node.class);
                LOG.info("Node {} Has Been ReMoved at {}", node.getPath(), new Date());
                node.remove();
                session.save();
            }
        } catch (ItemExistsException itemExistsException) {
            LOG.error("Item Exists Exception when Removing a Node. ", itemExistsException);
        } catch (AccessDeniedException accessDeniedException) {
            LOG.error("Access Denied Exception when Removing a Node. ", accessDeniedException);
        } catch (InvalidItemStateException invalidItemStateException) {
            LOG.error("Invalid Items State Exception when Removing a Node. ", invalidItemStateException);
        } catch (ReferentialIntegrityException referentialIntegrityException) {
            LOG.error("Refrential Integrity Exception when Removing a Node. ", referentialIntegrityException);
        } catch (NoSuchNodeTypeException noSuchNodeTypeException) {
            LOG.error("No Such Node Type Exception when Removing a Node. ", noSuchNodeTypeException);
        } catch (VersionException versionException) {
            LOG.error("Version  Exception when Removing a Node. ", versionException);
        } catch (ConstraintViolationException constraintViolationException) {
            LOG.info("Constraint Voilation Exception when Removing a Node. ", constraintViolationException);
        } catch (LockException lockException) {
            LOG.error("Lock Exception when Removing a Node. ", lockException);
        } catch (RepositoryException repositoryException) {
            LOG.error("repository Exception While removing nodes from /var/user/... ", repositoryException);
        } finally {
            if (session.isLive() && session != null) {
                session.logout();
            }
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }
}
