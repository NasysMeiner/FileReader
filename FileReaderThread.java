public class FileReaderThread extends Thread {
	private int startIndex;
	private int count;
	private String[] files;

	public FileReaderThread(int startIndex, int count, String[] files) {
		this.startIndex = startIndex;
		this.count = count;
		this.files = files;
	}

	@Override
	public void run() {
		for(int i = startIndex; i < files.length; i++) {
			
		}
	}
}
