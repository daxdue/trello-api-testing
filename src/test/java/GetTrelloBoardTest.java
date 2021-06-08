import dataprovider.DataSource;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import ru.training.apitesting.beans.TrelloBoard;
import ru.training.apitesting.constant.ResponseMessages;
import ru.training.apitesting.constant.TrelloBoardDataProviders;

import static ru.training.apitesting.services.TrelloBoardService.*;
import static org.hamcrest.MatcherAssert.*;

public class GetTrelloBoardTest {

    @Test (dataProvider = TrelloBoardDataProviders.TRELLO_BOARD_JSON_DATA_PROVIDER,
            dataProviderClass = DataSource.class)
    public void getExistingBoardTest(TrelloBoard expectedBoard) {
        TrelloBoard actualBoard = getBoardFromResponse(
                requestBuilder()
                .setPathParam(expectedBoard.getId())
                .build()
                .sendRequest(goodResponseSpec())
                .extract()
                .response()
        );
        assertThat(actualBoard, Matchers.equalTo(expectedBoard));
    }

    @Test (dataProvider = TrelloBoardDataProviders.TRELLO_NONEXISTENT_BOARD_DATA_PROVIDER,
            dataProviderClass = DataSource.class)
    public void getNonexistentBoardTest(TrelloBoard trelloBoard) {
        String result = getStringFromResponse(
                requestBuilder()
                .setPathParam(trelloBoard.getId())
                .build()
                .sendRequest(badResponseSpec())
                .extract()
                .response()
        );
        assertThat(result, Matchers.equalTo(ResponseMessages.INVALID_ID));
    }

    @Test (dataProvider = TrelloBoardDataProviders.TRELLO_BOARD_JSON_DATA_PROVIDER,
            dataProviderClass = DataSource.class)
    public void getSpecificBoardFieldTest(TrelloBoard expectedBoard) {

    }
}
