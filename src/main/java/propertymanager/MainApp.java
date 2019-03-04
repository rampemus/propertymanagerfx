package propertymanager;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainApp extends Application {
    // https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html

    // The JavaFX runtime does the following, in order, whenever an application is launched:

    // 1. Starts the JavaFX runtime, if not already started (see Platform.startup(Runnable) for more information)
    // 2. Constructs an instance of the specified Application class
    // 3. Calls the init() method
    // 4. Calls the start(javafx.stage.Stage) method
    // 5. Waits for the application to finish, which happens when either of the following occur:
    //   a) the application calls Platform.exit()
    //   b) the last window has been closed and the implicitExit attribute on Platform is true
    // 6. Calls the stop() method

    public static ObjectProperty<AppState> appState;

    @Override
    public void init() { /* ei toiminnallisuutta tässä */ }

    @Override
    public void stop() { /* ei toiminnallisuutta tässä */ }

    @Override
    public void start(Stage stage) throws Exception {
        // ladataan FXML-tiedosto propertyadder.fxml
        FXMLLoader adderLoader = new FXMLLoader(getClass().getResource("propertyadder.fxml"));
        Parent adder = adderLoader.load();
        FXMLLoader searchLoader = new FXMLLoader(getClass().getResource("propertysearch.fxml"));
        Parent searcher = searchLoader.load();

        // ei tarvita vielä, mutta tätä kautta saa controller-viittauksen
        AddPropertyController adderController = adderLoader.getController();
        PropertySearchController searcherController = searchLoader.getController();

        // Create menu with animation in the label
        Button addPropertyButton = new Button("+ property");
        addPropertyButton.setOnMouseClicked(e -> {
            appState.setValue(AppState.AddingProperty);
        });
        Button propertySearchButton = new Button("s property");
        propertySearchButton.setOnMouseClicked(e -> {
            appState.setValue(AppState.SearchingProperty);
        });
        Button exitButton = new Button("Exit");
        exitButton.setOnMouseClicked(e -> {
            appState.setValue(AppState.Exit);
        });

        //Animation!
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyy/MMM/dd HH:mm:ss");
        String hymiö = "xD";
        Label dateLabel = new Label("animation has not started");
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), actionEvent -> {
            if ( dateLabel.getText().contains(hymiö)) {
                dateLabel.setText(timeFormatter.format(LocalDateTime.now()));
            } else {
                dateLabel.setText(hymiö);
            }
        });
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.play();

        //everything in VBox
        VBox menu = new VBox(10,addPropertyButton,propertySearchButton,exitButton, dateLabel);
        menu.setAlignment(Pos.CENTER);
        menu.setPrefHeight(800);
        menu.setPrefWidth(600);

        //init appState enum
        StackPane main = new StackPane(menu, adder, searcher);
        Scene scene = new Scene(main);
        appState = new SimpleObjectProperty<>();
        appState.addListener( new ChangeListener<AppState>() {
            @Override
            public void changed(ObservableValue<? extends AppState> observableValue, AppState appState, AppState newAppState) {
                switch (newAppState) {
                    case MainMenu:
                        showStackElement(main, menu);
                        stage.setTitle("Property Manager");
                        break;
                    case AddingProperty:
                        showStackElement(main, adder);
                        stage.setTitle("Property Add Form");
                        break;
                    case SearchingProperty:
                        showStackElement(main, searcher);
                        stage.setTitle("Property Search");
                        break;
                    case Exit: Platform.exit(); break;
                }
            }
        });
        appState.setValue(AppState.MainMenu);

        stage.setScene(scene);
        stage.show();

    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Handles which pane is visible from stack
     * todo: extend StackPane and move this away from here
     * @param main
     * @param parent that will be made visible
     */
    private void showStackElement(StackPane main, Parent parent){
        int index = main.getChildren().indexOf(parent);
        for ( int i = 0; i < main.getChildren().size(); i++) {
            main.getChildren().get(i).setVisible( i == index ? true : false);
        }
    }
}