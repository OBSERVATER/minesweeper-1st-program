import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class LaunchFrame extends Frame implements MouseListener{
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    int[][] diff = {{9,9,10},{16,16,40},{30,16,99}};
    private volatile boolean run;
    private boolean bombrun;

    Image icon = Toolkit.getDefaultToolkit().getImage("res/icon.png");
    private Image offscreenimage = null;
    int time = 0;
    int remaining_bomb;
    List<List<chess>> chessX = new CopyOnWriteArrayList<>();
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
        System.out.println(rx+"<----->"+ry);
        if (AbsToRe_X(e.getX())>=0 && AbsToRe_Y(e.getY())>=0 &&
                AbsToRe_X(e.getX())<diff[Setting.DIFFICULTY][0] && AbsToRe_Y(e.getY())<diff[Setting.DIFFICULTY][1]){
            //雷生成器
            if (!bombrun){
                bombrun = true;
                run = true;
                counter.start();
                int[] xx = new int[diff[Setting.DIFFICULTY][2]];
                int[] yy = new int[diff[Setting.DIFFICULTY][2]];
                Random random = new Random();
                for (int i = 0; i < diff[Setting.DIFFICULTY][2]; i++){
                    boolean isrepeat = false;
                    xx[i]=random.nextInt(0,diff[Setting.DIFFICULTY][0]-1);
                    yy[i]=random.nextInt(0,diff[Setting.DIFFICULTY][1]-1);
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
                for (int i = 0; i < diff[Setting.DIFFICULTY][2]; i++){
                    chessX.get(xx[i]).get(yy[i]).setBomb(true);
                }//激活雷,计算周围雷数量
                for (List<chess> y : chessX){
                    for (chess a : y){
                        a.setRun(true);
                        int ax = a.getRx();
                        int ay = a.getRy();
                        for (int i = -1; i<=1 ;i++){
                            for (int j = -1; j<=1;j++){
                                if (ax+i<0 || ax+i>diff[Setting.DIFFICULTY][0]-1
                                        || ay+j<0 || ay+j>diff[Setting.DIFFICULTY][1]-1
                                        || (i==ax && j == ay))
                                    continue;
                                if (chessX.get(ax+i).get(ay+j).isBomb()){
                                    a.setBombcount(a.getBombcount()+1);
                                }
                            }
                        }
                    }
                }
                bcount();
            }
            //获取相对坐标
            List<chess> temp = chessX.get(AbsToRe_X(e.getX()));
            chess tempchess = temp.get(AbsToRe_Y(e.getY()));
            //左键：开雷
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
                                if (ax+i<0 || ax+i>diff[Setting.DIFFICULTY][0]-1
                                        || ay+j<0 || ay+j>diff[Setting.DIFFICULTY][1]-1
                                        || (i==ax && j == ay) || chessX.get(ax+i).get(ay+j).isFlagged())
                                    continue;
                                chess dc = chessX.get(ax+i).get(ay+j);
                                if (dc.getBombcount() == 0){
                                    widen(dc);
                                }else if (dc.isBomb()){
                                    dc.setBombtriggered(true);
                                    counter.interrupt();
                                    try {
                                        counter.join();
                                        Thread.sleep(1000);
                                    } catch (InterruptedException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    lose();
                                    break;
                                }else{
                                    dc.setPressed(true);
                                }
                            }
                            if (!run) break;
                        }
                    }
                }
                if (run) winning();
                //插旗
            } else if (e.getButton()==MouseEvent.BUTTON3) {
                if (e.getClickCount() >= 1 && !tempchess.isPressed()){
                    tempchess.setFlagged(!tempchess.isFlagged());
                    bcount();
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
                if (ax+i<0 || ax+i>diff[Setting.DIFFICULTY][0]-1
                        || ay+j<0 || ay+j>diff[Setting.DIFFICULTY][1]-1
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
        if (a.getBombcount() == 0 && !a.isBomb() ){
            for (int i = -1; i<=1 ;i++){
                for (int j = -1; j<=1;j++){
                    if (ax+i<0 || ax+i>diff[Setting.DIFFICULTY][0]-1
                            || ay+j<0 || ay+j>diff[Setting.DIFFICULTY][1]-1
                            || (i==ax && j == ay) || chessX.get(ax+i).get(ay+j).isPressed()
                            || chessX.get(ax+i).get(ay+j).isFlagged())
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
        JOptionPane.showMessageDialog(null,"You Lose.\nPress button to continue.",getTitle(), JOptionPane.INFORMATION_MESSAGE);
        reset();
    }

    private void winning(){
        int notBomb = 0;
        int pressedcount = 0;
        for (List<chess> a : chessX){
            for (chess tempchess : a){
                if (!tempchess.isBomb()){
                    notBomb++;
                }
                if (tempchess.isPressed()){
                    pressedcount++;
                }
            }
        }
        if (notBomb == pressedcount && pressedcount > 0){
            run = false;
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            JOptionPane.showMessageDialog(null,"You Win.\n" +
                    "Total time:"+time+"s" +
                    "\nPress button to continue.",getTitle(), JOptionPane.INFORMATION_MESSAGE);
            reset();
        }
    }

    private void bcount(){
        int bcounter = 0;
        for (List<chess> a : chessX){
            for (chess tempchess : a){
                if (tempchess.isFlagged()){
                    bcounter++;
                }
            }
        }
        this.remaining_bomb = diff[Setting.DIFFICULTY][2]-bcounter;
    }
    private int AbsToRe_X(int x){
        int INITX = (WIDTH/2-diff[Setting.DIFFICULTY][0]*16);
        return (x-INITX)/32;
    }
    private int AbsToRe_Y(int y){
        int INITY =  (HEIGHT/2-diff[Setting.DIFFICULTY][1]*16) + 30;
        return (y-INITY)/32;
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
        generateChess(diff[Setting.DIFFICULTY]);
        bombrun = false;
        run =false;
        time = 0;
        remaining_bomb = 0;
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
        setIconImage(icon);
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
        Font font = new Font("Arial",Font.BOLD,20);
        g.setFont(font);
        int INITX =  (WIDTH/2-diff[Setting.DIFFICULTY][0]*16);
        g.drawString("Time: " + (run ? this.time : 0), INITX, 90);
        g.drawString("Remaining Bomb: " + this.remaining_bomb, INITX, 120);
        for (List<chess> y : chessX){
            for (chess a : y){
                a.render(g);
            }
        }
    }

}