package inf101.v17.boulderdash.gui;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * The main class of the BoulderDash program. Sets up a simple gui of a given
 * map and handles the timing.
 */
public class BoulderDashGUI extends Application implements EventHandler<KeyEvent> {
    public static final double NOMINAL_WIDTH = 1900;
    public static final double NOMINAL_HEIGHT = 1000;
    /**
     * Determines how many milliseconds pass between two steps of the program.
     */
    private static final int SPEED = 120;
    private static BDMap theMap;
    private Stage stage;
    private BDMap map;
    private BDMapComponent mapComponent;
    private Text message;
    private AnimationTimer timer;
    private Paint background;

    public BoulderDashGUI() {
        this.map = theMap;
    }

    /**
     * Runs the program on a given map.
     *
     * @param map
     */
    public static void run(BDMap map) {
        theMap = map;
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        double spacing = 10;

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        Group root = new Group();
        double width = Math.min(primaryScreenBounds.getWidth() - 40, map.getWidth() * 200);
        double height = Math.min(primaryScreenBounds.getHeight() - 100, map.getWidth() * 200);
        Scene scene = new Scene(root, width, height, this.background);
        stage.setScene(scene);

        message = new Text(10, 0, "");
        message.setFont(new Font(20));
        message.setFill(Color.WHITE);
        message.setText("Diamonds: " + map.getPlayer().numberOfDiamonds() + "    " + "Time: " + map.getTimeLeft());

        mapComponent = new BDMapComponent(map);
        mapComponent.widthProperty().bind(scene.widthProperty());
        mapComponent.heightProperty().bind(scene.heightProperty());
        mapComponent.heightProperty().bind(Bindings.subtract(Bindings.subtract(scene.heightProperty(),
                message.getLayoutBounds().getHeight()), spacing));
        // mapComponent.setScaleY(-1.0);


        timer = new AnimationTimer() {

            private long lastUpdateTime;

            @Override
            public void handle(long now) {
                if (now - lastUpdateTime > SPEED * 1_000_000) {
                    lastUpdateTime = now;
                    step();
                }
            }

        };

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().add(message);
        vbox.getChildren().add(mapComponent);
        root.getChildren().add(vbox);

        stage.addEventHandler(KeyEvent.KEY_PRESSED, this);
        timer.start();
        stage.show();
    }

    protected void step() {
        if (map.getPlayer().isAlive() && !map.getFinished()) {
            map.step();
            message.setText("Diamonds: " + map.getPlayer().numberOfDiamonds() + "    " + "Time: " + map.getTimeLeft());
        } else if (map.getFinished()) {
            message.setText("Finished with: " + map.getPlayerPoints());
        } else {
            message.setText("Player is dead.");
        }
        mapComponent.draw();
    }

    @Override
    public void handle(KeyEvent event) {
        KeyCode code = event.getCode();
        if (code == KeyCode.ESCAPE || code == KeyCode.Q) {
            System.exit(0);
        } else if (code == KeyCode.F) {
            stage.setFullScreen(!stage.isFullScreen());
        } else {
            map.getPlayer().keyPressed(code);
        }
    }
}
