package com.Ranit;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;

public class GmailPageObjects {

    // Create instance of the details class
    Details details = new Details();

    private WebDriver driver;
    @FindBy(how=How.XPATH, xpath="//input[@id='identifierId']")
    WebElement emailField;

    @FindBy(how=How.XPATH, xpath="//*[@id='password']/div[1]/div/div[1]/input")
    WebElement passwordField;

    @FindBy(how=How.XPATH, xpath="//span[@class='bog']")
    List<WebElement> emailThreads;

    @FindBy(how=How.XPATH, xpath = "//span[@class='bqe']")
    List<WebElement> unreadEmails;

    @FindBy(how=How.XPATH, xpath="//*[@id=\"gb\"]/div[2]/div[3]/div/div[2]/div/a/span")
    WebElement profileLogo;

    @FindBy(how=How.XPATH, xpath = "//a[@target='_blank']")
    List<WebElement> emailExtraction;

    @FindBy(how=How.XPATH, xpath = "//div[@dir='ltr']")
    List<WebElement> phoneNoExtraction;

    @FindBy(how=How.XPATH, xpath = "//*[@id=\":4\"]/div[2]/div[1]/div/div[1]/div")
    WebElement backButton;

    //Initialization of excel workbook

    public GmailPageObjects(WebDriver driver)
    {
        this.driver=driver;
    }

    public void enterEmail(String emailID)
    {
        waitForVisible(driver, emailField);
        Actions actions=new Actions(driver);
        actions.moveToElement(emailField);
        actions.click();
        actions.sendKeys(emailID + Keys.ENTER);
        actions.build().perform();
        System.out.println("Email entered");
    }

    public void enterPassword(String password)
    {
        waitForVisible(driver, passwordField);
        Actions actions=new Actions(driver);
        actions.moveToElement(passwordField);
        actions.click();
        actions.sendKeys(password + Keys.ENTER);
        actions.build().perform();
        System.out.println("Password entered");
    }


    public ArrayList<Details> extractInfo(String emailSubject)
    {
        waitForVisible(driver, profileLogo);
        ArrayList<Details> details = new ArrayList<>();

        for (int i = 0; i < emailThreads.size(); i++) {

            if (emailThreads.get(i).getText().contains(emailSubject)) {
                emailThreads.get(i).click();
                System.out.println("email clicked");
                String email = extractEmail();
                String phoneNumber = extractPhoneNo();
                String s_no = Integer.toString(i+1);
                Details newDetail = Details.createDetails(s_no,email,phoneNumber);
                details.add(newDetail);
                moveBack();
            }


        }
        return (details);
    }

    public void waitForVisible(WebDriver driver, WebElement element) {
        try {
            Thread.sleep(1000);
            System.out.println("Waiting for element visibility");
            WebDriverWait wait = new WebDriverWait(driver, 15);
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //Method to extract emailId from the mail

    public String extractEmail(){
        String printedEmail= null;
        for(int i=0; i< emailExtraction.size(); i++){
            String emailAddress = emailExtraction.get(i).getText();
            if (validate(emailAddress)){
                System.out.println(emailAddress);
                printedEmail = emailAddress;
            }
        }
        return printedEmail;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }


    public static final Pattern VALID_PHONE_NUMBER_REGEX =
            Pattern.compile("(0/91)?[6-9][0-9]{9}", Pattern.CASE_INSENSITIVE);

    public static final boolean validateNumber(String phNumber) {
        Matcher matcher = VALID_PHONE_NUMBER_REGEX.matcher(phNumber);
        return matcher.find();
    }


    // Java program to check if given mobile number
// is valid.



        public static String isValid(String s)
        {
            String printedNumber = null;
            // The given argument to compile() method
            // is regular expression. With the help of
            // regular expression we can validate mobile
            // number.
            // 1) Begins with 0 or 91
            // 2) Then contains 7 or 8 or 9.
            // 3) Then contains 9 digits
            Pattern p = Pattern.compile("(0/91)?[6-9][0-9]{9}");

            // Pattern class contains matcher() method
            // to find matching between given number
            // and regular expression
            Matcher m = p.matcher(s);
            if (m.find()) {
                System.out.println(m.group());
                printedNumber = m.group();
            }

        return printedNumber;
        }


        //Method to extract phone number

    public String extractPhoneNo(){
        String printedNumber = null;
        for(int i =0; i < phoneNoExtraction.size(); i++) {
            String phoneNumber = phoneNoExtraction.get(i).getText();
            if (validateNumber(phoneNumber)) {
                    printedNumber =isValid(phoneNumber);
            }
        }

    return printedNumber;
    }

    public void moveBack(){
        waitForVisible(driver, backButton);
        backButton.click();
    }

    public ArrayList<Details> clickEmail(String emailSubject){
        ArrayList<Details> info = extractInfo(emailSubject);
        ArrayList<Details> excelData = new ArrayList<>();
        Details myDetails = new Details("SNO","Email id", "Phone Number");
        excelData.add(myDetails);
        for (int i=0; i< info.size();i++){
            System.out.println("The details of the person are: email: "+info.get(i).getEmail()+" phone number: "+info.get(i).getPhoneNumber());
            String iteration = Integer.toString(i+1);
            myDetails = new Details(iteration, info.get(i).getEmail(), info.get(i).getPhoneNumber());
            excelData.add(myDetails);
        }
        return (excelData);
     }

    public ArrayList<Details> checkEmail(int rowCount,String emailSubject){
        ArrayList<Details> info = extractInfo(emailSubject);
        ArrayList<Details> excelData = new ArrayList<>();


        for (int i=0; i< info.size();i++){
            System.out.println("The details of the person are: email: "+info.get(i).getEmail()+" phone number: "+info.get(i).getPhoneNumber());
            String iteration = Integer.toString(rowCount);
            Details myDetails = new Details(iteration, info.get(i).getEmail(), info.get(i).getPhoneNumber());
            excelData.add(myDetails);
            rowCount++;
        }
        return (excelData);
    }


}

