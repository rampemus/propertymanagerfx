package propertymanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    @Override
    public void init() { /* ei toiminnallisuutta tässä */ }

    @Override
    public void stop() { /* ei toiminnallisuutta tässä */ }

    @Override
    public void start(Stage stage) throws Exception {
        // ladataan FXML-tiedosto propertyadder.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("propertyadder.fxml"));
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("propertysearch.fxml"));

        // MVC-mallin mukainen model-luokka FXML-lomakkeesta
        Parent root = loader.load();
        Parent root2 = loader2.load();

        // ei tarvita vielä, mutta tätä kautta saa controller-viittauksen
        AddPropertyController controller = loader.getController();
        PropertySearchController controller2 = loader2.getController();

        Scene scene = new Scene(false ? root : root2);
        stage.setTitle("Property Manager");
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
}