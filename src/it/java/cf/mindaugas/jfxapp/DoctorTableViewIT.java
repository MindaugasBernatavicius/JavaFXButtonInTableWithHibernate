package cf.mindaugas.jfxapp;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;

@ExtendWith(ApplicationExtension.class)
public class DoctorTableViewIT {

    private DoctorTableView doctorTableView;
    private Stage stage;

    @BeforeAll
    public static void init(){
        DoctorTableView.addInitialData();
    }

    @Start // given, called with @BeforeEach semantics
    private void start(Stage stage) {
        doctorTableView = new DoctorTableView();
        doctorTableView.start(stage);
        this.stage = stage;
        stage.setScene(doctorTableView.getScene());
        stage.show();
        stage.toFront();
        System.out.println("Is showing: "+ stage.isShowing());
}

    @Stop // teardown, called with @After semantics
    private void stop() {
        stage.close();
        System.out.println("Is showing: "+ stage.isShowing());
    }

    @Test
    void givenDefaultParams_WhenApplicationLoaded_ThenSearchButtonIsPresent(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#id-search-button").queryButton()).hasText("Searchz");
    }

    @Test
    void givenDefaultParams_WhenTextEnteredIntoSearchBar_ThenSearchResultsAreAppropriate(FxRobot robot) throws InterruptedException {
        // when;
        robot.clickOn("#id-search-bar");
        robot.eraseText(((TextField)stage.getScene().lookup("#id-search-field")).getText().length());
        robot.write("Mindaugas");
        robot.clickOn("#id-search-button");

        // To get an idea of how it work, we can pause!
        Thread.sleep(8000);

        // then
        // Assertions.assertThat().hasText("Search");
    }
}
