package api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import model.Pokemon;

public class FontePokeApi implements FontePokemon {

    private static final String URL_BASE = "https://pokeapi.co/api/v2/pokemon/";

    private final PokeApiClient client = new PokeApiClient();
    private final PokemonMapper mapper = new PokemonMapper(client);
    private final Map<String, Pokemon> cache = new HashMap<>();

    @Override
    public Pokemon buscar(String nome)
            throws PokemonNaoEncontradoException, ApiIndisponivelException {
        String chave = nome.toLowerCase().trim();
        if (cache.containsKey(chave)) {
            return copiar(cache.get(chave));
        }

        JsonObject json = client.get(URL_BASE + chave);
        Pokemon pokemon = mapper.montar(json);
        cache.put(chave, pokemon);

        return copiar(pokemon);
    }

    private Pokemon copiar(Pokemon original) {
        return new Pokemon(
            original.getNome(),
            new ArrayList<>(original.getTipos()),
            original.getStatus(),
            new ArrayList<>(original.getAtaques()),
            original.getSpriteUrl());
    }
}
