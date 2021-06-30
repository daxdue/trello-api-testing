package ru.training.apitesting;

import ru.training.apitesting.beans.TrelloBoard;
import ru.training.apitesting.dataprovider.DataSource;
import io.restassured.http.Method;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.training.apitesting.constant.*;
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
                .setRequestSpecType(RequestSpecType.QUERY_PARAMS)
                .setResponseSpecification(goodResponseSpec())
                .build());
        boardId = board.getId();
    }

    @Test (groups = {TestGroups.REQUIRE_CREATE_BEFORE})
    public void deleteExistingBoardTest() {
        requestBuilder()
                .addPathParam(PathParams.BOARD_ID_PARAM, boardId)
                .setRequestMethod(Method.DELETE)
                .setRequestSpecType(RequestSpecType.PATH_PARAMS)
                .setResponseSpecification(goodResponseSpec())
                .build();
    }

    @Test (dataProvider = TrelloBoardDataProviders.TRELLO_NONEXISTENT_BOARD_DATA_PROVIDER,
            dataProviderClass = DataSource.class)
    public void deleteNonexistentBoardTest(TrelloBoard trelloBoard) {
        String result = getStringFromResponse(
            requestBuilder()
                .addPathParam(PathParams.BOARD_ID_PARAM, trelloBoard.getId())
                .setRequestMethod(Method.DELETE)
                .setRequestSpecType(RequestSpecType.PATH_PARAMS)
                .setResponseSpecification(badResponseSpec())
                .build());
        assertThat(result, Matchers.equalTo(ResponseMessages.INVALID_ID));
    }
}
