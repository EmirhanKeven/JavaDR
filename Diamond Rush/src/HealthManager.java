import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class HealthManager {
    private int health; // Oyuncunun canı
    private int initialX; // Oyuncunun başlangıç X konumu
    private int initialY; // Oyuncunun başlangıç Y konumu
    private JLabel playerLabel; // Oyuncunun JLabel referansı
    private boolean invincible; // Geçici olarak hasar alınamaz durumu
    private Timer invincibilityTimer; // Çarpışma sonrası koruma süresi
    private PlayerMovement playerMovement;

    public HealthManager(int initialHealth, int initialX, int initialY, JLabel playerLabel, PlayerMovement playerMovement) {
        this.health = initialHealth;
        this.initialX = initialX;
        this.initialY = initialY;
        this.playerLabel = playerLabel;
        this.invincible = false;
        this.playerMovement = playerMovement;
    }

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
            resetPlayerPosition();   // Pozisyon sıfırlama
        }
    }

    // Oyuncunun başlangıç pozisyonuna döndür
    public void resetPlayerPosition() {
        playerLabel.setBounds(initialX, initialY, playerLabel.getWidth(), playerLabel.getHeight());

        // PlayerMovement referansını ekleyin (World sınıfında)
        if (playerMovement != null) {
            playerMovement.resetToInitialPosition(initialX, initialY);
        }

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

        // Oyunun kapanması
        JOptionPane.showMessageDialog(null, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0); // Uygulamayı tamamen kapat
    }

    // Oyuncunun canını öğrenmek için
    public int getHealth() {
        return health;
    }

    public boolean isInvincible() {
        return invincible;
    }
}
