package com.checkout.commerceProvider;


import com.adobe.cq.commerce.api.PlacedOrderResult;
import com.adobe.cq.commerce.common.AbstractJcrCommerceSession;


import java.math.BigDecimal;
import java.util.*;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;

import com.adobe.cq.commerce.common.DefaultJcrPlacedOrder;
import com.adobe.granite.security.user.UserProperties;
import com.day.cq.personalization.UserPropertiesUtil;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.text.ISO9075;
import org.apache.commons.collections.Predicate;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;

import com.adobe.cq.commerce.api.CommerceConstants;
import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.PlacedOrder;
import com.adobe.cq.commerce.common.AbstractJcrCommerceService;
import com.day.cq.i18n.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HavellsCommerceSessionImpl extends AbstractJcrCommerceSession {

    private static final Logger LOG = LoggerFactory.getLogger(HavellsCommerceSessionImpl.class);

    public HavellsCommerceSessionImpl(AbstractJcrCommerceService commerceService,
                                      SlingHttpServletRequest request,
                                      SlingHttpServletResponse response,
                                      Resource resource) throws CommerceException {
        super(commerceService, request, response, resource);
        PN_UNIT_PRICE = HavellsProductImpl.PN_PRICE;
    }

    @Override
    protected BigDecimal getShipping(String method) {
        //
        // A simple shipping pricing architecture with fixed shipping costs.
        //
        String[][] shippingCosts = {{"ground", "10.00"},
                {"3day", "20.00"},
                {"2day", "25.00"},
                {"overnight", "40.00"}};

        for (String[] entry : shippingCosts) {
            if (entry[0].equals(method)) {
                return new BigDecimal(entry[1]);
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    protected String tokenizePaymentInfo(Map<String, String> paymentDetails) throws CommerceException {
        //
        // This is only a stub implementation for the Geometrixx-Outdoors demo site, for which there is no
        // real payment processing (or payment info tokenization).
        //
        return "faux-payment-token";
    }

    @Override
    protected void initiateOrderProcessing(String orderPath) throws CommerceException {
        //
        // This is only a stub implementation for the Geometrixx-Outdoors demo site, for which there is no
        // real order processing.
        //
        try {
            Node order = resolver.getResource(orderPath).adaptTo(Node.class);
            order.setProperty("orderStatus", "Processing");
            order.getSession().save();
        } catch (Exception e) {
            LOG.error("Failed to update order", e);
        }
    }

    @Override
    protected String getOrderStatus(String orderId) throws CommerceException {
        //
        // Status is kept in the vendor section (/etc/commerce); need to find corresponding order there.
        //
        Session adminSession = null;
        try {
            adminSession = commerceService.serviceContext().slingRepository.loginAdministrative(null);
            //
            // example query: /jcr:root/etc/commerce/orders//element(*)[@orderId='foo')]
            //
            StringBuilder buffer = new StringBuilder();
            buffer.append("/jcr:root/etc/commerce/orders//element(*)[@orderId = '")
                    .append(Text.escapeIllegalXpathSearchChars(orderId).replaceAll("'", "''"))
                    .append("']");

            final Query query = adminSession.getWorkspace().getQueryManager().createQuery(buffer.toString(), Query.XPATH);
            NodeIterator nodeIterator = query.execute().getNodes();
            if (nodeIterator.hasNext()) {
                return nodeIterator.nextNode().getProperty("orderStatus").getString();
            }
        } catch (Exception e) {
            // fail-safe when the query above contains errors
            LOG.error("Error while fetching order status for orderId '" + orderId + "'", e);
        } finally {
            if (adminSession != null) {
                adminSession.logout();
            }
        }
        final I18n i18n = new I18n(request);
        return i18n.get("unknown", "order status");
    }

    @Override
    protected Predicate getPredicate(String predicateName) {
        //
        // This stub implementation supports only the openOrders predicate.
        //
        if (predicateName != null && predicateName.equals(CommerceConstants.OPEN_ORDERS_PREDICATE)) {
            return new Predicate() {
                public boolean evaluate(Object object) {
                    try {
                        PlacedOrder order = (PlacedOrder) object;
                        String status = (String) order.getOrder().get("orderStatus");
                        return (status != null && !status.equals("Completed") && !status.equals("Cancelled"));
                    } catch (CommerceException e) {
                        return false;
                    }
                }
            };
        }
        return null;
    }


    @Override
    public PlacedOrderResult getPlacedOrders(String predicate, int pageNumber, int pageSize, String sortId)
            throws CommerceException {
        List<PlacedOrder> orders = new ArrayList();
        try {
            Session userSession = (Session) this.resolver.adaptTo(Session.class);
            UserProperties userProperties = (UserProperties) this.request.adaptTo(UserProperties.class);
            if ((userProperties != null) && (!UserPropertiesUtil.isAnonymous(userProperties))) {
                UserManager um = ((JackrabbitSession) userSession).getUserManager();
                Authorizable user = um.getAuthorizable(userProperties.getAuthorizableID());
                QueryBuilder queryBuilder = resource.getResourceResolver ().adaptTo(QueryBuilder.class);
                String path = ISO9075.encodePath(user.getPath())+"/commerce/orders/";
                Map map = new HashMap();
                map.put("type","unstructured");
                map.put("path",path);
                map.put("path.flat",true);
                map.put("orderby","@orderPlaced");
                map.put("orderby.sort","desc");
                map.put("1_relativedaterange.property","orderPlaced");
                map.put("1_relativedaterange.lowerBound","-6M");
                map.put("1_relativedaterange.upperBound", "0");
                com.day.cq.search.Query query = queryBuilder.createQuery(PredicateGroup.create(map), userSession);
                Iterator<Node> nodeIterator = query.getResult().getNodes();
                Predicate filter = getPredicate(predicate);
                while (nodeIterator.hasNext()) {
                    DefaultJcrPlacedOrder order = newPlacedOrderImpl(nodeIterator.next().getPath());
                    if ((filter == null) || (filter.evaluate(order))) {
                        orders.add(order);
                    }
                }
                userSession.save();
            }
        } catch (Exception e) {
            LOG.error("Error while fetching orders", e.getMessage());
        }
        return new PlacedOrderResult(orders, null, null);
    }

}
