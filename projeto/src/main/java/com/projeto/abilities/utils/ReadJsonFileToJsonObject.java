package com.projeto.abilities.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

// Essa classe lê um arquivo JSON e retorna um objeto JSON a partir do seu conteúdo.
public class ReadJsonFileToJsonObject {
    public JSONObject read() throws IOException {
        String file = "src/main/resources/openapi/response.json";
        String content = new String(Files.readAllBytes(Paths.get(file)));
        return new JSONObject(content);
    }
}
