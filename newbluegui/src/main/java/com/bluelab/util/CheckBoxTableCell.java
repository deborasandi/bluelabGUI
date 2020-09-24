package com.bluelab.util;

import com.bluelab.database.DBJob;
import com.bluelab.database.DBPayment;
import com.bluelab.job.Job;
import com.bluelab.payment.Payment;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

public class CheckBoxTableCell extends TableCell<Job, Boolean> {

	private CheckBox checkBox;
	private boolean value = false;

	public CheckBoxTableCell(boolean disable) {
		checkBox = new CheckBox();
		checkBox.setStyle("-fx-opacity: 1");

		checkBox.setOnMouseClicked(e -> {
			commitEdit();
		});

		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				value = newValue;
			}
		});

		setGraphic(checkBox);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		setDisable(disable);
		setEditable(true);
	}

	@Override
	public void startEdit() {
		super.startEdit();
		if (isEmpty()) {
			return;
		}
		checkBox.requestFocus();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
	}

	public void commitEdit() {
		super.commitEdit(value);

		Job j = this.getTableRow().getItem();
		j.setPaid(value);

		DBJob.update(j.getId(), value);

		if (value) {
			DBPayment.insert(new Payment(j.getClient(), j, DateUtil.dateNow()));
		} else {
			DBPayment.delete(j.getId());
		}
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
}
