
package view;

import controller.FinanceiroController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GraficoFinanceiro extends JFrame {

    private final FinanceiroController controller;

    private static final NumberFormat MOEDA =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private static final Color[] CORES = {
        new Color(0, 230, 118),
        new Color(41, 182, 246),
        new Color(255, 183, 77),
        new Color(206, 147, 216),
        new Color(255, 82, 82),
        new Color(255, 238, 88),
        new Color(77, 208, 225),
        new Color(244, 143, 177),
        new Color(129, 199, 132),
        new Color(121, 134, 203),
        new Color(255, 138, 101),
        new Color(174, 213, 129)
    };

    public GraficoFinanceiro(FinanceiroController controller) {
        this.controller = controller;

        setTitle("Gráficos financeiros");
        setSize(1100, 760);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        montar();
    }

    private String dinheiro(double valor) {
        return MOEDA.format(valor);
    }

    private String porcentagem(double valor, double total) {
        if (total <= 0) {
            return "0,0%";
        }

        return String.format(
                new Locale("pt", "BR"),
                "%.1f%%",
                valor * 100.0 / total
        );
    }

    private void centralizarTexto(
            Graphics2D g, String texto, int centroX, int y) {

        FontMetrics fm = g.getFontMetrics();
        g.drawString(texto, centroX - fm.stringWidth(texto) / 2, y);
    }

    private void montar() {
        JPanel raiz = new JPanel(new BorderLayout(0, 16));
        raiz.setBackground(Estilo.FUNDO);
        raiz.setBorder(new EmptyBorder(24, 26, 24, 26));

        JLabel titulo = new JLabel("Análise visual");
        titulo.setFont(Estilo.fonte(26, true));
        titulo.setForeground(Estilo.TEXTO);

        JLabel subtitulo = new JLabel(
                "Valores em reais, percentuais e evolução financeira."
        );
        subtitulo.setFont(Estilo.fonte(12, false));
        subtitulo.setForeground(Estilo.SECUNDARIO);

        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(Estilo.FUNDO);
        cabecalho.setLayout(
                new BoxLayout(cabecalho, BoxLayout.Y_AXIS)
        );

        cabecalho.add(titulo);
        cabecalho.add(Box.createVerticalStrut(6));
        cabecalho.add(subtitulo);

        JPanel grade = new JPanel(new GridLayout(2, 2, 14, 14));
        grade.setBackground(Estilo.FUNDO);

        grade.add(new PainelBarras());
        grade.add(new PainelPizza());
        grade.add(new PainelLinhas());

        JPanel explicacao = new JPanel(new BorderLayout());
        explicacao.setBackground(Estilo.PAINEL);
        explicacao.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel expTitulo = new JLabel("Como ler os gráficos");
        expTitulo.setFont(Estilo.fonte(14, true));
        expTitulo.setForeground(Estilo.VERDE);

        JLabel exp = new JLabel(
            "<html><div style='width:300px;color:#99AA9F;line-height:1.5'>"
            + "<b>Barras:</b> receitas e despesas em reais e percentual.<br><br>"
            + "<b>Pizza:</b> cada nome de despesa e sua participação "
            + "no total de gastos.<br><br>"
            + "<b>Linhas:</b> evolução mensal das receitas e despesas."
            + "</div></html>"
        );

        explicacao.add(expTitulo, BorderLayout.NORTH);
        explicacao.add(exp, BorderLayout.CENTER);

        JButton fechar = Estilo.botao("Fechar", true);
        fechar.addActionListener(e -> dispose());

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(Estilo.FUNDO);
        rodape.add(fechar);

        raiz.add(cabecalho, BorderLayout.NORTH);
        raiz.add(grade, BorderLayout.CENTER);
        raiz.add(rodape, BorderLayout.SOUTH);

        setContentPane(raiz);
    }

    private abstract class PainelGrafico extends JPanel {

        PainelGrafico() {
            setBackground(Estilo.PAINEL);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Estilo.BORDA),
                    new EmptyBorder(15, 15, 15, 15)
            ));
        }

        void titulo(Graphics2D g, String texto) {
            g.setColor(Estilo.TEXTO);
            g.setFont(Estilo.fonte(14, true));
            g.drawString(texto, 16, 24);
        }

        void linha(Graphics2D g, int x1, int y1, int x2, int y2) {
            g.setColor(Estilo.BORDA);
            g.drawLine(x1, y1, x2, y2);
        }

        void mensagem(Graphics2D g, String texto) {
            g.setColor(Estilo.SECUNDARIO);
            g.setFont(Estilo.fonte(12, false));
            g.drawString(texto, 20, getHeight() / 2);
        }
    }

    // =====================================================
    // GRÁFICO DE BARRAS
    // =====================================================

    private class PainelBarras extends PainelGrafico {

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            titulo(g, "Receitas x despesas");

            double receitas = controller.getTotalReceitas();
            double despesas = controller.getTotalDespesas();
            double total = receitas + despesas;

            int base = getHeight() - 48;
            int alturaMaxima = Math.max(30, getHeight() - 145);
            double maior = Math.max(receitas, despesas);

            if (maior <= 0) {
                maior = 1;
            }

            int largura = Math.min(75, getWidth() / 5);
            int centroR = getWidth() / 3;
            int centroD = getWidth() * 2 / 3;

            int alturaR = (int) (alturaMaxima * receitas / maior);
            int alturaD = (int) (alturaMaxima * despesas / maior);

            linha(g, 25, base, getWidth() - 20, base);

            g.setColor(Estilo.VERDE);
            g.fillRoundRect(
                    centroR - largura / 2,
                    base - alturaR,
                    largura, Math.max(alturaR, 2), 12, 12
            );

            g.setColor(Estilo.VERMELHO);
            g.fillRoundRect(
                    centroD - largura / 2,
                    base - alturaD,
                    largura, Math.max(alturaD, 2), 12, 12
            );

            g.setFont(Estilo.fonte(10, true));
            g.setColor(Estilo.TEXTO);

            centralizarTexto(g, dinheiro(receitas), centroR,
                    Math.max(53, base - alturaR - 30));
            centralizarTexto(g, porcentagem(receitas, total), centroR,
                    Math.max(67, base - alturaR - 15));

            centralizarTexto(g, dinheiro(despesas), centroD,
                    Math.max(53, base - alturaD - 30));
            centralizarTexto(g, porcentagem(despesas, total), centroD,
                    Math.max(67, base - alturaD - 15));

            g.setFont(Estilo.fonte(11, true));
            g.setColor(Estilo.VERDE);
            centralizarTexto(g, "Receitas", centroR, base + 18);

            g.setColor(Estilo.VERMELHO);
            centralizarTexto(g, "Despesas", centroD, base + 18);

            g.dispose();
        }
    }

    // =====================================================
    // GRÁFICO DE PIZZA: DESPESAS AGRUPADAS PELO NOME
    // =====================================================

    private class PainelPizza extends PainelGrafico {

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            titulo(g, "Despesas por nome");

            // Usa o nome da movimentação, não a categoria.
            Map<String, Double> mapa =
                    controller.getDespesasPorNome();

            List<Map.Entry<String, Double>> despesas =
                    new ArrayList<>(mapa.entrySet());

            despesas.sort(
                    Map.Entry.<String, Double>comparingByValue(
                            Comparator.reverseOrder()
                    )
            );

            double total = despesas.stream()
                    .mapToDouble(Map.Entry::getValue)
                    .sum();

            if (total <= 0) {
                mensagem(g, "Cadastre uma despesa para ver o gráfico.");
                g.dispose();
                return;
            }

            int diametro = Math.max(
                    80,
                    Math.min(getHeight() - 85, getWidth() / 2 - 10)
            );

            int xPizza = 18;
            int yPizza = 42;

            int inicio = 0;
            int angulosDesenhados = 0;

            // Cada nome tem sua própria fatia e cor.
            for (int i = 0; i < despesas.size(); i++) {
                Map.Entry<String, Double> item = despesas.get(i);

                int arco;

                if (i == despesas.size() - 1) {
                    // Evita diferenças de arredondamento no círculo.
                    arco = 360 - angulosDesenhados;
                } else {
                    arco = (int) Math.round(
                            360.0 * item.getValue() / total
                    );

                    arco = Math.max(
                            0,
                            Math.min(arco, 360 - angulosDesenhados)
                    );
                }

                g.setColor(CORES[i % CORES.length]);
                g.fillArc(
                        xPizza, yPizza,
                        diametro, diametro,
                        inicio, arco
                );

                inicio += arco;
                angulosDesenhados += arco;
            }

            // Exibe até quatro nomes na legenda.
            int limite = 4;
            int exibidos = Math.min(limite, despesas.size());

            double valorOutros = 0;
            for (int i = exibidos; i < despesas.size(); i++) {
                valorOutros += despesas.get(i).getValue();
            }

            int legendaX = xPizza + diametro + 20;
            int legendaY = 57;
            int larguraDisponivel = getWidth() - legendaX - 18;

            for (int i = 0; i < exibidos; i++) {
                Map.Entry<String, Double> item = despesas.get(i);

                g.setColor(CORES[i % CORES.length]);
                g.fillRoundRect(
                        legendaX, legendaY - 10,
                        10, 10, 3, 3
                );

                g.setColor(Estilo.TEXTO);
                g.setFont(Estilo.fonte(10, true));

                String nome = item.getKey();
                int maxCaracteres = Math.max(
                        8, Math.min(20, larguraDisponivel / 7)
                );

                if (nome.length() > maxCaracteres) {
                    nome = nome.substring(0, maxCaracteres - 1) + "...";
                }

                g.drawString(nome, legendaX + 16, legendaY);

                g.setColor(Estilo.SECUNDARIO);
                g.setFont(Estilo.fonte(9, false));

                String detalhe = dinheiro(item.getValue())
                        + " | " + porcentagem(item.getValue(), total);

                g.drawString(detalhe, legendaX + 16, legendaY + 14);

                legendaY += 37;
            }

            if (despesas.size() > exibidos) {
                g.setColor(CORES[exibidos % CORES.length]);
                g.fillRoundRect(
                        legendaX, legendaY - 10,
                        10, 10, 3, 3
                );

                g.setColor(Estilo.TEXTO);
                g.setFont(Estilo.fonte(10, true));
                g.drawString("Outros gastos", legendaX + 16, legendaY);

                g.setColor(Estilo.SECUNDARIO);
                g.setFont(Estilo.fonte(9, false));
                g.drawString(
                        dinheiro(valorOutros)
                                + " | " + porcentagem(valorOutros, total),
                        legendaX + 16,
                        legendaY + 14
                );
            }

            g.dispose();
        }
    }

    // =====================================================
    // GRÁFICO DE LINHAS: EVOLUÇÃO MENSAL
    // =====================================================

    private class PainelLinhas extends PainelGrafico {

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            titulo(g, "Evolução mensal");

            LinkedHashMap<String, double[]> dados =
                    controller.getResumoMensal(6);

            if (dados == null || dados.isEmpty()) {
                mensagem(g, "Ainda não há dados mensais.");
                g.dispose();
                return;
            }

            int left = 42;
            int right = getWidth() - 22;
            int top = 65;
            int bottom = getHeight() - 58;

            linha(g, left, bottom, right, bottom);
            linha(g, left, top, left, bottom);

            double max = 1;
            double totalPeriodo = 0;

            for (double[] valores : dados.values()) {
                max = Math.max(max, Math.max(valores[0], valores[1]));
                totalPeriodo += valores[0] + valores[1];
            }

            int n = dados.size();
            int indice = 0;

            int anteriorXR = -1;
            int anteriorYR = -1;
            int anteriorXD = -1;
            int anteriorYD = -1;

            int areaAltura = bottom - top;
            int areaLargura = right - left;

            for (Map.Entry<String, double[]> entrada : dados.entrySet()) {
                double receita = entrada.getValue()[0];
                double despesa = entrada.getValue()[1];

                int x = n == 1
                        ? left + areaLargura / 2
                        : left + areaLargura * indice / (n - 1);

                int yReceita = bottom
                        - (int) (areaAltura * receita / max);

                int yDespesa = bottom
                        - (int) (areaAltura * despesa / max);

                g.setStroke(new BasicStroke(2.2f));

                g.setColor(Estilo.VERDE);
                if (anteriorXR >= 0) {
                    g.drawLine(anteriorXR, anteriorYR, x, yReceita);
                }
                g.fillOval(x - 4, yReceita - 4, 8, 8);

                g.setColor(Estilo.VERMELHO);
                if (anteriorXD >= 0) {
                    g.drawLine(anteriorXD, anteriorYD, x, yDespesa);
                }
                g.fillOval(x - 4, yDespesa - 4, 8, 8);

                // Mantém os valores e percentuais próximos dos pontos.
                g.setFont(Estilo.fonte(9, true));

                String rotuloR = dinheiro(receita)
                        + " (" + porcentagem(receita, totalPeriodo) + ")";

                String rotuloD = dinheiro(despesa)
                        + " (" + porcentagem(despesa, totalPeriodo) + ")";

                g.setColor(Estilo.VERDE);
                centralizarTexto(
                        g, rotuloR, x,
                        Math.max(45, yReceita - 10)
                );

                g.setColor(Estilo.VERMELHO);
                centralizarTexto(
                        g, rotuloD, x,
                        Math.min(bottom - 3, yDespesa + 20)
                );

                // Chave no formato yyyy-MM: mostra MM/yy.
                String mes = entrada.getKey();
                if (mes.length() >= 7) {
                    mes = mes.substring(5, 7)
                            + "/" + mes.substring(2, 4);
                }

                g.setColor(Estilo.SECUNDARIO);
                g.setFont(Estilo.fonte(9, false));
                centralizarTexto(g, mes, x, bottom + 16);

                anteriorXR = x;
                anteriorYR = yReceita;
                anteriorXD = x;
                anteriorYD = yDespesa;

                indice++;
            }

            g.setFont(Estilo.fonte(10, true));

            g.setColor(Estilo.VERDE);
            g.drawString("● Receitas", left, getHeight() - 12);

            g.setColor(Estilo.VERMELHO);
            g.drawString("● Despesas", left + 100, getHeight() - 12);

            g.dispose();
        }
    }
}
