
package model;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Financeiro {

    private final ArrayList<Movimentacao> movimentacoes =
            new ArrayList<>();

    public void adicionar(Movimentacao movimentacao) {
        if (movimentacao == null) {
            throw new IllegalArgumentException(
                    "A movimentação não pode ser nula.");
        }

        movimentacoes.add(movimentacao);
    }

    public ArrayList<Movimentacao> getMovimentacoes() {
        return new ArrayList<>(movimentacoes);
    }

    public Movimentacao getMovimentacao(int indice) {
        return movimentacoes.get(indice);
    }

    public int tamanho() {
        return movimentacoes.size();
    }

    public void editar(int indice, Movimentacao novaMovimentacao) {
        if (novaMovimentacao == null) {
            throw new IllegalArgumentException(
                    "A movimentação não pode ser nula.");
        }

        movimentacoes.set(indice, novaMovimentacao);
    }

    public void excluir(int indice) {
        movimentacoes.remove(indice);
    }

    public double getTotalReceitas() {
        return movimentacoes.stream()
                .filter(m -> "Receita".equals(m.getTipo()))
                .mapToDouble(Movimentacao::getValor)
                .sum();
    }

    public double getTotalDespesas() {
        return movimentacoes.stream()
                .filter(m -> "Despesa".equals(m.getTipo()))
                .mapToDouble(Movimentacao::getValor)
                .sum();
    }

    public double getSaldo() {
        return getTotalReceitas() - getTotalDespesas();
    }

    // Despesas agrupadas por categoria.
    // Mantém o funcionamento dos relatórios existentes.
    public Map<String, Double> getDespesasPorCategoria() {
        Map<String, Double> mapa = new TreeMap<>();

        for (Movimentacao m : movimentacoes) {
            if ("Despesa".equals(m.getTipo())) {
                String categoria = m.getCategoria();

                if (categoria == null || categoria.isBlank()) {
                    categoria = "Sem categoria";
                }

                mapa.merge(categoria, m.getValor(), Double::sum);
            }
        }

        return mapa;
    }

    // Despesas agrupadas pelo nome/descrição.
    // Este método será usado no gráfico de pizza.
    public Map<String, Double> getDespesasPorNome() {
        Map<String, Double> mapa = new TreeMap<>();

        for (Movimentacao m : movimentacoes) {
            if ("Despesa".equals(m.getTipo())) {
                String nome = m.getDescricao();

                if (nome == null || nome.isBlank()) {
                    nome = "Despesa sem nome";
                }

                mapa.merge(nome, m.getValor(), Double::sum);
            }
        }

        return mapa;
    }

    public String getCategoriaComMaiorDespesa() {
        return getDespesasPorCategoria()
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }

    // Evolução mensal dos últimos meses, incluindo meses sem lançamentos.
    // A chave yyyy-MM garante a ordem cronológica.
    public LinkedHashMap<String, double[]> getResumoMensal(int meses) {
        LinkedHashMap<String, double[]> resultado =
                new LinkedHashMap<>();

        if (meses <= 0) {
            return resultado;
        }

        YearMonth mesAtual = YearMonth.now();
        YearMonth primeiroMes = mesAtual.minusMonths(meses - 1L);

        // Inicializa todos os meses com receitas e despesas iguais a zero.
        for (int i = 0; i < meses; i++) {
            YearMonth mes = primeiroMes.plusMonths(i);

            resultado.put(
                    mes.toString(),
                    new double[] { 0.0, 0.0 }
            );
        }

        // Usa a data real de cada movimentação.
        for (Movimentacao m : movimentacoes) {
            YearMonth mesMovimentacao =
                    YearMonth.from(m.getDataHora());

            if (mesMovimentacao.isBefore(primeiroMes)
                    || mesMovimentacao.isAfter(mesAtual)) {
                continue;
            }

            double[] valores =
                    resultado.get(mesMovimentacao.toString());

            if (valores == null) {
                continue;
            }

            if ("Receita".equals(m.getTipo())) {
                valores[0] += m.getValor();
            } else if ("Despesa".equals(m.getTipo())) {
                valores[1] += m.getValor();
            }
        }

        return resultado;
    }
}
