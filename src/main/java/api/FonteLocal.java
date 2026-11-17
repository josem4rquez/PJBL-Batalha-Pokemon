package api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.Ataque;
import model.AtaqueEspecial;
import model.AtaqueFisico;
import model.Pokemon;
import model.Status;

// lê os pokémon do pokemons.csv, usada quando a API não responde
public class FonteLocal implements FontePokemon {

    @Override
    public Pokemon buscar(String nome) throws PokemonNaoEncontradoException {
        try {
            BufferedReader leitor = new BufferedReader(new FileReader("pokemons.csv"));
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados[0].equals(nome.toLowerCase().trim())) {
                    leitor.close();
                    return montar(dados);
                }
            }
            leitor.close();
        } catch (IOException e) {
            throw new PokemonNaoEncontradoException("Erro ao ler o pokemons.csv");
        }
        throw new PokemonNaoEncontradoException("Pokemon não encontrado: " + nome);
    }

    // nome,tipo,hp,ataque,defesa,ataqueEsp,defesaEsp,velocidade,sprite e 4 ataques (nome,poder,tipo,F ou E)
    private Pokemon montar(String[] dados) {
        ArrayList<String> tipos = new ArrayList<>();
        tipos.add(dados[1]);

        Status status = new Status(Integer.parseInt(dados[2]), Integer.parseInt(dados[3]),
                Integer.parseInt(dados[4]), Integer.parseInt(dados[5]),
                Integer.parseInt(dados[6]), Integer.parseInt(dados[7]));

        ArrayList<Ataque> ataques = new ArrayList<>();
        for (int i = 9; i < 25; i += 4) {
            String nomeAtaque = dados[i];
            int poder = Integer.parseInt(dados[i + 1]);
            String tipoAtaque = dados[i + 2];

            Ataque ataque;
            if (dados[i + 3].equals("F")) {
                ataque = new AtaqueFisico(nomeAtaque, poder, tipoAtaque);
            } else {
                ataque = new AtaqueEspecial(nomeAtaque, poder, tipoAtaque);
            }
            ataques.add(ataque);
        }
        return new Pokemon(dados[0], tipos, status, ataques, dados[8]);
    }
}