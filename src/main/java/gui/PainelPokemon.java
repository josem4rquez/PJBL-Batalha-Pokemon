package gui;

import model.Pokemon;

import javax.swing.*;
import java.awt.*;

// Um dos lados da batalha. Versão básica: só nome e sprite (a barra de HP vem depois).
// "extends JPanel": herança.
public class PainelPokemon extends JPanel {

    private final JLabel nome = new JLabel("", SwingConstants.CENTER);
    private final JLabel sprite = new JLabel("", SwingConstants.CENTER);

    public PainelPokemon(String titulo) {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder(titulo));
        add(nome, BorderLayout.NORTH);
        add(sprite, BorderLayout.CENTER);
    }

    public void mostrar(Pokemon p) {
        nome.setText(p.getNome() + "  " + p.getTipos());
        sprite.setIcon(Imagens.sprite(p.getSpriteUrl(), 192));
    }
}
