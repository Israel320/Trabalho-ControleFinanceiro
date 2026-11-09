package view;

import controller.FinanceiroController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class TelaRelatorio extends JFrame {
    private final FinanceiroController controller;
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public TelaRelatorio(FinanceiroController controller) {
        this.controller = controller;
        setTitle("Relatório financeiro");
        setSize(650, 520);
        setMinimumSize(new Dimension(550, 450));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        montar();
    }

    private void montar() {
        JPanel raiz = new JPanel(new BorderLayout(0, 20));
        raiz.setBackground(Estilo.FUNDO);
        raiz.setBorder(new EmptyBorder(28, 30, 28, 30));
        JLabel titulo = new JLabel("Relatório financeiro");
        titulo.setFont(Estilo.fonte(26, true));
        titulo.setForeground(Estilo.TEXTO);
        JLabel subtitulo = new JLabel("Resumo atualizado das movimentações registradas.");
        subtitulo.setFont(Estilo.fonte(12, false));
        subtitulo.setForeground(Estilo.SECUNDARIO);
        JPanel cab = new JPanel();
        cab.setBackground(Estilo.FUNDO);
        cab.setLayout(new BoxLayout(cab, BoxLayout.Y_AXIS));
        cab.add(titulo); cab.add(Box.createVerticalStrut(6)); cab.add(subtitulo);

        JPanel dados = new JPanel(new GridLayout(4, 1, 10, 10));
        dados.setBackground(Estilo.PAINEL);
        dados.setBorder(new EmptyBorder(22, 24, 22, 24));
        dados.add(linha("Total de receitas", moeda.format(controller.getTotalReceitas()), Estilo.VERDE));
        dados.add(linha("Total de despesas", moeda.format(controller.getTotalDespesas()), Estilo.VERMELHO));
        dados.add(linha("Saldo atual", moeda.format(controller.getSaldo()), Estilo.TEXTO));
        String categoria = controller.getCategoriaComMaiorDespesa();
        double totalCategoria = 0;
        if (!categoria.isBlank()) totalCategoria = controller.getDespesasPorCategoria().getOrDefault(categoria, 0.0);
        dados.add(linha("Maior categoria de despesas",
                categoria.isBlank() ? "Nenhuma despesa registrada" : categoria + " — " + moeda.format(totalCategoria),
                Estilo.VERDE));

        JButton fechar = Estilo.botao("Fechar", true);
        fechar.addActionListener(e -> dispose());
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(Estilo.FUNDO);
        rodape.add(fechar);

        raiz.add(cab, BorderLayout.NORTH);
        raiz.add(dados, BorderLayout.CENTER);
        raiz.add(rodape, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    private JPanel linha(String titulo, String valor, Color cor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Estilo.PAINEL);
        JLabel l = new JLabel(titulo);
        l.setFont(Estilo.fonte(13, false));
        l.setForeground(Estilo.SECUNDARIO);
        JLabel v = new JLabel(valor);
        v.setFont(Estilo.fonte(15, true));
        v.setForeground(cor);
        p.add(l, BorderLayout.WEST);
        p.add(v, BorderLayout.EAST);
        return p;
    }
}