package gui;

import api.ApiIndisponivelException;
import api.FontePokeApi;
import api.FontePokemon;
import api.PokemonNaoEncontradoException;
import model.Pokemon;
import model.TimeCheioException;
import model.Treinador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Tela 1: o jogador busca 3 Pokémon pelo nome. Versão básica: busca na PokéAPI e mostra na lista.
// "implements ActionListener": a própria tela trata os cliques dos botões, no actionPerformed.
public class TelaMontarTime extends JPanel implements ActionListener {

    private final JanelaPrincipal janela;
    private FontePokemon fonteApi = new FontePokeApi();
    private Treinador jogador = new Treinador("Você");

    private final JTextField campoBusca = new JTextField(15);
    private final JLabel sprite = new JLabel("", SwingConstants.CENTER);
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private final JList<String> listaTime = new JList<>(modeloLista);
    private final JButton botaoBuscar = new JButton("Buscar");
    private final JButton botaoIniciar = new JButton("Iniciar batalha");

    public TelaMontarTime(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new BorderLayout());

        JPanel topo = new JPanel();
        topo.add(new JLabel("Pokémon:"));
        topo.add(campoBusca);
        topo.add(botaoBuscar);
        add(topo, BorderLayout.NORTH);

        JPanel meio = new JPanel(new GridLayout(1, 2));
        meio.add(sprite);
        meio.add(new JScrollPane(listaTime));
        add(meio, BorderLayout.CENTER);

        JPanel baixo = new JPanel();
        baixo.add(botaoIniciar);
        add(baixo, BorderLayout.SOUTH);

        botaoIniciar.setEnabled(false);             // só liga quando tiver 3 Pokémon
        botaoBuscar.addActionListener(this);
        campoBusca.addActionListener(this);
        botaoIniciar.addActionListener(this);
    }

    // Roda quando alguém clica num botão (ou aperta Enter no campo). getSource() diz quem foi.
    @Override
    public void actionPerformed(ActionEvent e) {
        Object origem = e.getSource();
        if (origem == botaoBuscar || origem == campoBusca) {
            buscar();
        } else if (origem == botaoIniciar) {
            janela.mostrar("batalha");              // por enquanto só troca de tela
        }
    }

    // Lê o nome digitado, busca na API e coloca no time.
    private void buscar() {
        String nome = campoBusca.getText().trim();
        if (nome.isEmpty()) {
            aviso("Digite o nome de um Pokémon.");
            return;
        }
        try {
            Pokemon pokemon = fonteApi.buscar(nome);
            jogador.getTime().adicionar(pokemon);   // pode lançar TimeCheioException
            modeloLista.addElement(pokemon.getNome() + " " + pokemon.getTipos());
            sprite.setIcon(Imagens.sprite(pokemon.getSpriteUrl(), 160));
            campoBusca.setText("");
            botaoIniciar.setEnabled(jogador.getTime().getPokemons().size() == 3);
        } catch (PokemonNaoEncontradoException ex) {
            aviso(ex.getMessage());
        } catch (ApiIndisponivelException ex) {
            aviso("Sem conexão com a PokéAPI.");
        } catch (TimeCheioException ex) {
            aviso(ex.getMessage());
        }
    }

    private void aviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }
}
