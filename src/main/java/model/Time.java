package model;
import java.util.ArrayList;

public class Time {
    private ArrayList<Pokemon> pokemons = new ArrayList<>();

    public void adicionar(Pokemon pokemon) throws TimeCheioException {
        if (pokemons.size() >= 3) {
            throw new TimeCheioException("O time já tem 3 Pokémon.");
        }
        pokemons.add(pokemon);
    }

    // o ativo é o primeiro que ainda não desmaiou
    public Pokemon getAtivo() {
        for (Pokemon pokemon : pokemons) {
            if (!pokemon.estaDesmaiado()) {
                return pokemon;
            }
        }
        return null;
    }

    public boolean todosDesmaiados() {
        return getAtivo() == null;
    }

    // devolve uma cópia pra ninguém adicionar Pokémon sem passar pelo adicionar()
    public ArrayList<Pokemon> getPokemons() {
        return new ArrayList<>(pokemons);
    }
}