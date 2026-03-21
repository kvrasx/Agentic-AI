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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.openai.core.JsonValue.*;

public class BashTool{

    private static FunctionParameters getBashParameters(){
        return FunctionParameters.builder()
                .putAdditionalProperty("type", from("object"))
                .putAdditionalProperty(
                        "properties",
                        from(
                                Map.of(
                                        "cmd",
                                        Map.of(
                                                "type", "string",
                                                "description",
                                                "the command to execute in shell")
                                )
                        ))
                .build();
    }
    public static ChatCompletionTool build (){
        return ChatCompletionTool.builder()
                .type(from("function"))
                .function(
                        FunctionDefinition.builder()
                                .name("Bash")
                                .description("the command to execute in shell")
                                .parameters(getBashParameters())
                                .build())
                .build();
    }
    public static String execute(JsonNode argsNode) {

        String cmd = argsNode.get("cmd").asText();
        StringBuilder output = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();   
    }
};