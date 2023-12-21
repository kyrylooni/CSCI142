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
        System.out.println("Enter the number of slides you want to create: ");
        int slideCount = input.nextInt();
        System.out.println("Enter the title of your presentation: ");
        String presentationTitle = input.next();

        try {
            // Create a new JSON structure
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode jsonData = JsonNodeFactory.instance.objectNode();
            ObjectNode presentation = jsonData.putObject("presentation");

            // Add presentation details
            presentation.put("title", presentationTitle);
            presentation.put("subtitle", "Your Presentation Subtitle");
            presentation.put("slideCount", slideCount);  // Set the number of slides

            ArrayNode slides = presentation.putArray("slides");

            // Generate content for each slide
            for (int i = 1; i <= 3; i++) {  // Change 3 to the desired number of slides
                ObjectNode slide = slides.addObject();
                slide.put("title", "Slide " + i);

                // Generate content for the slide using ChatGPT API
                String generatedContent = OpenAI.chatGPT("Slide " + i);
                slide.put("generatedContent", generatedContent);
            }

            // Save the JSON to a file
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("output.json"), jsonData);

            System.out.println("JSON file created successfully.");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
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

        // add picture to each slide
        try {
            File image = new File("src/main/java/brain.jpg");
            byte[] picture = org.apache.commons.io.FileUtils.readFileToByteArray(image);
            XSLFPictureData idx = slide.getSlideShow().addPicture(picture, PictureData.PictureType.JPEG);
            slide.createPicture(idx);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addContentSlide(XSLFSlide slide, JsonNode slideData) {
        String slideTitle = slideData.get("title").asText();
        JsonNode content = slideData.get("content");
        String mainContent = content.get("main").asText();
        String subtitleContent = content.has("subtitle") ? content.get("subtitle").asText() : "";

        XSLFTextBox titleTextBox = slide.createTextBox();
        titleTextBox.setAnchor(new java.awt.Rectangle(50, 50, 600, 50));
        XSLFTextRun titleRun = titleTextBox.addNewTextParagraph().addNewTextRun();
        titleRun.setText(slideTitle);

        XSLFTextBox subtitleTextBox = slide.createTextBox();
        subtitleTextBox.setAnchor(new java.awt.Rectangle(50, 100, 600, 50));
        XSLFTextRun subtitleRun = subtitleTextBox.addNewTextParagraph().addNewTextRun();
        subtitleRun.setText(subtitleContent);

        XSLFTextBox contentTextBox = slide.createTextBox();
        contentTextBox.setAnchor(new java.awt.Rectangle(50, 150, 600, 200));
        XSLFTextRun contentRun = contentTextBox.addNewTextParagraph().addNewTextRun();
        contentRun.setText(mainContent);
    }

}
