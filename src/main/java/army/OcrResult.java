package army;

import java.util.List;

/**
 * Created by luwei on 2018/1/15.
 */
public class OcrResult {
    private String log_id;
    private List<WordsResult> words_result;
    private String words_result_num;

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public List<WordsResult> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResult> words_result) {
        this.words_result = words_result;
    }

    public String getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(String words_result_num) {
        this.words_result_num = words_result_num;
    }

    @Override
    public String toString() {
        return "OcrResult{" +
                "log_id='" + log_id + '\'' +
                ", words_result=" + words_result +
                ", words_result_num='" + words_result_num + '\'' +
                '}';
    }
}
