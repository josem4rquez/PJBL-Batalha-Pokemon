package api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.Ataque;
import model.AtaqueEspecial;
import model.AtaqueFisico;
import model.Pokemon;
import model.Status;

public class PokemonMapper {
    
    private final PokeApiClient client;

    
    public PokemonMapper(PokeApiClient client) {
        this.client = client;
    }

    public Pokemon montar(JsonObject json)
            throws PokemonNaoEncontradoException, ApiIndisponivelException {
        String nome = json.get("name").getAsString();
        String sprite = json.getAsJsonObject("sprites").get("front_default").getAsString();
        ArrayList<String> tipos = lerTipos(json);
        Status status = lerStatus(json);
        ArrayList<Ataque> moves = lerAtaques(json);
        return new Pokemon(nome, tipos, status, moves, sprite);
    }

    private ArrayList<String> lerTipos(JsonObject json) {
        ArrayList<String> tipos = new ArrayList<>();
          for (JsonElement e : json.getAsJsonArray("types")) {
            JsonObject item = e.getAsJsonObject();
            String nome = item.getAsJsonObject("type").get("name").getAsString();
            tipos.add(nome);
        }
        return tipos;
    }

    private Status lerStatus(JsonObject json) {
        Map<String, Integer> statusMap = new HashMap<>();
        for (JsonElement e : json.getAsJsonArray("stats")) {
            JsonObject item = e.getAsJsonObject();
            String nomeStat = item.getAsJsonObject("stat").get("name").getAsString();
            int valorStat = item.get("base_stat").getAsInt();
            statusMap.put(nomeStat, valorStat);
        }
        return new Status(statusMap.get("hp"), statusMap.get("attack"), statusMap.get("defense"),
                statusMap.get("special-attack"), statusMap.get("special-defense"), statusMap.get("speed"));
    }

    private ArrayList<Ataque> lerAtaques(JsonObject json)
            throws PokemonNaoEncontradoException, ApiIndisponivelException {
        ArrayList<Ataque> moves = new ArrayList<>();
                for (JsonElement e : json.getAsJsonArray("moves")) {
            JsonObject item = e.getAsJsonObject();
            String urlAtaque = item.getAsJsonObject("move").get("url").getAsString();
            JsonObject jsonAtaque = client.get(urlAtaque);
            if  (jsonAtaque.get("power").isJsonNull()) {
                continue;
            }
            String classDamage = jsonAtaque.get("damage_class").getAsJsonObject().get("name").getAsString();
            if (!classDamage.equals("physical") && !classDamage.equals("special")) {
                continue;
            }
            String nomeAtaque = jsonAtaque.get("name").getAsString();
            int poder = jsonAtaque.get("power").getAsInt();
            String tipoAtaque = jsonAtaque.getAsJsonObject("type").get("name").getAsString();

            Ataque ataque;
            if (classDamage.equals("physical")) {
                ataque = new AtaqueFisico(nomeAtaque, poder, tipoAtaque);
            } else {
                ataque = new AtaqueEspecial(nomeAtaque, poder, tipoAtaque);
            }
            moves.add(ataque);

            if (moves.size() >= 4) {
                break;
                }
            }
                if (moves.size() < 4) {
                    throw new PokemonNaoEncontradoException(
                        "Pokémon sem ataques suficientes: só " + moves.size() + " válido(s)");
                }
        return moves;
    }
}