/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arcadia;

/**
 *
 * @author Jonathan Green
 */
import org.apache.poi.* ;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;



import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.TreeMap;
import java.io.*;
import java.io.File;
import java.util.Map;

public class Config {
    
    private static String configFile, account;
    public  List<String> INVALID_SUBGRADES_36;
    public  List<String> INVALID_SUBGRADES_60;
    public  Map<Integer, List<String>> TERM_SUBGRADE_CHECK;
    public double orderMax;
    public String watch, termAllowance, wholeFrac;
    public TreeMap<Integer, String> creditModel;

    
    public Config(String configFile, String account){
        this.configFile = configFile;
        this.account = account;
    }
    
    public void readFile(){
        try {
            FileInputStream file = new FileInputStream(new File(configFile));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
//            System.out.println("Sheet name: "+ sheet.getSheetName());
            //remember that Java indexing begins at 0, so Excel Cell B3=Java (1,2) for example

            orderMax = sheet.getRow(5).getCell(7).getNumericCellValue();

            termAllowance = sheet.getRow(6).getCell(7).getStringCellValue();
 
            watch = sheet.getRow(7).getCell(7).getStringCellValue();
 
            creditModel = new TreeMap<>();
            creditModel.put(36, sheet.getRow(2).getCell(7).getStringCellValue());
            creditModel.put(60, sheet.getRow(3).getCell(7).getStringCellValue());
            
            INVALID_SUBGRADES_36 = new ArrayList<String>();            
            INVALID_SUBGRADES_60 = new ArrayList<String>();
            
            for (int row = 11; row < 46; row++){
                if (sheet.getRow(row).getCell(2).getStringCellValue().matches("NO")) {
                INVALID_SUBGRADES_36.add(sheet.getRow(row).getCell(0).getStringCellValue());
                }
                if (sheet.getRow(row).getCell(4).getStringCellValue().matches("NO")) {
                INVALID_SUBGRADES_60.add(sheet.getRow(row).getCell(0).getStringCellValue());
                }                
            }
                     
 		TERM_SUBGRADE_CHECK = new HashMap<>();
		TERM_SUBGRADE_CHECK.put(36, INVALID_SUBGRADES_36);
		TERM_SUBGRADE_CHECK.put(60, INVALID_SUBGRADES_60);  
                
            workbook.close();
            file.close();
            
        } catch (Exception e) {
            System.out.println("File " + configFile + " not found!");
            e.printStackTrace();
            }
       
    }

}
