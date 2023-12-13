import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class launchFrame extends Frame implements MouseListener{
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    private Image offscreenimage = null;
    int time = 0;
    chess a = new chess();
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("鼠标dianji");
        a.pressed(e.getX(),e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private class time_Thread implements Runnable{
        public void run(){
            while (true) {
                try {
                    Thread.sleep(1000);
                    time++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class paint_Thread implements Runnable{
        public void run(){
            while (true) {
                try {
                    repaint();
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void win() {
        setSize(WIDTH, HEIGHT);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setLocationRelativeTo(null);
        setTitle("Minecraft");
        setVisible(true);
        setResizable(false);
        Thread counter = new Thread(new time_Thread());
        counter.start();
        Thread render = new Thread(new paint_Thread());
        render.start();
        addMouseListener(this);
    }

    public void update(Graphics g) {
        if (this.offscreenimage==null){
            this.offscreenimage = this.createImage(WIDTH,HEIGHT);
        }
        Graphics Goff = offscreenimage.getGraphics();
        Color c = Goff.getColor();
        Goff.setColor(Color.GRAY);
        Goff.fillRect(0,0,WIDTH,HEIGHT);
        Goff.setColor(c);
        paint(Goff);
        g.drawImage(this.offscreenimage,0,0,null);
    }

    public void paint(Graphics g) {
        g.setColor(Color.blue);
        g.drawString("Time: " + this.time, 10, 40);
        a.render(g);
    }

    public static void main(String[] args) {
        launchFrame lr = new launchFrame();
        lr.win();
    }
}