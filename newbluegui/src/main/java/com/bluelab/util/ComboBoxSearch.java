package com.bluelab.util;


import com.jfoenix.controls.JFXComboBox;

import javafx.collections.ObservableList;


public class ComboBoxSearch<T> extends JFXComboBox<T> {

    private String stringSearch;

    public ComboBoxSearch() {
        super();

        this.setOnMouseClicked(event -> {
            stringSearch = "";
        });

        this.setOnKeyReleased(event -> {
            stringSearch += event.getText();

            T s = jumpTo(stringSearch, getValue(), getItems());
            if (s != null) {
                setValue((T) s);
            }
        });
    }

    private T jumpTo(String keyPressed, T t, ObservableList<T> observableList) {
        String key = keyPressed.toUpperCase();

        if (key.matches("^[A-Z]+")) {
            boolean letterFound = false;

            for (T s : observableList) {
                if (s.toString().toUpperCase().startsWith(key)) {
                    letterFound = true;
                    return (T) s;
                }
            }

            if (letterFound) {
                return jumpTo(keyPressed, null, observableList);
            }
        }
        return null;
    }
}
