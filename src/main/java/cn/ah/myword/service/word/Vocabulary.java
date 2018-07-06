package cn.ah.myword.service.word;

import com.csvreader.CsvReader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hui ZHANG on 2018/3/21.
 * email: hvzhang@hotmail.com
 */
public class Vocabulary {

    public static Map<Integer, Word> words = new HashMap<>();
//    static Map<String, Map> moduleWordMap = new HashMap<>();
//    static Map<String, String> engChnMap = new HashMap<>();

    public static void readCSV() {
        try {
            // 用来保存数据
            ArrayList<String[]> csvFileList = new ArrayList<String[]>();
            // 定义一个CSV路径
//            System.out.println(System.getProperty("user.dir"));
            String csvFilePath = System.getProperty("user.dir")+"/vocabulary3.csv";
            // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
            CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));
            // 跳过表头 如果需要表头的话，这句可以忽略
            reader.readHeaders();
            // 逐行读入除表头的数据
            while (reader.readRecord()) {
//                System.out.println(reader.getRawRecord());
                csvFileList.add(reader.getValues());
            }
            reader.close();

            // 遍历读取的CSV文件
            for (int row = 0; row < csvFileList.size(); row++) {
                // 取得第row行第0列的数据
//                String cell = csvFileList.get(row)[0];
//                System.out.println("------------>" + cell);
//                engChnMap.put(csvFileList.get(row)[0],  csvFileList.get(row)[2]);
//                moduleWordMap.put(csvFileList.get(row)[1], engChnMap);
                    words.put(row, new Word(csvFileList.get(row)[0], csvFileList.get(row)[1],  csvFileList.get(row)[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Vocabulary() {
    }
}
