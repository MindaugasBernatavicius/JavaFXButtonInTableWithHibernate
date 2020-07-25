package cf.mindaugas.jfxapp;

import cf.mindaugas.jfxapp.model.Doctor;
import cf.mindaugas.jfxapp.repository.DoctorRepository;
import cf.mindaugas.jfxapp.service.DoctorSearchService;
import com.sun.javafx.scene.control.InputField;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DoctorTableView extends Application {

    private static StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build(); // Create registry
    private static MetadataSources sources = new MetadataSources(registry); // Create MetadataSources
    private static Metadata metadata = sources.getMetadataBuilder().build(); // Create Metadata
    private static SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build(); // Create SessionFactory
    private static DoctorRepository doctorRepository = new DoctorRepository(sessionFactory);
    private static DoctorSearchService dss = new DoctorSearchService(doctorRepository);

    private final TextField searchField = new TextField("Enter the name of the doctor to find");
    private final TableView<Doctor> doctorTable = new TableView<>();
    private final ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
    private final Label doctorCountLabel = new Label();

    public static void main(String[] args) {
        // ... inject some data to make the application self sufficient
        doctorRepository.create(new Doctor("Mindaugas", "Bernatavičius"));
        doctorRepository.create(new Doctor("Jonas", "Jonaitis"));
        doctorRepository.create(new Doctor("Petras", "Petraitis"));

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Tableview with button column");
        stage.setWidth(600);
        stage.setHeight(300);

        setTableappearance();

        fillTableObservableListWithSampleData();
        doctorTable.setItems(doctorList);

        TableColumn<Doctor, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Doctor, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Doctor, String> colSurname = new TableColumn<>("Surname");
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));

        doctorTable.getColumns().addAll(colId, colName, colSurname);
        addButtonToTable();

        VBox root = new VBox();
        HBox searchBar = new HBox();
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBar.getChildren().add(searchField);
        Button searchButton = new Button("Search");
        searchButton.setOnAction(actionEvent -> {
            doctorList.setAll(dss.findByName(searchField.getText()));
        });
        searchBar.getChildren().add(searchButton);
        root.getChildren().add(searchBar);
        root.getChildren().add(doctorTable);
        root.getChildren().add(doctorCountLabel);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void setTableappearance() {
        doctorTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        doctorTable.setPrefWidth(600);
        doctorTable.setPrefHeight(300);
    }

    private void fillTableObservableListWithSampleData() {
        doctorList.setAll(doctorRepository.getAll());
        doctorCountLabel.setText("Total doctors: " + doctorList.size());
    }

    private void addButtonToTable() {
        TableColumn<Doctor, Void> colBtn = new TableColumn("Button Column");
        Callback<TableColumn<Doctor, Void>, TableCell<Doctor, Void>> cellFactory = new Callback<TableColumn<Doctor, Void>, TableCell<Doctor, Void>>() {
            @Override
            public TableCell<Doctor, Void> call(final TableColumn<Doctor, Void> param) {
                final TableCell<Doctor, Void> cell = new TableCell<Doctor, Void>() {
                    private final Button btn = new Button("Delete item");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Doctor doctor = getTableView().getItems().get(getIndex());
                            System.out.println("clicked on: " + doctor);
                            Session session = sessionFactory.openSession();
                            Transaction transaction = session.beginTransaction();
                            Doctor doctorToDelete = session.load(Doctor.class, doctor.getId());
                            session.delete(doctorToDelete);
                            transaction.commit();
                            session.close();
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
        doctorTable.getColumns().add(colBtn);
    }
}
