package api;

import com.google.gson.JsonObject;

public class TesteApi {
    public static void main(String[] args) throws Exception {
        PokeApiClient api = new PokeApiClient();
        JsonObject json = api.get("https://pokeapi.co/api/v2/pokemon/charmander");
        System.out.println(json.get("name").getAsString());
    }
}