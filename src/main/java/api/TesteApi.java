package api;

import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.google.gson.JsonObject;
import model.Ataque;
import model.Pokemon;
import model.Status;

public class TesteApi {
    public static void main(String[] args) throws Exception {
        PokeApiClient client = new PokeApiClient();
        PokemonMapper mapper = new PokemonMapper(client);

        String[] nomes = {"charmander", "bulbasaur", "squirtle", "charizard", "ditto", "pikachuuu"};

        for (String nome : nomes) {
            System.out.println("=== " + nome + " ===");
            try {
                JsonObject json = client.get("https://pokeapi.co/api/v2/pokemon/" + nome);
                Pokemon p = mapper.montar(json);

                System.out.println("Nome: " + p.getNome());
                System.out.println("Tipos: " + p.getTipos());
                System.out.println("Sprite: " + p.getSpriteUrl());
                JOptionPane.showMessageDialog(null, p.getNome(), "Sprite",
        JOptionPane.PLAIN_MESSAGE, new ImageIcon(URI.create(p.getSpriteUrl()).toURL()));

                Status s = p.getStatus();
                System.out.println("HP: " + s.getHp()
                        + " | Atk: " + s.getAtaque()
                        + " | Def: " + s.getDefesa()
                        + " | SpAtk: " + s.getAtaqueEspecial()
                        + " | SpDef: " + s.getDefesaEspecial()
                        + " | Vel: " + s.getVelocidade());
                System.out.println("HP atual: " + p.getHpAtual());

                System.out.println("Ataques:");
                for (Ataque a : p.getAtaques()) {
                    System.out.println("  - " + a.getNome()
                            + " | poder " + a.getPoder()
                            + " | tipo " + a.getTipo()
                            + " | " + a.getClass().getSimpleName());
                }
            } catch (PokemonNaoEncontradoException ex) {
                System.out.println("Recusado: " + ex.getMessage());
            } catch (ApiIndisponivelException ex) {
                System.out.println("Sem conexão: " + ex.getMessage());
            }
            System.out.println();
        }
    }
}