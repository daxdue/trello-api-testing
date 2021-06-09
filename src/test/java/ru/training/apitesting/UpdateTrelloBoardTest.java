package ru.training.apitesting;

import ru.training.apitesting.beans.TrelloBoard;
import ru.training.apitesting.dataprovider.DataSource;
import io.restassured.http.Method;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import ru.training.apitesting.constant.PathParams;
import ru.training.apitesting.constant.RequestSpecType;
import ru.training.apitesting.constant.ResponseMessages;

import static ru.training.apitesting.services.TrelloBoardService.*;
import static ru.training.apitesting.constant.TrelloBoardDataProviders.*;
import static org.hamcrest.MatcherAssert.*;

public class UpdateTrelloBoardTest {

    @Test (dataProvider = TRELLO_BOARD_JSON_DATA_PROVIDER,
            dataProviderClass = DataSource.class)
    public void updateExistingBoardTest(TrelloBoard trelloBoard) {
        TrelloBoard updatedBoard = getBoardFromResponse(
                requestBuilder()
                    .setRequestMethod(Method.PUT)
                    .addPathParam(PathParams.BOARD_ID_PARAM, trelloBoard.getId())
                    .setBoardName(trelloBoard.getName())
                    .build()
                .sendRequest(RequestSpecType.PATH_PARAMS, goodResponseSpec())
                .extract()
                .response()
        );
        assertThat(updatedBoard.getName(), Matchers.equalTo(trelloBoard.getName()));
    }

    @Test (dataProvider = TRELLO_NONEXISTENT_BOARD_DATA_PROVIDER,
            dataProviderClass = DataSource.class)
    public void updateNonexistentBoardTest(TrelloBoard trelloBoard) {
        String result = getStringFromResponse(
            requestBuilder()
                .setRequestMethod(Method.PUT)
                .addPathParam(PathParams.BOARD_ID_PARAM, trelloBoard.getId())
                .build()
            .sendRequest(RequestSpecType.PATH_PARAMS, badResponseSpec())
            .extract()
            .response()
        );
        assertThat(result, Matchers.equalTo(ResponseMessages.INVALID_ID));
    }
}
