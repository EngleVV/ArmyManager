package army;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luwei on 2018/1/15.
 */
public class AnalysisResult {

    private List<String> allMembers;
    private List<String> participantList;
    private List<String> notParticipantList;

    public AnalysisResult() {
        allMembers = new ArrayList<String>();
        participantList = new ArrayList<String>();
        notParticipantList = new ArrayList<String>();
    }

    public void setAllMembers(List<String> allMembers) {
        this.allMembers = allMembers;
    }

    public void addParticipant(String name) {
        participantList.add(name);
    }

    public void addNotParticipant(String name) {
        notParticipantList.add(name);
    }

    @Override
    public String toString() {

        SimpleDateFormat myFmt3=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分 E ");

        StringBuilder sb = new StringBuilder();
        sb.append("日期:");
        sb.append(myFmt3.format(new Date()));
        sb.append("\n军团总人数:");
        sb.append(allMembers.size());
        sb.append("\n参加活动人数:");
        sb.append(participantList.size());
        sb.append("\n未参加活动人数:");
        sb.append(notParticipantList.size());
        sb.append("\n未参加人员名单:");
        sb.append(notParticipantList.toString());
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(new AnalysisResult().toString());
    }
}
