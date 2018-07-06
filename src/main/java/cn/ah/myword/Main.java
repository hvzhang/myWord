package cn.ah.myword;

import cn.ah.myword.service.audio.MyRecord;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Hui ZHANG on 2018/3/20.
 * email: hvzhang@hotmail.com
 */
public class Main {

    public static void main(String[] args){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyWordConfig.class);

//        PrintName printName = context.getBean(PrintName.class);
//        printName.print();

//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext(MyWordConfig.class);
//        Vocabulary.readCSV();

        MyRecord myRecord = context.getBean(MyRecord.class);
        //创造一个实例



    }
}
