package com.bluelab.util;


import java.text.NumberFormat;
import java.util.Locale;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;


public class RealTableCell<T> extends TextFieldTableCell<T, Double> {

    private static Locale ptBr = new Locale("pt", "BR");

    public RealTableCell(boolean editable) {
        super(new DoubleStringConverter());
        setEditable(editable);
    }

    @Override
    public void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null) {
            setGraphic(null);
            setText("");
        }
        else {
            setText(NumberFormat.getCurrencyInstance(ptBr).format(item));
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        updateItem(this.getItem(), false);
    }

    @Override
    public void commitEdit(Double newValue) {
        super.commitEdit(newValue);
    }
   
    
}
