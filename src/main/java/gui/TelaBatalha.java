package gui;

import model.Ataque;
import model.Batalha;
import model.Pokemon;
import model.Time;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// Tela 2: a batalha. Esta classe só MOSTRA e repassa os cliques;
// as regras (dano, ordem, troca) ficam todas na classe Batalha do model.
// "implements ActionListener": a própria tela trata os cliques dos botões, no actionPerformed.
public class TelaBatalha extends JPanel implements ActionListener {

    private final JanelaPrincipal janela;
    private Batalha batalha;                       // criada na hora de iniciar (ver iniciar())

    private final PainelPokemon painelJogador = new PainelPokemon("Você");
    private final PainelPokemon painelCpu = new PainelPokemon("CPU");
    private final JButton[] botoesAtaque = new JButton[4];
    private final JButton botaoNova = new JButton("Nova partida");
    private final JTextArea log = new JTextArea(7, 40);

    public TelaBatalha(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new BorderLayout(0, 8));

        // ---------- meio: os dois Pokémon lado a lado ----------
        JPanel arena = new JPanel(new GridLayout(1, 2, 10, 0));
        arena.add(painelJogador);
        arena.add(painelCpu);
        add(arena, BorderLayout.CENTER);

        // ---------- baixo: 4 botões de ataque, log e botão de nova partida ----------
        JPanel painelBotoes = new JPanel(new GridLayout(1, 4, 5, 0));
        for (int i = 0; i < 4; i++) {
            botoesAtaque[i] = new JButton("-");
            botoesAtaque[i].addActionListener(this);
            painelBotoes.add(botoesAtaque[i]);
        }

        log.setEditable(false);
        log.setLineWrap(true);
        log.setWrapStyleWord(true);

        botaoNova.addActionListener(this);

        JPanel baixo = new JPanel(new BorderLayout(0, 5));
        baixo.add(painelBotoes, BorderLayout.NORTH);
        baixo.add(new JScrollPane(log), BorderLayout.CENTER);
        baixo.add(botaoNova, BorderLayout.SOUTH);
        add(baixo, BorderLayout.SOUTH);
    }

    // Roda quando alguém clica num botão. getSource() diz qual botão foi.
    @Override
    public void actionPerformed(ActionEvent e) {
        Object origem = e.getSource();
        if (origem == botaoNova) {
            janela.novaPartida();
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (origem == botoesAtaque[i]) {
                atacar(i);                          // o botão número i
                return;
            }
        }
    }

    // A janela chama isto quando o jogador clica em "Iniciar batalha".
    public void iniciar(Batalha batalha) {
        this.batalha = batalha;
        log.setText("A batalha começou!\n");
        atualizar();
    }

    // Chamado quando clicam no botão de ataque de número "indice".
    private void atacar(int indice) {
        if (batalha == null || batalha.terminou()) {
            return;
        }
        Pokemon meu = batalha.getJogador().getTime().getAtivo();
        if (indice >= meu.getAtaques().size()) {
            return;
        }
        Ataque escolhido = meu.getAtaques().get(indice);

        // o model faz tudo (CPU escolhe, ordem por velocidade, dano, troca)
        // e devolve só as linhas de texto desse turno
        for (String linha : batalha.executarTurno(escolhido)) {
            log.append(linha + "\n");
        }
        log.setCaretPosition(log.getDocument().getLength());   // rola o log até o fim

        atualizar();
        if (batalha.terminou()) {
            fimDeJogo();
        }
    }

    // Redesenha tudo a partir do estado atual da Batalha.
    private void atualizar() {
        Time timeJogador = batalha.getJogador().getTime();
        Time timeCpu = batalha.getCpu().getTime();

        painelJogador.mostrar(pokemonParaMostrar(timeJogador), emPe(timeJogador), timeJogador.getPokemons().size());
        painelCpu.mostrar(pokemonParaMostrar(timeCpu), emPe(timeCpu), timeCpu.getPokemons().size());

        // os 4 botões mostram os ataques do Pokémon ATIVO (ele muda quando um desmaia)
        Pokemon meu = timeJogador.getAtivo();
        for (int i = 0; i < 4; i++) {
            boolean temAtaque = !batalha.terminou() && meu != null && i < meu.getAtaques().size();
            if (temAtaque) {
                Ataque a = meu.getAtaques().get(i);
                botoesAtaque[i].setText("<html><center>" + a.getNome() + "<br>" + a.getTipo()
                        + " · poder " + a.getPoder() + "</center></html>");
            } else {
                botoesAtaque[i].setText("-");
            }
            botoesAtaque[i].setEnabled(temAtaque);
        }
    }

    // getAtivo() devolve null quando todos desmaiaram; aí mostramos o último, com HP 0.
    private Pokemon pokemonParaMostrar(Time time) {
        Pokemon ativo = time.getAtivo();
        if (ativo != null) {
            return ativo;
        }
        List<Pokemon> todos = time.getPokemons();
        return todos.get(todos.size() - 1);
    }

    private int emPe(Time time) {
        int quantos = 0;
        for (Pokemon p : time.getPokemons()) {
            if (!p.estaDesmaiado()) {
                quantos++;
            }
        }
        return quantos;
    }

    // Fim da batalha: mostra quem venceu.
    private void fimDeJogo() {
        String mensagem = "Vencedor: " + batalha.vencedor().getNome();
        log.append(mensagem + "\n");
        JOptionPane.showMessageDialog(this, mensagem, "Fim de jogo", JOptionPane.INFORMATION_MESSAGE);
    }
}
