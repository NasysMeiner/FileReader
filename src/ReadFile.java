import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class ReadFile {
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

	//[max],[min],[sum]
	//  0     1     2
	private int[] intArr = new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE, 0};
	private float[] floatArr = new float[] {Float.MIN_VALUE, Float.MAX_VALUE, 0};
	private int[] stringArr = new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE};

    private String output = "";

    public void Read(String[] args) {
        if (args.length == 0) {
			System.err.println("Not correct input!");
			return;
		}

        ArrayList<String> files = new ArrayList<>();

        SetSettings(args, files);

        if(files.size() == 0) {
            System.err.println("Files are missing!");
            return;
        }

        ReadFiles(files);

        if(statistics > 0) {
            output = StatisticsCollection();
        }

        OutputFiles();

		if(output != "")
        	System.out.println(output);
    }

    private void SetSettings(String[] args, ArrayList<String> files) {
        for(int i = 0; i < args.length; i++) {
			switch(args[i]) {
				case ("-s"):
					if(statistics != 2)
						statistics = 1;
				break;

				case ("-f"):
					statistics = 2;
				break;

				case ("-a"):
					isRewrite = false;
				break;

				case ("-o"):
					path = args[++i] + "/";
				break;

				case ("-p"):
					prefix = args[++i];
				break;

				default:
					if(args[i].startsWith("-")) {
						System.err.println("Unknown flag: " + args[i]);
						break;
					}

					files.add(args[i]);
				break;
			}
		}
    }

    private void ReadFiles(ArrayList<String> files) {
        for(String file : files) {
			try {
				try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
                    String line;

                    while((line = bf.readLine()) != null) {
                    	try {
                    		int numInt = Integer.parseInt(line);
                    		resInt.add(numInt);

                    		intArr[0] = numInt > intArr[0] ? numInt : intArr[0];
                    		intArr[1] = numInt < intArr[1] ? numInt : intArr[1];
                    		intArr[2] += numInt;
                    	}
                    	catch(NumberFormatException eInt) {
                    		try {
                    			float numFloat = Float.parseFloat(line);
                    			resFloat.add(numFloat);

                    			floatArr[0] = numFloat > floatArr[0] ? numFloat : floatArr[0];
                    			floatArr[1] = numFloat < floatArr[1] ? numFloat : floatArr[1];
                    			floatArr[2] += numFloat;
                    		}
                    		catch(NumberFormatException eFloat) {
                    			resString.add(line);

                    			stringArr[0] = line.length() > stringArr[0] ? line.length() : stringArr[0];
                    			stringArr[1] = line.length() < stringArr[1] ? line.length() : stringArr[1];
                            }

                    	}
                    }
                }
			}
			catch (IOException e) {
				System.err.println("Failed to open file: " + file);
			}
		}
    }

    private String StatisticsCollection() {
        StringBuilder builder = new StringBuilder();
		builder.append("\nStatistics file:\n");

        if(statistics != 0) {
			builder.append(prefix + integerNameFile);
			int count = 0;
			if(!isRewrite) {
				count = CheckCountLine(path + prefix + integerNameFile);
			}

			builder.append(":\n	Size: " + (count + resInt.size()));

			if(statistics == 2) {
				builder.append("\n	MaxValue: " + intArr[0] + "\n	MinValue: " + intArr[1] + "\n	Sum: " + intArr[2] + "\n	Average: " + intArr[2] / resInt.size());
			}

			builder.append("\n\n" + prefix + floatNameFile);

			if(!isRewrite) {
				count = CheckCountLine(path + prefix + floatNameFile);
			}

			builder.append(":\n	Size: " + (count + resFloat.size()));

			if(statistics == 2) {
				builder.append("\n	MaxValue: " + floatArr[0] + "\n	MinValue: " + floatArr[1] + "\n	Sum: " + floatArr[2] + "\n	Average: " + floatArr[2] / resFloat.size());
			}

			builder.append("\n\n" + prefix + stringNameFile);

			if(!isRewrite) {
				count = CheckCountLine(path + prefix + stringNameFile);
			}

			builder.append(":\n	Size: " + (count + resString.size()));

			if(statistics == 2) {
				builder.append("\n	MaxValue: " + stringArr[0] + "\n	MinValue: " + stringArr[1]);
            }
		}

        return builder.toString();
    }

	private int CheckCountLine(String nameFile) {
		try (BufferedReader bf = new BufferedReader(new FileReader(nameFile))) {
			int count = 0;
			while((bf.readLine()) != null) {
				count++;
			}

			return count;
		}
		catch (IOException e) {
			System.err.println("File not found: " + nameFile);
		}

		return 0;
	}

    private void OutputFiles() {
        boolean isPath = false;
		if(!new File(path).exists()) {
			isPath = new File(path).mkdirs();

			if(!isPath)
				System.err.println("Not correct path: " + path + "\nThe files will be written to the root folder");
		}
		else {
			isPath = true;
		}

		try(FileWriter fr = new FileWriter(("./" + (isPath ? path + "/" : "") + prefix + integerNameFile), !isRewrite)) {
			for(int number : resInt) {
				fr.write(number + "\n");
			}
		}
		catch (IOException e) {
			System.err.println("Error in prefix or path file");
		}

		try(FileWriter fr = new FileWriter(("./" + (isPath ? path + "/" : "") + prefix + floatNameFile), !isRewrite)) {
			for(float number : resFloat) {
				fr.write(number + "\n");
			}
		}
		catch (IOException e) {
			System.err.println("Error in prefix or path file");
		}

		try (FileWriter fr = new FileWriter(("./" + (isPath ? path + "/" : "") + prefix + stringNameFile), !isRewrite)) {
			for(String str : resString) {
				fr.write(str + "\n");
			}
		}
		catch(IOException e) {
            System.err.println("Error in prefix or path file");
		}
    }
}