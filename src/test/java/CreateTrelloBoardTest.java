import io.restassured.http.Method;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import ru.training.apitesting.beans.TrelloBoard;
import ru.training.apitesting.constant.QueryParams;
import ru.training.apitesting.constant.RequestSpecType;

import static ru.training.apitesting.constant.TestGroups.*;
import static ru.training.apitesting.services.TrelloBoardService.*;
import static org.hamcrest.MatcherAssert.*;

public class CreateTrelloBoardTest extends BaseTrelloTest {

    @AfterTest(groups = {REQUIRE_DELETE_AFTER})
    public void tearDown() {
        if(boardId != null) {
            requestBuilder()
                .setRequestMethod(Method.DELETE)
                .addPathParam(QueryParams.BOARD_ID_PATH, boardId)
                /*.setPathParam(boardId)*/
                .build()
            .sendRequest(RequestSpecType.WITH_PATH_PARAMS, goodResponseSpec());
        }
    }

    @Test(groups = {REQUIRE_DELETE_AFTER})
    public void createNewBoardTest() {
        TrelloBoard createdBoard = getBoardFromResponse(
                requestBuilder()
                    .setRequestMethod(Method.POST)
                    .setBoardName("Board name")
                    .build()
                .sendRequest(RequestSpecType.WITHOUT_PATH_PARAMS, goodResponseSpec())
                .extract()
                .response()
        );
        boardId = createdBoard.getId();
        assertThat(createdBoard.getName(), Matchers.equalTo("Board name"));
    }

    @Test
    public void createNewBoardWithoutBoardNameTest() {
        requestBuilder()
            .setRequestMethod(Method.POST)
            .build()
        .sendRequest(RequestSpecType.WITHOUT_PATH_PARAMS, badResponseSpec())
        .extract()
        .response();
    }
}
