import java.awt.*;
import javax.swing.*;

public class Player {
    private JLabel playerLabel;
    


    public Player(int x, int y, int width, int height, Color color) {
        playerLabel = new JLabel();
        playerLabel.setOpaque(true);
        playerLabel.setBackground(color);
        playerLabel.setBounds(x, y, width, height); // Oyuncunun başlangıç pozisyonu ve boyutu
        
    }

    public JLabel getLabel() {
        return playerLabel;
    }


}
