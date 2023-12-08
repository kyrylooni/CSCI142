import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ppt {

    public static void main(String[] args) {
        try {
            // Read data from the JSON file
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonData = mapper.readValue(new File("/src/main/java/prompt_test.json"), JsonNode.class);
            JsonNode presentation = jsonData.get("presentation");

            // Extract presentation details
            String presentationTitle = presentation.get("title").asText();
            String presentationSubtitle = presentation.get("subtitle").asText();
            int slideCount = presentation.get("slideCount").asInt();
            JsonNode slides = presentation.get("slides");

            // Create a new PowerPoint presentation
            XMLSlideShow ppt = new XMLSlideShow();

            // Add title slide
            XSLFSlide titleSlide = ppt.createSlide();
            addTitleSlide(titleSlide, presentationTitle, presentationSubtitle);

            // Add slides to the presentation
            for (JsonNode slide : slides) {
                XSLFSlide contentSlide = ppt.createSlide();
                addContentSlide(contentSlide, slide);
            }

            // Save the presentation to a file
            try (FileOutputStream out = new FileOutputStream("output.pptx")) {
                ppt.write(out);
            }

            System.out.println("PowerPoint presentation created successfully.");

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
