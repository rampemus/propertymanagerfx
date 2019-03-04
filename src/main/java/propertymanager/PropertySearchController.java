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

        initFilters();
    }

    @FXML
    void cancelPropertyAdd(ActionEvent event) {
        MainApp.appState.set(AppState.MainMenu);
    }

    @FXML
    void generateApartments(ActionEvent event) {
        generator.luoAsuntoja(20);
    }

    private void initListeners() {
        updateFilterOnKeyTyped(nimi, osoite, rakennusvuosi1, rakennusvuosi2, neliomaara1, neliomaara2, vuokra1, vuokra2);

        search.setOnMouseClicked(e -> {
            for ( int i = 0; i < filteredData.size(); i++) {
                System.out.println(filteredData.get(i).getNimi());
            }
        });

        //update tulostieto
        filteredData.addListener(new ListChangeListener<Property>() {
            @Override
            public void onChanged(Change<? extends Property> change) {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        tulostieto.setText("Tuloksia: " + filteredData.size() + " kpl");
                    }
                });

            }
        });

        data.addListener(new ListChangeListener<Property>() {
            @Override
            public void onChanged(Change<? extends Property> c) {
                while (c.next()) {
                    //only checks if something is added, doesn't understand if property is removed
                    for ( int i = c.getFrom(); i < c.getTo(); i++) {
                        Property added = data.get(i);
                        System.out.println("There is new property called: " + added.getNimi());

                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                addPropertyCardToTulokset(added);
                            }
                        });

                    }
                }
            }
        });


    }

    private void initFilters() {
        TextFieldFilter.setIntegerFilter(rakennusvuosi1);
        TextFieldFilter.setIntegerFilter(rakennusvuosi2);
        TextFieldFilter.setFloatNumberFilter(neliomaara1);
        TextFieldFilter.setFloatNumberFilter(neliomaara2);
        TextFieldFilter.setIntegerFilter(vuokra1);
        TextFieldFilter.setIntegerFilter(vuokra2);
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

            /**
             * When filtered data is changed the propertyCard needs
             * to hide itself. When propertyCard is visible again
             * it will pop back to top of the list.
             *  As a side effect this will also order the propertyCards
             * into filteredData's reverse order for future use...
             */
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
        if (nimiResultFilter(property) && osoiteResultFilter(property)) {
            try {
                if ( rakennusvuosiFilter(property)
                        && vuokraFilter(property)
                        && neliomaaraFilter(property)) {
                    return true;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private boolean nimiResultFilter(Property property ) {
        return nimi.getText().length() == 0 || property.getNimi().toLowerCase().contains(nimi.getText().toLowerCase());
    }

    private boolean osoiteResultFilter(Property property) {
        return osoite.getText().length() == 0 || property.getOsoite().toLowerCase().contains(osoite.getText().toLowerCase());
    }

    private boolean rakennusvuosiFilter(Property property) {
        return (rakennusvuosi1.getText().length() == 0 || property.getRakennusvuosi() > Integer.parseInt(rakennusvuosi1.getText()))
                && (rakennusvuosi2.getText().length() == 0 || property.getRakennusvuosi() < Integer.parseInt(rakennusvuosi2.getText()));
    }

    private boolean neliomaaraFilter(Property property) {
        return (neliomaara1.getText().length() == 0 || property.getNeliömäärä() > Float.parseFloat(neliomaara1.getText()))
                && (neliomaara2.getText().length() == 0 || property.getNeliömäärä() < Float.parseFloat(neliomaara2.getText()));
    }

    private boolean vuokraFilter(Property property) {
        return (vuokra1.getText().length() == 0 || property.getVuokra() > Integer.parseInt(vuokra1.getText()))
                && (vuokra2.getText().length() == 0 || property.getVuokra() < Integer.parseInt(vuokra2.getText()));
    }

    /**
     * makes filteredData to update itself on keytyped
     * @param textFields all textfields that need to be reactive
     */
    private void updateFilterOnKeyTyped(TextField... textFields) {
        for ( int i = 0; i < textFields.length; i++) {
            textFields[i].setOnKeyTyped(e -> {
                filteredData.setPredicate(property -> searchResultFilter(property));
            });
        }
    }
}
