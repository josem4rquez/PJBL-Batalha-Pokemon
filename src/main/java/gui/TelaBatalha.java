package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Tela 2: a batalha. Versão básica: só o desenho da tela (a lógica vem nos próximos commits).
public class TelaBatalha extends JPanel implements ActionListener {

    private final JanelaPrincipal janela;

    private final PainelPokemon painelJogador = new PainelPokemon("Você");
    private final PainelPokemon painelCpu = new PainelPokemon("CPU");
    private final JButton[] botoesAtaque = new JButton[4];
    private final JButton botaoNova = new JButton("Nova partida");
    private final JTextArea log = new JTextArea(7, 40);

    public TelaBatalha(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new BorderLayout(0, 8));

        // meio: os dois Pokémon lado a lado
        JPanel arena = new JPanel(new GridLayout(1, 2, 10, 0));
        arena.add(painelJogador);
        arena.add(painelCpu);
        add(arena, BorderLayout.CENTER);

        // baixo: 4 botões de ataque, log e botão de nova partida
        JPanel painelBotoes = new JPanel(new GridLayout(1, 4, 5, 0));
        for (int i = 0; i < 4; i++) {
            botoesAtaque[i] = new JButton("-");
            botoesAtaque[i].setEnabled(false);      // ainda não fazem nada
            painelBotoes.add(botoesAtaque[i]);
        }

        log.setEditable(false);
        botaoNova.addActionListener(this);

        JPanel baixo = new JPanel(new BorderLayout(0, 5));
        baixo.add(painelBotoes, BorderLayout.NORTH);
        baixo.add(new JScrollPane(log), BorderLayout.CENTER);
        baixo.add(botaoNova, BorderLayout.SOUTH);
        add(baixo, BorderLayout.SOUTH);
    }

    // Roda quando alguém clica num botão.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botaoNova) {
            janela.mostrar("montar");
        }
    }
}
