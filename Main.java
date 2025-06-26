import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	private String integerNameFile = "integers.txt";
	private String floatNameFile = "floats.txt";
	private String stringNameFile = "strings.txt";

	private int  statistics = 0;

	private boolean isRewrite = true;

	private String path = "/";
	private String prefix = "";

	private ArrayList<Integer> resInt = new ArrayList<>();
	private ArrayList<Float> resFloat =  new ArrayList<>();
	private ArrayList<String> resString = new ArrayList<>();

	//[max],[min],[sum],[average]
	//  0     1     2       3
	private int[] intArr = new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE, 0};
	private float[] floatArr = new float[] {Float.MIN_VALUE, Float.MAX_VALUE, 0};
	private int[] stringArr = new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE};

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Not correct input!");
			return;
		}

		Main data = new Main();

		ArrayList<String> files = new ArrayList<>();

		for(int i = 0; i < args.length; i++) {
			switch(args[i]) {
				case ("-s"):
					data.statistics = 1;
				break;

				case ("-f"):
					data.statistics = 2;
				break;

				case ("-a"):
					data.isRewrite = false;
				break;

				case ("-o"):
					data.path = args[++i];
				break;

				case ("-p"):
					data.prefix = args[++i];
				break;

				default:
					files.add(args[i]);
				break;
			}
		}

		for(String file : files) {
			try {
				BufferedReader bf = new BufferedReader(new FileReader(file));
				String line;

				while((line = bf.readLine()) != null) {
					try {
						int numInt = Integer.parseInt(line);
						System.out.println("Int: " + numInt);

						data.resInt.add(numInt);

						data.intArr[0] = numInt > data.intArr[0] ? numInt : data.intArr[0];
						data.intArr[1] = numInt < data.intArr[1] ? numInt : data.intArr[1];
						data.intArr[2] += numInt;
					}
					catch(NumberFormatException ei) {
						try {
							float numFloat = Float.parseFloat(line);
							System.out.println("Float: " + numFloat);

							data.resFloat.add(numFloat);

							data.floatArr[0] = numFloat > data.floatArr[0] ? numFloat : data.floatArr[0];
							data.floatArr[1] = numFloat < data.floatArr[1] ? numFloat : data.floatArr[1];
							data.floatArr[2] += numFloat;
						}
						catch(NumberFormatException ef) {
							System.out.println("String: " + line);

							data.resString.add(line);

							data.stringArr[0] = line.length() > data.stringArr[0] ? line.length() : data.stringArr[0];
							data.stringArr[1] = line.length() < data.stringArr[1] ? line.length() : data.stringArr[1];
						}
					}
				}
			}
			catch (IOException e) {
				System.out.println("Failed to open file: " + file);
			}
		}

		//Output info
		if(data.statistics != 0) {
			System.out.println(data.prefix + data.integerNameFile + ":\n	Size: " + data.resInt.size());
			if(data.statistics == 2) {
				System.out.println("	MaxValue: " + data.intArr[0] + "\n	MinValue: " + data.intArr[1] + "\n	Sum: " + data.intArr[2] + "\n	Average: " + data.intArr[2] / data.resInt.size());
			}

			System.out.println(data.prefix + data.floatNameFile + ":\n	Size: " + data.resFloat.size());
			if(data.statistics == 2) {
				System.out.println("	MaxValue: " + data.floatArr[0] + "\n	MinValue: " + data.floatArr[1] + "\n	Sum: " + data.floatArr[2] + "\n	Average: " + data.floatArr[2] / data.resFloat.size());
			}


			System.out.println(data.prefix + data.stringNameFile + ":\n	Size: " + data.resString.size());
			if(data.statistics == 2) {
				System.out.println("	MaxValue: " + data.stringArr[0] + "\n	MinValue: " + data.stringArr[1]);
			}
		}
		//------
	}
}
