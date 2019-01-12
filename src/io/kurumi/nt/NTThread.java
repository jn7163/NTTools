package io.kurumi.nt;

public class NTThread extends Thread {

	private Config conf;

	public NTThread(Config conf) {
		this.conf = conf;
	}

	@Override
	public void run() {

		if (conf.isRepeatEnable()) {
            
            
            
        }

	}
    
    public static class Config {

        public boolean isRepeatEnable () {
            return true;
        }

        public int getMinRepeatCount() {
            return 2;
        }

        public int getPullInterval() {
            return 6000;
        }

        public int getPullCount() {
            return 50;
        }


    }

}
