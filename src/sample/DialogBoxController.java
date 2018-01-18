package sample;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.com.fuad.backend.ListItem;
import sample.com.fuad.database.TodoData;

import java.time.LocalDate;

public class DialogBoxController {

    @FXML
    private TextField shortDescription;
    @FXML
    private TextArea details;
    @FXML
    private DatePicker deadline;


    @FXML
    public ListItem processData()
    {
        String sDesc= shortDescription.getText().trim();
        String longDesc= details.getText().trim();
        LocalDate date = deadline.getValue();
        ListItem item =new ListItem(sDesc,longDesc,date);
        TodoData.getOnlyInstance().addToList(item);
        return item;
    }
}
