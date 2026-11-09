
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Movimentacao {

    private String descricao;
    private double valor;
    private LocalDateTime dataHora;
    private String tipo;
    private String categoria;

    // Construtor para novas movimentações com data e hora
    public Movimentacao(String descricao, double valor,
            LocalDateTime dataHora, String tipo, String categoria) {

        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "A descrição é obrigatória.");
        }

        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException(
                    "O valor deve ser maior que zero.");
        }

        if (dataHora == null) {
            throw new IllegalArgumentException(
                    "A data e a hora são obrigatórias.");
        }

        if (!"Receita".equals(tipo) && !"Despesa".equals(tipo)) {
            throw new IllegalArgumentException(
                    "O tipo deve ser Receita ou Despesa.");
        }

        this.descricao = descricao.trim();
        this.valor = valor;
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.categoria = categoria == null ? "" : categoria.trim();
    }

    // Mantém compatibilidade com o código antigo que usa LocalDate
    public Movimentacao(String descricao, double valor,
            LocalDate data, String tipo, String categoria) {

        this(descricao, valor,
                data == null ? null : data.atStartOfDay(),
                tipo, categoria);
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    // Retorna somente a data, para manter compatibilidade
    public LocalDate getData() {
        return dataHora.toLocalDate();
    }

    // Retorna a data e a hora completas
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "A descrição é obrigatória.");
        }
        this.descricao = descricao.trim();
    }

    public void setValor(double valor) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException(
                    "O valor deve ser maior que zero.");
        }
        this.valor = valor;
    }

    public void setDataHora(LocalDateTime dataHora) {
        if (dataHora == null) {
            throw new IllegalArgumentException(
                    "A data e a hora são obrigatórias.");
        }
        this.dataHora = dataHora;
    }

    public void setTipo(String tipo) {
        if (!"Receita".equals(tipo) && !"Despesa".equals(tipo)) {
            throw new IllegalArgumentException(
                    "O tipo deve ser Receita ou Despesa.");
        }
        this.tipo = tipo;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria == null ? "" : categoria.trim();
    }
}