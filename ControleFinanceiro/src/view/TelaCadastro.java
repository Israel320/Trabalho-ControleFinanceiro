package view;

import controller.FinanceiroController;
import model.Movimentacao;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TelaCadastro extends JFrame {
    private final FinanceiroController controller;
    private final int indiceEdicao;
    private final Runnable aoSalvar;
    private final JTextField descricao = new JTextField();
    private final JTextField valor = new JTextField();
    private final JTextField data = new JTextField();
    private final JTextField hora = new JTextField();
    private final JTextField categoria = new JTextField();
    private final JComboBox<String> tipo = new JComboBox<>(new String[]{"Despesa", "Receita"});
    private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    private final DateTimeFormatter formatoDataHora = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm");
    private final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    public TelaCadastro(FinanceiroController controller) {
        this(controller, -1, null, null);
    }

    public TelaCadastro(FinanceiroController controller, int indiceEdicao,
                        Movimentacao existente, Runnable aoSalvar) {
        this.controller = controller;
        this.indiceEdicao = indiceEdicao;
        this.aoSalvar = aoSalvar;
        setTitle(indiceEdicao >= 0 ? "Editar movimentação" : "Nova movimentação");
        setSize(560, 650);
        setMinimumSize(new Dimension(500, 610));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        data.setText(LocalDate.now().format(formato));
        hora.setText(LocalTime.now().format(formatoHora));
        montar();
        if (existente != null) preencher(existente);
    }

    private void montar() {
        JPanel raiz = new JPanel(new BorderLayout(0, 18));
        raiz.setBackground(Estilo.FUNDO);
        raiz.setBorder(new EmptyBorder(25, 28, 25, 28));

        JLabel titulo = new JLabel(indiceEdicao >= 0 ? "Editar lançamento" : "Novo lançamento");
        titulo.setFont(Estilo.fonte(25, true));
        titulo.setForeground(Estilo.TEXTO);
        JLabel sub = new JLabel("Informe os dados da movimentação. A categoria é opcional.");
        sub.setFont(Estilo.fonte(12, false));
        sub.setForeground(Estilo.SECUNDARIO);
        JPanel cab = new JPanel();
        cab.setBackground(Estilo.FUNDO);
        cab.setLayout(new BoxLayout(cab, BoxLayout.Y_AXIS));
        cab.add(titulo);
        cab.add(Box.createVerticalStrut(7));
        cab.add(sub);

        JPanel form = new JPanel(new GridLayout(6, 1, 0, 10));
        form.setBackground(Estilo.FUNDO);
        form.add(campo("Nome da movimentação *", descricao, "Ex.: Gasolina do carro"));
        form.add(campo("Valor (R$) *", valor, "Ex.: 150,00"));
        form.add(campo("Data (dd/MM/aaaa) *", data, java.time.LocalDate.now().format(formato)));
        form.add(campo("Hora (HH:mm) *", hora, java.time.LocalTime.now().format(formatoHora)));
        form.add(campo("Tipo *", tipo, null));
        form.add(campo("Categoria (opcional)", categoria, "Ex.: Casa, carro, estudos"));

        JButton salvar = Estilo.botao(indiceEdicao >= 0 ? "Salvar alterações" : "Salvar movimentação", true);
        JButton cancelar = Estilo.botao("Cancelar", false);
        salvar.addActionListener(e -> salvar());
        cancelar.addActionListener(e -> dispose());
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.setBackground(Estilo.FUNDO);
        botoes.add(cancelar);
        botoes.add(salvar);

        raiz.add(cab, BorderLayout.NORTH);
        raiz.add(form, BorderLayout.CENTER);
        raiz.add(botoes, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    private JPanel campo(String rotulo, JComponent componente, String dica) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(Estilo.FUNDO);
        JLabel l = new JLabel(rotulo);
        l.setFont(Estilo.fonte(12, true));
        l.setForeground(Estilo.SECUNDARIO);
        if (componente instanceof JTextField) {
            Estilo.campo((JTextField) componente);
            if (dica != null) ((JTextField) componente).setToolTipText(dica);
        } else if (componente instanceof JComboBox) {
            Estilo.combo((JComboBox<?>) componente);
        }
        p.add(l, BorderLayout.NORTH);
        p.add(componente, BorderLayout.CENTER);
        return p;
    }

    private void preencher(Movimentacao m) {
        descricao.setText(m.getDescricao());
        valor.setText(String.valueOf(m.getValor()).replace(".", ","));
        data.setText(m.getDataHora().format(formato));
        hora.setText(m.getDataHora().format(formatoHora));
        tipo.setSelectedItem(m.getTipo());
        categoria.setText(m.getCategoria());
    }

    private void salvar() {
        try {
            String nome = descricao.getText().trim();
            double quantia = Double.parseDouble(valor.getText().trim().replace(".", "").replace(",", "."));
            LocalDateTime dataHora = LocalDateTime.parse(
                    data.getText().trim() + " " + hora.getText().trim(), formatoDataHora);
            String tipoSelecionado = (String) tipo.getSelectedItem();
            Movimentacao m = new Movimentacao(nome, quantia, dataHora, tipoSelecionado, categoria.getText());

            if (indiceEdicao >= 0) controller.editarMovimentacao(indiceEdicao, m);
            else controller.cadastrarMovimentacao(m);

            if (aoSalvar != null) aoSalvar.run();
            JOptionPane.showMessageDialog(this, "Movimentação salva com sucesso.",
                    "Concluído", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um valor numérico válido, maior que zero.",
                    "Valor inválido", JOptionPane.WARNING_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Informe a data no formato dd/MM/aaaa e a hora no formato HH:mm (24 horas).",
                    "Data ou hora inválida", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Verifique os dados", JOptionPane.WARNING_MESSAGE);
        }
    }
}