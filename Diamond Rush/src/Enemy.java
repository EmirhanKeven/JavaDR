import java.awt.Color;
import javax.swing.*;

public class Enemy {
    private JLabel enemyLabel;
    private double dx; // X ekseninde hareket miktarı (daha hassas)
    private double dy; // Y ekseninde hareket miktarı
    private int minX, maxX; // X ekseni hareket sınırları
    private int minY, maxY; // Y ekseni hareket sınırları
    private boolean isVertical; // Hareket ekseni (true: Y ekseni, false: X ekseni)
    private double posX, posY; // Mevcut hassas pozisyon (float/double ile)

    public Enemy(int x, int y, int width, int height, Color color, int min, int max, boolean isVertical) {
        enemyLabel = new JLabel();
        enemyLabel.setOpaque(true);
        enemyLabel.setBackground(color);
        enemyLabel.setBounds(x, y, width, height);

        // Başlangıç pozisyonunu hassas olarak sakla
        this.posX = x;
        this.posY = y;

        // Hareket sınırlarını ve eksenini ayarla
        this.isVertical = isVertical;
        if (isVertical) {
            this.minY = min;
            this.maxY = max;
            this.minX = this.maxX = x; // X ekseni sabit
            dy = 1.5; // Daha küçük hareket adımları
            dx = 0;
        } else {
            this.minX = min;
            this.maxX = max;
            this.minY = this.maxY = y; // Y ekseni sabit
            dx = 1.5; // Daha küçük hareket adımları
            dy = 0;
        }
    }

    public JLabel getLabel() {
        return enemyLabel;
    }

    public void move() {
        // Yeni pozisyonu hassas bir şekilde hesapla
        posX += dx;
        posY += dy;

        // Hareket sınırlarına ulaştığında yön değiştir
        if (!isVertical) {
            if (posX < minX || posX + enemyLabel.getWidth() > maxX) {
                dx = -dx;
            }
        } else {
            if (posY < minY || posY + enemyLabel.getHeight() > maxY) {
                dy = -dy;
            }
        }

        // JLabel pozisyonunu güncelle
        enemyLabel.setLocation((int) posX, (int) posY);
    }
}
