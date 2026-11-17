package gui;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Batalha;
import model.EstrategiaAleatoria;
import model.EstrategiaDeAtaque;
import model.EstrategiaMaisForte;
import model.Treinador;

// A janela do programa. "extends JFrame" = ela É uma janela (herança).
// Guarda uma "pilha de cartas" (CardLayout): cada tela é uma carta,
// e só uma carta aparece por vez. Também cria a Batalha e liga as telas ao model.
public class JanelaPrincipal extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel conteudo = new JPanel(cards);   // painel que guarda as cartas

    private final TelaMontarTime telaMontar = new TelaMontarTime(this);
    private final TelaBatalha telaBatalha = new TelaBatalha(this);

    public JanelaPrincipal() {
        super("Batalha Pokémon");

        // cada tela entra com uma etiqueta (o nome da carta)
        conteudo.add(telaMontar, "montar");
        conteudo.add(telaBatalha, "batalha");

        setContentPane(conteudo);
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fechar a janela encerra o programa
        setLocationRelativeTo(null);                    // abre no centro da tela
    }

    // as telas chamam este método para trocar de carta
    public void mostrar(String nome) {
        cards.show(conteudo, nome);
    }

    // Chamado pela TelaMontarTime (botão "Iniciar batalha"): cria a Batalha e mostra a tela.
    public void iniciarBatalha(Treinador jogador, Treinador cpu, boolean dificil) {
        // polimorfismo: a Batalha recebe a estratégia sem saber qual é
        EstrategiaDeAtaque estrategia = dificil ? new EstrategiaMaisForte() : new EstrategiaAleatoria();
        telaBatalha.iniciar(new Batalha(jogador, cpu, estrategia));
        mostrar("batalha");
    }

    // Volta para a primeira tela com tudo zerado.
    public void novaPartida() {
        telaMontar.reiniciar();
        mostrar("montar");
    }

    public static void main(String[] args) {
        JanelaPrincipal janela = new JanelaPrincipal();
        janela.setVisible(true);
    }
}
