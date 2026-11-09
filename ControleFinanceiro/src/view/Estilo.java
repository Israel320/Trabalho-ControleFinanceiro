package view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JComponent;

public final class Estilo {
    private Estilo() {}

    public static final Color FUNDO = new Color(15, 20, 17);
    public static final Color PAINEL = new Color(24, 31, 27);
    public static final Color PAINEL_CLARO = new Color(31, 41, 35);
    public static final Color VERDE = new Color(52, 211, 112);
    public static final Color VERDE_ESCURO = new Color(24, 102, 59);
    public static final Color TEXTO = new Color(238, 245, 239);
    public static final Color SECUNDARIO = new Color(153, 170, 159);
    public static final Color VERMELHO = new Color(251, 113, 133);
    public static final Color BORDA = new Color(49, 62, 53);

    public static Font fonte(int tamanho, boolean negrito) {
        return new Font("Segoe UI", negrito ? Font.BOLD : Font.PLAIN, tamanho);
    }

    public static void campo(JTextField campo) {
        campo.setFont(fonte(14, false));
        campo.setForeground(TEXTO);
        campo.setCaretColor(VERDE);
        campo.setBackground(PAINEL_CLARO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
    }

    public static void combo(JComboBox<?> combo) {
        combo.setFont(fonte(14, false));
        combo.setForeground(TEXTO);
        combo.setBackground(PAINEL_CLARO);
    }

    public static JButton botao(String texto, boolean destaque) {
        JButton b = new JButton(texto);
        b.setFont(fonte(13, true));
        b.setForeground(destaque ? FUNDO : TEXTO);
        b.setBackground(destaque ? VERDE : PAINEL_CLARO);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(11, 16, 11, 16));
        b.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return b;
    }

    public static void preparar(JComponent componente) {
        componente.setBackground(FUNDO);
        componente.setForeground(TEXTO);
    }
}