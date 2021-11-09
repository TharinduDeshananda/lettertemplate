package com.example.letter.lettertemplate.contentHandlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TemplateFileHandler {
    private String dirPath;


    public boolean createLetterTemplate(String fileName,String htmlContent){
        File parentDir = new File(dirPath);
        FileWriter fileWriter=null;
        System.out.println("template directory exists: "+parentDir.exists());
        File tempFile = new File(parentDir,fileName);
        try{

            fileWriter = new FileWriter(tempFile);
            fileWriter.write(htmlContent);
            fileWriter.flush();
            System.out.println("file written complete");
            fileWriter.close();
            return true;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }finally {
        }

    }

    public List<String> getAllHtmlFileNames(){

        try{
            File parentDir = new File(dirPath);
            System.out.println("is directory : "+parentDir.isDirectory());
            String[] htmlFileNames = parentDir.list((File dir,String filename)->{
                if(FilenameUtils.getExtension(filename).equalsIgnoreCase("html"))return true;
                return false;
            });
        return Arrays.asList(htmlFileNames);
        }catch(Exception e){
            System.out.println(e);
        }
        return new ArrayList<String>();
    }

    public String getHtmlContentByFileName(String fileName){
        try{
            File parentDir = new File(dirPath);
            File tempFile = new File(parentDir,fileName+".html");

            String htmlContent = FileUtils.readFileToString(tempFile);
            return htmlContent;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }

    }

    public boolean deleteTemplateFileByName(String fileName){
        try{
            File parentDir = new File(dirPath);
            File tempFile = new File(parentDir,fileName+".html");

            File file = new File(parentDir,fileName);
            if(!file.exists())return false;
            return file.delete();
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }


    public void replaceTemplate(String fileName, String htmlContent) {
        File fileToBeModified = new File(dirPath);

        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;

        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));
            //Reading all the lines of input text file into oldContent
            String line = reader.readLine();
            while (line != null)
            {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }
            //Replacing oldString with newString in the oldContent
            String newContent = oldContent.replaceAll(htmlContent, htmlContent);
            //Rewriting the input text file with newContent
            writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
            reader.close();
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
