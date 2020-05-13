package util;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import job.Job;

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
            ImageView i = new ImageView(new Image(getClass().getResource("../icons/check.png").toExternalForm()));
            i.setFitWidth(15.0);
            i.setFitHeight(15.0);
            setGraphic(i);
        }
    }
}
