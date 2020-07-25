package cf.mindaugas.jfxapp;

import cf.mindaugas.jfxapp.controller.DoctorController;
import cf.mindaugas.jfxapp.model.Doctor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;

public class DoctorTableViewWFxml extends Application {

    static StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build(); // Create registry
    static MetadataSources sources = new MetadataSources(registry); // Create MetadataSources
    static Metadata metadata = sources.getMetadataBuilder().build(); // Create Metadata
    static SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build(); // Create SessionFactory

    public static void main(String[] args) {
        // ... inject some data to make the application self sufficient
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(new Doctor("Mindaugas", "Bernatavičius"));
        session.persist(new Doctor("Jonas", "Jonaitis"));
        session.persist(new Doctor("Petras", "Petraitis"));
        transaction.commit();
        session.close();

        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Tableview with button column");
        stage.setWidth(600);
        stage.setHeight(300);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\..\\..\\view\\ScreenWithTable.fxml"));
        fxmlLoader.setController(new DoctorController(sessionFactory.openSession()));
        Pane p = fxmlLoader.load();

        Scene scene = new Scene(new Group(p));
        stage.setScene(scene);
        stage.show();
    }
}
