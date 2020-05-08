package alert;


import java.util.Optional;

import client.Client;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import job.Job;
import jobprice.JobPrice;
import jobtype.JobType;
import pricetable.PriceTable;


public class AlertDialog {

    public static boolean showSaveTable(PriceTable p, String old) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Tabela");
        alert.setHeaderText("Deseja alterar tabela " + old + " para " + p.getName() + "?");

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

    public static boolean showSaveJobType(JobType j, String old) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Trabalho");
        alert.setHeaderText("Deseja alterar trabalho " + old + " para " + j.getName() + "?");

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
        alert.setHeaderText("Deseja alterar " + j.getPriceTable().getName() + " - " + j.getJob().getName() + " - "
                + j.getPrice() + "?");

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

    public static void showUpdateSuccess() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Preço");
        alert.setHeaderText("Preços Salvos com Sucesso!");

        alert.showAndWait();
    }

    public static boolean showSaveNew(Client c) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cliente");
        alert.setHeaderText("Novo cliente");
        alert.setContentText("Deseja salvar " + c.getClientName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }

    public static boolean showSaveUpdate(Client c) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cliente");
        alert.setHeaderText("Alterar cliente");
        alert.setContentText("Deseja alterar " + c.getClientName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }

    public static boolean showDelete(Client c) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cliente");
        alert.setHeaderText("Excluir cliente");
        alert.setContentText("Excluir " + c.getClientName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }

    public static boolean showSaveNew(Job j) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Trabalho");
        alert.setHeaderText("Novo Trabalho");
        alert.setContentText("Deseja salvar " + j.getClient().getClientName() + " - " + j.getJobType().getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }

    public static boolean showSaveUpdate(Job j) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Trabalho");
        alert.setHeaderText("Alterar Trabalho");
        alert.setContentText("Deseja alterar " + j.getClient().getClientName() + " - " + j.getJobType().getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }

    public static boolean showDelete(Job j) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Trabalho");
        alert.setHeaderText("Excluir Trabalho");
        alert.setContentText("Excluir " + j.getClient().getClientName() + " - " + j.getJobType().getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }
}
