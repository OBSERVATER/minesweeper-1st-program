import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

public class chess extends JFrame {
    private int a = 1;
    BufferedImage chessimg = null;
    {
        try {
            chessimg = ImageIO.read(new FileInputStream("D:\\jvav\\minesweeper\\res\\normal.png"));
            win();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void win(){
        setSize(640, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
    public void update(Graphics g) {
        while (true) {
            a += 3;
            g.setColor(Color.BLUE);
            g.drawString("Missiles Count: " + a, 10, 20);
            g.drawString("Explodes Count: ", 10, 40);
            g.drawString("Tanks Count: ", 10, 60);
            g.drawString("HeroTank Life: ", 10, 80);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void paint(Graphics g){

            g.setColor(Color.BLUE);
            //g.drawString("Missiles Count: " + a, 10, 20);
            g.drawString("Explodes Count: ", 10, 40);
            g.drawString("Tanks Count:" + a+a, 10, 60);
            g.drawString("HeroTank Life: " , 10, 80);
    }
}
