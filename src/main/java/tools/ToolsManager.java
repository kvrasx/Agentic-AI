package tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.models.chat.completions.*;

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
    public static void toolCallLoop(ChatCompletion response, List<ChatCompletionMessageParam> messages){
        Optional<List<ChatCompletionMessageToolCall>> tools = response.choices().getFirst().message().toolCalls();

        tools.ifPresent(toolList -> {
            for (ChatCompletionMessageToolCall tool : toolList) {

                String functionName = tool.function().name();
                String arguments = tool.function().arguments();
                JsonNode argsNode;
                ObjectMapper mapper = new ObjectMapper();

                try {
                    argsNode = mapper.readTree(arguments);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse arguments", e);
                }

                switch (functionName) {
                    case "Read" -> {
                         messages.add(ChatCompletionMessageParam.ofUser(
                                ChatCompletionUserMessageParam.builder()
                                        .content(ReadTool.execute(argsNode))
                                        .build()));
                    }
                    default -> throw new RuntimeException("Unknown function: " + functionName);
                }
            }
        });
    }
}
