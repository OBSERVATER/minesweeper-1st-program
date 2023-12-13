import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

public class chess extends Frame{
    Image imgnormal = Toolkit.getDefaultToolkit().getImage("res/normal.png");
    Image imgsafe = Toolkit.getDefaultToolkit().getImage("res/safe0.png");
    private boolean isBomb;
    private boolean ispressed = false;
    private int rx;
    private int ry;

    public void pressed(int x,int y){
        System.out.println(x + "<---->" + y);
        if (x <= 26 && y <=116){
            ispressed = true;
        }
    }
    public void render(Graphics g){
        if (ispressed)
            g.drawImage(imgsafe,10,100,16,16,this);
        else
            g.drawImage(imgnormal,10,100,16,16,this);
    }

}
