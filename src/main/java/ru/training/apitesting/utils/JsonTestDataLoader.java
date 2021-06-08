package ru.training.apitesting.utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import ru.training.apitesting.beans.TrelloBoard;
import ru.training.apitesting.constant.TestBoardType;

import java.io.*;

public class JsonTestDataLoader {

    private static final String TEST_BOARD_JSON_FILE_PATH =
            "src/test/resources/test_data/test_board.json";
    private static final String TEST_NONEXISTENT_BOARD_FILE_PATH =
            "src/test/resources/test_data/test_board_nonexistent.json";
    private static final String CHARSET = "UTF-8";

    public static TrelloBoard load(TestBoardType testBoardType) {
        TrelloBoard trelloBoard = null;
        String testBoardFilePath = "";
        switch (testBoardType) {
            case EXISTENT_BOARD:
                testBoardFilePath = TEST_BOARD_JSON_FILE_PATH;
                break;

            case NONEXISTENT_BOARD:
                testBoardFilePath = TEST_NONEXISTENT_BOARD_FILE_PATH;
                break;
        }

        if (!testBoardFilePath.isEmpty()) {
            try (InputStream inputStream = new FileInputStream(testBoardFilePath)) {
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream, CHARSET));
                trelloBoard = new Gson().fromJson(reader, TrelloBoard.class);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return trelloBoard;
    }
}
