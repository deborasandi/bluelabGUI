package alert;


import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import jobprice.JobPrice;
import jobtype.JobType;
import pricetable.PriceTable;


public class AlertDialog {

    public static boolean showSaveTable(PriceTable p) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Tabela");
        alert.setHeaderText("Salvar Tabela " + p.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }

    public static boolean showDeleteTable(PriceTable p) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Tabela");
        alert.setHeaderText("Excluir Tabela " + p.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }

    public static boolean showSaveJobType(JobType j) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Trabalho");
        alert.setHeaderText("Salvar Trabalho " + j.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }

    public static boolean showDeleteJobType(JobType j) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Trabalho");
        alert.setHeaderText("Excluir Trabalho " + j.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }

    public static boolean showSavePrice(JobPrice j) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Preço");
        alert.setHeaderText(
                "Salvar " + j.getPriceTable().getName() + " - " + j.getJob().getName() + " - " + j.getPrice() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }

    public static boolean showDeletePrice(JobPrice j) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Preço");
        alert.setHeaderText(
                "Excluir " + j.getPriceTable().getName() + " - " + j.getJob().getName() + " - " + j.getPrice() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }
}
