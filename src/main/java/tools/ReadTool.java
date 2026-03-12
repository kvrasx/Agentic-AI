package tools;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
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

public class ReadTool{

    private static FunctionParameters getReadParameters(){
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
                                                "the path to the file to read"))))
                .build();
    }
    public static ChatCompletionTool build (){
        return ChatCompletionTool.builder()
                .type(JsonValue.from("function"))
                .function(
                        FunctionDefinition.builder()
                                .name("Read")
                                .description("Read and return the contents of a file")
                                .parameters(getReadParameters())
                                .build())
                .build();
    }
    public static void execute(String arguments) {
        File objectFile = new File(arguments);
        try {
            String fileContent = Files.readString(objectFile.toPath());
            System.out.println(fileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
};