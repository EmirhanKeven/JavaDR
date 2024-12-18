import javax.swing.JLabel;
import javax.swing.Timer;

public class HealthManager {
    private int health; // Oyuncunun canı
    private int initialX; // Oyuncunun başlangıç X konumu
    private int initialY; // Oyuncunun başlangıç Y konumu
    private JLabel playerLabel; // Oyuncunun JLabel referansı
    private boolean invincible; // Geçici olarak hasar alınamaz durumu
    private Timer invincibilityTimer; // Çarpışma sonrası koruma süresi

    public HealthManager(int initialHealth, int initialX, int initialY, JLabel playerLabel) {
        this.health = initialHealth;
        this.initialX = initialX;
        this.initialY = initialY;
        this.playerLabel = playerLabel;
        this.invincible = false;
    }

    // Oyuncunun canını azalt
    public void reduceHealth() {
        if (invincible) {
            return; // Eğer geçici olarak hasar alınamıyorsa, işlem yapma
        }

        health--;
        System.out.println("Player health: " + health);

        if (health <= 0) {
            System.out.println("Player is dead!");
            restartGame();
        } else {
            activateInvincibility(); // Çarpışma sonrası koruma başlat
            resetPlayerPosition();
        }
    }

    // Oyuncunun başlangıç pozisyonuna döndür
    public void resetPlayerPosition() {
        playerLabel.setBounds(initialX, initialY, playerLabel.getWidth(), playerLabel.getHeight());
        playerLabel.getParent().repaint();
    }

    // Çarpışma sonrası koruma süresi başlat
    private void activateInvincibility() {
        invincible = true; // Hasar alınamaz duruma geç
        if (invincibilityTimer != null) {
            invincibilityTimer.stop();
        }

        invincibilityTimer = new Timer(2000, e -> invincible = false); // 2 saniye koruma
        invincibilityTimer.setRepeats(false);
        invincibilityTimer.start();
    }

    // Oyunun yeniden başlatılması
    private void restartGame() {
        health = 3; // Oyuncunun canını sıfırla
        resetPlayerPosition(); // Oyuncunun pozisyonunu sıfırla
        System.out.println("Game restarting...");
        
        // Buraya oyunun tekrar başlatılmasıyla ilgili kodları ekleyebilirsiniz
    }

    // Oyuncunun canını öğrenmek için
    public int getHealth() {
        return health;
    }
}