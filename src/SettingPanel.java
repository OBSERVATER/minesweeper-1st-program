import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingPanel extends JFrame{
    int diff;
    public SettingPanel() {
        setSize(640,480);
        setTitle("Setting");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,2));
        panel.setBorder(BorderFactory.createTitledBorder("Difficulty"));

        JRadioButton[] btns = new JRadioButton[4];
        btns[0] = new JRadioButton();
        btns[0].setText("<html>初级<br>10颗雷<br>9x9网格</html>");
        btns[0].setBounds(20,30,75,22);
        btns[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diff = 0;
            }
        });

        btns[1] = new JRadioButton();
        btns[1].setText("<html>中级<br>40颗雷<br>16x16网格</html>");
        btns[1].setBounds(20,30,75,22);
        btns[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diff = 1;
            }
        });

        btns[2] = new JRadioButton();
        btns[2].setText("<html>高级<br>99颗雷<br>16x30网格</html>");
        btns[2].setBounds(20,30,75,22);
        btns[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diff = 2;
            }
        });

        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Setting.DIFFICULTY = diff;
                Init.lr.reset();
                setVisible(false);
            }
        });

        ButtonGroup g = new ButtonGroup();
        for (int i = 0; i < 3; i++) {
            g.add(btns[i]);
            panel.add(btns[i]);
        }
        panel.add(confirm);

        add(panel);
    }
}
