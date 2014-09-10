package com.haogrgr.test.main;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

public class DynamicCronExp {

    public static void main(String[] args) throws Exception {
        CronChangeFinder finder = new CronChangeFinder();

        finder.start();//开始以5秒的间隔执行

        Thread.sleep(10000);//等待10秒

        //模拟数据库修改操作, 按道理应该在5~10秒后,就会变成三秒执行一次
        finder.dbCron = "0/3 * * * * *";

        Thread.sleep(10000);//等待10秒
        finder.dbFlag = false;

        Thread.sleep(10000);//等待10秒
        finder.dbFlag = true;
        finder.dbCron = "0/1 * * * * *";
    }

}

class CronChangeFinder extends Thread {

    String currentCron;
    ConcurrentTaskScheduler scheduler;
    BizRunnable task;
    ScheduledFuture<?> futrue;
    volatile boolean stop = false;

    String dbCron = "0/5 * * * * *";//这个只是为了模拟
    boolean dbFlag = true;//这个只是为了模拟

    public CronChangeFinder() {
        this.currentCron = getCronFromDB();
        this.scheduler = new ConcurrentTaskScheduler();
        this.task = new BizRunnable(this);

        this.futrue = this.scheduler.schedule(this.task, new CronTrigger(this.currentCron));
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                Thread.sleep(5000);//这个看情况设置,这里是5秒,表示,数据库修改5~10秒后,才会生效

                //从数据库中查询cron表达式
                String dbCron = getCronFromDB();

                //数据库改变值啦
                if (!dbCron.equals(currentCron)) {
                    currentCron = dbCron;
                    onCronChange();
                } else {
                    System.err.println(new Date() + " db not change");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onCronChange() {
        System.err.println(new Date() + " db change the cron expression");
        
        //取消当前的定时任务
        futrue.cancel(true);

        //加入新的定时任务
        this.futrue = this.scheduler.schedule(this.task, new CronTrigger(this.currentCron));

    }

    String getCronFromDB() {
        return dbCron;
    }

    boolean getFlagFromDB() {
        return dbFlag;
    }
}

class BizRunnable implements Runnable {
    CronChangeFinder finder;//只是为了getFlagFromDB()调这个方法

    public BizRunnable(CronChangeFinder finder) {
        this.finder = finder;
    }

    @Override
    public void run() {
        if (finder.getFlagFromDB()) {
            System.out.println(new Date());
        } else {
            System.out.println(new Date() + " dbflag is false");
        }
    }
}
