package tools;

import com.openai.models.chat.completions.ChatCompletionTool;

import java.util.ArrayList;
import java.util.List;

public class ToolsManager {
    public static List<ChatCompletionTool> getAvailableTools() {

        ChatCompletionTool readTool = ReadTool.build();

        List<ChatCompletionTool> tools = new ArrayList<>();
        tools.add(readTool);
        return tools;
    }
}
