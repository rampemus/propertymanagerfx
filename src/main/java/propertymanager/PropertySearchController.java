package propertymanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;

public class PropertySearchController {

    @FXML
    private Label tulostieto;

    @FXML
    private ProgressBar eteneminen;

    @FXML
    private TextField nimi;

    @FXML
    private TextField osoite;

    @FXML
    private TextField rakennusvuosi1;

    @FXML
    private TextField rakennusvuosi2;

    @FXML
    private TextField neliomaara1;

    @FXML
    private TextField neliomaara2;

    @FXML
    private TextField vuokra1;

    @FXML
    private TextField vuokra2;

    @FXML
    private ChoiceBox<?> kuntoluokitus;

    @FXML
    private TilePane tulokset;

    @FXML
    void cancelPropertyAdd(ActionEvent event) {

    }

    @FXML
    void generateApartments(ActionEvent event) {

    }

    @FXML
    void searchProperty(ActionEvent event) {

    }

}
