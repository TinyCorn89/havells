package com.havells.core.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomDateFormatter {

    private static final Logger LOG = LoggerFactory.getLogger(CustomDateFormatter.class);

    /**
     * This method returns date as string format e.g from 06/06/06 to Friday, 06 Jun 2014
     * @param dateParam Date in format <h3>mm/dd/yyyy</h3>
     *  @return convertedDate Returns the date as String <h3> Friday, 06 Jun 2014 </h3>
     */
   public String convertToPublicationDate(String dateParam){
       String convertedDate = "";
       DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
       Calendar calander = null;
       Date date = null;
       try{
           date = formatter.parse(dateParam);
           calander = Calendar.getInstance();
           calander.setTime(date);
           int year = calander.get(Calendar.YEAR);
           String dat = new SimpleDateFormat("dd").format(calander.getTime());
           String month = new SimpleDateFormat("MMM").format(calander.getTime());
           String dayOfWeek =  new SimpleDateFormat("EEEE").format(calander.getTime());
           convertedDate = dayOfWeek+", "+dat+" "+month+" "+year;
       }
       catch (ParseException parseex){
           LOG.error("Error during parsing of date :: " + parseex.getMessage());
       }

       return convertedDate;

   }

}
