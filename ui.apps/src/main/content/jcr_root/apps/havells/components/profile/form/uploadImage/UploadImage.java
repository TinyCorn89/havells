package apps.havells.components.profile.form.uploadImage;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.wcm.foundation.forms.FormsHelper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

public class UploadImage extends WCMUse {

    private Resource imgResource;
    private Long width;
    private Long height;
    private boolean hideTitle;
    private boolean readOnly;
    private String id;
    private String title;
    private String name;
    private String description;
    private String css;
    private String imagePath;

    @Override
    public void activate() throws Exception {

        Resource resource = getResource();
        SlingHttpServletRequest slingRequest = getRequest();
        ValueMap properties = getProperties();

        hideTitle = properties.get("hideTitle", false);
        height = properties.get("height", 100l);
        width = properties.get("width", 100l);
        description = properties.get("jcr:description", "");
        title = properties.get("jcr:title", "");

        name = FormsHelper.getParameterName(resource);
        id = FormsHelper.getFieldId(slingRequest, resource);
        imgResource = FormsHelper.getResource(slingRequest, resource, name);
        readOnly = FormsHelper.isReadOnly(slingRequest, resource);
        css = FormsHelper.getCss(properties, "form_field form_field_file");

        imagePath = (imgResource != null) ? imgResource.getPath() : null;
    }

    public boolean isHideTitle() {
        return hideTitle;
    }

    public Resource getImgResource() {
        return this.imgResource;
    }

    public Long getHeight() {
        return this.height;
    }

    public Long getWidth() {
        return this.width;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public String getCss() {
        return css;
    }
}