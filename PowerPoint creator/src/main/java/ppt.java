import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



public class ppt {

    /*
    TO DO:
1. Create a new Json file method
2. Generate content for created JSON file with ChatGPT
3. Write the generated content of the JSON file
4. Parse the JSON file
5. Create a PowerPoint based on the Json file’s nodes
     */


    public static void createPresentationFromJson() {
        manageJSON newJSONFile = new manageJSON();
        newJSONFile.createJsonFile();

        // Read JSON data from the generated file
        JsonNode jsonData = readJsonFile("src/main/java/output.json");

        // Create a new PowerPoint presentation
        XMLSlideShow ppt = new XMLSlideShow();

        // Extract presentation details
        String presentationTitle = jsonData.get("presentation").get("title").asText();
        String presentationSubtitle = jsonData.get("presentation").get("subtitle").asText();
        //int slideCount = jsonData.get("presentation").get("slideCount").asInt();

        // Create the title slide
        XSLFSlide titleSlide = ppt.createSlide();
        addTitleSlide(titleSlide, presentationTitle, presentationSubtitle);

        // Create and add content slides
        JsonNode slides = jsonData.get("presentation").get("slides");
        for (JsonNode slideData : slides) {
            XSLFSlide contentSlide = ppt.createSlide();
            addContentSlide(contentSlide, slideData);
        }

        // Save the PowerPoint presentation to a file
        try {
            ppt.write(new FileOutputStream("src/main/java/presentation.pptx"));
            System.out.println("PowerPoint presentation created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addTitleSlide(XSLFSlide slide, String title, String subtitle) {
        XSLFTextBox titleTextBox = slide.createTextBox();
        titleTextBox.setAnchor(new java.awt.Rectangle(50, 50, 600, 50));
        XSLFTextRun titleRun = titleTextBox.addNewTextParagraph().addNewTextRun();
        titleRun.setText(title);

        XSLFTextBox subtitleTextBox = slide.createTextBox();
        subtitleTextBox.setAnchor(new java.awt.Rectangle(50, 100, 600, 50));
        XSLFTextRun subtitleRun = subtitleTextBox.addNewTextParagraph().addNewTextRun();
        subtitleRun.setText(subtitle);
    }

    private static void addContentSlide(XSLFSlide slide, JsonNode slideData) {
        String slideTitle = slideData.get("title").asText();
        ///String generatedHeading = slideData.get("heading").asText();
        String generatedMain = slideData.get("main").asText();

        XSLFTextBox titleTextBox = slide.createTextBox();
        titleTextBox.setAnchor(new java.awt.Rectangle(50, 50, 600, 50));
        XSLFTextRun titleRun = titleTextBox.addNewTextParagraph().addNewTextRun();
        titleRun.setText(slideTitle);

        /*
        XSLFTextBox headingTextBox = slide.createTextBox();
        headingTextBox.setAnchor(new java.awt.Rectangle(50, 100, 600, 50));
        XSLFTextRun headingRun = headingTextBox.addNewTextParagraph().addNewTextRun();
        headingRun.setText(generatedHeading);
*/
        XSLFTextBox contentTextBox = slide.createTextBox();
        contentTextBox.setAnchor(new java.awt.Rectangle(50, 100, 600, 400));
        XSLFTextRun contentRun = contentTextBox.addNewTextParagraph().addNewTextRun();
        contentRun.setText(generatedMain);
    }

    private static JsonNode readJsonFile(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + e.getMessage());
        }
    }
}


