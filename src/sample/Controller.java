package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import sample.com.fuad.backend.ListItem;
import sample.com.fuad.database.TodoData;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;


public class Controller
{
    @FXML
    private BorderPane MainWindow;
   // private List<ListItem> viewList= new ArrayList<ListItem>();
    @FXML
    private ListView<ListItem> leftList;
    @FXML
    private Label dueLabel;
    @FXML
    private TextArea detailedList;
    @FXML
    private ContextMenu context;
    @FXML
    private ToggleButton toogle;

    private FilteredList<ListItem> filteredList;
    private Predicate<ListItem> allPredicate;
    private Predicate<ListItem> todaysPredicate;

    @FXML
    public void initialize()
    {

        context= new ContextMenu();
        MenuItem itemToDelete= new MenuItem("Delete");
        itemToDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListItem item= leftList.getSelectionModel().getSelectedItem();
                DeleteItem(item);
            }
        });
        context.getItems().setAll(itemToDelete);

       leftList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ListItem>() {
            @Override
            public void changed(ObservableValue<? extends ListItem> observable, ListItem oldValue, ListItem newValue) {
                if(newValue!=null)
                {
                    ListItem item= leftList.getSelectionModel().getSelectedItem();
                    detailedList.setText(item.getLongDescription());
                    DateTimeFormatter df= DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    dueLabel.setText("Deadline : "+df.format(item.getDeadLine()));

                }
            }
        });
        todaysPredicate=new Predicate<ListItem>() {
            @Override
            public boolean test(ListItem listItem) {
                return (listItem.getDeadLine().equals(LocalDate.now()));
            }
        };

        allPredicate= new Predicate<ListItem>() {
            @Override
            public boolean test(ListItem listItem) {
                return true;
            }
        };
       filteredList = new FilteredList<ListItem>(TodoData.getOnlyInstance().getDataList(),allPredicate);
        SortedList<ListItem> sortedList = new SortedList<ListItem>(filteredList,
                new Comparator<ListItem>() {
                    @Override
                    public int compare(ListItem o1, ListItem o2) {
                        return o1.getDeadLine().compareTo(o2.getDeadLine());
                    }
                });

        //leftList.setItems(TodoData.getOnlyInstance().getDataList());
        leftList.setItems(sortedList);
        leftList.getSelectionModel().selectFirst();
        leftList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        leftList.setCellFactory(new Callback<ListView<ListItem>, ListCell<ListItem>>() {
            @Override
            public ListCell<ListItem> call(ListView<ListItem> param) {
                ListCell<ListItem> cell = new ListCell<ListItem>()
                {
                    @Override
                    protected void updateItem(ListItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty)
                        {
                            setText(null);
                        }
                        else
                        {
                            setText(item.getShortDescription());
                            if(item.getDeadLine().equals(LocalDate.now()))
                            {
                                setTextFill(Color.BLUE);
                            }
                            else if(item.getDeadLine().equals(LocalDate.now().plusDays(1)))
                            {
                                setTextFill(Color.YELLOW);
                            }
                            else if(item.getDeadLine().equals(LocalDate.now().plusDays(2)) || item.getDeadLine().equals(LocalDate.now().plusDays(3)))
                            {
                                setTextFill(Color.GREEN);
                            }
                            else if(item.getDeadLine().isBefore(LocalDate.now()))
                            {
                                setTextFill(Color.RED);
                            }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs,wasEmpty,isNowEmpty) ->
                        {
                            if(isNowEmpty)
                                cell.setContextMenu(null);
                            else cell.setContextMenu(context);
                        }

                );


                return cell;
            }
        });
    }


    @FXML
    public void NewItemDialog()
    {

        Dialog<ButtonType> dialog= new Dialog<>();
        dialog.initOwner(MainWindow.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("DialogBox.fxml"));

        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result= dialog.showAndWait();

        if(result.isPresent() && result.get()==ButtonType.OK)
        {
            DialogBoxController controller = fxmlLoader.getController();
            ListItem item= controller.processData();
            leftList.getSelectionModel().select(item);
        }

    }



    public void onClick()
    {
        ListItem item= leftList.getSelectionModel().getSelectedItem();

        StringBuilder s= new StringBuilder(item.getLongDescription());
        DateTimeFormatter df= DateTimeFormatter.ofPattern("MMMM d, yyyy");
        dueLabel.setText("Deadline : "+df.format(item.getDeadLine()));
        detailedList.setText(s.toString());
    }
    public  void DeleteByKey(KeyEvent keyEvent)
    {
        ListItem item = leftList.getSelectionModel().getSelectedItem();
        if(item!=null && keyEvent.getCode().equals(KeyCode.DELETE))
            DeleteItem(item);
    }

    public void DeleteItem(ListItem item)
    {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Delete " +item.getShortDescription()+" " );
        alert.setContentText("Are you sure? ");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK)
        {
            TodoData.getOnlyInstance().Delete(item);
        }
    }

    @FXML
    public void ToogleAction()
    {
        if(toogle.isSelected())
        {
            filteredList.setPredicate(todaysPredicate);
        }
        else
        {
            filteredList.setPredicate(allPredicate);
        }
    }

    @FXML
    public void Exit()
    {
        Platform.exit();
    }
}
