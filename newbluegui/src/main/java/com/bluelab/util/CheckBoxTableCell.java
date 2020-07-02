package com.bluelab.util;


import com.bluelab.job.Job;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;


public class CheckBoxTableCell extends TableCell<Job, Boolean> {

    private CheckBox checkBox;

    public CheckBoxTableCell() {
        checkBox = new CheckBox();
        checkBox.setStyle("-fx-opacity: 1");

        setGraphic(checkBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setDisable(true);
    }

    @Override
    public void updateItem(Boolean item, boolean empty) {
        if (empty) {
            setGraphic(null);
        }
        else {
            checkBox.setSelected(item);
            setGraphic(checkBox);
        }
    }
}
