package dataprovider;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.testng.annotations.DataProvider;
import ru.training.apitesting.beans.TrelloBoard;
import ru.training.apitesting.constant.TrelloBoardDataProviders;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataSource {

    private static final String TEST_BOARD_JSON_FILE_PATH =
            "src/test/java/resources/test_data/test_board.json";
    private static final String CHARSET = "UTF-8";

    private TrelloBoard load() {
        TrelloBoard trelloBoard = null;
        try (InputStream inputStream = new FileInputStream(TEST_BOARD_JSON_FILE_PATH)) {
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, CHARSET));
            trelloBoard = new Gson().fromJson(reader, TrelloBoard.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return trelloBoard;
    }

    @DataProvider(name = TrelloBoardDataProviders.TRELLO_BOARD_JSON_DATA_PROVIDER)
    public Object[][] trelloBoardJsonDataProvider() {
        TrelloBoard trelloBoard = load();
        return new Object[][]{{trelloBoard}};
    }
}
