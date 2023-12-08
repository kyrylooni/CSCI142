import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
/*
public class Main {

    public static void main(String[] args) throws IOException {
        // set up the mapper object
        ObjectMapper mapper = new ObjectMapper();

        // create a new instance of the class
        OpenAI openai = new OpenAI();

        try {
            Map<String, String> data = mapper.readValue(new File("src/main/java/prompt_test.json"), Map.class);
            System.out.println(OpenAI.chatGPT("Create a presentation title for the topic " + data.get("Topic") + "with slide count of" + data.get("Slide count") +"."));
        }
        catch (IOException e) {
            System.out.println("Error: " + e);
        }
        //





    }
}
*/