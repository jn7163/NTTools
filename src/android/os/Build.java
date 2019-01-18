package android.os;

public class Build
{
    public static java.lang.String BOARD;

    public static java.lang.String BOOTLOADER;

    public static java.lang.String BRAND;

    /** @deprecated */
    @java.lang.Deprecated()
    public static java.lang.String CPU_ABI;

    /** @deprecated */
    @java.lang.Deprecated()
    public static java.lang.String CPU_ABI2;

    public static java.lang.String DEVICE;

    public static java.lang.String DISPLAY;

    public static java.lang.String FINGERPRINT;

    public static java.lang.String HARDWARE;

    public static java.lang.String HOST;

    public static java.lang.String ID;

    public static java.lang.String MANUFACTURER;

    public static java.lang.String MODEL;

    public static java.lang.String PRODUCT;

    /** @deprecated */
    @java.lang.Deprecated()
    public static java.lang.String RADIO;

    /** @deprecated */
    @java.lang.Deprecated()
    public static java.lang.String SERIAL;

    public static java.lang.String[] SUPPORTED_32_BIT_ABIS;

    public static java.lang.String[] SUPPORTED_64_BIT_ABIS;

    public static java.lang.String[] SUPPORTED_ABIS;

    public static java.lang.String TAGS;

    public static long TIME;

    public static java.lang.String TYPE;

    public static java.lang.String UNKNOWN = "unknown";

    public static java.lang.String USER;

    public static java.lang.String getSerial() { return null; }

    public static java.lang.String getRadioVersion() { return null; 
    }

    public Build() {}


    public static class VERSION
    {
        public static java.lang.String BASE_OS;

        public static java.lang.String CODENAME;

        public static java.lang.String INCREMENTAL;

        public static int PREVIEW_SDK_INT;

        public static java.lang.String RELEASE;

        /** @deprecated */
        @java.lang.Deprecated()
        public static java.lang.String SDK;

        public static int SDK_INT;

        public static java.lang.String SECURITY_PATCH;

        public VERSION() {}

    }


    public static class VERSION_CODES
    {
        public static int BASE = 1;

        public static int BASE_1_1 = 2;

        public static int CUPCAKE = 3;

        public static int CUR_DEVELOPMENT = 10000;

        public static int DONUT = 4;

        public static int ECLAIR = 5;

        public static int ECLAIR_0_1 = 6;

        public static int ECLAIR_MR1 = 7;

        public static int FROYO = 8;

        public static int GINGERBREAD = 9;

        public static int GINGERBREAD_MR1 = 10;

        public static int HONEYCOMB = 11;

        public static int HONEYCOMB_MR1 = 12;

        public static int HONEYCOMB_MR2 = 13;

        public static int ICE_CREAM_SANDWICH = 14;

        public static int ICE_CREAM_SANDWICH_MR1 = 15;

        public static int JELLY_BEAN = 16;

        public static int JELLY_BEAN_MR1 = 17;

        public static int JELLY_BEAN_MR2 = 18;

        public static int KITKAT = 19;

        public static int KITKAT_WATCH = 20;

        public static int LOLLIPOP = 21;

        public static int LOLLIPOP_MR1 = 22;

        public static int M = 23;

        public static int N = 24;

        public static int N_MR1 = 25;

        public static int O = 26;

        public static int O_MR1 = 27;

        public VERSION_CODES() {}

    }

}
