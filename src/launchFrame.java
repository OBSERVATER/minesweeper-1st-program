import javax.swing.*;
import java.awt.*;
public class launchFrame extends JFrame {
    int time = 0;
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private Image offscreenimage = null;
    private class time_Thread implements Runnable{
        public void run(){
            while (true) {
                try {
                    repaint();
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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        Thread counter = new Thread(new time_Thread());
        counter.start();
        //Thread render = new Thread(new paint_Thread());
        //render.start();
    }

    public void update(Graphics g) {
        if (this.offscreenimage==null){
            this.offscreenimage = this.createImage(WIDTH,HEIGHT);
        }
        Graphics Goff = offscreenimage.getGraphics();
        Goff.setColor(Color.blue);
        paint(Goff);
        g.drawImage(this.offscreenimage,0,0,null);
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(Color.BLUE);
        g.drawString("Time: " + this.time, 10, 40);
    }

    public static void main(String[] args) {
        launchFrame lr = new launchFrame();
        lr.win();
    }
}