goal of the Code
-
The purpose of this Java code is to interact with Dobby-70B (a language model powered by Fireworks API) through an interactive chat session. The user sends a message, and the program makes an API call to Fireworks' API, which processes the input and returns a response from Dobby-70B. This allows real-time conversational interaction with the model.

The main functionality of the code includes:

1-Sending user input to the Fireworks API.  

2-Receiving model-generated responses from Dobby-70B.

3-Displaying the response back to the user.

4-Maintaining a chat loop where users can continuously interact with the model.

5-Graceful exit from the chat session when the user types "exit."


Key Benefits and Use Cases
-
Real-time Interaction: Provides a real-time conversational experience with the Dobby-70B model.

Flexible API Integration: Can be easily integrated into other applications or expanded for different conversational AI purposes.

Customizable Responses: The parameters (like temperature and max_tokens) allow for customizing the behavior of the model and the style of its responses.

Endless Conversations: Suitable for chatbots, virtual assistants, or companion agents that can hold ongoing conversations.

1- Prerequisites
-
Java 11+ installed.

Maven or Gradle for dependency management.

A Fireworks API Key (Get it from Fireworks AI).

2- Set Up Your Java Project
-
You need the OkHttp library to make HTTP requests and Gson for JSON parsing.

Maven (pom.xml)

    <dependencies>
        <dependency>
            <groupId>com.squareup.okhttp3</groupId>
            <artifactId>okhttp</artifactId>
            <version>4.9.3</version>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.9</version>
        </dependency>
    </dependencies>

Gradle (build.gradle)
-
    dependencies {
        implementation 'com.squareup.okhttp3:okhttp:4.9.3'
        implementation 'com.google.code.gson:gson:2.8.9'
    }

3- Implement Interactive Chat with Fireworks API
-
    import java.io.IOException;
    import java.util.Scanner;
    import com.google.gson.JsonArray;
    import com.google.gson.JsonObject;
    import com.google.gson.JsonParser;
    import okhttp3.*;
    
    public class DobbyChat {
        private static final String API_URL = "https://api.fireworks.ai/inference/v1/chat/completions";
        private static final String API_KEY = "YOUR_FIREWORKS_API_KEY"; // Replace with your actual API key
    
        private final OkHttpClient client = new OkHttpClient();
    
        public String chatWithDobby(String userMessage) throws IOException {
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", "accounts/sentientfoundation/models/dobby-unhinged-llama-3-3-70b-new");
            requestBody.addProperty("max_tokens", 1024);
            requestBody.addProperty("top_p", 1);
            requestBody.addProperty("top_k", 40);
            requestBody.addProperty("presence_penalty", 0);
            requestBody.addProperty("frequency_penalty", 0);
            requestBody.addProperty("temperature", 0.6);
    
            // Create message history
            JsonArray messages = new JsonArray();
            JsonObject userMessageObj = new JsonObject();
            userMessageObj.addProperty("role", "user");
            userMessageObj.addProperty("content", userMessage);
            messages.add(userMessageObj);
    
            requestBody.add("messages", messages);
    
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(RequestBody.create(requestBody.toString(), MediaType.get("application/json; charset=utf-8")))
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Accept", "application/json")
                    .build();
    
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return "Error: " + response.code() + " - " + response.message();
                }
                JsonObject jsonResponse = JsonParser.parseString(response.body().string()).getAsJsonObject();
                return jsonResponse.getAsJsonArray("choices").get(0).getAsJsonObject().get("message").getAsJsonObject().get("content").getAsString();
            }
        }
    
        public static void main(String[] args) {
            DobbyChat chatBot = new DobbyChat();
            Scanner scanner = new Scanner(System.in);
    
            System.out.println("Dobby-70B Interactive Chat. Type 'exit' to quit.");
            while (true) {
                System.out.print("You: ");
                String userInput = scanner.nextLine();
    
                if ("exit".equalsIgnoreCase(userInput)) {
                    System.out.println("Exiting chat. Goodbye!");
                    break;
                }
    
                try {
                    String response = chatBot.chatWithDobby(userInput);
                    System.out.println("Dobby: " + response);
                } catch (IOException e) {
                    System.out.println("Error communicating with Dobby: " + e.getMessage());
                }
            }
            scanner.close();
        }
    }

4- Running the Chatbot
-
Replace "YOUR_FIREWORKS_API_KEY" with your actual API key.

Compile & Run the Java program:

    javac DobbyChat.java
    java DobbyChat

Start Chatting





   
