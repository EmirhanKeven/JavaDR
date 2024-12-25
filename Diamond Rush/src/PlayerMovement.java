import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

public class PlayerMovement extends KeyAdapter {
    private JLabel player; // Oyuncunun JLabel temsilcisi
    private JPanel panel; // Oyun paneli
    private int worldWidth, worldHeight; // Oyun dünyasının boyutları
    private int targetX, targetY; // Oyuncunun gitmek istediği hedef pozisyon
    private Timer movementTimer; // Smooth hareket için zamanlayıcı
    private int step = 5; // Her adımda karakterin hareket ettiği piksel miktarı
    private Set<Integer> activeKeys; // Aktif tuşları takip etmek için

    // Constructor
    public PlayerMovement(JLabel player, JPanel panel, int worldWidth, int worldHeight) {
        this.player = player;
        this.panel = panel;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        // Oyuncunun başlangıç hedef pozisyonu mevcut pozisyonudur
        this.targetX = player.getX();
        this.targetY = player.getY();

        // Aktif tuşları takip etmek için Set
        activeKeys = new HashSet<>();

        // Smooth hareket için zamanlayıcı
        movementTimer = new Timer(10, e -> moveSmoothly());
        movementTimer.start();
    }
    // Oyuncuyu başlangıç konumuna geri götüren metodu değiştirin
    public void resetToInitialPosition(int initialX, int initialY) {
        // Hedef pozisyonu başlangıç konumuna ayarlayın
        this.targetX = initialX;
        this.targetY = initialY;

        // Aktif tuşları temizleyin ki hareket engellensin
        activeKeys.clear();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int unit = player.getWidth(); // Hareket birimi, her adımda hareket edeceği mesafe

        // Tuş zaten aktifse işlem yapma
        if (activeKeys.contains(keyCode)) {
            return;
        }

        // Tuşu aktif olarak işaretle
        activeKeys.add(keyCode);

        // Oyuncunun hedef pozisyonunu ayarla
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
            targetY -= unit;
        } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
            targetY += unit;
        } else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
            targetX -= unit;
        } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
            targetX += unit;
        }

        // Hedef pozisyonun sınırların dışına çıkmamasını sağla
        if (targetX < 0) targetX = 0; // Sol sınır
        if (targetY < 0) targetY = 0; // Üst sınır
        if (targetX + player.getWidth() > worldWidth) targetX = worldWidth - player.getWidth(); // Sağ sınır
        if (targetY + player.getHeight() > worldHeight) targetY = worldHeight - player.getHeight(); // Alt sınır
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Tuş serbest bırakıldığında aktif tuşlardan çıkar
        activeKeys.remove(e.getKeyCode());
    }

    // Smooth hareketi gerçekleştiren metod
    private void moveSmoothly() {
        int currentX = player.getX();
        int currentY = player.getY();

        // X pozisyonu için hareket
        if (currentX < targetX) {
            currentX += step;
            if (currentX > targetX) currentX = targetX; // Hedefi aşma
        } else if (currentX > targetX) {
            currentX -= step;
            if (currentX < targetX) currentX = targetX; // Hedefi aşma
        }

        // Y pozisyonu için hareket
        if (currentY < targetY) {
            currentY += step;
            if (currentY > targetY) currentY = targetY; // Hedefi aşma
        } else if (currentY > targetY) {
            currentY -= step;
            if (currentY < targetY) currentY = targetY; // Hedefi aşma
        }

        // Oyuncunun pozisyonunu güncelle
        player.setLocation(currentX, currentY);
    }
    public void resetTarget() {
    targetX = player.getX();
    targetY = player.getY();
}



}
