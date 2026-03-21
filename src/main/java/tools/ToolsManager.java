package tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.models.chat.completions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolsManager {
    public static List<ChatCompletionTool> getAvailableTools() {

        List<ChatCompletionTool> tools = new ArrayList<>();

        ChatCompletionTool readTool = ReadTool.build();
        ChatCompletionTool writeTool = WriteTool.build();
        ChatCompletionTool bashTool = BashTool.build();
        tools.add(readTool);
        tools.add(writeTool);
        tools.add(bashTool);
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
                         messages.add(ChatCompletionMessageParam.ofTool(ChatCompletionToolMessageParam.builder()
                                 .toolCallId(tool.id())
                                 .content(ReadTool.execute(argsNode))
                                 .build()));
                    }
                    case "Write" -> {
                        messages.add(ChatCompletionMessageParam.ofTool(ChatCompletionToolMessageParam.builder()
                                .toolCallId(tool.id())
                                .content(WriteTool.execute(argsNode))
                                .build()));
                    }
                    case "Bash" -> {
                        messages.add(ChatCompletionMessageParam.ofTool(ChatCompletionToolMessageParam.builder()
                                .toolCallId(tool.id())
                                .content(BashTool.execute(argsNode))
                                .build()));
                    }
                    default -> throw new RuntimeException("Unknown function: " + functionName);
                }
            }
        });
    }
}
