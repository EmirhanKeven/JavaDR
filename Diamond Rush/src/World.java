    import java.awt.*;
    import java.util.ArrayList;
    import javax.swing.*;


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
        private ArrayList<Enemy> enemies; // Düşmanları tutan liste
        private HealthManager healthManager; // Sağlık yöneticisi
        private PlayerMovement playerMovement;
        


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

            enemies = new ArrayList<>(); // Düşman listesi

            // Düşmanları ekle (yalnızca bir eksende hareket)
            addEnemy(200, 300, 40, 40, Color.BLACK, 200, 400, false); // X ekseni hareketi
            addEnemy(500, 300, 40, 40, Color.MAGENTA, 300, 500, true); // Y ekseni hareketi

            Timer enemyMovementTimer = new Timer(50, e -> moveEnemies());  // 50ms'de bir düşman hareketi

            enemyMovementTimer.start();


            Timer collisionCheckTimer = new Timer(16, e -> checkCollisionsWithEnemies());
            collisionCheckTimer.start();

            playerMovement = new PlayerMovement(player, levelPanel, worldWidth, worldHeight);
            levelPanel.addKeyListener(playerMovement);

            // HealthManager oluştururken PlayerMovement referansını ekleyin
            healthManager = new HealthManager(3, 250, 500, player, playerMovement);

            
        }

        private void addPlayer() {
            player = new JLabel();
            player.setOpaque(true);
            player.setBackground(Color.RED);
            player.setBounds(500, 500, 20, 20);
            healthManager = new HealthManager(3, 250, 500, player,playerMovement);
            levelPanel.add(player);

            //levelPanel.setComponentZOrder(player, 0);
            // oldumu

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

        private void addEnemy(int x, int y, int width, int height, Color color, int min, int max, boolean isVertical) {
            Enemy enemy = new Enemy(x, y, width, height, color, min, max, isVertical);
            enemies.add(enemy);
            levelPanel.add(enemy.getLabel());
        }

        private void moveEnemies() {
            for (Enemy enemy : enemies) {
                enemy.move();
            }
        }

        private void checkCollisionsWithEnemies() {
            if (healthManager.getHealth() > 0) {
                Rectangle playerBounds = player.getBounds();
            
                for (Enemy enemy : enemies) {
                    if (playerBounds.intersects(enemy.getLabel().getBounds())) {
                        // Sadece bir kez çarpışma tespit edildiğinde sağlık azaltılmalı ve pozisyon sıfırlanmalı
                        if (!healthManager.isInvincible()) { // Eğer invincibility durumu aktif değilse
                            healthManager.reduceHealth();
                            break; // Tek bir çarpışma algılandığında döngüyü sonlandır
                        }
                    }
                }
            }
        }
        
        

        private void onPlayerCollisionWithEnemy() {
            System.out.println("Player collided with an enemy!");
            healthManager.reduceHealth(); // Sağlık azaltılır ve gerekirse oyuncu sıfırlanır
        }
        

        public static void main(String[] args) {
            new World();
        }
    }
