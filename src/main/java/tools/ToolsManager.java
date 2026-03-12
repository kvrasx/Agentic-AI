package tools;

import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionMessageToolCall;
import com.openai.models.chat.completions.ChatCompletionTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolsManager {
    public static List<ChatCompletionTool> getAvailableTools() {

        ChatCompletionTool readTool = ReadTool.build();

        List<ChatCompletionTool> tools = new ArrayList<>();
        tools.add(readTool);
        return tools;
    }
    public static void toolCallLoop(ChatCompletion response){
        Optional<List<ChatCompletionMessageToolCall>> tools = response.choices().get(0).message().toolCalls();
        tools.ifPresent(toolList -> {
            for (ChatCompletionMessageToolCall tool : toolList) {

                String functionName = tool.function().name();
                String arguments = tool.function().arguments();

                switch (functionName) {
                    case "Read" -> {
                        System.out.println(ReadTool.execute(arguments));
                    }
                    default -> throw new RuntimeException("Unknown function: " + functionName);
                }
            }
        });
    }
}
