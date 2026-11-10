package api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PokeApiClient {

    // Navegador http pra solicitar requisões pra API
    private final HttpClient client = HttpClient.newHttpClient();

    public JsonObject get(String url) throws PokemonNaoEncontradoException, ApiIndisponivelException {
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ApiIndisponivelException("Erro ao acessar a API", e);
        } 
            if (response.statusCode() == 404) {
                    throw new PokemonNaoEncontradoException("Pokemon não encontrado " + url);
                }
        return JsonParser.parseString(response.body()).getAsJsonObject();
    }
}


