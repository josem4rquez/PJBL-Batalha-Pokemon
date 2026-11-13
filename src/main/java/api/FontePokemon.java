package api;

import model.Pokemon;

public interface FontePokemon {
    Pokemon buscar(String nome) throws PokemonNaoEncontradoException, ApiIndisponivelException;
    
}
