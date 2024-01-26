package Housemanager.UI;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class DTOViewer {


    public static <T> void displayDTOs(List<T> dtoList) {
        Stage stage = new Stage();

        VBox DTOBox = new VBox();
        DTOBox.setSpacing(10);
        DTOBox.setPadding(new Insets(10));

        for (T dto : dtoList) {
            Label label = new Label(dto.toString());
            DTOBox.getChildren().add(label);
        }

        ScrollPane scrollPane = new ScrollPane(DTOBox);

        Scene scene = new Scene(scrollPane, 500, 400);

        stage.setTitle("DTO Display");
        stage.setScene(scene);
        stage.show();
    }

    
}
