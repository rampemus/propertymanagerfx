package propertymanager;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

public class PropertySearchController {

    public ObservableList<Property> data;
    public FilteredList<Property> filteredData;
    private HashMap<Property,Node> propertyNodes;

    private PropertyGenerator generator;

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
    private Button search;

    @FXML
    private TilePane tulokset;

    @FXML
    void initialize() {
        generator = new PropertyGenerator(null);
        AddPropertyController.initData(generator);
        data = generator.annaAsunnot();
        filteredData = new FilteredList<Property>(data, p -> true);

        tulokset.setAlignment(Pos.TOP_CENTER);
        propertyNodes = new HashMap<>();

        initListeners();
    }

    @FXML
    void cancelPropertyAdd(ActionEvent event) {
        MainApp.appState.set(AppState.MainMenu);
    }

    @FXML
    void generateApartments(ActionEvent event) {

    }

    private void addPropertyCardToTulokset(Property p) {
        FXMLLoader propertyLoader = new FXMLLoader(getClass().getResource("searchitem.fxml"));
        try {
            Node propertyCard = propertyLoader.load();
//            propertyNodes.put(p,propertyCard);  //save propertyCard UI element reference
                                                // to change their visibility later

            String imageName = p.getKuvaTiedosto();
            Image image = new Image(getClass().getResourceAsStream(imageName));
            ImageView imageView = ((ImageView)propertyCard.lookup("#kuva"));
            imageView.setImage(image);

            Label kohde = ((Label)propertyCard.lookup("#kohde"));
            kohde.setText(p.getNimi());
            Label kuvaus2 = ((Label)propertyCard.lookup("#kuvaus2"));
            kuvaus2.setText(p.toString());

            tulokset.getChildren().add(propertyCard);

            filteredData.addListener(new ListChangeListener<Property>() {
                @Override
                public void onChanged(Change<? extends Property> change) {
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            //todo: instead this should use change-object
                            if ( filteredData.contains(p)) {
                                propertyCard.setVisible(true);
                                propertyCard.toBack();
                            } else {
                                propertyCard.setVisible(false);
                            }
                        }
                    });

                }
            });


        } catch (IOException e ) {
            e.printStackTrace();
        }
    }

    private boolean searchResultFilter(Property property) {
        if (property.getNimi().toLowerCase().contains(nimi.getText().toLowerCase())) {
            return true;
        }
        return false;
    }

    private void hideAllPropertyCards() {
        Collection<Node> cards = propertyNodes.values();
        for ( Node card : cards) {
            card.setVisible(false);
        }
    }
}
