package propertymanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class AddPropertyController {

    static PropertyGenerator writableData;

    @FXML
    private ImageView kuva;

    @FXML
    private TextField tiedosto;

    @FXML
    private TextField nimi;

    @FXML
    private TextField osoite;

    @FXML
    private TextField rakennusvuosi;

    @FXML
    private TextField neliomaara;

    @FXML
    private TextField vuokra;

    @FXML
    private TextField muutehdot;

    @FXML
    private ChoiceBox<?> kuntoluokitus;

    @FXML
    private TextArea kohteenkuvaus;

    @FXML
    private TextField sahkoposti;

    public static void initData(PropertyGenerator data) {
        writableData = data;
        System.out.println("Data initialized");
    }

    @FXML
    void addProperty(ActionEvent event) {

    }

    @FXML
    void browseImage(ActionEvent event) {

    }

    @FXML
    void cancelPropertyAdd(ActionEvent event) {
        MainApp.appState.set(AppState.MainMenu);
    }

}
