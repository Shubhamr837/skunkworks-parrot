package Controller;

import Data.*;
import Model.Group;
import Model.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NotificationGroupController implements Initializable {

    private Group group;
    @FXML
    private
    TableView tableView;
    @FXML private Label groupLink;
    ObservableList observableList = FXCollections.observableArrayList();
    @FXML private Button clipboardButton;
    @FXML private Button showQRButton;


    NotificationGroupController(Group group) {
        this.group = group;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        groupLink.setText(group.getGroupLink());
        setListView();
    }

    private void setListView()
    {
        DatabaseCommunicator dc = new DatabaseCommunicator();

        ObservableList<Object> notificationSet = FXCollections.observableArrayList(dc.getNotificationsList(group.getId()));

        TableColumn<Notification, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(150);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Notification, String> messageColumn = new TableColumn<>("Message");
        messageColumn.setMinWidth(300);
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));

        TableColumn<Notification, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(150);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_str"));


        tableView.setItems(notificationSet);
        tableView.getColumns().clear();
        tableView.getColumns().addAll(titleColumn,messageColumn,dateColumn);

    }

    public void copyToClipboardClicked(MouseEvent mouseEvent) {
        if(group.getGroupLink()!=null){
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(group.getGroupLink());
            clipboard.setContent(content);
            clipboardButton.setText("Copied");
        }
    }

    public void showQRButtonClicked(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/QRCodeBox.fxml"));
        fxmlLoader.setController(new QRCodeBoxController(group));
        Stage stage = new Stage();
        stage.setTitle("Scan the QR Code");
        try {
            stage.setScene(new Scene(fxmlLoader.load(),392,328));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
    }


}
