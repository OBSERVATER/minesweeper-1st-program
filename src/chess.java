import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class chess extends Frame{
    Image imgnormal = Toolkit.getDefaultToolkit().getImage("res/normal.png");
    List<Image> imgsafei = new ArrayList<>();
    public class addimage{
        public addimage(){
            for (int i = 0;i<=8;i++){
                imgsafei.add(Toolkit.getDefaultToolkit().getImage("res/safe"+i+".png"));
            }
        }
    }
    Image imgflag = Toolkit.getDefaultToolkit().getImage("res/flag1.png");
    Image imgbomb = Toolkit.getDefaultToolkit().getImage("res/mine1.png");
    Image imgbombtrigerred = Toolkit.getDefaultToolkit().getImage("res/mine2.png");
    public static final int DIFFICULTY = 0;//改变难度(0-2)-2

    int[][] diff = {{9,9,10},{16,16,40},{30,16,99}};
    public final int INITX =  (800/2-diff[DIFFICULTY][0]*12);
    public final int INITY =  (600/2-diff[DIFFICULTY][1]*12);
    public static final int WIDTH = 24;
    public static final int HEIGHT = 24;
    private boolean Bomb;
    private boolean pressed = false;

    private boolean flagged = false;
    private boolean bombtriggered = false;
    private boolean run;
    private final int rx;

    private final int ry;
    private int bombcount = 0;


    public chess(int rx, int ry){
        this.rx = rx;
        this.ry = ry;
        new addimage();
    }
    public void setRun(boolean run) {
        this.run = run;
    }

    public int getBombcount() {
        return bombcount;
    }

    public int getRx() {
        return rx;
    }

    public int getRy() {
        return ry;
    }

    public void setBombcount(int bombcount) {
        this.bombcount = bombcount;
    }
    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public void setBombtriggered(boolean bombtriggered) {
        this.bombtriggered = bombtriggered;
    }
    public boolean isBomb() {
        return Bomb;
    }

    public void setBomb(boolean bomb) {
        Bomb = bomb;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
    @Override
    public String toString() {
        return "chess{" +
                "bomb=" + Bomb +
                ", x=" + rx +
                ", y=" + ry +
                '}';
    }
    public void cross(Graphics g,int x,int y){
        Color c = g.getColor();
        g.setColor(Color.red);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3.0f));
        g2.drawLine(x+4,y+4,x+WIDTH-4,y+HEIGHT-4);
        g2.drawLine(x+WIDTH-4,y+4,x+4,y+HEIGHT-4);
        g.setColor(c);
    }
    public void render(Graphics g){
        if (run) {
            if (isPressed())
                g.drawImage(imgsafei.get(bombcount),
                        INITX + WIDTH * rx, INITY + HEIGHT * ry, WIDTH, HEIGHT,
                        this);
             else {
                g.drawImage(imgnormal, INITX + WIDTH * rx, INITY + HEIGHT * ry, WIDTH, HEIGHT,
                        this);
                if (isFlagged())
                    g.drawImage(imgflag, INITX + WIDTH * rx, INITY + HEIGHT * ry, WIDTH, HEIGHT,
                            this);
            }
        }else {
            if (isFlagged()) {
                if (isBomb()){
                    g.drawImage(imgbomb, INITX + WIDTH * rx, INITY + HEIGHT * ry, WIDTH, HEIGHT, this);
                    g.drawImage(imgflag, INITX + WIDTH * rx, INITY + HEIGHT * ry, WIDTH, HEIGHT, this);}
                else {
                    g.drawImage(imgbomb, INITX + WIDTH * rx, INITY + HEIGHT * ry, WIDTH, HEIGHT, this);
                    cross(g,INITX + WIDTH * rx, INITY + HEIGHT * ry);
                }
            } else if (bombtriggered) {
                g.drawImage(imgbombtrigerred, INITX + WIDTH * rx, INITY + HEIGHT * ry, WIDTH, HEIGHT, this);
            } else if (isPressed()) {
                if (isBomb()) {
                    g.drawImage(imgbomb, INITX + WIDTH * rx, INITY + HEIGHT * ry, WIDTH, HEIGHT, this);
                } else
                    g.drawImage(imgsafei.get(bombcount), INITX + WIDTH * rx, INITY + HEIGHT * ry, WIDTH, HEIGHT, this);
            } else
                g.drawImage(imgnormal, INITX + WIDTH * rx, INITY + HEIGHT * ry, WIDTH, HEIGHT, this);
        }
    }
}
