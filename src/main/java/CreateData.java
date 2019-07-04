import org.apache.commons.lang3.RandomUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateData {

    /**
     * 江苏位介于东经116°18′-121°57′，北纬30°45′-35°20′之间
     *
     * @param
     */
    public static Map<String, Double> getPos() {
        Map<String, Double> map = new HashMap<String, Double>();
        double lon = RandomUtils.nextDouble(116, 122);
        double lat = RandomUtils.nextDouble(30, 36);
        map.put("lon", lon);
        map.put("lat", lat);
        return map;
    }

    /**
     * 生成开始时间 结束时间
     *
     * @return
     */
    public static Map<String, Date> getTime() {
        Map<String, Date> map = new HashMap<String, Date>();
        long longBounded = RandomUtils.nextLong(1546272000000L, 1561093832578L);
        System.out.println(longBounded);
        Date start = new Date(longBounded);
        System.out.println(start);
        long addTime = RandomUtils.nextLong(180000L, 3600000L);
        Date end = new Date(longBounded + addTime);
        System.out.println(end);
        map.put("start", start);
        map.put("end", end);
        return map;
    }

    /**
     * 名字
     *
     * @return
     */
    public static String[] getNames() {
        String name = "鬼谷子、甘德、石申、李冰、扁鹊、范雎、蔡泽、郭隗、唐蔑、宋玉、触龙、毛遂、鲁仲连、公孙龙";
        String[] names = name.split("、");
        return names;
    }

    /**
     * 聊天内容
     *
     * @return
     */
    public static String[] getSentences() {
        String[] sentens = new String[1000];
        int i = 0;
        try {
            FileReader fr = new FileReader("./1000");
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                sentens[i] = str.split("^\\d+、")[1];
                i++;
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sentens;
    }
}
