
package controller;

import model.Financeiro;
import model.Movimentacao;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

public class FinanceiroController {

    private final Financeiro financeiro;

    public FinanceiroController(Financeiro financeiro) {
        this.financeiro = financeiro;
    }

    // Cadastro de movimentações
    public void cadastrarMovimentacao(Movimentacao m) {
        financeiro.adicionar(m);
    }

    // Consulta de movimentações
    public ArrayList<Movimentacao> listarMovimentacoes() {
        return financeiro.getMovimentacoes();
    }

    public Movimentacao obterMovimentacao(int indice) {
        return financeiro.getMovimentacao(indice);
    }

    public int quantidadeMovimentacoes() {
        return financeiro.tamanho();
    }

    // Edição e exclusão
    public void editarMovimentacao(int indice, Movimentacao m) {
        financeiro.editar(indice, m);
    }

    public void excluirMovimentacao(int indice) {
        financeiro.excluir(indice);
    }

    // Totais financeiros
    public double getTotalReceitas() {
        return financeiro.getTotalReceitas();
    }

    public double getTotalDespesas() {
        return financeiro.getTotalDespesas();
    }

    public double getSaldo() {
        return financeiro.getSaldo();
    }

    // Despesas agrupadas por categoria
    public Map<String, Double> getDespesasPorCategoria() {
        return financeiro.getDespesasPorCategoria();
    }

    // Despesas agrupadas por descrição
    public Map<String, Double> getDespesasPorNome() {
        return financeiro.getDespesasPorNome();
    }

    // Categoria com maior valor de despesas
    public String getCategoriaComMaiorDespesa() {
        return financeiro.getCategoriaComMaiorDespesa();
    }

    // Resumo mensal para os gráficos
    public LinkedHashMap<String, double[]> getResumoMensal(int meses) {
        return financeiro.getResumoMensal(meses);
    }

    // Filtragem das movimentações
    public ArrayList<Movimentacao> filtrar(
            String tipo, String categoria, String pesquisa) {

        ArrayList<Movimentacao> resultado = new ArrayList<>();

        for (Movimentacao m : financeiro.getMovimentacoes()) {

            boolean tipoOk = tipo == null
                    || "Todos".equals(tipo)
                    || tipo.equals(m.getTipo());

            String cat = m.getCategoria();

            if (cat == null || cat.isBlank()) {
                cat = "Sem categoria";
            }

            boolean categoriaOk = categoria == null
                    || "Todas".equals(categoria)
                    || categoria.equals(cat);

            String descricao = m.getDescricao();

            boolean pesquisaOk = pesquisa == null
                    || pesquisa.isBlank()
                    || (descricao != null
                    && descricao.toLowerCase().contains(
                            pesquisa.toLowerCase()));

            if (tipoOk && categoriaOk && pesquisaOk) {
                resultado.add(m);
            }
        }

        return resultado;
    }
}
