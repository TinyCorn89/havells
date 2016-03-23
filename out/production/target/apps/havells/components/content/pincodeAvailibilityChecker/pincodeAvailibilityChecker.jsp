<%@include file="/apps/havells/global.jsp"%>
<%@page session="false" %>

<cq:text tagName="h4" placeholder="Drop Your Customized Image"/>

<div class="midContainer">
    <div class="container">
        <div class="master">
            <div class="forgotPwdWrap">
                <div class="" id="pincodeAvailability">
                    <p>Please enter the pincode:</p>
                    <input name="pin" id="pin" type="text" class="input" value=""/>
                    <input name="pinloc" id="pinloc" type="hidden" class="input" value="${properties.pincodeLocation}"/>
                    <input name="codAvailable" id="codAvailable" type="hidden" class="input"
                           value="${properties.codAvailable}"/>
                    <input name="codUnavailable" id="codUnavailable" type="hidden" class="input"
                           value="${properties.codUnavailable}"/>
                    <input name="pinNotAvailable" id="pinNotAvailable" type="hidden" class="input"
                           value="${properties.pinNotAvailable}"/>

                    <div class="buttonWrapper">
                        <div id="checkPinAvailability" class="buttonGlbl"><a href="javascript:;"><span><i
                                class="fa fa-arrow-right"></i> Check Availability</span></a></div>
                    </div>
                    <label class="result"></label>
                </div>
            </div>

        </div>
    </div>
</div>
<div style="clear:both"></div>
