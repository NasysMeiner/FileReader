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

	private boolean isPath = false;

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

	private String[] nameFiles = new String[3];

    private String output = "";

    public void Read(String[] args) {
        if (args.length == 0) {
			System.err.println("Not correct input!");
			return;
		}

		nameFiles[0] = integerNameFile;
		nameFiles[1] = floatNameFile;
		nameFiles[2] = stringNameFile;

        ArrayList<String> files = new ArrayList<>();

        SetSettings(args, files);

		PathChecked();

        if(files.size() == 0) {
            System.err.println("Files are missing!");
            return;
        }

        WriteFiles(files);

        OutputFiles();
		ReadFiles();

		if(statistics > 0 && (intArr[0] > 0 || floatArr[0] > 0 || stringArr[0] > 0)) {
            output = StatisticsCollection();

			if(output != "")
        		System.out.println(output);
        }
    }

	private void PathChecked() {
		if(!new File(path).exists()) {
			isPath = new File(path).mkdirs();

			if(!isPath) {
				System.err.println("Not correct path: " + path + "\nThe files will be written to the root folder");
			}
			else {
				isPath = true;
				path = "./" + path + "/";
			}
		}
		else {
			isPath = true;
			path = "./" + path + "/";
		}
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
					path = args[++i];
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

    private void WriteFiles(ArrayList<String> files) {
        for(String file : files) {
			try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
                String line;

                while((line = bf.readLine()) != null) {
                	try {
                		int numInt = Integer.parseInt(line);
                		resInt.add(numInt);
                   	}
                   	catch(NumberFormatException eInt) {
						try {
                			float numFloat = Float.parseFloat(line);
                			resFloat.add(numFloat);
                    	}
                    	catch(NumberFormatException eFloat) {
                    		resString.add(line);
                        }
                    }
                }
            }
			catch (IOException e) {
				System.err.println("Failed to open file: " + file);
			}
		}
    }

	private void ReadFiles() {
		for(int i = 0; i < 3; i++) {
			String file = path + prefix + nameFiles[i];
			try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
                String line;

                while((line = bf.readLine()) != null) {
                	try {
                		int numInt = Integer.parseInt(line);

                		intArr[0] = numInt > intArr[0] ? numInt : intArr[0];
                    	intArr[1] = numInt < intArr[1] ? numInt : intArr[1];
                    	intArr[2] += numInt;
                   	}
                   	catch(NumberFormatException eInt) {
						try {
                			float numFloat = Float.parseFloat(line);

                			floatArr[0] = numFloat > floatArr[0] ? numFloat : floatArr[0];
                			floatArr[1] = numFloat < floatArr[1] ? numFloat : floatArr[1];
                    		floatArr[2] += numFloat;
                    	}
                    	catch(NumberFormatException eFloat) {
                    		stringArr[0] = line.length() > stringArr[0] ? line.length() : stringArr[0];
                    		stringArr[1] = line.length() < stringArr[1] ? line.length() : stringArr[1];
                        }

                    }
                }
            }
			catch (IOException e) {
				//System.err.println("Failed to open file2:" + file);
			}
		}
	}

    private String StatisticsCollection() {
        StringBuilder builder = new StringBuilder();
		builder.append("---------------------------------\nStatistics file:\n");

        if(statistics != 0) {
			int count;

			if(intArr[0] > 0) {
				builder.append(prefix + integerNameFile);
				count = CheckCountLine(path + prefix + integerNameFile);
				builder.append(":\n	Size: " + count);

				if(statistics == 2) {
					builder.append("\n	MaxValue: " + intArr[0] + "\n	MinValue: " + intArr[1] + "\n	Sum: " + intArr[2] + "\n	Average: " + intArr[2] / count);
				}

				builder.append("\n\n");
			}

			if(floatArr[0] > 0) {
				builder.append(prefix + floatNameFile);
				count = CheckCountLine(path + prefix + floatNameFile);
				builder.append(":\n	Size: " + count);

				if(statistics == 2) {
					builder.append("\n	MaxValue: " + floatArr[0] + "\n	MinValue: " + floatArr[1] + "\n	Sum: " + floatArr[2] + "\n	Average: " + floatArr[2] / count);
				}

				builder.append("\n\n");
			}
			
			if(stringArr[0] > 0) {
				builder.append(prefix + stringNameFile);
				count = CheckCountLine(path + prefix + floatNameFile);
				builder.append(":\n	Size: " + count);

				if(statistics == 2) {
					builder.append("\n	MaxValue: " + stringArr[0] + "\n	MinValue: " + stringArr[1]);
            	}

				builder.append("\n\n");
			}

			builder.append("---------------------------------");
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
		if(resInt.size() > 0) {
			try(FileWriter fr = new FileWriter((path + prefix + integerNameFile), !isRewrite)) {
				for(int number : resInt) {
					fr.write(number + "\n");
				}
			}
			catch (IOException e) {
				System.err.println("Error in prefix or path file1");
			}
		}

		if(resFloat.size() > 0) {
			try(FileWriter fr = new FileWriter(path + prefix + floatNameFile, !isRewrite)) {
				for(float number : resFloat) {
					fr.write(number + "\n");
				}
			}
			catch (IOException e) {
				System.err.println("Error in prefix or path file2");
			}
		}
		
		if(resString.size() > 0) {
			try (FileWriter fr = new FileWriter(path + prefix + stringNameFile, !isRewrite)) {
				for(String str : resString) {
					fr.write(str + "\n");
				}
			}
			catch(IOException e) {
            	System.err.println("Error in prefix or path file3");
			}
		}
    }
}