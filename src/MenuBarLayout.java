import java.awt.*;

public class MenuBarLayout {
    public MenuBar mb = new MenuBar();

    public MenuBarLayout(Frame f) {
        f.setMenuBar(mb);

        Menu m1 = new Menu("Game");

        mb.add(m1);

        MenuItem mi1 = new MenuItem("New Game...");
        MenuItem mi2 = new MenuItem("Settings");

        m1.add(mi1);
        m1.addSeparator();
        m1.add(mi2);

        mi2.addActionListener(ActionCommandController.getInstance());
        mi2.setActionCommand("Setting");

        mi1.addActionListener(ActionCommandController.getInstance());
        mi1.setActionCommand("NewGame");

        f.setVisible(true);
    }
}
