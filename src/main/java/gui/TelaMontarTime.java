package gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import api.ApiIndisponivelException;
import api.FonteLocal;
import api.FontePokeApi;
import api.FontePokemon;
import api.PokemonNaoEncontradoException;
import model.Pokemon;
import model.TimeCheioException;
import model.Treinador;

// Tela 1: o jogador busca 3 Pokémon pelo nome e escolhe a dificuldade.
// "implements ActionListener": a própria tela trata os cliques dos botões, no actionPerformed.
public class TelaMontarTime extends JPanel implements ActionListener {

    // Pokémon que a CPU pode usar (sorteamos 3 diferentes).
    // Os 6 primeiros também estão no pokemons.csv, então servem no modo offline.
    private static final List<String> NOMES_CPU = List.of(
            "charmander", "squirtle", "bulbasaur", "chikorita", "cyndaquil", "totodile",
            "charmeleon", "wartortle", "ivysaur", "vulpix", "growlithe", "psyduck", "oddish", "bellsprout");

    private final JanelaPrincipal janela;

    // fonteLocal é do tipo FonteLocal (e não FontePokemon) de propósito:
    // assim o Java sabe que ela só lança PokemonNaoEncontradoException.
    private FontePokemon fonteApi = new FontePokeApi();    // a PokéAPI (precisa de internet)
    private FonteLocal fonteLocal = new FonteLocal();      // o pokemons.csv (funciona sem internet)

    private Treinador jogador = new Treinador("Você");     // o jogador e o time dele (classes do model)

    private final JTextField campoBusca = new JTextField(15);
    private final JLabel sprite = new JLabel("", SwingConstants.CENTER);
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>(); // os dados da lista
    private final JList<String> listaTime = new JList<>(modeloLista);              // o componente visual
    private final JComboBox<String> dificuldade = new JComboBox<>(new String[]{"Fácil", "Difícil"});
    private final JButton botaoBuscar = new JButton("Buscar");
    private final JButton botaoLimpar = new JButton("Limpar time");
    private final JButton botaoIniciar = new JButton("Iniciar batalha");

    public TelaMontarTime(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new BorderLayout());

        // ---------- topo: campo de busca ----------
        JPanel topo = new JPanel();
        topo.add(new JLabel("Pokémon:"));
        topo.add(campoBusca);
        topo.add(botaoBuscar);
        add(topo, BorderLayout.NORTH);

        // ---------- meio: sprite à esquerda, lista do time à direita ----------
        JPanel meio = new JPanel(new GridLayout(1, 2));   // 1 linha, 2 colunas
        meio.add(sprite);
        meio.add(new JScrollPane(listaTime));
        add(meio, BorderLayout.CENTER);

        // ---------- baixo: dificuldade e botões ----------
        JPanel baixo = new JPanel();
        baixo.add(new JLabel("Dificuldade:"));
        baixo.add(dificuldade);
        baixo.add(botaoLimpar);
        baixo.add(botaoIniciar);
        add(baixo, BorderLayout.SOUTH);

        botaoIniciar.setEnabled(false);                   // só liga quando tiver 3 Pokémon

        // todos os botões (e o Enter no campo de texto) chamam o actionPerformed desta classe
        botaoBuscar.addActionListener(this);
        campoBusca.addActionListener(this);
        botaoLimpar.addActionListener(this);
        botaoIniciar.addActionListener(this);
    }

    // Roda quando alguém clica num botão (ou aperta Enter no campo). getSource() diz quem foi.
    @Override
    public void actionPerformed(ActionEvent e) {
        Object origem = e.getSource();
        if (origem == botaoBuscar || origem == campoBusca) {
            buscar();
        } else if (origem == botaoLimpar) {
            reiniciar();
        } else if (origem == botaoIniciar) {
            iniciar();
        }
    }

    // Tenta a API; se estiver sem internet, busca no pokemons.csv.
    public Pokemon buscarPokemon(String nome) throws PokemonNaoEncontradoException {
        try {
            return fonteApi.buscar(nome);
        } catch (ApiIndisponivelException e) {
            return fonteLocal.buscar(nome);
        }
    }

    // Lê o nome digitado, busca e coloca no time.
    private void buscar() {
        String nome = campoBusca.getText().trim();
        if (nome.isEmpty()) {
            aviso("Digite o nome de um Pokémon.");
            return;
        }
        if (jogador.getTime().getPokemons().size() >= 3) {
            aviso("O time já tem 3 Pokémon. Use \"Limpar time\" para recomeçar.");
            return;
        }
        try {
            Pokemon pokemon = buscarPokemon(nome);
            jogador.getTime().adicionar(pokemon);   // pode lançar TimeCheioException
            modeloLista.addElement(descricao(pokemon));
            mostrarSprite(pokemon.getSpriteUrl());
            campoBusca.setText("");
            botaoIniciar.setEnabled(jogador.getTime().getPokemons().size() == 3);
        } catch (PokemonNaoEncontradoException ex) {
            aviso(ex.getMessage());                 // ex.: Pokémon não existe
        } catch (TimeCheioException ex) {
            aviso(ex.getMessage());
        } catch (RuntimeException ex) {
            // dado inesperado vindo da API (ex.: Pokémon sem imagem): não deixa travar
            aviso("Não foi possível ler os dados desse Pokémon. Tente outro.");
        }
    }

    // Monta o time da CPU e pede à janela para começar a batalha.
    private void iniciar() {
        Treinador cpu = montarTimeCpu();
        if (cpu == null) {
            return;                                 // já mostrou o erro ao usuário
        }
        janela.iniciarBatalha(jogador, cpu, dificuldadeDificil());
    }

    // Sorteia nomes da lista e busca até conseguir 3 Pokémon diferentes.
    private Treinador montarTimeCpu() {
        Treinador cpu = new Treinador("CPU");
        List<String> nomes = new ArrayList<>(NOMES_CPU);
        Collections.shuffle(nomes);

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            for (String nome : nomes) {
                if (cpu.getTime().getPokemons().size() == 3) {
                    break;
                }
                try {
                    cpu.getTime().adicionar(buscarPokemon(nome));
                } catch (PokemonNaoEncontradoException | TimeCheioException | RuntimeException e) {
                    // esse Pokémon não serviu (não existe, poucos ataques, dado estranho): tenta o próximo
                }
            }
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }

        if (cpu.getTime().getPokemons().size() < 3) {
            aviso("Não foi possível montar o time da CPU. Tente de novo.");
            return null;
        }
        return cpu;
    }

    // Começa de novo: jogador novo, time vazio. Também usada ao terminar uma partida.
    public void reiniciar() {
        jogador = new Treinador("Você");
        modeloLista.clear();
        sprite.setIcon(null);
        campoBusca.setText("");
        botaoIniciar.setEnabled(false);
    }

    // texto que aparece na lista, ex.: "charmander [fire] HP 39"
    private String descricao(Pokemon p) {
        return p.getNome() + " " + p.getTipos() + " HP " + p.getStatus().getHp();
    }

    private void mostrarSprite(String url) {
        sprite.setIcon(Imagens.sprite(url, 160));   // null se não carregar: fica sem imagem
    }

    private void aviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    private boolean dificuldadeDificil() {
        return "Difícil".equals(dificuldade.getSelectedItem());
    }
}
