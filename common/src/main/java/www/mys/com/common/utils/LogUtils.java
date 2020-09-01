package www.mys.com.common.utils;

import www.mys.com.common.base.StaticParam;
import www.mys.com.utils.AppendFileUtils;
import www.mys.com.utils.FileUtils;
import www.mys.com.utils.TimeEnum;
import www.mys.com.utils.TimeUtils;

import java.util.Date;

public class LogUtils {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogUtils.class);
    public static final String logRootPath = StaticParam.ROOT_PATH + StaticParam.FILE_SPLIT + StaticParam.LOG_PATH;
    private static String currentDirPath;

    public static void initRootPath() {
        StringBuilder tempSB = new StringBuilder(logRootPath);
        String timeData = TimeUtils.getTimeZoneDateString(new Date(), 8, TimeEnum.FORMAT_DAY);
        String[] tempTimeData = timeData.split("-");
        if (FileUtils.sureDir(logRootPath) == null) {
            log.error("sureDir error.dir=" + logRootPath);
            return;
        }
        tempSB.append(StaticParam.FILE_SPLIT).append(tempTimeData[0]);
        if (FileUtils.sureDir(tempSB.toString()) == null) {
            log.error("sureDir error.dir=" + tempSB);
            return;
        }
        tempSB.append(StaticParam.FILE_SPLIT).append(tempTimeData[1]);
        if (FileUtils.sureDir(tempSB.toString()) == null) {
            log.error("sureDir error.dir=" + tempSB);
            return;
        }
        tempSB.append(StaticParam.FILE_SPLIT).append(tempTimeData[2]);
        if (FileUtils.sureDir(tempSB.toString()) == null) {
            log.error("sureDir error.dir=" + tempSB);
            return;
        }
        currentDirPath = tempSB.toString();
    }

    public static void log(String logStr) {
        logStr = TimeUtils.getTimeZoneDateString(new Date(), 8, TimeEnum.FORMAT_DAY_MSEC)
                + "=====" + logStr;
        System.out.println(logStr);
        log.error(logStr);
        String timeData = TimeUtils.getTimeZoneDateString(new Date(), 8, TimeEnum.FORMAT_DAY);
        String[] tempTimeData = timeData.split("-");
        StringBuilder filePath = new StringBuilder(logRootPath);
        filePath.append(StaticParam.FILE_SPLIT).append(tempTimeData[0])
                .append(StaticParam.FILE_SPLIT).append(tempTimeData[1])
                .append(StaticParam.FILE_SPLIT).append(tempTimeData[2]);
        if (!filePath.toString().equals(currentDirPath)) {
            initRootPath();
        }
        AppendFileUtils.getInstance(FileUtils.sureFile(
                filePath.append(StaticParam.FILE_SPLIT).append("text.log").toString())
        ).appendLineString(logStr);
    }

}
