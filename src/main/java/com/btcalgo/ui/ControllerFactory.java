package com.btcalgo.ui;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.io.InputStream;

public class ControllerFactory {

    public SinglePageController createController(String url) throws IOException {
        InputStream fxmlStream = null;
        try {
            fxmlStream = getClass().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(url));
            loader.load(fxmlStream);
            return loader.getController();
        } finally {
            if (fxmlStream != null) {
                fxmlStream.close();
            }
        }
    }
}
