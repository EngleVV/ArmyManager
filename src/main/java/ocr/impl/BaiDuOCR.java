package ocr.impl;

import com.baidu.aip.ocr.AipOcr;
import ocr.OCR;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by luwei on 2018/1/12.
 * @author lingfengsan
 */
public class BaiDuOCR implements OCR{

    private static final String APP_ID = "10690670";
    private static final String API_KEY = "zn0E008YhzYagG4thzqZMq3q";
    private static final String SECRET_KEY = "X3pr3XFyAI92yVM7uK6pVe7mZPHpFNP3";
    private static final AipOcr CLIENT=new AipOcr(APP_ID, API_KEY, SECRET_KEY);

    public BaiDuOCR(){
        // 可选：设置网络连接参数
        CLIENT.setConnectionTimeoutInMillis(2000);
        CLIENT.setSocketTimeoutInMillis(600000);
    }
    @Override
    public String getOCR(File file) {
        String path=file.getAbsolutePath();
        // 调用接口
        JSONObject res = CLIENT.basicGeneral(path, new HashMap<String, String>());
        String result=res.toString();
        return result;
    }
}
