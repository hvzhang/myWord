package cn.ah.myword.service.word;

/**
 * Created by Hui ZHANG on 2018/3/21.
 * email: hvzhang@hotmail.com
 */
public class Word {
    private String english;
    private String module;
    private String chinese;

    public Word(String english, String module, String chinese) {
        this.english = english;
        this.module = module;
        this.chinese = chinese;
    }

    public Word() {
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
}
