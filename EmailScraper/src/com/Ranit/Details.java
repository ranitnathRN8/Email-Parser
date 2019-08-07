package com.Ranit;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.awt.print.Book;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Details {
    private String s_no;
    private String email;
    private String phoneNumber;

    public Details(){

    }

    public Details(String s_no, String email, String phoneNumber) {
        this.s_no = s_no;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getS_no() { return s_no; }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //because it is a statis method, it can be accessed elsewhere
    public static Details createDetails(String s_no, String email, String phoneNumber){
        return new Details(s_no,email,phoneNumber);
    }

    public void writeExcel(List<Details> listDetails, String excelFilePath) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        int rowCount = 0;

        for (Details aDetail : listDetails) {
            Row row = sheet.createRow(++rowCount);
            writeBook(aDetail, row);
        }

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateExcel(List <Details> listDetails, String excelFilePath) throws IOException{
        try{
            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

            List <Details> updatedList = checkEntry(listDetails, sheet);

            if (updatedList == null){
                FileOutputStream outputStream = new FileOutputStream(excelFilePath);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
            }
            else {
                int rowCount = sheet.getLastRowNum();

                for (Details aDetail : updatedList) {
                    Row row = sheet.createRow(++rowCount);
                    writeBook(aDetail, row);
                }

                inputStream.close();

                FileOutputStream outputStream = new FileOutputStream(excelFilePath);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();

            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Details> checkEntry(List <Details> listDetails, Sheet sheet){
        Iterator<Row> iterator = sheet.iterator();

        while (iterator.hasNext()){
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();

                System.out.println(cell.getStringCellValue());
                for (int i = 0; i < listDetails.size();  i++){

                    if (listDetails.get(i).getEmail().equals(cell.getStringCellValue())){
                        System.out.println("Common record is "+listDetails.get(i).getEmail());
                        listDetails.remove(i);
                    }
                }
            }
        }
        return (listDetails);
    }

    public int getRowCount(String excelFilePath) throws IOException{
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum();
        inputStream.close();
        return rowCount;
    }

    private void writeBook(Details aDetail, Row row) {
        Cell cell = row.createCell(1);
        cell.setCellValue(aDetail.getS_no());

        cell = row.createCell(2);
        cell.setCellValue(aDetail.getEmail());

        cell = row.createCell(3);
        cell.setCellValue(aDetail.getPhoneNumber());

    }

}
