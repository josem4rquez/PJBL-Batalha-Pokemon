package api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TesteApi {
    public static void main(String[] args) throws Exception {
        PokeApiClient api = new PokeApiClient();
        JsonObject json = api.get("https://pokeapi.co/api/v2/pokemon/charmander");
        System.out.println(json.get("name").getAsString());

        List<String> abilities = new ArrayList<>();
        
        for (JsonElement e : json.getAsJsonArray("abilities") ) {
            JsonObject item = e.getAsJsonObject();
            String nome = item.getAsJsonObject("ability").get("name").getAsString();
            abilities.add(nome);
        
        }

        List<String> tipos = new ArrayList<>();
        for (JsonElement e : json.getAsJsonArray("types")) {
            JsonObject item = e.getAsJsonObject();
            String nome = item.getAsJsonObject("type").get("name").getAsString();
            tipos.add(nome);
        }

        
        String sprite = json.getAsJsonObject("sprites").get("front_default").getAsString();
        
        System.out.println("Abilities: " + abilities);
        System.out.println("Types: " + tipos);
        System.out.println("Sprite: " + sprite);
        
        for (JsonElement e : json.getAsJsonArray("stats")) {
            JsonObject item = e.getAsJsonObject();
            String nomeStat = item.getAsJsonObject("stat").get("name").getAsString();
            int valorStat = item.get("base_stat").getAsInt();
            System.out.println(nomeStat + ": " + valorStat);
        }
    } 
}