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
