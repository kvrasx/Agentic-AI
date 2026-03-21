package tools;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.core.JsonValue;
import com.openai.models.FunctionDefinition;
import com.openai.models.FunctionParameters;
import com.openai.models.chat.completions.ChatCompletionTool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.openai.core.JsonValue.*;

public class WriteTool{

    private static FunctionParameters getWriteParameters(){
        return FunctionParameters.builder()
                .putAdditionalProperty("type", JsonValue.from("object"))
                .putAdditionalProperty(
                        "properties",
                        JsonValue.from(
                                Map.of(
                                        "filepath",
                                        Map.of(
                                                "type", "string",
                                                "description",
                                                "the path to the file to write on"),
                                        "content",
                                        Map.of(
                                                "type", "string",
                                                "description",
                                                "content to write on the file")
                                )
                        ))
                .build();
    }
    public static ChatCompletionTool build (){
        return ChatCompletionTool.builder()
                .type(JsonValue.from("function"))
                .function(
                        FunctionDefinition.builder()
                                .name("Write")
                                .description("Write the content to a specific file")
                                .parameters(getWriteParameters())
                                .build())
                .build();
    }
    public static String execute(JsonNode argsNode) {

        String filePath = argsNode.get("filepath").asText();
        String content = argsNode.get("content").asText();
        File objectFile = new File(filePath);
        try {
            Files.writeString(objectFile.toPath(), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Successfully wrote to " + filePath;
    }
};