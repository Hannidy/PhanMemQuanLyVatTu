
package manager;

import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import java.awt.EventQueue;
import javax.swing.JComponent;
import main.Main;

public class FormsManager {
    private Main main;
    private static FormsManager instance;

    public static FormsManager getInstance() {
        if (instance == null) {
            instance = new FormsManager();
        }
        return instance;
    }

    private FormsManager() {

    }

    public void initMain(Main main) {
        this.main = main;
    }

    public void showForm(JComponent form) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            main.setContentPane(form);
            main.revalidate();
            main.repaint();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }
    
    public Main getMain() {
        return main;
    }
    
}
