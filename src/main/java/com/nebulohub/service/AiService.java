package com.nebulohub.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AiService {

    private final Client genaiClient;
    private final String modelName;

    public AiService(@Value("${nebulohub.ai.model-name}") String modelName) {
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("A variável de ambiente 'GEMINI_API_KEY' não foi definida.");
        }
        this.modelName = modelName;
        this.genaiClient = new Client();
    }

    /**
     * Gera uma análise de "Prós e Contras" para uma ideia de startup.
     * **MODIFICADO:** Agora aceita um 'language' ("en" ou "pt").
     *
     * @param postDescription A descrição da ideia.
     * @param language O código do idioma (ex: "pt" ou "en").
     * @return Uma string formatada com a análise.
     * @throws IOException Se houver um erro na chamada da API.
     */
    public String getStartupCritique(String postDescription, String language) throws IOException {

        // 1. Seleciona o prompt com base no idioma
        String promptText;
        if ("pt".equals(language)) {
            promptText = """
                Você é um analista de startups experiente, similar a um investidor "Shark Tank".
                Analise a seguinte ideia de startup e forneça sua opinião.
                
                Seja conciso e direto. Responda em Português.
                Retorne EXATAMENTE no seguinte formato:
                
                **Prós:**
                - [Pró 1]
                - [Pró 2]
                - [Pró 3]
                
                **Contras:**
                - [Contra 1]
                - [Contra 2]
                - [Contra 3]

                ---
                Ideia: "%s"
                """.formatted(postDescription);
        } else { // Default para Inglês
            promptText = """
                You are an experienced startup analyst, similar to a "Shark Tank" investor.
                Analyze the following startup idea and provide your opinion.
                
                Be concise and direct. Respond in English.
                Return the response in EXACTLY the following format:
                
                **Pros:**
                - [Pro 1]
                - [Pro 2]
                - [Pro 3]
                
                **Cons:**
                - [Con 1]
                - [Con 2]
                - [Con 3]

                ---
                Idea: "%s"
                """.formatted(postDescription);
        }

        // 2. Chama a API
        GenerateContentResponse response = this.genaiClient.models.generateContent(
                this.modelName,
                promptText,
                null
        );

        // 3. Retorna o texto da resposta
        return response.text();
    }
}