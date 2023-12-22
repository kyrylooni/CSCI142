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

    

    //create a new JSON file method
    public static void createJsonFile() {
        Scanner input = new Scanner(System.in);

        // Title of the presentation
        System.out.println("Enter the title of the presentation: ");
        String presentationTitle = input.nextLine();

        System.out.println("Enter the number of slides you want to create: ");
        int slideCount = input.nextInt();

        try {
            // Create a new JSON structure
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode jsonData = JsonNodeFactory.instance.objectNode();
            ObjectNode presentation = jsonData.putObject("presentation");

            // Add presentation details
            presentation.put("title", presentationTitle);
            presentation.put("slideCount", slideCount);
            presentation.put("subtitle", OpenAI.chatGPT("Subtitle for " + presentationTitle + " presentation"));


            ArrayNode slides = presentation.putArray("slides");
            int numOfWords = 100;

            // Generate content for each slide
            for (int i = 1; i <= slideCount; i++) {
                ObjectNode slide = slides.addObject();
                slide.put("title", "Slide " + i);


                // Generate content for the heading using ChatGPT API
                //String generatedHeading = OpenAI.chatGPT("Heading for Slide  in" + presentationTitle + " presentation");
                //slide.put("heading", generatedHeading);

                // Generate content for the main using ChatGPT API
                String generatedMain = OpenAI.chatGPT("Main content for Slide" + i + "in" + presentationTitle + " presentation with maximum number of " + numOfWords + " words per slide");
                slide.put("main", generatedMain);

            }

            // Save the JSON to a file
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/java/output.json"), jsonData);

            System.out.println("JSON file created successfully.");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void createPresentationFromJson() {
        // Read JSON data from the generated file
        JsonNode jsonData = readJsonFile("src/main/java/output.json");

        // Create a new PowerPoint presentation
        XMLSlideShow ppt = new XMLSlideShow();

        // Extract presentation details
        String presentationTitle = jsonData.get("presentation").get("title").asText();
        String presentationSubtitle = jsonData.get("presentation").get("subtitle").asText();
        int slideCount = jsonData.get("presentation").get("slideCount").asInt();

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
        contentTextBox.setAnchor(new java.awt.Rectangle(50, 150, 600, 400));
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


