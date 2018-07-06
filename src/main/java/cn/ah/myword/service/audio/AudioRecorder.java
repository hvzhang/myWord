package cn.ah.myword.service.audio;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Hui ZHANG on 2018/3/21.
 * email: hvzhang@hotmail.com
 */
public class AudioRecorder {


    //定义录音格式
    AudioFormat audioFormat = null;
    //定义目标数据行,可以从中读取音频数据,该 TargetDataLine 接口提供从目标数据行的缓冲区读取所捕获数据的方法。
    TargetDataLine targetDataLine = null;
    //定义源数据行,源数据行是可以写入数据的数据行。它充当其混频器的源。应用程序将音频字节写入源数据行，这样可处理字节缓冲并将它们传递给混频器。
    SourceDataLine sourceDataLine = null;
    //定义字节数组输入输出流
    ByteArrayInputStream byteArrayInputStream = null;
    ByteArrayOutputStream byteArrayOutputStream = null;
    //定义音频输入流
    AudioInputStream audioInputStream = null;


    //定义停止录音的标志，来控制录音线程的运行
    Boolean stopflag = false;

    public void capture() {
        try {
            //audioFormat为AudioFormat也就是音频格式
            audioFormat = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) (AudioSystem.getLine(info));

            //打开具有指定格式的行，这样可使行获得所有所需的系统资源并变得可操作。
            targetDataLine.open(audioFormat);
            //允许某一数据行执行数据 I/O
            targetDataLine.start();

            //创建播放录音的线程
            Record record = new Record();
            Thread t1 = new Thread(record);
            t1.start();

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
            return;
        }
    }

    //录音类，因为要用到AudioRecorder类中的变量，所以将其做成内部类
    class Record implements Runnable {
        //定义存放录音的字节数组,作为缓冲区
        byte bts[] = new byte[10000];

        //将字节数组包装到流里，最终存入到baos中
        //重写run函数
        public void run() {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                System.out.println("开始录音...");
                stopflag = false;
                while (stopflag != true) {
                    //当停止录音没按下时，该线程一直执行
                    //从数据行的输入缓冲区读取音频数据。
                    //要读取bts.length长度的字节,cnt 是实际读取的字节数
                    int cnt = targetDataLine.read(bts, 0, bts.length);
                    if (cnt > 0) {
                        byteArrayOutputStream.write(bts, 0, cnt);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    //关闭打开的字节数组流
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    targetDataLine.drain();
                    targetDataLine.close();
                }
            }
        }

    }

    //播放录音
    public void play() {
        //将baos中的数据转换为字节数据
        byte audioData[] = byteArrayOutputStream.toByteArray();
        //转换为输入流
        byteArrayInputStream = new ByteArrayInputStream(audioData);
        audioFormat = getAudioFormat();
        audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, audioData.length / audioFormat.getFrameSize());

        try {
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            //创建播放进程
            Play play = new Play();
            Thread t2 = new Thread(play);
            t2.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                if (audioInputStream != null) {
                    audioInputStream.close();
                }
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //保存录音
    public void save() {
        //取得录音输入流
        audioFormat = getAudioFormat();

//        System.out.print(audioFormat);

        byte audioData[] = byteArrayOutputStream.toByteArray();
        byteArrayInputStream = new ByteArrayInputStream(audioData);
        audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, audioData.length / audioFormat.getFrameSize());
        //定义最终保存的文件名
        File file = null;
        //写入文件
        try {
            //以当前的时间命名录音的名字
            //将录音的文件存放到F盘下语音文件夹下
            File filePath = new File("d:/语音文件");
            if (!filePath.exists()) {//如果文件不存在，则创建该目录
                filePath.mkdir();
            }
            file = new File(filePath.getPath() + "/" + System.currentTimeMillis() + ".wav");
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, file);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {

                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                if (audioInputStream != null) {
                    audioInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //播放类,同样也做成内部类
    class Play implements Runnable {
        //播放baos中的数据即可
        public void run() {
            byte bts[] = new byte[10000];
            try {
                int cnt;
                //读取数据到缓存数据
                while ((cnt = audioInputStream.read(bts, 0, bts.length)) != -1) {
                    if (cnt > 0) {
                        //写入缓存数据
                        //将音频数据写入到混频器
                        sourceDataLine.write(bts, 0, cnt);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sourceDataLine.drain();
                sourceDataLine.close();
            }


        }
    }

    // provides PCM bytes
    public byte[] getAudioBytes(){
        return  byteArrayOutputStream.toByteArray();
    }


    //停止录音
    public void stop() {
        stopflag = true;

        // below uses Baidu voice recognistion API to recognize voice in Chinese
//        AsrMain demo = new AsrMain();
//        String result = null;
//        try {
//            result = demo.run(this.getAudioBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (DemoException e) {
//            e.printStackTrace();
//        }
//        System.out.println("识别结束：结果是：");
//        System.out.println(result);

        // need to check below why it does not close in capture()
        targetDataLine.close();




    }
    //设置AudioFormat的参数
    public AudioFormat getAudioFormat() {
        //下面注释部分是另外一种音频格式，两者都可以
//        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
//        float rate = 8000f;
//        int sampleSize = 16;
//        String signedString = "signed";
//        boolean bigEndian = true;
//        int channels = 1;
//        return new AudioFormat(encoding, rate, sampleSize, channels,
//                (sampleSize / 8) * channels, rate, bigEndian);


        //采样率是每秒播放和录制的样本数
        float sampleRate = 16000.0F;
        // 采样率8000,11025,16000,22050,44100
        //sampleSizeInBits表示每个具有此格式的声音样本中的位数
        int sampleSizeInBits = 16;
        // 8,16
        int channels = 1;
        // 单声道为1，立体声为2
        boolean signed = true;
        // true,false
        boolean bigEndian = false;
        // true,false
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }


    public AudioRecorder() {
    }

}
