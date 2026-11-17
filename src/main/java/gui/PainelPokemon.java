package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import model.Pokemon;

// Um dos lados da batalha: nome, sprite e barra de HP de UM Pokémon.
// "extends JPanel": herança. A TelaBatalha usa dois objetos desta classe (jogador e CPU).
public class PainelPokemon extends JPanel {

    private final JLabel nome = new JLabel("", SwingConstants.CENTER);
    private final JLabel sprite = new JLabel("", SwingConstants.CENTER);
    private final JProgressBar barraHp = new JProgressBar();
    private final JLabel restantes = new JLabel("", SwingConstants.CENTER);

    public PainelPokemon(String titulo) {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder(titulo));

        nome.setFont(nome.getFont().deriveFont(Font.BOLD, 16f));
        barraHp.setStringPainted(true);             // escreve "x / y" dentro da barra

        JPanel baixo = new JPanel(new GridLayout(2, 1, 0, 4));
        baixo.add(barraHp);
        baixo.add(restantes);

        add(nome, BorderLayout.NORTH);
        add(sprite, BorderLayout.CENTER);
        add(baixo, BorderLayout.SOUTH);
    }

    // Atualiza o painel para mostrar este Pokémon (só LÊ os dados, nunca altera o HP).
    public void mostrar(Pokemon p, int emPe, int total) {
        nome.setText(p.getNome() + "  " + p.getTipos());

        ImageIcon icone = Imagens.sprite(p.getSpriteUrl(), 192);
        sprite.setIcon(icone);
        sprite.setText(icone == null ? "(sem imagem)" : "");

        int hpMaximo = p.getStatus().getHp();
        int hpAtual = p.getHpAtual();
        barraHp.setMaximum(hpMaximo);
        barraHp.setValue(hpAtual);
        barraHp.setString(hpAtual + " / " + hpMaximo);

        double fracao = (double) hpAtual / hpMaximo;  // verde, amarelo ou vermelho
        if (fracao > 0.5) {
            barraHp.setForeground(new Color(60, 170, 70));
        } else if (fracao > 0.2) {
            barraHp.setForeground(new Color(230, 170, 30));
        } else {
            barraHp.setForeground(new Color(210, 60, 50));
        }

        restantes.setText("Pokémon em pé: " + emPe + " de " + total);
    }
}
