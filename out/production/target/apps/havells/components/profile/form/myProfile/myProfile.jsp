<%@ page import="org.apache.sling.api.resource.Resource" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>
<div class="master">
    <div class="myProfileWrap">
        <h1>${properties.greetingText} <span class="firstname"></span></h1>
        <div class="col">

            <div class="fieldWrap">
                <div class="label">Email Address</div>
                <div class="details email"></div>
            </div>
            <div class="fieldWrap">
                <div class="label">Password</div>
                <div class="details password">*********</div>
            </div>
            <div class="fieldWrap">
                <div class="label">Repeat Password</div>
                <div class="details repeatPassword">*********</div>
            </div>
            <div class="fieldWrap">
                <div class="label">FirstName</div>
                <div class="details name"></div>
            </div>
            <div class="fieldWrap">
                <div class="label">Lastname</div>
                <div class="details surname"></div>
            </div>

            <div class="buttonWrapper">
                <div class="buttonGlbl edit"><a href="${properties.editPath}"><span><i class="fa fa-arrow-right"></i> Edit</span></a></div>
            </div>
        </div>
        <div class="col">
            <div class="fieldWrap posRel">
                <div class="label">Profile Picture</div>
                  <div class="details"></div>
                  <img src="image-placeholder.jpg" class="profilePh" />
            </div>
            <div class="fieldWrap">
                <div class="label">Mobile</div>
                <div class="details mobile"></div>
            </div>
            <div class="fieldWrap">
                <div class="label">Date of Birth</div>
                <div class="details dateOfBirth"></div>
            </div>
            <div class="fieldWrap">
                <div class="label">Billing Address</div>
                <div class="details billingAddress"></div>
            </div>
            <div class="fieldWrap">
                <div class="label">Shipping Address</div>
                <div class="details shippingAddress"></div>
            </div>
        </div>
    </div>
    <div class="anonymous">
    </div>
</div>
