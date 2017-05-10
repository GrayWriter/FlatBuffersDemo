package com.example.guofengwu.flatbufferdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.guofengwu.flatbufferdemo.flatbuffersBean.Person;
import com.example.guofengwu.flatbufferdemo.javaBean.JavaPerson;
import com.google.flatbuffers.FlatBufferBuilder;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private int mTestCount = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        findViewById(R.id.flatTest).setOnClickListener(this);
        findViewById(R.id.flatDTest).setOnClickListener(this);
        findViewById(R.id.javaTest).setOnClickListener(this);
        findViewById(R.id.javaDTest).setOnClickListener(this);
    }
    private FlatBufferBuilder createPerson(){
        FlatBufferBuilder bufferBuilder = new FlatBufferBuilder();
        int nameOffSet = bufferBuilder.createString("John");
        int spouseNameOffSet = bufferBuilder.createString("Mary");
        int spouseOffSet = Person.createPerson(bufferBuilder,spouseNameOffSet,0,0,0);
        int bobNameOffSet = bufferBuilder.createString("Bob");
        int bobOffSet = Person.createPerson(bufferBuilder,bobNameOffSet,0,0,0);
        int data[] =new int[]{bobOffSet};
        int friendsOffset = Person.createFriendsVector(bufferBuilder,data);
        Person.startPerson(bufferBuilder);
        Person.addName(bufferBuilder,nameOffSet);
        Person.addSpouse(bufferBuilder,spouseOffSet);
        Person.addFriends(bufferBuilder,friendsOffset);
        int john =Person.endPerson(bufferBuilder);
        bufferBuilder.finish(john);
        return bufferBuilder;

    }
     private JavaPerson createJavaPerson(){
         JavaPerson john = new JavaPerson();
         john.setName("John");
         JavaPerson mary = new JavaPerson();
         mary.setName("Mary");
         john.setSpouse(mary);
         JavaPerson bob = new JavaPerson();
         bob.setName("Bob");
         List<JavaPerson> friends = new ArrayList<>();
         friends.add(bob);
         john.setFriends(friends);
         return john;
     }
    /**
     * 保存到本地
     * @param fbb
     * @param file
     */
     private  void savePerson(FlatBufferBuilder fbb,File file){

         try {
             DataOutputStream os = new DataOutputStream(new FileOutputStream(file));
             os.write(fbb.dataBuffer().array(), fbb.dataBuffer().position(), fbb.offset());
             os.close();
         } catch(java.io.IOException e) {
             return;
         }

     }
    /**
     * 反序列化
     * @param file
     * @return
     */
    private Person getPerson(File file){
        byte[] data = null;
        RandomAccessFile f = null;
        try {
            Log.d("Test","flatDTest:"+file.length()+"B");
            f = new RandomAccessFile(file, "r");
            data = new byte[(int)f.length()];
            f.readFully(data);
            f.close();
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);

            Person john = Person.getRootAsPerson(byteBuffer);
            Log.d("Test",john.toString());
            return john;
        } catch(java.io.IOException e) {
            return null;
        }
    }
    public  void saveObj(File file,Object object)
    {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(object);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void getObj(File file)
    {
        ObjectInputStream ois = null;
        try {
            Log.d("Test","JavaDTest:"+file.length()+"B");
            ois = new ObjectInputStream(new FileInputStream(file));
            JavaPerson p = (JavaPerson) ois.readObject();
            Log.d("Test","JavaPerson:"+p.toString());
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        File flatFile = new File(this.getFilesDir(),"flat.bin");
        File javaFile = new File(this.getFilesDir(),"java.bin");

        switch (v.getId()){
            case R.id.flatTest:
                long time = System.currentTimeMillis();
                for (int i =0;i<mTestCount;i++){
                FlatBufferBuilder fbb = createPerson();
                savePerson(fbb,flatFile);
                }
                time = System.currentTimeMillis()-time;
                Log.d("Test","flatTest:"+time+"ms"+"----count:"+mTestCount);
                break;
            case R.id.flatDTest:
                time = System.currentTimeMillis();
                for (int i =0;i<mTestCount;i++){
                    getPerson(flatFile);
                }
                time = System.currentTimeMillis()-time;
                Log.d("Test","flatDTest:"+time+"ms"+"----count:"+mTestCount);
                break;
            case R.id.javaTest:
                time = System.currentTimeMillis();
                for (int i =0;i<mTestCount;i++){
                    JavaPerson javaPerson = createJavaPerson();
                    saveObj(javaFile,javaPerson);
                }
                time = System.currentTimeMillis()-time;
                Log.d("Test","javaTest:"+time+"ms"+"----count:"+mTestCount);
                break;
            case R.id.javaDTest:
                time = System.currentTimeMillis();
                for (int i =0;i<mTestCount;i++){
                    getObj(javaFile);
                }
                time = System.currentTimeMillis()-time;
                Log.d("Test","javaDTest:"+time+"ms"+"----count:"+mTestCount);
                break;
            default:
        }

    }
}


