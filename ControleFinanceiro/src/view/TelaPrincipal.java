package view;

import controller.FinanceiroController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TelaPrincipal extends JFrame {
    private final FinanceiroController controller;
    private JLabel receitaValor, despesaValor, saldoValor;
    private JLabel relogio;
    private Timer timerRelogio;
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public TelaPrincipal(FinanceiroController controller) {
        this.controller = controller;
        setTitle("Finance — Controle Financeiro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 740);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);
        montar();
        atualizarResumo();
        iniciarRelogio();
    }

    private void montar() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(Estilo.FUNDO);
        raiz.add(menu(), BorderLayout.WEST);

        JPanel conteudo = new JPanel(new BorderLayout(0, 25));
        conteudo.setBackground(Estilo.FUNDO);
        conteudo.setBorder(new EmptyBorder(32, 34, 30, 34));
        conteudo.add(cabecalho(), BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setBackground(Estilo.FUNDO);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JLabel resumo = new JLabel("Seu resumo");
        resumo.setFont(Estilo.fonte(20, true));
        resumo.setForeground(Estilo.TEXTO);
        resumo.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(resumo);
        centro.add(Box.createVerticalStrut(15));

        JPanel cartoes = new JPanel(new GridLayout(1, 3, 16, 0));
        cartoes.setBackground(Estilo.FUNDO);
        cartoes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 148));
        receitaValor = new JLabel("R$ 0,00");
        despesaValor = new JLabel("R$ 0,00");
        saldoValor = new JLabel("R$ 0,00");
        cartoes.add(cartao("RECEITAS", receitaValor, Estilo.VERDE, "Total de entradas"));
        cartoes.add(cartao("DESPESAS", despesaValor, Estilo.VERMELHO, "Total de saídas"));
        cartoes.add(cartao("SALDO ATUAL", saldoValor, Estilo.TEXTO, "Entradas menos saídas"));
        centro.add(cartoes);
        centro.add(Box.createVerticalStrut(30));

        JLabel acoesTitulo = new JLabel("Acesso rápido");
        acoesTitulo.setFont(Estilo.fonte(20, true));
        acoesTitulo.setForeground(Estilo.TEXTO);
        acoesTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(acoesTitulo);
        centro.add(Box.createVerticalStrut(15));

        JPanel acoes = new JPanel(new GridLayout(2, 2, 14, 14));
        acoes.setBackground(Estilo.FUNDO);
        acoes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
        acoes.add(acao("＋  Nova movimentação", "Registre uma receita ou despesa", () -> abrir("cadastro")));
        acoes.add(acao("↗  Movimentações", "Consulte, pesquise, edite e exclua", () -> abrir("consulta")));
        acoes.add(acao("▤  Relatório financeiro", "Veja totais e a categoria com maior despesa", () -> abrir("relatorio")));
        acoes.add(acao("▥  Gráficos", "Compare receitas, despesas e meses", () -> abrir("graficos")));
        centro.add(acoes);

        JPanel nota = new JPanel(new BorderLayout());
        nota.setBackground(Estilo.PAINEL);
        nota.setBorder(new EmptyBorder(18, 20, 18, 20));
        JLabel notaTitulo = new JLabel("Dica financeira");
        notaTitulo.setFont(Estilo.fonte(14, true));
        notaTitulo.setForeground(Estilo.VERDE);
        JLabel notaTexto = new JLabel("Use nomes claros nos lançamentos, como “Gasolina do carro” ou “Conta de internet”.");
        notaTexto.setFont(Estilo.fonte(13, false));
        notaTexto.setForeground(Estilo.SECUNDARIO);
        nota.add(notaTitulo, BorderLayout.NORTH);
        nota.add(notaTexto, BorderLayout.CENTER);
        nota.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
        centro.add(Box.createVerticalStrut(22));
        centro.add(nota);

        conteudo.add(centro, BorderLayout.CENTER);
        raiz.add(conteudo, BorderLayout.CENTER);
        setContentPane(raiz);
    }

    private JPanel menu() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Estilo.PAINEL);
        p.setBorder(new EmptyBorder(28, 18, 24, 18));
        p.setPreferredSize(new Dimension(235, 0));

        JLabel logo = new JLabel("◈ FINANCE");
        logo.setFont(Estilo.fonte(23, true));
        logo.setForeground(Estilo.VERDE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subtitulo = new JLabel("PERSONAL FINANCE");
        subtitulo.setFont(Estilo.fonte(10, true));
        subtitulo.setForeground(Estilo.SECUNDARIO);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(logo);
        p.add(Box.createVerticalStrut(5));
        p.add(subtitulo);
        p.add(Box.createVerticalStrut(42));

        JLabel secao = new JLabel("ESPAÇO FINANCEIRO");
        secao.setFont(Estilo.fonte(10, true));
        secao.setForeground(Estilo.SECUNDARIO);
        secao.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(secao);
        p.add(Box.createVerticalStrut(14));
        p.add(menuBotao("Visão geral", null));
        p.add(Box.createVerticalStrut(8));
        p.add(menuBotao("Nova movimentação", () -> abrir("cadastro")));
        p.add(Box.createVerticalStrut(8));
        p.add(menuBotao("Movimentações", () -> abrir("consulta")));
        p.add(Box.createVerticalStrut(8));
        p.add(menuBotao("Relatórios", () -> abrir("relatorio")));
        p.add(Box.createVerticalStrut(8));
        p.add(menuBotao("Gráficos", () -> abrir("graficos")));
        p.add(Box.createVerticalGlue());

        JLabel rodape = new JLabel("Seu dinheiro. Sua visão.");
        rodape.setFont(Estilo.fonte(11, false));
        rodape.setForeground(Estilo.SECUNDARIO);
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(rodape);
        return p;
    }

    private JButton menuBotao(String texto, Runnable acao) {
        JButton b = new JButton(texto);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        b.setPreferredSize(new Dimension(195, 43));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(Estilo.fonte(13, false));
        b.setForeground(Estilo.TEXTO);
        b.setBackground(Estilo.PAINEL_CLARO);
        b.setBorder(new EmptyBorder(10, 12, 10, 8));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        if (acao != null) b.addActionListener(e -> acao.run());
        return b;
    }

    private JPanel cabecalho() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Estilo.FUNDO);
        JPanel textos = new JPanel();
        textos.setBackground(Estilo.FUNDO);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Dashboard");
        titulo.setFont(Estilo.fonte(30, true));
        titulo.setForeground(Estilo.TEXTO);
        JLabel sub = new JLabel("Uma visão clara de como seu dinheiro está se movimentando.");
        sub.setFont(Estilo.fonte(13, false));
        sub.setForeground(Estilo.SECUNDARIO);
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(7));
        textos.add(sub);
        JPanel ladoDireito = new JPanel();
        ladoDireito.setBackground(Estilo.FUNDO);
        ladoDireito.setLayout(new BoxLayout(ladoDireito, BoxLayout.Y_AXIS));
        relogio = new JLabel();
        relogio.setFont(Estilo.fonte(13, true));
        relogio.setForeground(Estilo.VERDE);
        relogio.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JButton atualizar = Estilo.botao("Atualizar resumo", true);
        atualizar.setAlignmentX(Component.RIGHT_ALIGNMENT);
        atualizar.addActionListener(e -> atualizarResumo());
        ladoDireito.add(relogio);
        ladoDireito.add(Box.createVerticalStrut(12));
        ladoDireito.add(atualizar);
        p.add(textos, BorderLayout.CENTER);
        p.add(ladoDireito, BorderLayout.EAST);
        return p;
    }

    private JPanel cartao(String titulo, JLabel valor, Color cor, String detalhe) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Estilo.PAINEL);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDA),
                new EmptyBorder(18, 18, 18, 18)));
        JLabel t = new JLabel(titulo);
        t.setFont(Estilo.fonte(11, true));
        t.setForeground(Estilo.SECUNDARIO);
        valor.setFont(Estilo.fonte(24, true));
        valor.setForeground(cor);
        JLabel d = new JLabel(detalhe);
        d.setFont(Estilo.fonte(11, false));
        d.setForeground(Estilo.SECUNDARIO);
        p.add(t);
        p.add(Box.createVerticalStrut(13));
        p.add(valor);
        p.add(Box.createVerticalStrut(9));
        p.add(d);
        return p;
    }

    private JPanel acao(String titulo, String descricao, Runnable executar) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(Estilo.PAINEL);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDA),
                new EmptyBorder(16, 18, 16, 18)));
        JLabel t = new JLabel(titulo);
        t.setFont(Estilo.fonte(14, true));
        t.setForeground(Estilo.TEXTO);
        JLabel d = new JLabel(descricao);
        d.setFont(Estilo.fonte(11, false));
        d.setForeground(Estilo.SECUNDARIO);
        p.add(t, BorderLayout.CENTER);
        p.add(d, BorderLayout.SOUTH);
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));
        p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { executar.run(); }
            public void mouseEntered(java.awt.event.MouseEvent e) { p.setBorder(BorderFactory.createLineBorder(Estilo.VERDE)); }
            public void mouseExited(java.awt.event.MouseEvent e) {
                p.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Estilo.BORDA),
                        new EmptyBorder(16, 18, 16, 18)));
            }
        });
        return p;
    }


    private void iniciarRelogio() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss");
        Runnable atualizar = () -> relogio.setText(LocalDateTime.now().format(formato));
        atualizar.run();
        timerRelogio = new Timer(1000, e -> atualizar.run());
        timerRelogio.start();
    }

    private void atualizarResumo() {
        receitaValor.setText(moeda.format(controller.getTotalReceitas()));
        despesaValor.setText(moeda.format(controller.getTotalDespesas()));
        saldoValor.setText(moeda.format(controller.getSaldo()));
    }

    private void abrir(String tela) {
        switch (tela) {
            case "cadastro": new TelaCadastro(controller, -1, null, this::atualizarResumo).setVisible(true); break;
            case "consulta": new TelaConsulta(controller, this::atualizarResumo).setVisible(true); break;
            case "relatorio": new TelaRelatorio(controller).setVisible(true); break;
            case "graficos": new GraficoFinanceiro(controller).setVisible(true); break;
            default: break;
        }
    }
}