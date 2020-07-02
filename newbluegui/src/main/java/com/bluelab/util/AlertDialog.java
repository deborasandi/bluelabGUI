package com.bluelab.util;


import java.util.Optional;

import com.bluelab.client.Client;
import com.bluelab.job.Job;
import com.bluelab.jobprice.JobPrice;
import com.bluelab.jobtype.JobType;
import com.bluelab.pricetable.PriceTable;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;


public class AlertDialog {
    
    private static boolean updateAlert(String className, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(className);
        alert.setHeaderText("Alterar " + className);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }
    
    public static boolean updateAlert(Client c) {
        String content = "Deseja alterar " + c.getName() + "?";
        boolean result = updateAlert(c.getClassName(), content);
        
        return result;
    }

    public static boolean updateAlert(PriceTable p, String old) {
        String content = "Deseja alterar tabela " + old + " para " + p.getName() + "?";
        boolean result = updateAlert(p.getClassName(), content);
        
        return result;
    }
    
    public static boolean updateAlert(JobType j, String old) {
        String content = "Deseja alterar trabalho " + old + " para " + j.getName() + "?";
        boolean result = updateAlert(j.getClassName(), content);
        
        return result;
    }
    
    public static boolean updateAlert(JobPrice j) {
        String content = "Deseja alterar " + j.getPriceTable().getName() + " - " + j.getJobType().getName() + " - "
                + j.getPrice() + "?";
        boolean result = updateAlert(j.getClassName(), content);
        
        return result;
    }
    
    public static boolean updateAlert(Job j) {
        String content = "Deseja alterar " + j.getClient().getName() + " - "
                + j.getJobPrice().getJobType().getName() + "?";
        boolean result = updateAlert(j.getClassName(), content);
        
        return result;
    }
    
    private static boolean deleteAlert(String className, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(className);
        alert.setHeaderText("Excluir " + className);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }
    
    public static boolean deleteAlert(Client c) {
        String content = "Excluir " + c.getName() + "?";
        boolean result = deleteAlert(c.getClassName(), content);
        
        return result;
    }

    public static boolean deleteAlert(PriceTable p) {
        String content = "Excluir Tabela " + p.getName() + "?";
        boolean result = deleteAlert(p.getClassName(), content);
        
        return result;
    }

    public static boolean deleteAlert(JobType j) {
        String content = "Excluir Trabalho " + j.getName() + "?";
        boolean result = deleteAlert(j.getClassName(), content);
        
        return result;
    }

    public static boolean deleteAlert(JobPrice j) {
        String content = "Excluir valor de " + j.getPriceTable().getName() + " - " + j.getJobType().getName() + "?";
        boolean result = deleteAlert(j.getClassName(), content);
        
        return result;
    }
    
    public static boolean deleteAlert(Job j) {
        String content = "Excluir " + j.getClient().getName() + " - " + j.getJobPrice().getJobType().getName() + "?";
        boolean result = deleteAlert(j.getClassName(), content);
        
        return result;
    }

    private static void successAlert(String className, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(className);
        alert.setHeaderText(content);

        alert.showAndWait();
    }
    
    public static void successAlert(JobPrice p) {
        String content = "Preços alterados com sucesso!";
        successAlert(p.getClassName(), content);
    }
    
    public static void successAlert(PriceTable p) {
        String content = "Tabela de preços inserida com sucesso!\nPreços inseridos com sucesso!";
        successAlert(p.getClassName(), content);
    }
    
    public static void successAlert(JobType p) {
        String content = "Tipo de Trabalho inserido com sucesso!\nPreços inseridos com sucesso!";
        successAlert(p.getClassName(), content);
    }
    
    public static double insertPriceAlert(JobPrice j) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Preço");
        dialog.setHeaderText(j.getJobType().getName() + " na Tabela " +  j.getPriceTable().getName() + " não possui preço cadastrado.");
        dialog.setContentText("Novo valor:");

        Optional<String> result = dialog.showAndWait();
        
        if(result.isPresent()) {
            try {
                return Double.parseDouble(result.get());
            }
            catch (NumberFormatException e) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Novo Preço");
                alert.setHeaderText("Valor Inválido");

                alert.showAndWait();
            }
        }
        
        return 0;
    }


    public static boolean saveNewAlert(Client c) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cliente");
        alert.setHeaderText("Novo cliente");
        alert.setContentText("Deseja salvar " + c.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }

    public static boolean saveNewAlert(Job j) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Trabalho");
        alert.setHeaderText("Novo Trabalho");
        alert.setContentText("Deseja salvar " + j.getClient().getName() + " - "
                + j.getJobPrice().getJobType().getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }



    public static boolean showSavePaid(Job j) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Trabalho");

        if (j.isPaid()) {
            alert.setHeaderText("Inserir Pagamento");
        }
        else {
            alert.setHeaderText("Remover Pagamento");
        }

        alert.setContentText("Deseja alterar " + j.getClient().getName() + " - "
                + j.getJobPrice().getJobType().getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK ? true : false;
    }


}
