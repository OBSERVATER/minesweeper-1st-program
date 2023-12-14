import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class launchFrame extends Frame implements MouseListener{
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    private boolean run;
    private boolean bombrun;
    public static final int INITX = (int) (WIDTH/2-4.5*24);
    public static final int INITY = (int) (HEIGHT/2-4.5*24);
    int[][] diff = {{9,9,10},{16,16,40},{16,30,99}};
    private Image offscreenimage = null;
    int time = 0;
    List<List<chess>> chessX = new ArrayList<>();
    public void generateChess(int width,int height){
        for (int x = 0; x < width; x++){
            List<chess> chessY = new ArrayList<>();
            for (int y = 0; y < height ;y++){
                chessY.add(new chess(x,y));
            }
            chessX.add(chessY);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) throws IndexOutOfBoundsException{
        //System.out.println("鼠标dianji");
        int rx = AbsToRe_X(e.getX());
        int ry = AbsToRe_Y(e.getY());
        System.out.println(rx+"<----->"+ry);
        if (AbsToRe_X(e.getX())>=0 && AbsToRe_Y(e.getY())>=0){
            //雷生成器
            if (!bombrun){
                bombrun = true;
                int[] xx = new int[10];
                int[] yy = new int[10];
                Random random = new Random();
                for (int i = 0; i<10; i++){
                    boolean isrepeat = false;
                    xx[i]=random.nextInt(0,9);
                    yy[i]=random.nextInt(0,9);
                    if (i>0) {
                        for (int j = i-1; j >= 0; j--) {
                            if (xx[i] == xx[j] && yy[i] == yy[j]) {
                                System.out.println(xx[i] + "==" + xx[j]);
                                System.out.println(yy[i] + "===" + yy[j]);
                                isrepeat = true;
                            }
                        }
                    }
                    if (isrepeat){
                        i--;
                        //System.out.println("yes");
                    }
                }
                for (int i = 0; i<10;i++){
                    chessX.get(xx[i]).get(yy[i]).setBomb(true);
                }
            }
            //获取相对坐标
            List<chess> temp = chessX.get(AbsToRe_X(e.getX()));
            chess tempchess = temp.get(AbsToRe_Y(e.getY()));
            //左键单击：开雷
            if (e.getButton()==MouseEvent.BUTTON1 && !tempchess.isFlagged()){
                if (e.getClickCount() >= 1) {
                    if (tempchess.isBomb()) {
                        //run = false;
                        JOptionPane.showMessageDialog(null,"You Lose");
                        tempchess.setBombtriggered(true);
                        for (List<chess> y : chessX){
                            for (chess a : y){
                                if (a.isBomb()){
                                    a.setPressed(true);
                                }
                            }
                        }

                    }
                    else {
                        tempchess.setPressed(true);
                    }
                } else if (e.getClickCount() == 2 && tempchess.isPressed()) {
                    System.out.println("yes");
                }
                //插旗
            } else if (e.getButton()==MouseEvent.BUTTON3) {
                if (e.getClickCount() >= 1 && !tempchess.isPressed()){
                    tempchess.setFlagged(!tempchess.isFlagged());
                }
            }
        }
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

    private int AbsToRe_X(int x){
        return (x-INITX)/24;
    }
    private int AbsToRe_Y(int y){
        return (y-INITY)/24;
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
        generateChess(9,9);
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
        for (List<chess> y : chessX){
            //System.out.println(y);
            for (chess a : y){
                a.render(g);
                //System.out.println(a);
            }
        }
    }

    public static void main(String[] args) {
        launchFrame lr = new launchFrame();
        lr.win();
    }
}