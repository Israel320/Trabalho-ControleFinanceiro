# Trabalho-ControleFinanceiro


# Sistema de Controle Financeiro Pessoal

**Disciplina:** Elicitação e Programação

**Professor:** Hudson Neves


**Curso:** Engenharia de Software — 2º semestre

**Projeto:** Grupo 04 — Controle Financeiro Pessoal

**Link para o vídeo no YouTube explicando Front-end e Back-end:**
 - https://youtu.be/N__HmKKwasg

## Integrantes e Matrícula


- Danilo Marques de Sousa – 0028249
- Israel Roberto Pinheiro – 0026780
- Jefter Samuel Rocha Alves – 0028515
- João Gabriel Lopes Liborio – 0027035
- Matheus Henrique Brito de Jesus – 0026897
- Nathan de Aguiar Carvalho Leite – 0026776

---


<div align="center"> Controle Financeiro Pessoal
Organização financeira com Java Swing e arquitetura MVC
Trabalho acadêmico · Grupo 04 · Engenharia de Software

Uma aplicação desktop para registrar movimentações, acompanhar receitas e despesas e visualizar o saldo pessoal.
</div> Sobre o projeto
O Controle Financeiro Pessoal é um projeto acadêmico da disciplina Elicitação e Programação, desenvolvido pelo Grupo 04 do segundo semestre de Engenharia de Software, sob orientação do professor Hudson Neves.

A proposta consiste em construir um sistema desktop em Java SE, com interface gráfica em Swing, capaz de organizar as finanças de um usuário. O sistema deve permitir o registro e a consulta de receitas e despesas, a edição e exclusão de movimentações e a geração de um resumo financeiro.

A organização do código segue o padrão MVC (Model–View–Controller), separando dados e regras de negócio, componentes visuais e coordenação das operações. Os registros são mantidos em memória por meio de arrays ou ArrayList, sem banco de dados.
Escopo acadêmico: as funcionalidades e a estrutura descritas abaixo correspondem aos requisitos do projeto. Sua implementação deve ser verificada no código-fonte do repositório.

 Funcionalidades previstas
Funcionalidade	Descrição
 Cadastro de receitas	Registrar descrição, valor e data de recebimento.
 Cadastro de despesas	Registrar descrição, valor, data e categoria.
 Listagem de movimentações	Exibir receitas e despesas em uma tabela JTable.
 Pesquisa e filtros	Consultar movimentações por tipo ou categoria.
 Edição e exclusão	Alterar ou remover lançamentos existentes.
 Cálculo automático	Apurar total de receitas, total de despesas e saldo.
 Relatório financeiro	Apresentar os totais e a categoria com maior volume de despesas.

Regras de negócio
- Não permitir receitas ou despesas com valor menor ou igual a zero.
- Validar os dados informados antes de registrar ou atualizar uma movimentação.
- Recalcular receitas, despesas e saldo após cadastros, edições e exclusões.
- Manter os registros acessíveis durante toda a execução, mesmo ao fechar e reabrir uma tela.
- Armazenar informações somente em memória: os dados não persistem após o encerramento do programa.
 Interface gráfica
A interface deve ser construída exclusivamente com Java Swing e conter, no mínimo, as seguintes telas:
1. Tela principal: identificação do sistema e acesso às funções.
2. Cadastro de movimentações: formulário de registro de receitas e despesas.
3. Consulta financeira: tabela com lançamentos, pesquisas e filtros.
4. Relatório financeiro: apresentação dos resultados e totais calculados.
Entre os componentes previstos estão JFrame, JPanel, JTextField, JButton, JTable, JComboBox e JOptionPane, com mensagens de validação, confirmação e erro.
    Arquitetura MVC
O projeto utiliza a separação de responsabilidades para facilitar o entendimento, a manutenção e a evolução do código.
```mermaid
flowchart LR
    U[Usuário] --> V[View<br/>Interface Swing]
    V --> C[Controller<br/>Operações e eventos]
    C --> M[Model<br/>Dados e regras de negócio]
    M --> C
    C --> V
```
- Model: representa as movimentações financeiras, mantém as coleções em memória e aplica validações e cálculos.
- View: apresenta telas, formulários, tabelas e mensagens, além de receber as ações do usuário.
- Controller: recebe os eventos da interface, coordena as operações e estabelece a comunicação entre View e Model.
   Estrutura sugerida do projeto
