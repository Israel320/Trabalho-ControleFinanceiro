package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import controller.FinanceiroController;
import model.Financeiro;
import view.TelaPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {}

            Financeiro financeiro = new Financeiro();
            // O sistema inicia vazio. Cadastre suas próprias movimentações.
            FinanceiroController controller = new FinanceiroController(financeiro);
            new TelaPrincipal(controller).setVisible(true);
        });
    }
}