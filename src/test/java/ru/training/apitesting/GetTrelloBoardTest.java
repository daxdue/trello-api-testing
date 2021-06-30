package ru.training.apitesting;

import ru.training.apitesting.beans.TrelloBoard;
import ru.training.apitesting.dataprovider.DataSource;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import ru.training.apitesting.beans.SpecificResponse;
import ru.training.apitesting.constant.*;

import static ru.training.apitesting.services.TrelloBoardService.*;
import static org.hamcrest.MatcherAssert.*;

public class GetTrelloBoardTest {

    @Test (dataProvider = TrelloBoardDataProviders.TRELLO_BOARD_JSON_DATA_PROVIDER,
            dataProviderClass = DataSource.class)
    public void getExistingBoardTest(TrelloBoard expectedBoard) {
        TrelloBoard actualBoard = getBoardFromResponse(
            requestBuilder()
                .addPathParam(PathParams.BOARD_ID_PARAM, expectedBoard.getId())
                .setRequestSpecType(RequestSpecType.PATH_PARAMS)
                .setResponseSpecification(goodResponseSpec())
                .build());
        assertThat(actualBoard, Matchers.equalTo(expectedBoard));
    }

    @Test (dataProvider = TrelloBoardDataProviders.TRELLO_NONEXISTENT_BOARD_DATA_PROVIDER,
            dataProviderClass = DataSource.class)
    public void getNonexistentBoardTest(TrelloBoard testTrelloBoard) {
        String result = getStringFromResponse(
            requestBuilder()
                .addPathParam(PathParams.BOARD_ID_PARAM, testTrelloBoard.getId())
                .setRequestSpecType(RequestSpecType.PATH_PARAMS)
                .setResponseSpecification(badResponseSpec())
                .build());
        assertThat(result, Matchers.equalTo(ResponseMessages.INVALID_ID));
    }

    @Test (dataProvider = TrelloBoardDataProviders.TRELLO_BOARD_JSON_DATA_PROVIDER,
            dataProviderClass = DataSource.class)
    public void getSpecificBoardFieldTest(TrelloBoard testTrelloBoard) {
        SpecificResponse specificResponse = getSpecificResponse(
            requestBuilder()
                .addPathParam(PathParams.BOARD_ID_PARAM, testTrelloBoard.getId())
                .addPathParam(PathParams.BOARD_NAME_PARAM, PathParams.BOARD_NAME_PARAM)
                .setRequestSpecType(RequestSpecType.PATH_PARAMS)
                .setResponseSpecification(goodResponseSpec())
                .build());
        assertThat(specificResponse.getValue(), Matchers.equalTo(testTrelloBoard.getName()));
    }

    @Test (dataProvider = TrelloBoardDataProviders.TRELLO_BOARD_JSON_DATA_PROVIDER,
            dataProviderClass = DataSource.class)
    public void getNonexistentBoardFieldTest(TrelloBoard testTrelloBoard) {
        String response = getStringFromResponse(
            requestBuilder()
                .addPathParam(PathParams.BOARD_ID_PARAM, testTrelloBoard.getId())
                .addPathParam(PathParams.BOARD_NONEXISTENT_PARAM, PathParams.BOARD_NONEXISTENT_PARAM)
                .setRequestSpecType(RequestSpecType.PATH_PARAMS)
                .setResponseSpecification(badResponseSpec())
                .build());
        assertThat(response, Matchers.containsString(ResponseMessages.CANNOT_GET));
    }
}
