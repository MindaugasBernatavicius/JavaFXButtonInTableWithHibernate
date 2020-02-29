package cf.mindaugas;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class TableViewSample extends Application {

    private final TableView<Data> table = new TableView<>();
    private final ObservableList<Data> tvObservableList = FXCollections.observableArrayList();

    static StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build(); // Create registry
    static MetadataSources sources = new MetadataSources(registry); // Create MetadataSources
    static Metadata metadata = sources.getMetadataBuilder().build(); // Create Metadata
    static SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build(); // Create SessionFactory

    public static void main(String[] args) {
        // ... inject some data to make the application self sufficient
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Data d1 = new Data("Mindaugas");
        Data d2 = new Data("Jonas");
        Data d3 = new Data("Petras");
        session.persist(d1);
        session.persist(d2);
        session.persist(d3);
        transaction.commit();
        session.close();

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Tableview with button column");
        stage.setWidth(600);
        stage.setHeight(300);

        setTableappearance();

        fillTableObservableListWithSampleData();
        table.setItems(tvObservableList);

        TableColumn<Data, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Data, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().addAll(colId, colName);
        addButtonToTable();

        Scene scene = new Scene(new Group(table));
        stage.setScene(scene);
        stage.show();
    }

    private void setTableappearance() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(600);
        table.setPrefHeight(300);
    }

    private void fillTableObservableListWithSampleData() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Data";
        Query query = session.createQuery(hql);
        List results = query.list();
        tvObservableList.setAll(results);
        transaction.commit();
        session.close();
    }

    private void addButtonToTable() {
        TableColumn<Data, Void> colBtn = new TableColumn("Button Column");
        Callback<TableColumn<Data, Void>, TableCell<Data, Void>> cellFactory = new Callback<TableColumn<Data, Void>, TableCell<Data, Void>>() {
            @Override
            public TableCell<Data, Void> call(final TableColumn<Data, Void> param) {
                final TableCell<Data, Void> cell = new TableCell<Data, Void>() {
                    private final Button btn = new Button("Delete item");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Data data = getTableView().getItems().get(getIndex());
                            System.out.println("clicked on: " + data);
                            Session session = sessionFactory.openSession();
                            Transaction transaction = session.beginTransaction();
                            Data dataToDelete = session.load(Data.class, data.getId());
                            session.delete(dataToDelete);
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
        table.getColumns().add(colBtn);
    }
}