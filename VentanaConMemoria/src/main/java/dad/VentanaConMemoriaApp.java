package dad;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Slider;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class VentanaConMemoriaApp extends Application {

    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private DoubleProperty with = new SimpleDoubleProperty();
    private DoubleProperty high = new SimpleDoubleProperty();
    private IntegerProperty red = new SimpleIntegerProperty();
    private IntegerProperty green = new SimpleIntegerProperty();
    private IntegerProperty blue = new SimpleIntegerProperty();

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("Iniciando");

        File profileFolder = new File(System.getProperty("user.home"));
        File configFolder = new File(profileFolder, ".VentanaConMemoria");
        File configFile = new File(configFolder, "config.properties");

        if (configFile.exists()) {
            FileInputStream fis = new FileInputStream(configFile);
            Properties prop = new Properties();
            prop.load(fis);
            with.set(Double.parseDouble(prop.getProperty("size.width")));
            high.set(Double.parseDouble(prop.getProperty("size.height")));
            x.set(Double.parseDouble(prop.getProperty("location.x")));
            y.set(Double.parseDouble(prop.getProperty("location.y")));
        } else {
            with.set(320);
            high.set(200);
            x.set(0);
            y.set(0);
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Slider redslider = new Slider(0, 255, 0);
        redslider.setShowTickLabels(true);
        redslider.setMajorTickUnit(255);
        redslider.setMinorTickCount(5);

        Slider greenslider = new Slider(0, 255, 0);
        greenslider.setShowTickLabels(true);
        greenslider.setMajorTickUnit(255);
        greenslider.setMinorTickCount(5);

        Slider blueslider = new Slider(0, 255, 0);
        blueslider.setShowTickLabels(true);
        blueslider.setMajorTickUnit(255);
        blueslider.setMinorTickCount(5);

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(redslider, greenslider, blueslider);

        Scene scene = new Scene(root, with.get(), high.get());
        primaryStage.setX(x.get());
        primaryStage.setY(y.get());
        primaryStage.setTitle("Ventana con memoria");
        primaryStage.setScene(scene);
        primaryStage.show();


        x.bind(primaryStage.xProperty());
        y.bind(primaryStage.yProperty());
        with.bind(primaryStage.widthProperty());
        high.bind(primaryStage.heightProperty());

        redslider.valueProperty().addListener((obs, oldVal, newVal) -> {
            red.set(newVal.intValue());
            updateBackground(root);
        });

        greenslider.valueProperty().addListener((obs, oldVal, newVal) -> {
            green.set(newVal.intValue());
            updateBackground(root);
        });

        blueslider.valueProperty().addListener((obs, oldVal, newVal) -> {
            blue.set(newVal.intValue());
            updateBackground(root);
        });
    }

    private void updateBackground(VBox root) {
        Color color = Color.rgb(red.get(), blue.get(), green.get());
        root.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, null)));
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Cerrado");
        File profileFolder = new File(System.getProperty("user.home"));
        File configFolder = new File(profileFolder, ".VentanaConMemoria");
        File configFile = new File(configFolder, "config.properties");

        if (!configFolder.exists()) {
            configFolder.mkdir();
        }

        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            Properties props = new Properties();
            props.setProperty("size.width", String.valueOf(with.get())); // Corrigiendo nombre de propiedad
            props.setProperty("size.height", String.valueOf(high.get())); // Corrigiendo nombre de propiedad
            props.setProperty("location.x", String.valueOf(x.get())); // Corrigiendo nombre de propiedad
            props.setProperty("location.y", String.valueOf(y.get())); // Corrigiendo nombre de propiedad
            props.store(fos, "Estado de la ventana");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
