package dataprovider;

import org.testng.annotations.DataProvider;
import ru.training.apitesting.beans.TrelloBoard;
import ru.training.apitesting.constant.TestBoardType;
import ru.training.apitesting.constant.TrelloBoardDataProviders;
import ru.training.apitesting.utils.JsonTestDataLoader;

public class DataSource {

    @DataProvider (name = TrelloBoardDataProviders.TRELLO_BOARD_JSON_DATA_PROVIDER)
    public Object[][] trelloBoardJsonDataProvider() {
        TrelloBoard trelloBoard = JsonTestDataLoader.load(TestBoardType.EXISTENT_BOARD);
        return new Object[][]{{trelloBoard}};
    }

    @DataProvider (name = TrelloBoardDataProviders.TRELLO_NONEXISTENT_BOARD_DATA_PROVIDER)
    public Object[][] trelloNonexistentBoardIdDataProvider() {
        TrelloBoard trelloBoard = JsonTestDataLoader.load(TestBoardType.NONEXISTENT_BOARD);
        return new Object[][]{{trelloBoard}};
    }
}
