package api;

import model.Ataque;
import model.Pokemon;

public class TesteApi {
    public static void main(String[] args) {
        FontePokemon fonte = new FontePokeApi();

        String[] nomes = {"charmander", "charmander", "Charmander ", "bulbasaur", "ditto", "pikachuuu"};

        for (String nome : nomes) {
            System.out.println("=== buscando \"" + nome + "\" ===");
            long inicio = System.currentTimeMillis();
            try {
                Pokemon p = fonte.buscar(nome);
                long tempo = System.currentTimeMillis() - inicio;

                System.out.println("Nome: " + p.getNome() + " | Tipos: " + p.getTipos()
                        + " | HP: " + p.getStatus().getHp());
                for (Ataque a : p.getAtaques()) {
                    System.out.println("  - " + a.getNome() + " (" + a.getPoder() + ")");
                }
                System.out.println("Tempo: " + tempo + " ms");
            } catch (PokemonNaoEncontradoException ex) {
                System.out.println("Recusado: " + ex.getMessage());
            } catch (ApiIndisponivelException ex) {
                System.out.println("Sem conexão: " + ex.getMessage());
            }
            System.out.println();
        }
    }
}