import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class manageJSON {

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


}