A estrutura abaixo é uma referência de organização, a ser ajustada aos nomes reais das classes implementadas.


## Estrutura do Projeto

O projeto está organizado em pacotes seguindo o padrão MVC:

```text
ControleFinanceiro/
├──src/
├── app/
│   └── Main.java
│
├── model/
│   ├── Movimentacao.java
│   └── Financeiro.java
│
├── controller/
│   └── FinanceiroController.java
│
└── view/
    ├── TelaPrincipal.java
    ├── TelaCadastro.java
    ├── TelaConsulta.java
    ├── TelaRelatorio.java
    ├── GraficoFinanceiro.java
    └── Estilo.java
```


    
 ## Tecnologias Utilizadas

| Tecnologia | Aplicação |
|---|---|
| Java SE 21 | Linguagem de programação |
| Java Swing | Interface gráfica |
| Eclipse IDE | Ambiente de desenvolvimento |
| ArrayList | Armazenamento em memória |
| Java Time API | Manipulação de datas e horários |
| MVC | Organização arquitetural |

**Restrições da atividade:**
- não utilizar banco de dados
- aplicações web
- Spring Boot 
- JavaFX ou frameworks externos
- A interação principal do sistema deve ocorrer pela interface gráfica, não pelo terminal.

---

 ## Como Executar

### Pré-requisitos

- Java JDK 21 ou superior.
- Eclipse IDE ou outra IDE compatível com Java.

### Passo a passo

1. Faça o download ou clone este repositório.
2. Abra o Eclipse IDE.
3. Importe o projeto Java.
4. Verifique se o JDK 21 está configurado.
5. Localize a classe `Main.java`, no pacote `app`.
6. Clique com o botão direito sobre ela.
7. Selecione **Run As → Java Application**.

Após a execução, a tela principal do sistema será aberta.

## Como o Sistema Funciona

Ao iniciar a aplicação, o usuário encontra um painel com os principais indicadores financeiros:

- Saldo disponível.
- Total de receitas.
- Total de despesas.

Na tela de cadastro, é possível registrar uma receita ou despesa, informando sua descrição, valor, data e horário.

As movimentações cadastradas podem ser consultadas em uma tabela, além de editadas, excluídas e filtradas.

O sistema também disponibiliza relatórios e gráficos que permitem visualizar melhor a distribuição das despesas e a evolução financeira ao longo dos meses.

## Conceitos Aplicados

Durante o desenvolvimento, foram aplicados conceitos importantes da programação:

- Programação Orientada a Objetos (POO).
- Classes e objetos.
- Encapsulamento.
- Separação de responsabilidades.
- Arquitetura MVC.
- Estruturas de dados com `ArrayList`.
- Estruturas de repetição e condicionais.
- Manipulação de datas e horários.
- Interfaces gráficas com Java Swing.
- Operações de consulta, edição e exclusão de dados.

## Armazenamento de Dados

Nesta versão, as movimentações são armazenadas em memória por meio de `ArrayList`.

Isso significa que os dados ficam disponíveis durante a execução do programa, mas são perdidos quando a aplicação é encerrada. O projeto ainda não utiliza banco de dados ou armazenamento permanente.

## Objetivo

Desenvolver uma aplicação desktop que facilite o controle financeiro pessoal, permitindo organizar receitas e despesas, acompanhar o saldo e visualizar informações financeiras de maneira simples e intuitiva.

O projeto também tem como finalidade consolidar os conhecimentos adquiridos na disciplina, aplicando conceitos de Java, programação orientada a objetos, interfaces gráficas e arquitetura MVC.


<div align="center">Controle Financeiro Pessoal · Grupo 04

Projeto acadêmico desenvolvido para aplicação dos fundamentos de programação e desenvolvimento desktop em Java.
</div>
