import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;
import tools.ToolsManager;

import java.util.ArrayList;
import java.util.List;

import static tools.ToolsManager.toolCallLoop;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2 || !"-p".equals(args[0])) {
            System.err.println("Usage: program -p <prompt>");
            System.exit(1);
        }

        String prompt = args[1];

        ChatCompletionMessageParam first_msg = ChatCompletionMessageParam.ofUser(
                ChatCompletionUserMessageParam.builder()
                        .content(prompt)
                        .build());
        List<ChatCompletionMessageParam> messages = new ArrayList<>();
        messages.add(first_msg);

        String apiKey = System.getenv("OPENROUTER_API_KEY");
        String baseUrl = System.getenv("OPENROUTER_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "https://openrouter.ai/api/v1";
        }

        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("OPENROUTER_API_KEY is not set");
        }

        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();
        ChatCompletion response;
        do {

            response = client.chat().completions().create(
                    ChatCompletionCreateParams.builder()
                                                    .model("anthropic/claude-haiku-4.5")
//                            .model("anthropic/claude-sonnet-4.6")
                            .messages(messages)
                            .tools(ToolsManager.getAvailableTools())
                            .build());
            if (response.choices().isEmpty()) {
                throw new RuntimeException("no choices in response");
            }
            toolCallLoop(response, messages);
        } while (response.choices().getFirst().message().toolCalls().isPresent());
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.err.println("Logs from your program will appear here!");

        // TODO: Uncomment the line below to pass the first stage
         System.out.print(response.choices().getFirst().message().content().orElse(""));
    }
}
