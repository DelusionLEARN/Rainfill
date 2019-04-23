package rain;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.MemoryImageSource;
import java.util.Random;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class rainfill extends JDialog implements ActionListener {

    private Random random = new Random();
    private Dimension screenSize;
    private JPanel graphicsPanel;
    
    private final static int gap = 20;
    //位置信息
    private int[] posArr;
    //行数
    private int lines;
    //列数
    private int columns;

    //调用函数
    public rainfill() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        graphicsPanel = new GraphicsPanel();
        add(graphicsPanel, BorderLayout.CENTER);
        //设置光标不可见
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        //背景
        Image image = defaultToolkit.createImage(new MemoryImageSource(0, 0, null, 0, 0));
        Cursor invisibleCursor = defaultToolkit.createCustomCursor(image, new Point(0, 0), "cursor");
        setCursor(invisibleCursor);
        //ESC键退出(监听)
        KeyPressListener keyPressListener = new KeyPressListener();
        this.addKeyListener(keyPressListener);
        //去标题栏
        this.setUndecorated(true);
        //全屏
        this.getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        lines = screenSize.height / gap;
        columns = screenSize.width / gap;

        posArr = new int[columns + 1];
        random = new Random();
        for (int i = 0; i < posArr.length; i++) {
            posArr[i] = random.nextInt(lines);
        }

        //每秒10帧
        new Timer(100, this).start();
    }
    
    //获得随机数
    private char getChr() { return (char) (random.nextInt(94) + 33); }

    public void actionPerformed(ActionEvent e) { graphicsPanel.repaint(); }

    private class GraphicsPanel extends JPanel {
        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(getFont().deriveFont(Font.BOLD));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, screenSize.width, screenSize.height);
            //当前列
            int currentColumn = 0;
            for (int x = 0; x < screenSize.width; x += gap) {
                int endPos = posArr[currentColumn];
                g2d.setColor(Color.CYAN);
                g2d.drawString(String.valueOf(getChr()), x, endPos * gap);
                int cg = 0;
                for (int j = endPos - 15; j < endPos; j++) {
                    //颜色渐变
                    cg += 20;
                    if (cg > 255)
                        cg = 255;
                    g2d.setColor(new Color(255-cg, cg, cg));
                    g2d.drawString(String.valueOf(getChr()), x, j * gap);
                }
                posArr[currentColumn] += random.nextInt(5);
                if (posArr[currentColumn] * gap > getHeight())
                    posArr[currentColumn] = random.nextInt(lines);
                currentColumn++;
            }
        }
    }

    private class KeyPressListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                System.exit(0);
        }
    }

    public static void main(String[] args) {
        new rainfill();
    }
}