C = javac
R = java
F = out
D = src
Jar = ReadFile.jar

Args := -s -o result -p out- in1.txt in2.txt

all: OUT Main.class Package Remove

Package:
	jar cfm $(Jar) $(D)/manifest.mf -C $(F)/ .

OUT:
	mkdir -p $(F)

Remove:
	rm -rf $(F)/

Main.class: $(D)/Main.java $(D)/ReadFile.java | $(F)
	$(C) -d $(F) $(D)/Main.java $(D)/ReadFile.java

run:
	$(R) -jar $(Jar) $(Args)

clean:
	rm -rf $(Jar)
