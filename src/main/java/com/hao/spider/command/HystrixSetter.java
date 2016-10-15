package com.hao.spider.command;

/**
 * @author dongh38@ziroom
 * @Date 16/10/15
 * @Time 下午4:31
 */
public class HystrixSetter {

    private String commandGroupKey = "spider-command-group-key";
    private String commandKey = "spider-command-key";

    private int eitTimeout = 1000;
    private int etimeout = 1000;

    private int fismRequests = 10;

    private int cbRequests = 20;
    private int cbSleepWindow = 5;
    private int cbErrorRate = 50;

    private String threadPoolKey = "spider-threadpool-key";
    private int threadPoolCoreSize = 10;
    private int threadPoolQueueSize = 5;

    public static HystrixSetter build() {
        return new HystrixSetter();
    }

    public static HystrixSetter buildSenior() {
        return new HystrixSetter(80000, 80000, 30, 50, 50, 10);
    }

    public static HystrixSetter buildSuper() {
        return new HystrixSetter(80000, 80000, 50, 50, 100, 15);
    }

    public HystrixSetter() {}

    public HystrixSetter(int eitTimeout, int etimeout,
                         int cbRequests, int cbErrorRate,int threadPoolCoreSize,
                         int threadPoolQueueSize) {
        this.eitTimeout = eitTimeout;
        this.etimeout = etimeout;
        this.cbRequests = cbRequests;
        this.cbErrorRate = cbErrorRate;
        this.threadPoolCoreSize = threadPoolCoreSize;
        this.threadPoolQueueSize = threadPoolQueueSize;
    }

    public String getCommandGroupKey() {
        return commandGroupKey;
    }
    public void setCommandGroupKey(String commandGroupKey) {
        this.commandGroupKey = commandGroupKey;
    }
    public String getCommandKey() {
        return commandKey;
    }
    public void setCommandKey(String commandKey) {
        this.commandKey = commandKey;
    }
    public int getEitTimeout() {
        return eitTimeout;
    }
    public void setEitTimeout(int eitTimeout) {
        this.eitTimeout = eitTimeout;
    }
    public int getEtimeout() {
        return etimeout;
    }
    public void setEtimeout(int etimeout) {
        this.etimeout = etimeout;
    }
    public int getFismRequests() {
        return fismRequests;
    }
    public void setFismRequests(int fismRequests) {
        this.fismRequests = fismRequests;
    }
    public int getCbRequests() {
        return cbRequests;
    }
    public void setCbRequests(int cbRequests) {
        this.cbRequests = cbRequests;
    }
    public int getCbSleepWindow() {
        return cbSleepWindow;
    }
    public void setCbSleepWindow(int cbSleepWindow) {
        this.cbSleepWindow = cbSleepWindow;
    }
    public int getCbErrorRate() {
        return cbErrorRate;
    }
    public void setCbErrorRate(int cbErrorRate) {
        this.cbErrorRate = cbErrorRate;
    }
    public String getThreadPoolKey() {
        return threadPoolKey;
    }
    public void setThreadPoolKey(String threadPoolKey) {
        this.threadPoolKey = threadPoolKey;
    }
    public int getThreadPoolCoreSize() {
        return threadPoolCoreSize;
    }
    public void setThreadPoolCoreSize(int threadPoolCoreSize) {
        this.threadPoolCoreSize = threadPoolCoreSize;
    }
    public int getThreadPoolQueueSize() {
        return threadPoolQueueSize;
    }
    public void setThreadPoolQueueSize(int threadPoolQueueSize) {
        this.threadPoolQueueSize = threadPoolQueueSize;
    }


}
