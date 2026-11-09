package view;

import controller.FinanceiroController;
import model.Movimentacao;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TelaConsulta extends JFrame {
    private final FinanceiroController controller;
    private final Runnable aoAlterar;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Nome", "Valor", "Data e hora", "Tipo", "Categoria"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);
    private final JComboBox<String> filtroTipo = new JComboBox<>(new String[]{"Todos", "Receita", "Despesa"});
    private final JTextField pesquisa = new JTextField(14);
    private final JComboBox<String> filtroCategoria = new JComboBox<>();
    private final ArrayList<Integer> indicesExibidos = new ArrayList<>();
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public TelaConsulta(FinanceiroController controller) { this(controller, null); }

    public TelaConsulta(FinanceiroController controller, Runnable aoAlterar) {
        this.controller = controller;
        this.aoAlterar = aoAlterar;
        setTitle("Movimentações");
        setSize(1000, 650);
        setMinimumSize(new Dimension(800, 520));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        montar();
        atualizarCategorias();
        carregarTabela();
    }

    private void montar() {
        JPanel raiz = new JPanel(new BorderLayout(0, 16));
        raiz.setBackground(Estilo.FUNDO);
        raiz.setBorder(new EmptyBorder(24, 26, 24, 26));

        JLabel titulo = new JLabel("Movimentações");
        titulo.setFont(Estilo.fonte(26, true));
        titulo.setForeground(Estilo.TEXTO);
        JLabel sub = new JLabel("Pesquise, filtre, edite ou exclua seus lançamentos.");
        sub.setFont(Estilo.fonte(12, false));
        sub.setForeground(Estilo.SECUNDARIO);
        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setBackground(Estilo.FUNDO);
        topo.add(titulo); topo.add(Box.createVerticalStrut(6)); topo.add(sub);

        Estilo.combo(filtroTipo);
        Estilo.combo(filtroCategoria);
        Estilo.campo(pesquisa);
        JButton buscar = Estilo.botao("Filtrar", true);
        JButton limpar = Estilo.botao("Limpar", false);
        buscar.addActionListener(e -> carregarTabela());
        limpar.addActionListener(e -> {
            filtroTipo.setSelectedItem("Todos");
            pesquisa.setText("");
            filtroCategoria.setSelectedItem("Todas");
            carregarTabela();
        });

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        filtros.setBackground(Estilo.PAINEL);
        filtros.add(new JLabel("Tipo:")); filtros.add(filtroTipo);
        filtros.add(new JLabel("Categoria:")); filtros.add(filtroCategoria);
        filtros.add(new JLabel("Nome:")); filtros.add(pesquisa);
        filtros.add(buscar); filtros.add(limpar);
        for (Component c : filtros.getComponents()) {
            if (c instanceof JLabel) ((JLabel)c).setForeground(Estilo.SECUNDARIO);
        }

        tabela.setFont(Estilo.fonte(13, false));
        tabela.setRowHeight(32);
        tabela.setBackground(Estilo.PAINEL);
        tabela.setForeground(Estilo.TEXTO);
        tabela.setGridColor(Estilo.BORDA);
        tabela.setSelectionBackground(new Color(35, 78, 50));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.getTableHeader().setFont(Estilo.fonte(12, true));
        tabela.getTableHeader().setBackground(Estilo.PAINEL_CLARO);
        tabela.getTableHeader().setForeground(Estilo.TEXTO);
        tabela.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.getViewport().setBackground(Estilo.PAINEL);
        scroll.setBorder(BorderFactory.createLineBorder(Estilo.BORDA));

        JButton editar = Estilo.botao("Editar selecionada", true);
        JButton excluir = Estilo.botao("Excluir selecionada", false);
        JButton fechar = Estilo.botao("Fechar", false);
        editar.addActionListener(e -> editarSelecionada());
        excluir.addActionListener(e -> excluirSelecionada());
        fechar.addActionListener(e -> dispose());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.setBackground(Estilo.FUNDO);
        botoes.add(fechar); botoes.add(excluir); botoes.add(editar);

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.setBackground(Estilo.FUNDO);
        centro.add(filtros, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        raiz.add(topo, BorderLayout.NORTH);
        raiz.add(centro, BorderLayout.CENTER);
        raiz.add(botoes, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    private void atualizarCategorias() {
        String selecionada = (String) filtroCategoria.getSelectedItem();
        filtroCategoria.removeAllItems();
        filtroCategoria.addItem("Todas");
        for (Movimentacao m : controller.listarMovimentacoes()) {
            String cat = m.getCategoria().isBlank() ? "Sem categoria" : m.getCategoria();
            boolean existe = false;
            for (int i = 0; i < filtroCategoria.getItemCount(); i++) {
                if (filtroCategoria.getItemAt(i).equals(cat)) existe = true;
            }
            if (!existe) filtroCategoria.addItem(cat);
        }
        if (selecionada != null) filtroCategoria.setSelectedItem(selecionada);
        if (filtroCategoria.getSelectedItem() == null) filtroCategoria.setSelectedItem("Todas");
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        indicesExibidos.clear();
        String tipo = (String) filtroTipo.getSelectedItem();
        String categoria = (String) filtroCategoria.getSelectedItem();
        ArrayList<Movimentacao> lista = controller.filtrar(tipo, categoria, pesquisa.getText());
        ArrayList<Movimentacao> todas = controller.listarMovimentacoes();
        for (Movimentacao m : lista) {
            int indice = -1;
            for (int i = 0; i < todas.size(); i++) {
                if (todas.get(i) == m) { indice = i; break; }
                // As listas são cópias, então usa os campos para localizar a ocorrência correspondente.
                if (indice == -1 && mesmasMovimentacoes(todas.get(i), m)) indice = i;
            }
            if (indice >= 0) {
                // Evita selecionar repetidamente a mesma linha quando há lançamentos iguais.
                while (indicesExibidos.contains(indice)) {
                    int proximo = -1;
                    for (int i = indice + 1; i < todas.size(); i++) {
                        if (mesmasMovimentacoes(todas.get(i), m) && !indicesExibidos.contains(i)) { proximo = i; break; }
                    }
                    if (proximo < 0) break;
                    indice = proximo;
                }
                indicesExibidos.add(indice);
            } else {
                indicesExibidos.add(-1);
            }
            modelo.addRow(new Object[]{
                    m.getDescricao(), moeda.format(m.getValor()),
                    m.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    m.getTipo(), m.getCategoria().isBlank() ? "—" : m.getCategoria()
            });
        }
        atualizarCategoriasSemRecarregar();
    }

    private void atualizarCategoriasSemRecarregar() {
        // A lista de categorias é atualizada após cadastro/edição/exclusão.
    }

    private boolean mesmasMovimentacoes(Movimentacao a, Movimentacao b) {
        return a.getDescricao().equals(b.getDescricao())
                && a.getValor() == b.getValor()
                && a.getDataHora().equals(b.getDataHora())
                && a.getTipo().equals(b.getTipo())
                && a.getCategoria().equals(b.getCategoria());
    }

    private void editarSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(this, "Selecione uma movimentação."); return; }
        int indice = indicesExibidos.get(linha);
        if (indice < 0) { JOptionPane.showMessageDialog(this, "Não foi possível identificar o lançamento."); return; }
        new TelaCadastro(controller, indice, controller.obterMovimentacao(indice), () -> {
            atualizarCategorias();
            carregarTabela();
            if (aoAlterar != null) aoAlterar.run();
        }).setVisible(true);
    }

    private void excluirSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(this, "Selecione uma movimentação."); return; }
        int indice = indicesExibidos.get(linha);
        if (indice < 0) return;
        int opcao = JOptionPane.showConfirmDialog(this, "Deseja excluir esta movimentação?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            controller.excluirMovimentacao(indice);
            atualizarCategorias();
            carregarTabela();
            if (aoAlterar != null) aoAlterar.run();
        }
    }
}