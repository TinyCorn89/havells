<script type="text/html" id="jobListTemplate">
    <div class="branceWrapper">
        <div class="branceNumber">{count}</div>
        <div class="currentOppBranch">
            <ul>
                <li>
                    <span>
                        <div class="experince">${properties.branchLabel}</div>
                        <div class="standard">{branch}</div>
                    </span>
                </li>
                <li>
                    <span>
                        <div class="experince">${properties.productLabel}</div>
                        <div class="standard">{product}</div>
                    </span>
                </li>
                <li>
                    <span>
                        <div class="experince">${properties.experienceLabel}</div>
                        <div class="standard">{experience}</div>
                    </span>
                </li>
                <li>
                    <span>
                        <div class="experince">${properties.locationLabel}</div>
                        <div class="standard">{location}</div>
                    </span>
                </li>
                <li>
                    <span>
                        <div class="experince">${properties.positionLabel}</div>
                        <div class="standard">{position}</div>
                    </span>
                </li>
                <li>
                    <span>
                        <div class="experince">${properties.hashOfPositionLabel}</div>
                        <div class="standard">{hashOfPosition}</div>
                    </span>
                </li>
            </ul>
        </div>
        <div class="industryPreferenceWrapper">
            <span>
                <div class="industryPreference">${properties.industrialPrefrencesLabel}</div>
                <div class="switches">{industryPrefrences}</div>
            </span>
        </div>
        <div class="viweAllWrapper">
            <span>
                <c:if test="${isChecked eq 'true'}">
                    <div class="viewAll"><a href="{path}.html" target="_blank">${not empty properties.viewMoreText ? properties.viewMoreText : "View More" }</a></div>
                </c:if>
                <div class="applyButton"><a href="{path}.apply.html" target="_blank">${not empty properties.applyButtonText ? properties.applyButtonText : "APPLY"}</a></div>
            </span>
        </div>
    </div>
</script>
