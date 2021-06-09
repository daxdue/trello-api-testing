package ru.training.apitesting;

import io.restassured.http.Method;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import ru.training.apitesting.beans.TrelloBoard;
import ru.training.apitesting.constant.PathParams;
import ru.training.apitesting.constant.RequestSpecType;
import ru.training.apitesting.constant.ResponseMessages;
import ru.training.apitesting.constant.TrelloBoardDataProviders;
import ru.training.apitesting.dataprovider.DataSource;

import static ru.training.apitesting.constant.TestGroups.*;
import static ru.training.apitesting.services.TrelloBoardService.*;
import static org.hamcrest.MatcherAssert.*;

public class CreateTrelloBoardTest extends BaseTrelloTest {

    @AfterTest(groups = {REQUIRE_DELETE_AFTER})
    public void tearDown() {
        if(boardId != null) {
            requestBuilder()
                .setRequestMethod(Method.DELETE)
                .addPathParam(PathParams.BOARD_ID_PARAM, boardId)
                .build()
            .sendRequest(RequestSpecType.PATH_PARAMS, goodResponseSpec());
        }
    }

    @Test(groups = {REQUIRE_DELETE_AFTER},
            dataProvider = TrelloBoardDataProviders.TRELLO_BOARD_JSON_DATA_PROVIDER,
            dataProviderClass = DataSource.class)
    public void createNewBoardTest(TrelloBoard testBoard) {
        TrelloBoard createdBoard = getBoardFromResponse(
                requestBuilder()
                    .setRequestMethod(Method.POST)
                    .setBoardName(testBoard.getName())
                    .build()
                .sendRequest(RequestSpecType.QUERY_PARAMS, goodResponseSpec())
                .extract()
                .response()
        );
        boardId = createdBoard.getId();
        assertThat(createdBoard.getName(), Matchers.equalTo(testBoard.getName()));
    }

    @Test
    public void createNewBoardWithoutBoardNameTest() {
        String response = getStringFromResponse(
            requestBuilder()
                .setRequestMethod(Method.POST)
                .build()
                .sendRequest(RequestSpecType.QUERY_PARAMS, badResponseSpec())
                .extract()
                .response()
        );
        assertThat(response, Matchers.equalTo(ResponseMessages.INVALID_NAME));
    }
}
