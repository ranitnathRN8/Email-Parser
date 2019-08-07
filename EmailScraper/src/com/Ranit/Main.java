package com.Ranit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        //Selenium and Chromedriver initiation
        Details myDetails = new Details();
        String driverPath = System.getProperty("user.dir");
        System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\forChrome\\chromedriver.exe");//Replace with directory location of chromedriver.
        //replace with only Chromedriver.exe if it is in the same project folder
        ChromeDriver driver = new ChromeDriver();


        driver.manage().window().maximize();
        driver.get("https://mail.google.com");//url for gmail
        GmailPageObjects gp=PageFactory.initElements(driver, GmailPageObjects.class);
        gp.enterEmail("myemail");//Replace with your email id
        gp.enterPassword("mypassword");//Replace with your password
        String emailSubject = "commonsubject"; //Replace with the common email subject

        String excelFilePath = "E:\\--\\--\\---\\EmailParsing.xls";//Replace with the location of the excel file
                                                                                        //or the location you want it to be saved to with the file name

        //Check if the excel file exists
        if (new File(excelFilePath).exists()){
            System.out.println("Excel file already exists. Updating current file...\n");
            int rowCount = myDetails.getRowCount(excelFilePath);
            ArrayList<Details> updateInfo = gp.checkEmail(rowCount, emailSubject);
            myDetails.updateExcel(updateInfo, excelFilePath);
        }
        else {
            //To create the new file
            ArrayList<Details> info = gp.clickEmail(emailSubject);
            myDetails.writeExcel(info, excelFilePath);
        }

    }
}
