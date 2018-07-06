package cn.ah.myword.service.word;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Hui ZHANG on 2018/3/21.
 * email: hvzhang@hotmail.com
 */
public class VocabularyTest {
    @Test
    public void readCSV() throws Exception {
        Vocabulary.readCSV();

        Word aWord = new Word("camp",	"M3",	"营地;帐篷");

        Assert.assertTrue(Vocabulary.words.containsValue(aWord));
//        Assert.assertTrue(Vocabulary.words.get("camp").contains("帐篷"));



    }

}