package gui;

import javax.swing.*;
import java.awt.*;

// A janela do programa. "extends JFrame" = ela É uma janela (herança).
// Guarda uma "pilha de cartas" (CardLayout): cada tela é uma carta, e só uma aparece por vez.
public class JanelaPrincipal extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel conteudo = new JPanel(cards);

    private final TelaMontarTime telaMontar = new TelaMontarTime(this);
    private final TelaBatalha telaBatalha = new TelaBatalha(this);

    public JanelaPrincipal() {
        super("Batalha Pokémon");
        conteudo.add(telaMontar, "montar");         // cada tela entra com uma etiqueta
        conteudo.add(telaBatalha, "batalha");
        setContentPane(conteudo);
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // as telas chamam este método para trocar de carta
    public void mostrar(String nome) {
        cards.show(conteudo, nome);
    }

    public static void main(String[] args) {
        JanelaPrincipal janela = new JanelaPrincipal();
        janela.setVisible(true);
    }
}
