import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionCommandController extends JFrame implements ActionListener {

    SettingPanel panel = new SettingPanel();
    private static ActionCommandController instance;
    ActionCommandController() {

    }

    public static synchronized ActionCommandController getInstance() {
        if (instance == null)
            instance = new ActionCommandController();
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("NewGame")) {
            Init.lr.reset();
        } else if (e.getActionCommand().equals("Setting")) {
            panel.setVisible(true);
        }
    }
}
