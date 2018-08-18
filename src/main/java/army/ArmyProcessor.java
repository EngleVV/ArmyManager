package army;

import com.google.gson.Gson;
import constant.ArmyMemberConstant;
import ocr.OCR;
import ocr.impl.BaiDuOCR;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import utils.ExcelUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by luwei on 2018/8/18.
 */
public class ArmyProcessor implements IProcessor {

    private static final String ROOT_DIR = "/Users/luwei/Documents/images";
    private static final String SRC_DIR = ROOT_DIR + "/army";
    private static final String RESULT_DIR = SRC_DIR + "/result";
    private static final String SRC_ALIEN = SRC_DIR + "/alien";
    private static final String SRC_IRON_BLOOD = SRC_DIR + "/ironBlood";

    private OCR ocr;

    public ArmyProcessor() {
        ocr = new BaiDuOCR();
    }

    @Override
    public void process() {

        //预处理图片
        deleteExistFile(new File(RESULT_DIR));
        preProcess(SRC_ALIEN, 712, 234, 320, 580);
        preProcess(SRC_IRON_BLOOD, 424, 294, 290, 576);

        List<String> participantList = generateParticipantList(ocr, RESULT_DIR);

        String[] armyName = ArmyMemberConstant.ARMY_NAME;
        ArmyMember[] armyMembers = calculateParticipantCount(participantList, armyName);

        Arrays.sort(armyMembers, new Comparator<ArmyMember>() {
            @Override
            public int compare(ArmyMember o1, ArmyMember o2) {
                return o2.getCount() - o1.getCount();
            }
        });

        showExcelResult(armyMembers);

    }

    private void showResult(String[] armyName, int[] count) {
        for(int i = 0; i < armyName.length; i++) {
            printInfo(String.format(Locale.CHINA, "%10s\t\t", armyName[i]), count[i]+"");
        }
    }

    private void showExcelResult(ArmyMember[] armyMembers) {
        HSSFWorkbook workbook = new HSSFWorkbook();

        String[][] data = new String[armyMembers.length][2];

        for(int i = 0; i < armyMembers.length; i++) {
            data[i][0] = armyMembers[i].getId();
            data[i][1] = String.valueOf(armyMembers[i].getCount());
        }

        ExcelUtil.getHSSFWorkbook("Hello", new String[]{"军团成员ID", "参加活动次数"},
                data,
                workbook);

        long time = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd_hh-mm-ss");
        String fileName = String.format(String.format(ROOT_DIR + "/result" + simpleDateFormat.format(new Date(time))));
        File file = new File(fileName + ".xls");

        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArmyMember[] calculateParticipantCount(List<String> participantList, String[] armyName) {
        ArmyMember[] armyMembers = new ArmyMember[armyName.length];
        for(int i = 0; i < armyName.length; i++) {
            armyMembers[i] = new ArmyMember();
            armyMembers[i].setId(armyName[i]);
            armyMembers[i].setCount(0);
        }

        for(String participant : participantList) {
            for(int j = 0; j < armyName.length; j++) {
                if(fuzzyMatch(participant, armyName[j])) {
                    armyMembers[j].setCount(armyMembers[j].getCount()+1);
                    break;
                }

            }
        }
        return armyMembers;
    }

    private static List<String> generateParticipantList(OCR ocr, String picPath) {
        File picDir = new File(picPath);
        Gson gson = new Gson();
        String result;
        String name;
        List<String> participantList = new ArrayList<String>();

        if(picDir.isDirectory()) {
            File[] picFiles = picDir.listFiles();
            if(picFiles != null) {
                for (File f : picFiles) {
                    if (f.getName().startsWith(".")) {
                        //过掉掉一些隐藏文件
                        continue;
                    }
                    result = ocr.getOCR(new File(f.getAbsolutePath()));
                    OcrResult ocrResult = gson.fromJson(result, OcrResult.class);
                    for (WordsResult wordsResult : ocrResult.getWords_result()) {
                        name = wordsResult.getWords();
                        participantList.add(name);
                    }
                }
            }
        }
        return participantList;
    }

    /**
     * 模糊匹配算法
     * @param src
     * @param target
     * @return
     */
    private static boolean fuzzyMatch(String src, String target) {

        if(src.equals(target)) {
            printInfo(src, target);
            return true;
        }

        int targetCount = 3;
        if(src.contains("群英会")) {
            targetCount = 5;
        }

        int count = 0;
        for(char ch : src.toCharArray()) {
            if(target.contains(String.valueOf(ch))) {
                count++;
                if(count >= targetCount) {
                    printInfo(src, target);
                    return true;
                }
            }
        }
        return false;

    }

    private static void printInfo(String src, String target) {
        System.out.print(src);
        System.out.print("   ");
        System.out.print(target);
        System.out.println();
    }

    private static void preProcess(String srcPath, int x, int y, int width, int height) {
        File path = new File(srcPath);
        File resultPath = new File(RESULT_DIR);
        try {
            if (path.exists()) {
                File[] pics = path.listFiles();

                if (pics != null) {
                    for (File pic : pics) {

                        if(!pic.getName().endsWith("jpg")) {
                            continue;
                        }

                        BufferedImage bi = ImageIO.read(pic);

                        bi = bi.getSubimage(x, y, width, height);

                        ImageIO.write(bi, "jpg", new File(resultPath.getAbsolutePath()+"/"+pic.getName()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void deleteExistFile(File resultPath) {
        if(resultPath != null && resultPath.isDirectory()) {
            File[] files = resultPath.listFiles();
            if(files != null) {
                for(File file : files) {
                    file.delete();
                }
            }
        }
    }
}
