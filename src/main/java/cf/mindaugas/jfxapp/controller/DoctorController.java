package cf.mindaugas.jfxapp.controller;

import cf.mindaugas.jfxapp.model.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DoctorController implements Initializable {

    @FXML
    public TableView<Doctor> table;
    private ObservableList<Doctor> tvObservableList;
    private Session session;

    public DoctorController(Session session) {
        // In a few words: The constructor is called first,
        // then any @FXML annotated fields are populated, then initialize() is called.
        // So the constructor does NOT have access to @FXML fields referring to components defined in the .fxml file,
        // while initialize() does have access to them.
        this.session = session;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(600);
        table.setPrefHeight(300);
        tvObservableList = FXCollections.observableArrayList();
        fillTableObservableListWithSampleData();
        addButtonToTable();
    }

    private void fillTableObservableListWithSampleData() {
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Doctor";
        Query query = session.createQuery(hql);
        List results = query.list();
        tvObservableList.setAll(results);
        transaction.commit();
        // session.close();
        table.setItems(tvObservableList);
    }

    private void addButtonToTable() {
        TableColumn<Doctor, Void> colBtn = new TableColumn("Button Column");
        Callback<TableColumn<Doctor, Void>, TableCell<Doctor, Void>> cellFactory
                = new Callback<TableColumn<Doctor, Void>, TableCell<Doctor, Void>>() {
            @Override
            public TableCell<Doctor, Void> call(final TableColumn<Doctor, Void> param) {
                final TableCell<Doctor, Void> cell = new TableCell<Doctor, Void>() {
                    private final Button btn = new Button("Delete item");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Doctor doctor = getTableView().getItems().get(getIndex());
                            System.out.println("clicked on: " + doctor);
                            Transaction transaction = session.beginTransaction();
                            Doctor doctorToDelete = session.load(Doctor.class, doctor.getId());
                            session.delete(doctorToDelete);
                            transaction.commit();
                            // session.close();
                            fillTableObservableListWithSampleData();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        table.getColumns().add(colBtn);
    }
}
