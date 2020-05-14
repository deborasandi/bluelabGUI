package util;


import alert.AlertDialog;
import database.DBJob;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import job.Job;


public class CheckBoxTableCell extends TableCell<Job, Boolean> {

    private CheckBox checkBox;

    public CheckBoxTableCell() {
        checkBox = new CheckBox();
        checkBox.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                commitEdit(checkBox.isSelected());
                updateItem(checkBox.isSelected(), false);
                Job j = (Job) getTableView().getItems().get(getTableRow().getIndex());
//                boolean old = j.isPaid();
                j.setPaid(checkBox.isSelected());
//                updateValue(j, old);
            }
        });
        setGraphic(checkBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setEditable(true);
    }
    
    @Override
    public void updateItem(Boolean item, boolean empty) {
        if (empty) {
            setGraphic(null);
        } else {
            checkBox.setSelected(item);
            setGraphic(checkBox);
        }
    }
    
    private void updateValue(Job j, boolean old) {
        if (AlertDialog.showSaveUpdate(j)) {
            DBJob.update(j.getId(), j.isPaid());
        }
        else {
            j.setPaid(old);
        }
        getTableView().refresh();
    }

}
