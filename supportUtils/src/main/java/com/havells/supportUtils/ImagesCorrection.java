package com.havells.supportUtils;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;

@SlingServlet(paths = {"/bin/search/correctImagePaths"},
        methods = {"GET"}, generateComponent = false)
@Component(label = "PIM Image  correction", enabled = true, metatype = false, immediate = true)
public class ImagesCorrection extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(PimImagePathMapperServlet.class);

    ResourceResolver resolver;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        String imageDirPath = request.getParameter("imageDirPath");
        LOGGER.info("directory "+imageDirPath+" --- ");
        resolver = request.getResourceResolver();
        listFolders(imageDirPath);
        LOGGER.info("no of directories found " + counter);
    }
    int counter = 0;
     /**
     * List all the folder under a directory
     * @param directoryName to be listed
     */
    public void listFolders(String directoryName){
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        if(fList != null && fList.length > 0) {
            for (File file : fList) {
                LOGGER.info("------------------------------------------------------------------------------");
                LOGGER.info(file.getPath());
                if (file.isFile() && (file.getName().equalsIgnoreCase("Thumbs.db") ||
                        file.getName().contains(".xlsx") || file.getName().contains(".psd")
                        || file.getName().contains(".indd") )) {
                    file.delete();
                }
                if (file.isDirectory()) {
                        String newDirName = PimNodeUtil.generateJcrFriendlyName(file.getName());
                        file.renameTo(new File(file.getParent() + "/" + newDirName));
                        counter++;
                }
                LOGGER.info(file.getPath());
                listFolders(file.getPath());
            }
        }
    }

    /**
     *
     * @param path
     */
    private void correctJcrImagePaths(String path){
        Resource resource = resolver.getResource(path);
         if(resource != null){
              Iterator<Resource> itr = resource.listChildren();
              while( itr.hasNext()){
                  Resource res = itr.next();
                  LOGGER.info(res.getPath());
                  if(res.hasChildren()){
                      correctJcrImagePaths(resource.getPath());
                  }
              }
         }
    }
}
