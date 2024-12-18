import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// GameObjectWrapper sınıfı: JLabel'i GameObject ile birlikte tutar
class GameObjectWrapper {
    JLabel label;       // Görsel bileşen
    GameObject object;  // Mantıksal bileşen (çarpışma sınırları)

    public GameObjectWrapper(JLabel label, GameObject object) {
        this.label = label;
        this.object = object;
    }
}

public class World {
    private JFrame frame;
    private JPanel levelPanel;
    private JLabel player;

    private Camera camera;
    private int worldWidth = 3840; // Oyun dünyası genişliği
    private int worldHeight = 2160; // Oyun dünyası yüksekliği

    private ArrayList<GameObjectWrapper> objects; // Objeleri tutan liste

    public World() {
        frame = new JFrame("Diamond Rush - Level 1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);

        objects = new ArrayList<>(); // Objeler listesi

        levelPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, worldWidth, worldHeight); // Tüm dünya boyutunu doldur
            }
        };
        levelPanel.setLayout(null);
        levelPanel.setPreferredSize(new Dimension(worldWidth, worldHeight));
        levelPanel.setBounds(0, 0, worldWidth, worldHeight);
        frame.add(levelPanel);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        camera = new Camera(screenSize.width, screenSize.height, worldWidth, worldHeight);

        addPlayer();
        addObject(0, 0, 50, 1000, Color.GREEN, "WALL");
        addObject(0, 0, 10000, 50, Color.BLUE, "CEILING");
        addObject(0, 832, 10000, 50, Color.RED, "FLOOR");

        PlayerMovement playerMovement = new PlayerMovement(player, levelPanel, worldWidth, worldHeight);
        levelPanel.addKeyListener(playerMovement);

        levelPanel.setFocusable(true);
        levelPanel.requestFocusInWindow();

        Timer timer = new Timer(16, e -> {
            updateCamera();
            checkCollisions();
        });
        timer.start();

        frame.pack();
        frame.setVisible(true);
    }

    private void addPlayer() {
        player = new JLabel();
        player.setOpaque(true);
        player.setBackground(Color.RED);
        player.setBounds(500, 500, 20, 20);
        levelPanel.add(player);
    }

    private void addObject(int x, int y, int width, int height, Color color, String name) {
        JLabel newLabel = new JLabel();
        newLabel.setOpaque(true);
        newLabel.setBackground(color);
        newLabel.setBounds(x, y, width, height);

        GameObject newObject = new GameObject(name, x, y, width, height);
        objects.add(new GameObjectWrapper(newLabel, newObject));

        levelPanel.add(newLabel);
    }

    private void updateCamera() {
        camera.update(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);

        int cameraWidth = 1920;
        int cameraHeight = 1080;

        int x = Math.max(0, Math.min(camera.getX(), worldWidth - cameraWidth));
        int y = Math.max(0, Math.min(camera.getY(), worldHeight - cameraHeight));

        levelPanel.setLocation(-x, -y);
    }

    private void checkCollisions() {
        Rectangle playerBounds = player.getBounds();

        for (GameObjectWrapper objWrapper : objects) {
            if (playerBounds.intersects(objWrapper.object.getBounds())) {
                onCollision(objWrapper.object); // Çarpışma olduğunda çağır
            }
        }
    }

    private void onCollision(GameObject object) {
        switch (object.getName()) {
            case "WALL":
                System.out.println("Collision with WALL at: " + object.getX() + ", " + object.getY());
                break;
            case "CEILING":
                System.out.println("Collision with CEILING at: " + object.getX() + ", " + object.getY());
                break;
            case "FLOOR":
                System.out.println("Collision with FLOOR at: " + object.getX() + ", " + object.getY());
                break;
            default:
                System.out.println("Collision with UNKNOWN object!");
                break;
        }
    }

    public static void main(String[] args) {
        new World();
    }
}
