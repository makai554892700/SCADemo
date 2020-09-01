package www.mys.com.common.base;

public class StaticParam {

    public static final String[] HEX_STR = new String[]{"0", "1", "2"
            , "3", "4", "5", "6", "7", "8", "9", "A", "B"
            , "C", "D", "E", "F"};

    public static final int SEX_NONE = -1;
    public static final int SEX_MAN = 1;
    public static final int SEX_WOMAN = 2;
    public static final long DAY = 24 * 60 * 60 * 1000;


    public static final String LINE = "\n";
    public static final String OK = "ok";
    public static final String FAILED = "failed";
    public static final String ORDER_MARK = "_";
    public static final String PNG_END = ".png";

    public static final String HEAD_SESSION = "mark";
    public static final int DEFAULT_NODE_PORT = 10086;
    public static final int MAX_RETRY_TIMES = 2;

    public static final int START_PORT = 40000;
    public static final int END_PORT = 60000;
    public static final String EMPTY = "";
    public static final String ZERO = "0";
    public static final String FILE_SPLIT = "/";

    public static String ROOT_PATH;
    public static String IMG_PATH = "imgs";
    public static String LOG_PATH = "log";
    public static final String IMG_PATH_KEY = "img_path_key";
    public static String IMG_HOST = "http://img.demo.com/imgs";
    public static final String IMG_HOST_KEY = "img_host_key";

    static {
        try {
            ROOT_PATH = System.getProperty("user.dir");
        } catch (Exception e) {
            System.out.println("root path error.e=" + e);
        }
    }

}
