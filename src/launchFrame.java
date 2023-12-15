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
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int DIFFICULTY = 0;//改变难度(0-2)-1
    int[][] diff = {{9,9,10},{16,16,40},{30,16,99}};
    private volatile boolean run;
    private boolean bombrun;
    public final int INITX =  (WIDTH/2-diff[DIFFICULTY][0]*12);
    public final int INITY =  (HEIGHT/2-diff[DIFFICULTY][1]*12);
    private Image offscreenimage = null;
    int time = 0;
    List<List<chess>> chessX = new ArrayList<>();
    public void generateChess(int[] diff){//传入diff数组的子元素
        for (int x = 0; x < diff[0]; x++){
            List<chess> chessY = new ArrayList<>();
            for (int y = 0; y < diff[1] ;y++){
                chessY.add(new chess(x,y));
            }
            chessX.add(chessY);
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) throws IndexOutOfBoundsException{
        //System.out.println("鼠标dianji");
        int rx = AbsToRe_X(e.getX());
        int ry = AbsToRe_Y(e.getY());
        Thread counter = new Thread(new time_Thread());
        //System.out.println(rx+"<----->"+ry);
        if (AbsToRe_X(e.getX())>=0 && AbsToRe_Y(e.getY())>=0){
            //雷生成器
            if (!bombrun){
                bombrun = true;
                run = true;
                counter.start();
                int[] xx = new int[diff[DIFFICULTY][2]];
                int[] yy = new int[diff[DIFFICULTY][2]];
                Random random = new Random();
                for (int i = 0; i < diff[DIFFICULTY][2]; i++){
                    boolean isrepeat = false;
                    xx[i]=random.nextInt(0,diff[DIFFICULTY][0]-1);
                    yy[i]=random.nextInt(0,diff[DIFFICULTY][1]-1);
                    if (i>0) {
                        for (int j = i-1; j >= 0; j--) {
                            if ((xx[i] == xx[j] && yy[i] == yy[j]) || (xx[i] == rx && yy[i] == ry)) {
                                isrepeat = true;
                                break;
                            }
                        }
                    }
                    if (isrepeat){
                        i--;
                        //System.out.println("yes");
                    }
                }
                for (int i = 0; i < diff[DIFFICULTY][2];i++){
                    chessX.get(xx[i]).get(yy[i]).setBomb(true);
                }//激活雷,计算雷数量
                for (List<chess> y : chessX){
                    for (chess a : y){
                        a.setRun(true);
                        int ax = a.getRx();
                        int ay = a.getRy();
                        for (int i = -1; i<=1 ;i++){
                            for (int j = -1; j<=1;j++){
                                if (ax+i<0 || ax+i>diff[DIFFICULTY][0]-1
                                        || ay+j<0 || ay+j>diff[DIFFICULTY][1]-1
                                        || (i==ax && j == ay))
                                    continue;
                                if (chessX.get(ax+i).get(ay+j).isBomb()){
                                    a.setBombcount(a.getBombcount()+1);
                                }
                            }
                        }
                    }
                }
            }
            //获取相对坐标
            List<chess> temp = chessX.get(AbsToRe_X(e.getX()));
            chess tempchess = temp.get(AbsToRe_Y(e.getY()));
            //左键单击：开雷
            if (e.getButton()==MouseEvent.BUTTON1 && !tempchess.isFlagged()){
                if (e.getClickCount() == 1) {
                    if (tempchess.isBomb()) {//点到雷，炸了
                        //run = false;
                        tempchess.setBombtriggered(true);
                        counter.interrupt();
                        try {
                            counter.join();
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        lose();
                    }
                    else {
                        widen(tempchess);
                    }
                    //双击，快捷开区域
                } else if (e.getClickCount() == 2 && tempchess.isPressed()) {
                    int local_bcount = getLocalBcount(tempchess);
                    int ax = tempchess.getRx();
                    int ay = tempchess.getRy();
                    if (local_bcount == tempchess.getBombcount()){
                        for (int i = -1; i<=1 ;i++){
                            for (int j = -1; j<=1;j++){
                                if (ax+i<0 || ax+i>diff[DIFFICULTY][0]-1
                                        || ay+j<0 || ay+j>diff[DIFFICULTY][1]-1
                                        || (i==ax && j == ay))
                                    continue;
                                chess dc = chessX.get(ax+i).get(ay+j);
                                if (dc.isFlagged()) {//炸雷
                                    continue;
                                }else if (dc.getBombcount() == 0){
                                    widen(dc);
                                }else if (dc.isBomb()){
                                    dc.setBombtriggered(true);
                                    counter.interrupt();
                                    try {
                                        counter.join();
                                    } catch (InterruptedException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    lose();
                                }else{
                                    dc.setPressed(true);
                                }
                            }
                        }
                    }
                }
                //插旗
            } else if (e.getButton()==MouseEvent.BUTTON3) {
                if (e.getClickCount() >= 1 && !tempchess.isPressed()){
                    tempchess.setFlagged(!tempchess.isFlagged());
                }
            }
        }
    }

    private int getLocalBcount(chess tempchess) {
        int local_bcount = 0;
        int ax = tempchess.getRx();
        int ay = tempchess.getRy();
        for (int i = -1; i<=1 ;i++){
            for (int j = -1; j<=1;j++){
                if (ax+i<0 || ax+i>diff[DIFFICULTY][0]-1
                        || ay+j<0 || ay+j>diff[DIFFICULTY][1]-1
                        || (i==ax && j == ay))
                    continue;
                if (chessX.get(ax+i).get(ay+j).isFlagged()){
                    local_bcount++;
                }
            }
        }
        return local_bcount;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    private void widen(chess a){
        a.setPressed(true);
        int ax = a.getRx();
        int ay = a.getRy();
        if (a.getBombcount() == 0 && !a.isBomb()){
            for (int i = -1; i<=1 ;i++){
                for (int j = -1; j<=1;j++){
                    if (ax+i<0 || ax+i>diff[DIFFICULTY][0]-1
                            || ay+j<0 || ay+j>diff[DIFFICULTY][1]-1
                            || (i==ax && j == ay) || chessX.get(ax+i).get(ay+j).isPressed())
                        continue;
                    widen(chessX.get(ax+i).get(ay+j));
                }
            }
        }
    }
    private void lose(){
        for (List<chess> y : chessX){
            for (chess a : y){
                a.setRun(false);
                if (a.isBomb()){
                    a.setPressed(true);
                }
            }
        }
        run = false;
        JOptionPane.showMessageDialog(null,"You Lose.\nPress button to continue.",getTitle(),1);
        reset();
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
            while (run) {
                try {
                    Thread.sleep(1000);
                    time++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void reset(){
        chessX.clear();
        Thread render = new Thread(new paint_Thread());
        render.start();
        generateChess(diff[DIFFICULTY]);
        bombrun = false;
        run =false;
        time = 0;
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
        addMouseListener(this);
        reset();
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
            for (chess a : y){
                a.render(g);
            }
        }
    }
    public static void main(String[] args) {
        launchFrame lr = new launchFrame();
        lr.win();
    }
}