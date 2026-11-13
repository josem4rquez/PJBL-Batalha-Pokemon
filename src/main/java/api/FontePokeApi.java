package api;

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
            return cache.get(chave);
        }

        JsonObject json = client.get(URL_BASE + chave);
        Pokemon pokemon = mapper.montar(json);

        cache.put(chave, pokemon);

        return pokemon;
    }
}
