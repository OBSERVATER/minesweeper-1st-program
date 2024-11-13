import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingPanel extends JFrame{
    int diff;
    public SettingPanel() {
        setTitle("Setting");
        setSize(640,480);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,2));
        panel.setBorder(BorderFactory.createTitledBorder("Difficulty"));

        JPanel p1 = new JPanel(new GridLayout(3,1));
        JPanel p2 = new JPanel();

        panel.add(p1);
        panel.add(p2);

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

        JPanel p3 = new JPanel(new GridLayout(3,2));
        JRadioButton customRadioBtn = new JRadioButton("自定义...");
        JLabel l1 = new JLabel("宽度: (9-30)");
        JLabel l2 = new JLabel("高度: (9-20)");
        JLabel l3 = new JLabel("雷数: (10-300)");
        JTextField text1 = new JTextField("20",1);
        JTextField text2 = new JTextField("16",1);
        JTextField text3 = new JTextField("32",1);

        p3.add(l1);
        p3.add(text1);
        p3.add(l2);
        p3.add(text2);
        p3.add(l3);
        p3.add(text3);

        p2.add(customRadioBtn);
        p2.add(p3);

        customRadioBtn.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                p3.setEnabled(!p3.isEnabled());
            }
        });
        customRadioBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diff = 3;
            }
        });

        JButton confirm = getjButton(text1, text2, text3);
        confirm.setSize(30,60);
        JButton cancel = new JButton("Cancel");
        cancel.setSize(30,60);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        ButtonGroup g = new ButtonGroup();
        for (int i = 0; i < 3; i++) {
            g.add(btns[i]);
            p1.add(btns[i]);
        }
        g.add(customRadioBtn);
        panel.add(confirm);
        panel.add(cancel);

        add(panel);
    }

    private JButton getjButton(JTextField text1, JTextField text2, JTextField text3) {
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Setting.DIFFICULTY = diff;
                if (diff == 3)
                {
                    Setting.width = Math.clamp(Integer.parseInt(text1.getText()),9,30);
                    Setting.height = Math.clamp(Integer.parseInt(text2.getText()),9,20);
                    Setting.bombCount = Math.clamp(Integer.parseInt(text3.getText()),10,548);

                    Init.diff[3] = new int[] {Setting.width,Setting.height,
                            Setting.bombCount >= Setting.width*Setting.height ? Setting.width*Setting.height/2 : Setting.bombCount};

                }
                Init.lr.reset();
                setVisible(false);
            }
        });
        return confirm;
    }
}
