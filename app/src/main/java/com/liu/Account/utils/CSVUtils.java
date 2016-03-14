package com.liu.Account.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.liu.Account.Constants.Constants;
import com.liu.Account.Database.Billdate;
import com.liu.Account.commonUtils.DateUtil;
import com.liu.Account.commonUtils.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by deonte on 16-3-14.
 */
public class CSVUtils {
    /**
     * 将csv写入list中
     * */
    public static List<Billdate> csvToList(Intent data) {
        Uri uri=data.getData();
        LogUtil.i(data.getDataString());
        File file=new File(uri.getPath());
        List<Billdate> list=new ArrayList<Billdate>();
        try {

            BufferedReader br = new BufferedReader(new FileReader(file));
            // 读取直到最后一行
            String line = "";
            while ((line = br.readLine()) != null) {
                // 把一行数据分割成多个字段
                StringTokenizer st = new StringTokenizer(line, ",");
                String[] colums=line.split(",");
                if (colums.length!=11){
                    LogUtil.d("此行为坏行 跳过");
                    continue;
                }
                Billdate bt =new Billdate();
                bt.setSpendMoney(colums[1]);
                bt.setRemark(colums[2]);
                bt.setDate(colums[3]);
                bt.setUnixTime(colums[4]);
                bt.setCreatTime(colums[5]);
                bt.setMoneyType(colums[6]);
                bt.setTag(colums[7]);
                bt.setYear_date(colums[8]);
                bt.setMonth_date(colums[9]);
                bt.setDay_year(colums[10]);
                list.add(bt);
                LogUtil.i("账单id:"+colums[0]+"账单备注:"+colums[2]);
            }
            br.close();

        } catch (FileNotFoundException e) {
            // 捕获File对象生成时的异常
            e.printStackTrace();
        } catch (IOException e) {
            // 捕获BufferedReader对象关闭时的异常
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 将list中的值写入到sd卡
     * */
    public static void writeToCsv(List<Billdate> list,Context context) {

        StringBuffer buffer = new StringBuffer();
        //  buffer.append("id,订单金额,订单备注,订单时间,订单时间戳,订单创建时间,订单类型,订单标签,订单年,订单月,订单日\r\n");
        for(Billdate u:list){
            buffer.append(u.get_Id()+","
                    +u.getSpendMoney()+","
                    +u.getRemark()+","
                    +u.getDate()+","
                    +u.getUnixTime()+","
                    +u.getCreatTime()+","
                    +u.getMoneyType()+","
                    +u.getTag()+","
                    +u.getYear_date()+","
                    +u.getMonth_date()+","
                    +u.getDay_year()
                    +"\r\n");
        }

        try {
//				String data =new String(buffer.toString().getBytes("utf-8"), "ansi") ;
            String data = buffer.toString();

            String filename = "jizhangyi_"+ DateUtil.getCurrentDate(DateUtil.dateFormatYMDHMS)+".csv";

            String path = Environment.getExternalStorageDirectory()+ Constants.FileName;

            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }
            File file = new File(path, filename);
            OutputStream out=new FileOutputStream(file);
            // excel需要BOM签名才能解析utf-8格式的编码

            byte b[] = {(byte)0xEF, (byte)0xBB, (byte)0xBF};

            out.write(b);
            out.write(data.getBytes());
            out.close();
            new AlertDialog.Builder(context).setTitle("导出成功")
                    .setMessage("数据导出成功,可在sd卡下jizhangyi目录下查看")
                    .setPositiveButton("确定",null)
                    .show();
        } catch (Exception e) {
            LogUtil.i("导出文件失败:" + e.toString());
        }
    }


}
