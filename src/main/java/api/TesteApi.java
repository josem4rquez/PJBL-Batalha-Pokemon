package api;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TesteApi {
    public static void main(String[] args) throws Exception {
        PokeApiClient api = new PokeApiClient();
        JsonObject json = api.get("https://pokeapi.co/api/v2/pokemon/pikachuuu");
        System.out.println(json.get("name").getAsString());

        List<String> abilities = new ArrayList<>();
        
        for (JsonElement e : json.getAsJsonArray("abilities") ) {
            JsonObject item = e.getAsJsonObject();
            String nome = item.getAsJsonObject("ability").get("name").getAsString();
            abilities.add(nome);
    }
} 
}