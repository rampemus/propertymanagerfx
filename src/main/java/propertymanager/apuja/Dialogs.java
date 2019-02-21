package propertymanager.apuja;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class Dialogs {
    public static void warning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void browseImage(Consumer<String> callback, String type, List<String> fileExtensions, Stage stage) {
        FileChooser f = new FileChooser();
        f.setTitle("Valitse " + type.toLowerCase());
        f.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(type+"t ("+String.join(" ", fileExtensions)+")", fileExtensions)
        );

        File result = f.showOpenDialog(stage);

        String filePath = "?";
        try {
            filePath = result.toURI().toURL().toExternalForm();
            System.out.println("DEBUG: got "+filePath);
            callback.accept(filePath);
        }
        catch(Exception e) {
            Dialogs.warning("Tiedoston lataus", "Tiedoston "+filePath+" lataus epäonnistui", e.getMessage());
        }
    }
}