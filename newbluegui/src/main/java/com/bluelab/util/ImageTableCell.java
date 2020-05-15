package com.bluelab.util;

import com.bluelab.job.Job;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageTableCell extends TableCell<Job, Boolean>{

    
    public ImageTableCell() {
        
    }
    
    @Override
    protected void updateItem(Boolean value, boolean empty) {                      
        super.updateItem(value, empty);
        if(empty){
            return;
        }

        if(value) {
            // TODO tem que ver como faz pra pegar esse icone ai
            ImageView i = new ImageView(new Image(getClass().getResource("../../../icons/check.png").toExternalForm()));
            i.setFitWidth(15.0);
            i.setFitHeight(15.0);
            setGraphic(i);
        }
    }
}
