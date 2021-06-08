import dataprovider.DataSource;
import io.restassured.http.Method;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.training.apitesting.beans.TrelloBoard;
import ru.training.apitesting.constant.ResponseMessages;
import ru.training.apitesting.constant.TestBoardType;
import ru.training.apitesting.constant.TestGroups;
import ru.training.apitesting.constant.TrelloBoardDataProviders;
import ru.training.apitesting.utils.JsonTestDataLoader;

import static ru.training.apitesting.services.TrelloBoardService.*;
import static org.hamcrest.MatcherAssert.*;

public class DeleteTrelloBoardTest extends BaseTrelloTest {

    @BeforeTest (groups = {TestGroups.REQUIRE_CREATE_BEFORE})
    public void setUp() {
        TrelloBoard board = getBoardFromResponse(
            requestBuilder()
                .setRequestMethod(Method.POST)
                .setBoardName(JsonTestDataLoader
                        .load(TestBoardType.NONEXISTENT_BOARD).getName())
                .build()
                .sendRequest(goodResponseSpec())
                .extract()
                .response()
        );
        boardId = board.getId();
    }

    @Test (groups = {TestGroups.REQUIRE_CREATE_BEFORE})
    public void deleteExistingBoardTest() {
        requestBuilder()
                .setPathParam(boardId)
                .setRequestMethod(Method.DELETE)
                .build()
        .sendRequest(goodResponseSpec());
    }

    @Test (dataProvider = TrelloBoardDataProviders.TRELLO_NONEXISTENT_BOARD_DATA_PROVIDER,
            dataProviderClass = DataSource.class)
    public void deleteNonexistentBoardTest(TrelloBoard trelloBoard) {
        String result = getStringFromResponse(
            requestBuilder()
                .setPathParam(trelloBoard.getId())
                .setRequestMethod(Method.DELETE)
                .build()
            .sendRequest(badResponseSpec())
            .extract()
            .response()
        );
        assertThat(result, Matchers.equalTo(ResponseMessages.INVALID_ID));
    }
}
