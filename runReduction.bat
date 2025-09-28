
rem java -jar art.jar reduction.art
javac --module-path="C:\home\openJFX\javafx-sdk-17.0.14\lib" --add-modules=javafx.controls -cp .;art.jar ARTVAluePlugin.java
java  --module-path="C:\home\openJFX\javafx-sdk-17.0.14\lib" --add-modules=javafx.controls -cp .;art.jar uk.ac.rhul.cs.csle.art.ARTFX reduction.art
